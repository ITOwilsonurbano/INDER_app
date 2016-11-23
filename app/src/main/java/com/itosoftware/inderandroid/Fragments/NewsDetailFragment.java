package com.itosoftware.inderandroid.Fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.graphicstest.JustifiedTextView;

public class NewsDetailFragment extends Fragment{

    View rootView;
    ActionBar actionBars;
    TextView textView_TitleActionBar;
    MainActivity mainActivity;
    ImageLoader mImageLoader;


    private static final String URL = "file:///android_asset/helveticaNeueLTStd-LtCn.html";

    protected int getContentView() {
        return R.layout.fragment_news_detail;
    }

    Globals globals;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.d("poraqui ", "esta ingresando ");

        globals = (Globals) getActivity().getApplication();

        mainActivity = (MainActivity) this.getActivity();
        actionBars = mainActivity.getActionBarActivity();
        textView_TitleActionBar = mainActivity.getTextView_TitleActionBar();

        mainActivity.isAuthenticatedUser(globals.getUserIsAuthenticated());

        textView_TitleActionBar.setText("Noticia");
        actionBars.setDisplayHomeAsUpEnabled(true);

        actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        mImageLoader = ApiConnection.getInstance(getContext()).getImageLoader();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        globals.setSiSwiping(false);
        String json = null;
        Bundle bundle = getArguments();
        if(bundle != null){
            json = bundle.getString("new");
        }

        Log.d("poraqui ","esta ingresando ");
        Log.d("poraqui ",json);
        Gson gson = new Gson();
        New notice = gson.fromJson(json, New.class);

        final ViewHolder holder;
        if(rootView == null){
            holder = new ViewHolder();
            rootView = inflater.inflate(R.layout.fragment_news_detail, container, false);

            holder.mNetworkImageView = (NetworkImageView) rootView.findViewById(R.id.fragment_news_detail_image);
            holder.title = (TextView) rootView.findViewById(R.id.fragment_news_detail_title);
            holder.mJTv =(JustifiedTextView) rootView.findViewById(R.id.fragment_news_detail_justifyContent);
            holder.progress = (ImageView) rootView.findViewById(R.id.fragment_news_detail_progressbar);
            holder.link_notice = (Button) rootView.findViewById(R.id.link_notice);

            rootView.setTag(holder);
        }else{
            holder = (ViewHolder) rootView.getTag();
        }

        Log.e("Is authenticated4", globals.getUserIsAuthenticated().toString());
        holder.mNetworkImageView.setImageUrl(notice.getImageUrl(), mImageLoader);
        if (holder.progress.getBackground() != null) {
            AnimationDrawable frameAnimation = (AnimationDrawable) holder.progress.getBackground();
            frameAnimation.start();
        }
        holder.mNetworkImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (holder.mNetworkImageView.getDrawable() != null) {
                    holder.progress.setVisibility(View.GONE);
                }
            }
        });

        holder.title.setText(notice.getTitle());
        holder.mJTv.setText(notice.getDescription());
        holder.mJTv.setLineSpacing(0);
        holder.mJTv.setAlignment(Paint.Align.LEFT);
        holder.mJTv.setTypeFace(Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeueLTStd-LtCn.otf"));

        if (!notice.getUrl().isEmpty()) {
            final String link = notice.getUrl();
            holder.link_notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browserIntent);
                }
            });
        } else {
            holder.link_notice.setVisibility(View.GONE);
        }
        Log.e("Is authenticated5Paila", notice.toString());
        Log.e("Is authenticated5", notice.toString());
        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("Is authenticated", globals.getUserIsAuthenticated().toString());
                try{
                    // Create fragment and give it an argument specifying the article it should show
                    actionBars.setTitle("Noticias");
                    actionBars.setDisplayHomeAsUpEnabled(false);
                    actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                    globals.setSiSwiping(true);
                    for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                        getFragmentManager().popBackStack();
                    }
                }catch (Exception e){
                    Log.e("Error =>", e.toString());
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ViewHolder {
        NetworkImageView mNetworkImageView;
        ImageView progress;
        JustifiedTextView mJTv;
        TextView title;
        Button link_notice;
    }




}



