package com.enzoconserve.fragments;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import 	androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enzoconserve.Adapter.AdapterEvent;
import com.enzoconserve.Adapter.AdapterPosts;
import com.enzoconserve.AddPostActivity;
import com.enzoconserve.GenerateEvent;
import com.enzoconserve.Login_Activity;
import com.enzoconserve.MainActivity;
import com.enzoconserve.Model.ModelEvent;
import com.enzoconserve.Model.ModelPost;
import com.enzoconserve.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class homefragment extends Fragment{
    @Nullable

    FloatingActionButton fab;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    ArrayList<ModelEvent> eventList;
    AdapterEvent adapterEvent;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        fab = view.findViewById(R.id.fab);
        firebaseAuth = FirebaseAuth.getInstance();

        //recycler view and its prop
        recyclerView=view.findViewById(R.id.homeposts);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        //show newest post first, for this load from last

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events");

        checkuserstatus();
        loadPosts();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        return view;
    }

    private void showEditProfileDialog(){

        String option[] = {"Add a new event","Add a new post"};

        //alert dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Choose Action");
        //set items to dialog
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item click
                if(which == 0){
                    startActivity(new Intent(getContext(), GenerateEvent.class));
                }
                else if(which== 1){
                    Intent intent=new Intent(getContext(), AddPostActivity.class);
                    startActivity(intent);
                }
            }
        });
        //create and show
        builder.create().show();

    }
    private void loadPosts() {
        //path of all posts
        final int[] i = {0};
        if(myRef!=null) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        eventList = new ArrayList<ModelEvent>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ModelEvent modelEvent = ds.getValue(ModelEvent.class);
                            i[0]++;
                            eventList.add(modelEvent);
                        }
                        adapterEvent = new AdapterEvent(getActivity(), eventList);
                        Log.d(TAG, "Value is: " + i[0]);
                        //set adapter to recycler view
                        recyclerView.setAdapter(adapterEvent);
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
    /*
    @Override
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
