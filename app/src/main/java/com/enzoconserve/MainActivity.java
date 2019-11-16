package com.enzoconserve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button btnprev, btnnext;
    int current_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDotLayout = findViewById(R.id.dot_layout);
        mSlideViewPager = findViewById(R.id.slidepager);
        btnnext = findViewById(R.id.btn_next);
        btnprev = findViewById(R.id.btn_prev);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(current_page - 1);
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                s = (String) btnnext.getText();
                if (s == "Finish") {
                    startActivity(new Intent(MainActivity.this,conservelogin.class));
                    finish();
                }
                mSlideViewPager.setCurrentItem(current_page + 1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            mDotLayout.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            current_page = i;
            if (i == 0) {
                btnnext.setEnabled(true);
                btnprev.setEnabled(false);
                btnprev.setVisibility(View.INVISIBLE);
                btnnext.setText("Next");
                btnprev.setText("");
            } else if (current_page == mDots.length - 1) {
                btnnext.setEnabled(true);
                btnprev.setEnabled(true);
                btnnext.setVisibility(View.VISIBLE);
                btnprev.setVisibility(View.VISIBLE);
                btnnext.setText("Finish");
                btnprev.setText("Back");
            } else {
                btnnext.setEnabled(true);
                btnprev.setEnabled(true);
                btnprev.setVisibility(View.VISIBLE);
                btnnext.setVisibility(View.VISIBLE);
                btnnext.setText("Next");
                btnprev.setText("Prev");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
