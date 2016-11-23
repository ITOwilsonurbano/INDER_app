package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.UserContainer;
import com.itosoftware.inderandroid.Fragments.MessageFragment;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.R;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPasswordActivity extends ActionBarActivity implements ApiUserListener {
    ActionBar actionBar;
    ImageView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Set title and back button
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Center the action bar title
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView textView_TitleActionBar = (TextView) viewActionBar.findViewById(R.id.title_actionbar);

        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true); // Se habilita custom para tomar el layout.
        actionBar.setDisplayShowTitleEnabled(false);

        textView_TitleActionBar.setText("Recuperar Contraseña");

        progress = (ImageView) findViewById(R.id.activity_forget_password_loading_panel);

        WebView webView = (WebView) findViewById(R.id.activity_forget_password_webview);
        webView.setWebViewClient(new MyWebViewClient());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl("http://www.inder.gov.co/index.php/Contrasena-perdida.html");

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try{
                    finish();
                }catch (Exception e){
                    Log.e("Error =>", e.toString());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Hide keyboard by touching anywhere on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


    @Override
    public void onFinishedAuthentication(UserContainer userContainer, Boolean authenticated) {}
    @Override
    public void onFinishedRegister(Boolean success, HashMap data) {}
    @Override
    public void onFinishedForgetPassword() {
        FragmentManager fm = getSupportFragmentManager();

        String message = "Se ha enviado un mensaje a su correo para que pueda recuperar su contraseña.";

        MessageFragment dFragment = new MessageFragment();

        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        dFragment.setArguments(bundle);

        // Show DialogFragment
        dFragment.show(fm, "Dialog Fragment");
    }
    @Override
    public void onFinishedForgetUser() {  }
    @Override
    public void onFinishedAddParticipant(Participant participant, Integer status) {

    }

    @Override
    public void onFinishedProfile(ApiUser.Info dataUser) {

    }

    @Override
    public void onFinishedProfilePqr(JSONObject response) {

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
}
