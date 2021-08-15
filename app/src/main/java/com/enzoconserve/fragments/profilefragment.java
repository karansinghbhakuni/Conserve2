package com.enzoconserve.fragments;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.enzoconserve.Userinfoupdate;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class profilefragment extends Fragment{

    //permisiions constants
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=200;
    private static final int IMAGE_PICK_GALLERY_CODE=300;
    private static final int IMAGE_PICK_CAMERA_CODE=400;

    //array of permisiion to be requested
    Uri image_uri;
    String cameraPermission[];
    String storgaePermission[];
    RecyclerView postsRecyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    ProgressDialog pd;
    //storage
    StorageReference storageReference;
    String storepath = "users_Profile_Imgs/";
    ImageView avatartv,covertv;
    TextView nametv,emailtv,phonetv;

    ArrayList<ModelPost> postList;
    AdapterPosts adapterPosts;
    String uid;
    Button Dedit,Dupload;
    TextView Dname,Demail;
    ImageView Dimage;
    String name,image,email;
    String porc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        Dname=view.findViewById(R.id.display_name);
        Demail=view.findViewById(R.id.display_email);
        Dimage=view.findViewById(R.id.display_picture);
        Dedit=view.findViewById(R.id.dedit);
        Dupload=view.findViewById(R.id.dupload);
        //get uid of clicked user to retrive his post
        Intent intent = getActivity().getIntent();
        uid=intent.getStringExtra("uid");


        //init arrays of permisiions


        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =  firebaseDatabase.getReference("users");
        storageReference = getInstance().getReference();

        postsRecyclerView = view.findViewById(R.id.recyclerview_posts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //show newest post first for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set this layout to recycler view
        postsRecyclerView.setLayoutManager(layoutManager);
        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //check until required data get
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    //get data
                    name= ""+ ds.child("name").getValue();
                    email= ""+ ds.child("email").getValue();
                    image= ""+ ds.child("image").getValue();
                    uid=""+ds.child("uid").getValue();
                    //set data
                    Dname.setText(name);
                    Demail.setText(email);
                    try{
                        Glide.with(getContext())
                                .load(image)
                                .placeholder(R.drawable.boy)
                                .error(R.drawable.boy)
                                .fallback(R.drawable.boy)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(Dimage);
                    }
                    catch (Exception e){

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pd=new ProgressDialog(getActivity());


        //button Listener
        Dupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent1= new Intent(getContext(), AddPostActivity.class);
                startActivity(intent1);
            }
        });
        Dedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        checkuserstatus();
        loadMyPosts();

        return view;
    }


    private void showEditProfileDialog(){

        String option[] = {"Edit Profile Picture","Edit Name"};

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
                    pd.setMessage("Updating Picture");
                    porc= "image";
                    showImagePicDialog();
                }
                else if(which== 1){
                    pd.setMessage("Updating Name");
                    showNamePhoneUpdateDialog("name");
                }
            }
        });
        //create and show
        builder.create().show();

    }
    private void showImagePicDialog() {
        //show dialog contaning optuon camera and gallery to pick the image

        String option[] = {"Camera","Gallery"};

        //alert dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
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
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermisiion(){
        //request runtime storage permisiion
        requestPermissions(storgaePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        //return true if enable
        //return false if not enable
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermisiion(){
        //request runtime storage permisiion
        requestPermissions(cameraPermission,CAMERA_REQUEST_CODE);
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
                        Toast.makeText(getActivity(), "Please enable camera and storage permisiions", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Please enable storage permisiion", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

    }
    private void pickFromCamera(){
        //intent of picking image from device camera
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        //put image uri
        image_uri= getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

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

    private void showNamePhoneUpdateDialog(final String key) {

        //custom dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("update "+key);
        // set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        final EditText editText=new EditText(getActivity());
        editText.setHint("Enter "+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add buttons in dialog
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                final String value = editText.getText().toString().trim();
                //validate if something is written or not
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String,Object> result = new HashMap<>();
                    result.put(key, value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //updated , dismiss progress
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Updated ...",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    //if user update his name . also change it from his posts
                    if(key.equals("name")){
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
                        Query query=ref.orderByChild("uid").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    String child = ds.getKey();
                                    dataSnapshot.getRef().child(child).child("uName").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else{
                    Toast.makeText(getActivity(),"Please enter "+key,Toast.LENGTH_SHORT).show();
                }
            }
        });
        // cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void loadMyPosts() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
        //querry to load post
        Query query=ref.orderByChild("uid").equalTo(uid);
        //get all data from this user
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList =new ArrayList<ModelPost>();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelPost mypost=ds.getValue(ModelPost.class);

                    //add to list
                    postList.add(mypost);
                }
                adapterPosts = new AdapterPosts(getActivity(),postList);
                postsRecyclerView.setAdapter(adapterPosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"recycler error ...",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //this meathod will be called after picking images from camera or gallery
        if(resultCode==RESULT_OK){
            if(requestCode ==IMAGE_PICK_GALLERY_CODE){
                //iamge pick from gallery , get uri of image
                image_uri = data.getData();

                uploadProfileCoverPhoto(image_uri);
            }
            if(requestCode==IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera
                uploadProfileCoverPhoto(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {

        //path and name of image to bve stored in firebase storage
        String filePathAndName= storepath+""+ porc +""+ user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is uploaded to storage , now get its uri and store in users database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        final   Uri downloadUri = uriTask.getResult();

                        //check if image is uploaded or not and url is  received
                        if (uriTask.isSuccessful()){
                            //image uploaded
                            //add / update url in users database
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(porc,downloadUri.toString());
                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //url in database of user is added successfully
                                            //dissmis progress bar
                                            pd.dismiss();
                                            Toast.makeText(getActivity(),"Image Updated ..",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Error while updating image..",Toast.LENGTH_SHORT).show();
                                }
                            });

                            if(porc.equals("image")){
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
                                Query query=ref.orderByChild("uid").equalTo(uid);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                                            String child = ds.getKey();
                                            dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                        else{
                            //error
                            pd.dismiss();
                            Toast.makeText(getActivity(),"error occured",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //show error message
                pd.dismiss();
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void checkuserstatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            //  display.setText(user.getEmail());
            uid=user.getUid();
        }
        else{
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
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
