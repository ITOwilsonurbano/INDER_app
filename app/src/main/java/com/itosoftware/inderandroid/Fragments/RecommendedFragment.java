package com.itosoftware.inderandroid.Fragments;

import com.itosoftware.inderandroid.Activities.NewsDetailActivity;
import com.itosoftware.inderandroid.Activities.RecommendedDetailActivity;
import com.itosoftware.inderandroid.Adapters.NewsAdapter;
import com.itosoftware.inderandroid.Adapters.RecommendedAdpater;
import com.itosoftware.inderandroid.Api.News.ApiNews;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.Recommended.ApiRecommended;
import com.itosoftware.inderandroid.Api.Recommended.Recommended;
import com.itosoftware.inderandroid.R;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class RecommendedFragment extends Fragment  implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    GridView gridView;
    ArrayList<Recommended> itemsRecommended = new ArrayList<Recommended>();
    View rootView;

    Integer page = new Integer(1);
    Integer limit = new Integer(10);
    Integer maxPage = new Integer(1);

    int currentLastVisible;
    int currentVisibleItemCount;
    Integer currentPage = new Integer(1);

    ImageView progress;

    RecommendedAdpater adapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashMap<String, Integer> params = new HashMap<>();
        params.put("page", page);
        params.put("limit",limit);

        ApiRecommended apiRecommended = new ApiRecommended(this);
        apiRecommended.getRecommended(params);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_recommended,container,false);
        gridView = (GridView) rootView.findViewById(R.id.fragment_recommended_grid_layout);
        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(this);

        progress = (ImageView) rootView.findViewById(R.id.fragment_recommended_progressbar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_ecommended_swipe);

        adapter = new RecommendedAdpater(getContext().getApplicationContext(), itemsRecommended);

        final  ApiRecommended apiRecommended = new ApiRecommended(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                page = 1;
                HashMap<String, Integer> params = new HashMap<>();
                params.put("page", page);
                params.put("limit",limit);

                apiRecommended.getRecommended(params);
            }
        });

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        try{
            // Create fragment and give it an argument specifying the article it should show

            Intent recomendedDetailIntent = new Intent(getActivity(), RecommendedDetailActivity.class );
            recomendedDetailIntent.putExtra("recomended", itemsRecommended.get(position).toString());
            getActivity().startActivityForResult(recomendedDetailIntent, 4);

            /*RecommendedDetailFragment recommendedDetailFragment = new RecommendedDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putString("recomended", itemsRecommended.get(position).toString());
            recommendedDetailFragment.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_recommended, recommendedDetailFragment);
            transaction.addToBackStack(null);
            transaction.commit();*/

        }catch (Exception e){
            Log.e("Error =>", e.toString());
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getId() == gridView.getId()) {
            if ((currentLastVisible + 1) == gridView.getCount()) {
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

                        ApiRecommended apiRecommended = new ApiRecommended(this);
                        apiRecommended.getRecommended(params);
                    }
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        currentLastVisible = gridView.getLastVisiblePosition();
        Log.w("list", "currentLast:" + currentLastVisible + "max:" + gridView.getCount());
        currentVisibleItemCount = visibleItemCount;
    }


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onFinishedConnection(ArrayList<Recommended> datos, Integer maxPage){
        this.maxPage = maxPage;
        this.currentPage = this.page;

        if(!datos.isEmpty()){
            for(Recommended dato : datos){
                adapter.add(dato);
            }
            int index = gridView.getFirstVisiblePosition();
            View v = gridView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
            gridView.setAdapter(adapter);
//            gridView.setSelectionFromTop(index, top);

            adapter.notifyDataSetChanged();
        }
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

    }




}