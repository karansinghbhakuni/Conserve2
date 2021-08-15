package com.enzoconserve;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Userinfoupdate extends AppCompatActivity {
    ImageView imageView;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfoupdate);

        imageView=findViewById(R.id.updateImageIv);
        editText=findViewById(R.id.updatenametv);
        button=findViewById(R.id.updatebtn);
    }
}
