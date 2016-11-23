package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.R;

/**
 * Created by rasuncion on 3/12/15.
 */
public class LinksInderDetailActivity extends ActionBarActivity {

    ActionBar actionBars;
    private WebView webView;
    ImageView progress;
    Globals globals;
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_links_inder_detail);
        globals = (Globals) getApplication();

        actionBars = getSupportActionBar();
        // Center the action bar title
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                //Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView textView_TitleActionBar = (TextView) viewActionBar.findViewById(R.id.title_actionbar);

        actionBars.setCustomView(viewActionBar, params);
        actionBars.setDisplayShowCustomEnabled(true); // Se habilita custom para tomar el layout.
        actionBars.setDisplayShowTitleEnabled(false);

        textView_TitleActionBar.setText("Enlaces Inder");

        actionBars.setDisplayHomeAsUpEnabled(true);
        actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        String url = "";
        String title = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            title = bundle.getString("title");
            url = bundle.getString("url");
        }

        Log.e("Losassd", url.toString());

        progress = (ImageView) findViewById(R.id.fragment_links_inder_detail_loading_panel);

        webView = (WebView) findViewById(R.id.fragment_links_inder_detail_webview);
        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl(url);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        menu.findItem(R.id.action_search_reservation).setVisible(false);
        menu.findItem(R.id.action_reload_map).setVisible(false);
        invalidateOptionsMenu();
        isAuthenticatedUser(globals.getUserIsAuthenticated());

        return super.onCreateOptionsMenu(menu);
    }

    public void isAuthenticatedUser(Boolean value){
        MenuItem menuItem = menu.findItem(R.id.logout_action);
        if(value){
            menuItem.setVisible(true);
        }else{
            menuItem.setVisible(false);
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("Is authenticated", globals.getUserIsAuthenticated().toString());
                try{
                    finish();
                }catch (Exception e){
                    Log.e("Error =>", e.toString());
                }
                return true;
            case R.id.logout_action :
                globals.logoutButton();
                isAuthenticatedUser(globals.getUserIsAuthenticated());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("valid", 0);
                setResult(Activity.RESULT_OK, resultIntent);
                return true;
            case  R.id.action_settings :
                Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
