package com.enzoconserve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;


public class Register_Activity extends AppCompatActivity {
    private EditText eid, epass,ename,ephn;
    private Button bregister;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private TextView account;
    String name="",number="";
    String MobilePattern = "[0-9]{10}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        eid = findViewById(R.id.editText_email);
        epass = findViewById(R.id.editText_password);
        ename = findViewById(R.id.editText_name);
        ephn = findViewById(R.id.editText_number);
        bregister = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        account = findViewById(R.id.textView_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = eid.getText().toString().trim();
                String pass = epass.getText().toString().trim();
                name=ename.getText().toString().trim();
                number=ephn.getText().toString().trim();
                if (name.length() > 20) {

                    Toast.makeText(getApplicationContext(), "pls enter less the 20 character in user name", Toast.LENGTH_SHORT).show();

                } else if (name.length() == 0 || number.length() == 0 || email.length() ==0)
                {
                    if(name.length()==0)
                    {
                        ename.setError("Enter Your Name");
                        ename.setFocusable(true);
                    }
                    if(number.length()==0)
                    {
                        ephn.setError("Enter Your Number");
                        ephn.setFocusable(true);
                    }
                    if(email.length()==0)
                    {
                        eid.setError("Enter Your Email");
                        eid.setFocusable(true);
                    }

                } else if(!number.matches(MobilePattern)) {
                    ephn.setError("Invalid Number");
                    ephn.setFocusable(true);
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    eid.setError("Invalid Email");
                    eid.setFocusable(true);
                }else if(pass.length()<8)
                {
                    epass.setError("Password Length Atleast 8 Character");
                    epass.setFocusable(true);
                }
                else {
                    registeruser(email, pass); //registering meathod
                }
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                finish();
            }
        });
    }
    private void registeruser (String email, String pass){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(Register_Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            sendEmailVerification();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", name);
                            hashMap.put("phone", number);
                            hashMap.put("image", "");
                            hashMap.put("description","");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("users");
                            reference.child(uid).setValue(hashMap);

                            user = mAuth.getCurrentUser();
                            //    Toast.makeText(RegisterActivity.this, "Registered ...\n" +user.getEmail(),
                            //         Toast.LENGTH_SHORT).show();


                            //  FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
                            // DatabaseReference myRef = firebaseDatabase.getReference(eid.getText().toString());
                            // UserInfo userInfo=new UserInfo("","","","");
                            // myRef.setValue(userInfo);

                            // startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Register_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    //...
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register_Activity.this, e.getMessage() + "",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendEmailVerification ()
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register_Activity.this, "Verification mail is sent", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Register_Activity.this, conservelogin.class);
                        startActivity(i);
                        finish();
                    }
                }
            });

        }
    }
    @Override
    public boolean onSupportNavigateUp () {
        onBackPressed(); //go previous activity
        return super.onSupportNavigateUp();
    }
}

