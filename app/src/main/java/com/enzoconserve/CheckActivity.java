package com.enzoconserve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CheckActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);


        firebaseAuth=FirebaseAuth.getInstance();
        checkuserstatus();
    }
    private void checkuserstatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(CheckActivity.this,DashboardActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(CheckActivity.this,MainActivity.class));
            finish();
        }
    }
}
