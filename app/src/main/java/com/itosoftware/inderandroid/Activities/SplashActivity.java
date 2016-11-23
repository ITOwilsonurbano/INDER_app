package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.itosoftware.inderandroid.Api.Device.ApiDevice;
import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.UserContainer;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class SplashActivity extends Activity implements ApiUserListener {

    private Animation animIcon;
    private ImageView image;
    private Globals globals;
    private String SENDER_ID = "302889663461";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    private String regid = null;
    Context context;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = (Globals) getApplication();

        setContentView(R.layout.activity_splash);
        image = (ImageView) findViewById(R.id.logo);
        StartAnimations();

        context = getApplicationContext();
        handler = new Handler();

        // Check device for Play Services APK.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());
            if (regid == null) {
                registerInBackground();
            }else {
//                storeRegistrationName();
                finishSplash();
            }
        }else {
            Toast.makeText(this, getString(R.string.google_play_ad), Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    private void finishSplash(){
        SharedPreferences settings = this.getApplicationContext().getSharedPreferences("Preferences", 0);
        String access_tokens = settings.getString("access_token", null);
        String refresh_tokens = settings.getString("refresh_token", null);
        Log.d("register_id",regid);

        if (access_tokens!=null&&refresh_tokens!=null){
            globals.setToken(access_tokens);
            globals.setRefreshToken(refresh_tokens);
            globals.setUserIsAuthenticated(true);

            ApiUser apiUser = new ApiUser(this);
            HashMap params = new HashMap();
            params.put("token", globals.getToken());
            apiUser.getProfile(params);

            handler.postDelayed(new Runnable() {
                public void run() {
                    animIcon.cancel();
                }
            }, 3000);
        }else {
            handler.postDelayed(new Runnable() {
                public void run() {
                    animIcon.cancel();
                }
            }, 3000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * inicio de animacion
     */
    private void StartAnimations() {
        animIcon = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animIcon.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent list = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(list);
                SplashActivity.this.finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        int time = 100;
        animIcon.setRepeatCount(time);
        image.startAnimation(animIcon);
    }

    public SplashActivity() {
        super();
    }

    @Override
    public void onFinishedAddParticipant(Participant participant, Integer status) {
    }

    @Override
    public void onFinishedAuthentication(UserContainer userContainer, Boolean authenticated) {
    }

    @Override
    public void onFinishedForgetUser() {
    }

    @Override
    public void onFinishedForgetPassword() {
    }

    @Override
    public void onFinishedRegister(Boolean success, HashMap data) {
    }

    @Override
    public void onFinishedProfile(ApiUser.Info dataUser) {
        animIcon.cancel();
    }

    @Override
    public void onFinishedProfilePqr(JSONObject response) {

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, getString(R.string.google_play_ad), Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences("Preferences", 0);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("getRegistrationId", "Registration not found.");
            return null;
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("getRegistrationId", "App version changed.");
            return null;
        }
        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    storeRegistrationId(context, regid);
                    finishSplash();
                } catch (IOException ex) {
                    finish();
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = context.getSharedPreferences("Preferences", 0);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

//    private void storeRegistrationName() {
//        final SharedPreferences prefs = context.getSharedPreferences("Preferences", 0);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.commit();
//    }

}
