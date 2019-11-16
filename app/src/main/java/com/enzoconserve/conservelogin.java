package com.enzoconserve;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class conservelogin extends AppCompatActivity {

    Button btc_login;
    TextView registerbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservelogin);
        btc_login=findViewById(R.id.btn_conservelogin);
        registerbtn=findViewById(R.id.btn_conserveregister);
        btc_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(conservelogin.this,Login_Activity.class));
                  finish();
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(conservelogin.this,Register_Activity.class));
                 finish();
            }
        });
    }


}
