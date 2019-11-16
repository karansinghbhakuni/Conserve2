package com.enzoconserve;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enzoconserve.fragments.newsfragment;
import com.enzoconserve.fragments.postsfragment;
import com.enzoconserve.fragments.homefragment;
import com.enzoconserve.fragments.profilefragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    TextView slidername,slideremail;
    ImageView slideimage;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    //context


    //database
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    String name,email,image;

    //fragmentmanager
    private static FragmentManager fragmentManager;

    private Context context=DashboardActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_dashboard);
        drawerLayout=findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        navigationView=findViewById(R.id.navigation_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //test
        View headerView = navigationView.getHeaderView(0);
        slidername = headerView.findViewById(R.id.slider_name);
        slideremail=headerView.findViewById(R.id.slider_email);
        slideimage=headerView.findViewById(R.id.profile_pic);

        //database
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =  firebaseDatabase.getReference("users");
        storageReference = getInstance().getReference();


        //context
        Context context=getApplicationContext();

        //fragmentmanager
        fragmentManager = getSupportFragmentManager();

        //Bundle
        Bundle bundle=new Bundle();
        bundle.putString("name",name);
        bundle.putString("image",image);



  //nav view when app opens
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new homefragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new homefragment()).commit();
                break;
            case R.id.profile:
                profilefragment argumentFragment=new profilefragment();//Get Fragment Instance
                Bundle bundle=new Bundle();
                bundle.putString("name",name);
                bundle.putString("image",image);
                argumentFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container,argumentFragment).commit();
                break;
            case R.id.posts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new postsfragment()).commit();
                break;
            case R.id.news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new newsfragment()).commit();
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                checkuserstatus();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onStart() {

        checkuserstatus();
        super.onStart();
    }
    private void checkuserstatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //check until required data get
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get data
                        name = "" + ds.child("name").getValue();
                        email = "" + ds.child("email").getValue();
                        //   String phone= ""+ ds.child("phone").getValue();
                         image= ""+ ds.child("image").getValue();
                    }
                    slidername.setText(name);
                    slideremail.setText(email);
                    Glide.with(context)
                            .load(image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(slideimage);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }
}
