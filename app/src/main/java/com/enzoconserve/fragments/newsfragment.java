package com.enzoconserve.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.enzoconserve.R;
import com.enzoconserve.news.Adapter;
import com.enzoconserve.news.ApiClient;
import com.enzoconserve.news.Articles;
import com.enzoconserve.news.Headlines;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newsfragment extends Fragment {

    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    Dialog dialog;
    final String API_KEY="c4f6128bf69545da89c0fa6e3b7148f0";
    Adapter adapter;
    String env="Climate";
    List<Articles> articles = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.newsapi,container,false);
        Context context=getActivity();

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_news);

        dialog = new Dialog(getContext());

        recyclerView = view.findViewById(R.id.recyclerview_news);
       // LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
  //      final String country=getCountry();
    //    retrieveJson(env,country,API_KEY);


        //swiperefresh
       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson(env,API_KEY);
            }
        });
        retrieveJson(env,API_KEY);
        return view;
    }

    public void retrieveJson(String query,String apikey){
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;

            call= ApiClient.getInstance().getApi().getSpecificData(query,"en",apikey);

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(getContext(), articles);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
  /*  public  String getCountry(){
        Locale locale=Locale.getDefault();
        String country=locale.getCountry();
        return country.toLowerCase();
    } */

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
