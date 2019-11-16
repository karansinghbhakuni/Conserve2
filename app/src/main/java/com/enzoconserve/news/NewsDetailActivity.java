package com.enzoconserve.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.enzoconserve.R;

public class NewsDetailActivity extends AppCompatActivity {

    TextView tvTitle,tvSource,tvTime,tvDesc;
    ImageView imageView;
    WebView webView;
    ProgressBar loader,loder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //init
        tvTitle = findViewById(R.id.news_title);
        tvSource = findViewById(R.id.news_source);
        tvTime = findViewById(R.id.news_date);
        tvDesc = findViewById(R.id.news_desc);
        imageView = findViewById(R.id.news_image);
        webView=findViewById(R.id.webview);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String source=intent.getStringExtra("source");
        String time=intent.getStringExtra("time");
        String desc=intent.getStringExtra("desc");
        String image=intent.getStringExtra("imageUrl");
        String url=intent.getStringExtra("url");

        loder1=findViewById(R.id.swipeloader);
        loder1.setVisibility(View.VISIBLE);
        loader=findViewById(R.id.swipe_loader);
        loader.setVisibility(View.VISIBLE);

        tvTitle.setText(title);
        tvSource.setText(source);
        tvTime.setText(time);
        tvDesc.setText(desc);

        Glide.with(NewsDetailActivity.this)
                .load(image)
                .error(R.drawable.black_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        if (webView.isShown()){
            loader.setVisibility(View.INVISIBLE);
            loder1.setVisibility(View.INVISIBLE);
        }
    }
}
