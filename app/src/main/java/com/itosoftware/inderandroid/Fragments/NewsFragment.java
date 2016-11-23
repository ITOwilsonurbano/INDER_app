package com.itosoftware.inderandroid.Fragments;

import com.itosoftware.inderandroid.Activities.NewsDetailActivity;
import com.itosoftware.inderandroid.Adapters.NewsAdapter;
import com.itosoftware.inderandroid.Api.News.ApiNews;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Interface.NewsListener;
import com.itosoftware.inderandroid.R;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.HashMap;

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, NewsListener {

    private ArrayList<New> news;

    Integer page = new Integer(1);
    Integer limit = new Integer(10);
    Integer maxPage = new Integer(1);

    ListView listView;
    int currentLastVisible;
    int currentVisibleItemCount;
    Integer currentPage = new Integer(1);

    ImageView progress;

    NewsAdapter adapter;
    NewsFragment newsFragment;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("page", page);
        params.put("limit",limit);

        ApiNews apiNews = new ApiNews(this);
        apiNews.getNews(params);

        newsFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_news, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.fragment_news);

        listView  = (ListView) rootview.findViewById(R.id.list_news);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);

        progress = (ImageView) rootview.findViewById(R.id.fragment_news_progressbar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        this.news = new ArrayList<New>();
        adapter = new NewsAdapter(getContext().getApplicationContext(), this.news);
        final ApiNews apiNews = new ApiNews(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                page = 1;
                HashMap<String, Integer> params = new HashMap<>();
                params.put("page", page);
                params.put("limit",limit);
                apiNews.getNews(params);
            }
        });

        return rootview;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getId() == listView.getId()) {
            if ((currentLastVisible + 1) == listView.getCount()) {
                if(currentPage == page){
                    if(maxPage >= page) {
                        page++;
                    }
                    if(maxPage >= page){
                        progress.setBackgroundResource(R.drawable.progress_animation);
                        progress.setVisibility(View.VISIBLE);
                        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
                        frameAnimation.start();

                        HashMap<String, Integer> params = new HashMap<>();
                        params.put("page", page);
                        params.put("limit",limit);

                        ApiNews apiNews = new ApiNews(this);
                        apiNews.getNews(params);
                    }
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        currentLastVisible = listView.getLastVisiblePosition();
        currentVisibleItemCount = visibleItemCount;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        try{
            Intent newsDetailIntent = new Intent(getActivity(), NewsDetailActivity.class );
            newsDetailIntent.putExtra("new", news.get(position).toString());
            newsDetailIntent.putExtra("type", "activity");
            getActivity().startActivityForResult(newsDetailIntent, 4);

        }catch (Exception e){
            Log.e("Error =>", e.toString());
        }
    }

    @Override
    public void onfinishedNews(ArrayList<New> datos, Integer maxPage) {
        this.maxPage = maxPage;
        this.currentPage = this.page;

        if(!datos.isEmpty()){
            for(New dato : datos){
                adapter.add(dato);
            }
            int index = listView.getFirstVisiblePosition();
            View v = listView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            listView.setAdapter(adapter);
            listView.setSelectionFromTop(index, top);
            progress.setBackgroundDrawable(null);
            progress.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onfinishedNew(New notice) {

    }
}