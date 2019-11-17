package com.enzoconserve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverActivity extends AppCompatActivity {
    private EditText editText;
    String email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        editText=findViewById(R.id.editText_email);
        Button button_recover = findViewById(R.id.button_recover);
        mAuth = FirebaseAuth.getInstance();


        button_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=editText.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editText.setError("Invalid Email");
                    editText.setFocusable(true);
                }
                else
                {

                    beginrecovery(email);
                }
            }
        });
    }
    //recover
    private void beginrecovery(String email) {

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RecoverActivity.this,"Email sent ",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RecoverActivity.this,Login_Activity.class));
                        }
                        else {
                            Toast.makeText(RecoverActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RecoverActivity.this,Login_Activity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(RecoverActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RecoverActivity.this,Login_Activity.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go previous activity
        return super.onSupportNavigateUp();
    }
}