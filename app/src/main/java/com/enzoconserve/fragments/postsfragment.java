package com.enzoconserve.fragments;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import 	androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enzoconserve.Adapter.AdapterPosts;
import com.enzoconserve.AddPostActivity;
import com.enzoconserve.Login_Activity;
import com.enzoconserve.MainActivity;
import com.enzoconserve.Model.ModelPost;
import com.enzoconserve.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class postsfragment extends Fragment{

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    ArrayList<ModelPost> postList;
    AdapterPosts adapterPosts;
    Boolean scrolling =false;
    int Currentitem,Totalitems,Scrolloutitems;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_aboutus,container,false);
        firebaseAuth = FirebaseAuth.getInstance();

        //recycler view and its prop
        recyclerView=view.findViewById(R.id.recyclerview_posts);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        //show newest post first, for this load from last
      //  progressBar=view.findViewById(R.id.progress_posts);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Posts");

        checkuserstatus();
        loadPosts();
     /*   recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
                {
                    scrolling=true;
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Currentitem =layoutManager.getChildCount();
                Totalitems=layoutManager.getItemCount();
                Scrolloutitems=layoutManager.findFirstVisibleItemPosition();
                if(scrolling && (Currentitem+Scrolloutitems == Totalitems ))
                {
                    adapterPosts.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });*/

        return  view;
    }

    private void loadPosts() {
        //path of all posts
        if(myRef!=null) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        postList = new ArrayList<ModelPost>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelPost modelPost = ds.getValue(ModelPost.class);

                            postList.add(modelPost);
                        }
                        adapterPosts = new AdapterPosts(getActivity(), postList);

                        //set adapter to recycler view
                        recyclerView.setAdapter(adapterPosts);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //in case of error
                    //  Toast.makeText(getActivity(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void checkuserstatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            //  display.setText(user.getEmail());
        }
        else{
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

   /* @Override
    public void onResume() {

        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }*/
}
