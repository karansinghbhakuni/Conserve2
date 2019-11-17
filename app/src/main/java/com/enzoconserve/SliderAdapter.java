package com.enzoconserve;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    //arrays
    public int[] slide_images={
            R.drawable.icon1,
            R.drawable.icon2,
            R.drawable.icon3
    };

    public String[] slide_headings= {
            "TREE",
            "RECYCLE",
            "PLANT"

    };
    public String slide_descs[] ={
            "Planting trees is planting hope",
            "What's more important than recycling? Producing something to recycle.\n" +
                    "\n",
            "So plant your own gardens and decorate your own soul, instead of waiting for someone to bring you flowers",
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, Object o) {
        return view==(RelativeLayout)o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);
        ImageView slideImageView=view.findViewById(R.id.slide_image);
        TextView slideHeading=view.findViewById(R.id.slide_heading);
        TextView slideDescription=view.findViewById(R.id.slide_desc);
        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
