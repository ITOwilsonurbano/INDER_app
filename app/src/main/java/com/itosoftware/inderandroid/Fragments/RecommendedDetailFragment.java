package com.itosoftware.inderandroid.Fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.Recommended.Recommended;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.R;

public class RecommendedDetailFragment extends Fragment {

    View rootView;
    ActionBar actionBars;
    TextView textView_TitleActionBar;
    private WebView webView;
    ImageView progress;
    Globals globals;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        globals = (Globals) getActivity().getApplication();
        MainActivity mainActivity = (MainActivity) this.getActivity();
        actionBars = mainActivity.getActionBarActivity();
        textView_TitleActionBar = mainActivity.getTextView_TitleActionBar();

        textView_TitleActionBar.setText("Recomendado");

        actionBars.setDisplayHomeAsUpEnabled(true);
        actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        globals.setSiSwiping(false);
        rootView = inflater.inflate(R.layout.fragment_recommended_detail, container, false);

        String json = null;
        Bundle bundle = getArguments();
        if(bundle != null){
            json = bundle.getString("recomended");
        }
        Gson gson = new Gson();
        Recommended recommended = gson.fromJson(json, Recommended.class);

        progress = (ImageView) rootView.findViewById(R.id.fragment_recommended_detail_loading_panel);

        webView = (WebView) rootView.findViewById(R.id.fragment_recommended_detail_webview);
        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(recommended.getUrl());

        return rootView;
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setBackgroundDrawable(null);
            progress.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
            progress.setBackgroundResource(R.drawable.progress_animation);
            AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
            frameAnimation.start();


            super.onPageStarted(view, url, favicon);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try{
                    // Create fragment and give it an argument specifying the article it should show
                    actionBars.setTitle("Recomendados");
                    actionBars.setDisplayHomeAsUpEnabled(false);
                    actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                    for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                        getFragmentManager().popBackStack();
                    }

                }catch (Exception e){
                    Log.e("Error =>", e.toString());
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


