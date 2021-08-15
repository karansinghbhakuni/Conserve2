package com.enzoconserve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddPostActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;
    Button uploadbtn;
    EditText titleet,descet;
    ImageView imaget,userimage;
    Uri image_uri=null;
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_CODE=300;
    private static final int IMAGE_PICK_CAMERA_CODE=400;
    String cameraPermission[];
    String storgaePermission[];

    //progress bar
    ProgressDialog pd;
    TextView username;
    String name,email,uid,dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        cameraPermission= new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storgaePermission= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd=new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();
        checkuserstatus();
        username=findViewById(R.id.username_post);
        userimage=findViewById(R.id.profile_pic_post);
        //get some info of current uswr to include in post
        userDbRef = FirebaseDatabase.getInstance().getReference("users");
        Query query=userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    name=""+ds.child("name").getValue();
                    email=""+ds.child("email").getValue();
                    dp=""+ds.child("image").getValue();
                }
                username.setText(name);
                Glide.with(getBaseContext())
                        .load(dp)
                        .placeholder(R.drawable.newsgradient)
                        .error(R.drawable.newsgradient)
                        .fallback(R.drawable.newsgradient)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(userimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        uploadbtn=findViewById(R.id.pupload);
        descet=findViewById(R.id.pdesc);
        imaget=findViewById(R.id.pimage);

        //get image form camera or gallery on click
        imaget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                showImagePicDialog();
            }
        });

        //listener
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc=descet.getText().toString().trim();
                if(TextUtils.isEmpty(desc)){
                    Toast.makeText(AddPostActivity.this,"Enter Description...",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_uri==null)
                {
                    Toast.makeText(AddPostActivity.this,"Select a Photo...",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    //post with image
                    uploadData(desc,String.valueOf(image_uri));
                }

            }
        });

    }

    private void uploadData(final  String descp, String uri) {

        pd.setMessage("Publishing Post...");
        pd.show();
        //for post image name id publish time;
        final String timestamp= String.valueOf(System.currentTimeMillis());
        String filePathAndName="Posts/"+"post_"+timestamp;

        StorageReference ref= FirebaseStorage.getInstance().getReference().child(filePathAndName);
        ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image is uploaded to firebase now get its url
                Task<Uri> uritask= taskSnapshot.getStorage().getDownloadUrl();
                while(!uritask.isSuccessful());

                String downloadUri=uritask.getResult().toString();

                if(uritask.isSuccessful()){
                    //url is recieved upload post to firebase

                    HashMap<Object,String>hashMap=new HashMap<>();
                    hashMap.put("uid",uid);
                    hashMap.put("uName",name);
                    hashMap.put("uEmail",email);
                    hashMap.put("uDp",dp);
                    hashMap.put("pId",timestamp);
                    hashMap.put("pDesc",descp);
                    hashMap.put("pImage",downloadUri);
                    hashMap.put("pTime",timestamp);

                    //path to store post data
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");

                    //put data in reference
                    ref.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //adding in database
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this,"Post Published ...",Toast.LENGTH_SHORT).show();
                            //reset views
                            descet.setText("");
                            imaget.setImageURI(null);
                            image_uri=null;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddPostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddPostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showImagePicDialog() {
        //show dialog contaning optuon camera and gallery to pick the image

        String option[] = {"Camera","Gallery"};

        //alert dialog
        android.app.AlertDialog.Builder builder=new AlertDialog.Builder(this);
        //set title
        builder.setTitle("Pick Image From");
        //set items to dialog
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item click
                if(which == 0){
                    if(!checkCameraPermission()){
                        requestCameraPermisiion();
                    }
                    else
                    {
                        pickFromCamera();
                    }
                }
                else if(which== 1){
                    if(!checkStoragePermission()){
                        requestStoragePermisiion();
                    }
                    else
                    {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show
        builder.create().show();

    }
    private boolean checkStoragePermission(){
        //return true if enable
        //return false if not enable
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermisiion(){
        //request runtime storage permisiion
        requestPermissions(storgaePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        //return true if enable
        //return false if not enable
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermisiion(){
        //request runtime storage permisiion
        requestPermissions(cameraPermission,CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera(){
        //intent of picking image from device camera
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        //put image uri
        image_uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }
    private  void  pickFromGallery(){
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this is called when user press allow or deny from permisiion request dialog
        //here we handle permisiion cases (allowed or denied)
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                //picking from camera , first check if camera permisiion is allowed or not
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        //permission enabled
                        pickFromCamera();
                    }
                    else{
                        //permission denied
                        Toast.makeText(this, "Please enable camera and storage permisiions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                //picking from gallery , first check if storage permisiion is allowed or not
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        //permission enabled
                        pickFromGallery();
                    }
                    else{
                        //permission denied
                        Toast.makeText(this, "Please enable storage permisiion", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //this meathod will be called after picking images from camera or gallery
        if(resultCode==RESULT_OK){
            if(requestCode ==IMAGE_PICK_GALLERY_CODE){
                //iamge pick from gallery , get uri of image
                image_uri = data.getData();
                imaget.setImageURI(image_uri);
                //uploadProfileCoverPhoto(image_uri);
            }
            if(requestCode==IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera
                //uploadProfileCoverPhoto(image_uri);
                imaget.setImageURI(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        checkuserstatus();
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkuserstatus();
        super.onResume();
    }

    private void checkuserstatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            email=user.getEmail();
            uid=user.getUid();
        }
        else{
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //goto previous activity

        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
