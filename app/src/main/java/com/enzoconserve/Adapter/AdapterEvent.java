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

import com.enzoconserve.Model.ModelEvent;
import com.enzoconserve.Model.ModelPost;
import com.enzoconserve.R;
import com.enzoconserve.profileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.MyHolder>{
    Context context;
    ArrayList<ModelEvent> eventList;

    public AdapterEvent(Context context, ArrayList<ModelEvent> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_events,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final String uid=eventList.get(i).getUid();
        String uEmail=eventList.get(i).getuEmail();
        String uName=eventList.get(i).getuName();
        String uDp=eventList.get(i).getuDp();
        String pId=eventList.get(i).getpId();
        String pDesc=eventList.get(i).getpDesc();
        String pImage=eventList.get(i).getpImage();
        String pTimeStamp=eventList.get(i).getpTime();
        String pTitle=eventList.get(i).getpTitle();
        String ploc=eventList.get(i).getpLoc();
        String pTime1=eventList.get(i).getpTime1();
        String pDate=eventList.get(i).getpDate();
        //convert time stamp into  dd/mm/yy hh:mm am/pm
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
      //  calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        //set data
        myHolder.pDate.setText(pDate);
        myHolder.pTimeTv.setText(pTime1);
        myHolder.pTitleTv.setText(pTitle);
        myHolder.pAddr.setText(ploc);
        myHolder.uNameTv.setText(uName);
   //     myHolder.pTimeTv.setText(pTime);
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
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views from row_post.xml
        ImageView uPictureIv,pImageIv;
        TextView uNameTv,pTimeTv,pTitleTv,pDescTv,pTime,pDate,pAddr;
        Button likeBtn,commentBtn,shareBtn;
        LinearLayout profileLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init views
            pTime=itemView.findViewById(R.id.ptimeTv);
            pDate=itemView.findViewById(R.id.pdateTv);
            uPictureIv=itemView.findViewById(R.id.uPictureIv);
            pImageIv=itemView.findViewById(R.id.pImageIv);
            uNameTv=itemView.findViewById(R.id.uNameTv);
            pTimeTv=itemView.findViewById(R.id.pTimeTv);
            pTitleTv=itemView.findViewById(R.id.ptitleTv);
            pDescTv=itemView.findViewById(R.id.pdescTv);
            pAddr=itemView.findViewById(R.id.ploctv);
            profileLayout=itemView.findViewById(R.id.profileLayout);
        }
    }
}
