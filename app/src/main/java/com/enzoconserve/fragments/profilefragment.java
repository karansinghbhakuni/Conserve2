package com.enzoconserve.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import 	androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enzoconserve.R;

public class profilefragment extends Fragment{
    Button Dedit,Dupload;
    TextView Dname;
    ImageView Dimage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        Dedit=view.findViewById(R.id.display_edit);
        Dupload=view.findViewById(R.id.display_upload);
        Dname=view.findViewById(R.id.display_name);
        Dimage=view.findViewById(R.id.display_picture);

        String name = getArguments().getString("name");
        String image= getArguments().getString("image");
        Dname.setText(name);

        Glide.with(getContext())
                .load(image)
                .placeholder(R.drawable.newsgradient)
                .error(R.drawable.newsgradient)
                .fallback(R.drawable.newsgradient)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Dimage);



        //button Listener
        Dedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Dupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }




    //hide toolbar
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
