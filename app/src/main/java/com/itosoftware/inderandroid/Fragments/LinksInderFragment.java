package com.itosoftware.inderandroid.Fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.itosoftware.inderandroid.Activities.LinksInderDetailActivity;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.NewsDetailActivity;
import com.itosoftware.inderandroid.Adapters.LinksInderAdapter;
import com.itosoftware.inderandroid.Api.LinksCategory.ApiLinksCategory;
import com.itosoftware.inderandroid.Api.LinksCategory.LinksCategory;
import com.itosoftware.inderandroid.Api.Recommended.ApiRecommended;
import com.itosoftware.inderandroid.Interface.LinksCategoryListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.graphicstest.AnimatedExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LinksInderFragment extends Fragment implements LinksCategoryListener, AbsListView.OnScrollListener {

    public AnimatedExpandableListView listView;
    public LinksInderAdapter adapter;
    public ImageView progress;
    private ArrayList<LinksCategory> linksCategory;
    Integer page = new Integer(1);
    Integer limit = new Integer(10);
    Integer maxPage = new Integer(1);

    int currentLastVisible;
    int currentVisibleItemCount;
    Integer currentPage = new Integer(1);
    List<LinksInderAdapter.GroupItem> items;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("page", page);
        params.put("limit",limit);

        ApiLinksCategory apiLinksCategory = new ApiLinksCategory(this);
        apiLinksCategory.getLinksCategory(params);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootview = inflater.inflate(R.layout.fragment_links_inder, container, false);

        items = new ArrayList<LinksInderAdapter.GroupItem>();

        adapter = new LinksInderAdapter(getContext());
        adapter.setData(items);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.fragment_links_inder_swipe);

        progress = (ImageView) rootview.findViewById(R.id.fragment_links_inder_progressbar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        listView = (AnimatedExpandableListView) rootview.findViewById(R.id.fragment_links_inder_list);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int ii, long l) {
                Integer posGroup = i;
                Integer posItem = ii;
                LinksInderAdapter.ChildItem item = items.get(posGroup).items.get(posItem);

                Intent inksInderDetailIntent = new Intent(getActivity(), LinksInderDetailActivity.class );
                inksInderDetailIntent.putExtra("title", item.title);
                inksInderDetailIntent.putExtra("url", item.url);
                getActivity().startActivityForResult(inksInderDetailIntent, 4);

                return false;
            }
        });

        final ApiLinksCategory apiLinksCategory = new ApiLinksCategory(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items = new ArrayList<LinksInderAdapter.GroupItem>();
                adapter = new LinksInderAdapter(getContext());
                adapter.setData(items);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                page = 1;
                HashMap<String, Integer> params = new HashMap<>();
                params.put("page", page);
                params.put("limit",limit);

                apiLinksCategory.getLinksCategory(params);
            }
        });

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            if (listView.isGroupExpanded(groupPosition)) {
                listView.collapseGroupWithAnimation(groupPosition);
            } else {
                listView.expandGroupWithAnimation(groupPosition);
            }
            return true;
            }

        });

        return rootview;

    }

    @Override
    public void onfinishedLoadLinksCategory(ArrayList<LinksCategory> linksCategory, Integer maxPage) {
        this.maxPage = maxPage;
        this.currentPage = this.page;
        if(!linksCategory.isEmpty()){
            int index = listView.getFirstVisiblePosition();
            View v = listView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();
//            listView.setSelectionFromTop(index, top);

            for(LinksCategory linkCategory : linksCategory){
                LinksInderAdapter.GroupItem item = new LinksInderAdapter.GroupItem();

                item.title = linkCategory.getNombre();
                ArrayList<HashMap> itemsParent = linkCategory.getEnlaces();

                for(HashMap childItem : itemsParent) {
                    LinksInderAdapter.ChildItem child = new LinksInderAdapter.ChildItem();
                    child.title = (String) childItem.get("titulo");
                    child.url = (String) childItem.get("url");

                    item.items.add(child);
                }

                items.add(item);

            }
            adapter.setData(items);

            adapter.notifyDataSetChanged();

        }
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
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

                        ApiLinksCategory apiNews = new ApiLinksCategory(this);
                        apiNews.getLinksCategory(params);
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


}
