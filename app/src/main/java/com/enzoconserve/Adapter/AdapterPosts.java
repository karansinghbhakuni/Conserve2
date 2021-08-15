package com.enzoconserve.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enzoconserve.profileActivity;
import com.enzoconserve.Model.ModelPost;
import com.enzoconserve.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

    Context context;
    ArrayList<ModelPost> postList;

    public AdapterPosts(Context context, ArrayList<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflator layout row_post
        View view= LayoutInflater.from(context).inflate(R.layout.row_posts,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final String uid=postList.get(i).getUid();
        String uEmail=postList.get(i).getuEmail();
        String uName=postList.get(i).getuName();
        String uDp=postList.get(i).getuDp();
        String pId=postList.get(i).getpId();
        String pDesc=postList.get(i).getpDesc();
        String pImage=postList.get(i).getpImage();
        String pTimeStamp=postList.get(i).getpTime();

        //convert time stamp into  dd/mm/yy hh:mm am/pm
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        //set data
        myHolder.uNameTv.setText(uName);
        myHolder.pTimeTv.setText(pTime);
        myHolder.pDescTv.setText(pDesc);

        //set userdp
        try{
            Picasso.get().load(uDp).placeholder(R.drawable.ic_default_img).into(myHolder.uPictureIv);
        }
        catch(Exception e){

        }
        //set post image
        try{
            Picasso.get().load(pImage).into(myHolder.pImageIv);
        }
        catch(Exception e){

        }
        //handle button clicks
   /*     myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"like",Toast.LENGTH_SHORT).show();
            }
        });   */
        myHolder.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click to go to thereprofileActivity with uid
                Intent intent=new Intent(context, profileActivity.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views from row_post.xml
        ImageView uPictureIv,pImageIv;
        TextView uNameTv,pTimeTv,pTitleTv,pDescTv,pLikesTv;
        ImageButton moreBtn;
        Button likeBtn,commentBtn,shareBtn;
        LinearLayout profileLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            uPictureIv=itemView.findViewById(R.id.uPictureIv);
            pImageIv=itemView.findViewById(R.id.pImageIv);
            uNameTv=itemView.findViewById(R.id.uNameTv);
            pTimeTv=itemView.findViewById(R.id.pTimeTv);
            pDescTv=itemView.findViewById(R.id.pdescTv);
            //         pLikesTv=itemView.findViewById(R.id.pLikesTv);
           // moreBtn=itemView.findViewById(R.id.moreBtn);
            //    likeBtn=itemView.findViewById(R.id.likeBtn);
            profileLayout=itemView.findViewById(R.id.profileLayout);
        }
    }

}
