package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Fragments.NewsFragment;
import com.itosoftware.inderandroid.Fragments.ReservationFragment;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.graphicstest.JustifiedTextView;

/**
 * Created by rasuncion on 2/12/15.
 */
public class NewsDetailActivity extends ActionBarActivity {

    View rootView;
    ActionBar actionBars;
    TextView textView_TitleActionBar;
    ImageLoader mImageLoader;
    Globals globals;
    NetworkImageView mNetworkImageView;
    TextView title;
    JustifiedTextView mJTv;
    ImageView progress;
    Menu menu;
    String type;
    Button link_notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_news_detail);
        globals = (Globals) getApplication();
        String json = null;
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            json = bundle.getString("new");
        }

        type = bundle.getString("type");

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

        textView_TitleActionBar.setText("Noticia");
        actionBars.setDisplayHomeAsUpEnabled(true);

        actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        Log.e("Is authenticated2", globals.getUserIsAuthenticated().toString());

        //mainActivity.isAuthenticatedUser(globals.getUserIsAuthenticated());

        mImageLoader = ApiConnection.getInstance(getBaseContext()).getImageLoader();

        Gson gson = new Gson();
        New notice = gson.fromJson(json, New.class);
        Log.e("Is authenticated3", globals.getUserIsAuthenticated().toString());

        mNetworkImageView = (NetworkImageView) findViewById(R.id.fragment_news_detail_image);
        title = (TextView) findViewById(R.id.fragment_news_detail_title);
        link_notice = (Button) findViewById(R.id.link_notice);
        mJTv =(JustifiedTextView) findViewById(R.id.fragment_news_detail_justifyContent);
        progress = (ImageView) findViewById(R.id.fragment_news_detail_progressbar);

        if (!notice.getUrl().isEmpty()) {
            String secureProtocole = notice.getUrl().substring(0,8);
            String protocole = notice.getUrl().substring(0,7);
            String protocoleSecureTrue = "https://";
            String protocoleTrue = "http://";

            String urlNotice = "";
            if(protocole.equals(protocoleTrue) || secureProtocole.equals(protocoleSecureTrue)){
                urlNotice = notice.getUrl();
            }else{
                urlNotice = "http://"+notice.getUrl();
            }

            Log.d("urlNotice",urlNotice);

            final String link = urlNotice;

            Log.d("secureProtocole",secureProtocole);
            Log.d("protocole",protocole);

            link_notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browserIntent);
                }
            });
        } else {
            link_notice.setVisibility(View.GONE);
        }

        Log.e("Is authenticated4", globals.getUserIsAuthenticated().toString());
        mNetworkImageView.setImageUrl(notice.getImageUrl(), mImageLoader);
        if (progress.getBackground() != null) {
            AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
            frameAnimation.start();
        }
        mNetworkImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (mNetworkImageView.getDrawable() != null) {
                    progress.setVisibility(View.GONE);
                }
                else{
                    mNetworkImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lista_noticias_broken) );
                    Log.i("Error", "Cargando imagen url.");
                }
            }
        });

        title.setText(notice.getTitle());
        mJTv.setText(notice.getDescription());
        mJTv.setLineSpacing(0);
        mJTv.setAlignment(Paint.Align.LEFT);
        mJTv.setTypeFace(Typeface.createFromAsset(getBaseContext().getAssets(), "HelveticaNeueLTStd-LtCn.otf"));
        Log.e("Is authenticated5", globals.getUserIsAuthenticated().toString());

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
                    if(type.equals("activity")){
                        finish();
                    }else if(type.equals("notification")){
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        mainActivity.putExtra("openTab", 1);
                        startActivity(mainActivity);
                    }
                }catch (Exception e){
                    Log.e("Error =>", e.toString());
                }
                return true;
            case  R.id.action_settings :
                Intent settingsActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsActivity);

                return true;
            case R.id.logout_action :
                globals.logoutButton();
                isAuthenticatedUser(globals.getUserIsAuthenticated());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("valid", 0);
                setResult(Activity.RESULT_OK, resultIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
