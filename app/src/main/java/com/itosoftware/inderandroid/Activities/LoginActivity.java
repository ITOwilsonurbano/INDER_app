package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.UserContainer;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.R;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends ActionBarActivity implements ApiUserListener {

    ActionBar actionBar;
    ImageView progress;
    public EditText txtUser;
    public EditText txtUserPasword;
    public static final String PROPERTY_REG_ID = "registration_id";
    String jsonSportScenario;
    Context context;
    Menu menu;
    Globals globals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this.getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globals = (Globals) getApplication();

        // Oculta Teclado
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent extras = getIntent();
        jsonSportScenario = extras.getStringExtra("sportScenario");

        // Set title and back button
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Center the action bar title
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                //Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView textView_TitleActionBar = (TextView) viewActionBar.findViewById(R.id.title_actionbar);

        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true); // Se habilita custom para tomar el layout.
        actionBar.setDisplayShowTitleEnabled(false);

        textView_TitleActionBar.setText("Iniciar Sesión");
        //actionBar.setTitle("");

        progress = (ImageView) this.findViewById(R.id.activity_login_progressbar);
        progress.setBackgroundDrawable(null);

        txtUser = (EditText) this.findViewById(R.id.activity_login_input_user);
        txtUserPasword = (EditText) this.findViewById(R.id.activity_login_input_pass);

        txtUser.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        txtUserPasword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        Button register = (Button) this.findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
                frameAnimation.start();

                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(registerActivity, 5);

            }
        });

        Button forgetUser = (Button) this.findViewById(R.id.activity_login_button_forget_user);
        forgetUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent forgetUserActivity = new Intent(getApplicationContext(), ForgetUserActivity.class);
                startActivity(forgetUserActivity);
            }
        });


        Button forgetPassword = (Button) this.findViewById(R.id.activity_login_button_forget_password);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent forgetPasswordActivity = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(forgetPasswordActivity);
            }
        });

        final Activity activity = this;
        Button login = (Button) this.findViewById(R.id.button_ingresar);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                String user = txtUser.getText().toString();
                String pass = txtUserPasword.getText().toString();
                int errors = 0;
                if(user.matches("") || user.equals(null) ){
                    txtUser.setError("Ingrese un nombre de usuario");
                    errors ++;
                }
                if(pass.matches("") || pass.equals(null)){
                    txtUserPasword.setError("Ingrese una contraseña");
                    errors ++;
                }
                if(errors == 0){
                    progress.setBackgroundResource(R.drawable.progress_animation);
                    AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
                    frameAnimation.start();


                    final SharedPreferences prefs = context.getSharedPreferences("Preferences", 0);
                    String registrationId = prefs.getString(PROPERTY_REG_ID, "");

                    ApiUser apiUser = new ApiUser(activity);

                    HashMap params = new HashMap();
                    params.put("username",txtUser.getText().toString());
                    params.put("password",txtUserPasword.getText().toString());
                    params.put("device_id",registrationId);
                    params.put("device_type","android");

                    apiUser.authenticatedUser(params);
                }else{
                    Toast.makeText(getApplicationContext(), "Los campos marcados son obligatorios", Toast.LENGTH_LONG).show();
                }




            }
        });

        ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    try{
                        InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        return true;
                    }catch (Exception e){
                        Log.e("Error scroll",e.getMessage());
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        progress.setBackgroundDrawable(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("valid", 0);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
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

    //Hide keyboard by touching anywhere on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }

    @Override
    public void onFinishedAuthentication(UserContainer userContainer, Boolean authenticated) {

        if(authenticated.equals(true)){
            Globals globals = (Globals) getApplication();

            globals.setUserIsAuthenticated(true);
            globals.setToken(userContainer.getAccess_token());
            globals.setRefreshToken(userContainer.getRefresh_token());
            SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("access_token", userContainer.getAccess_token());
            editor.putString("refresh_token", userContainer.getRefresh_token());
            editor.commit();

            HashMap parameters = new HashMap();
            parameters.put("token",userContainer.getAccess_token());

            ApiUser apiUser = new ApiUser(this);
            apiUser.getProfile(parameters);

        }else{
            progress.setBackgroundDrawable(null);
            Toast.makeText(getApplicationContext(), "Usuario o Contraseña no validos", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onFinishedRegister(Boolean success, HashMap data) {
    }
    @Override
    public void onFinishedForgetPassword() {
    }
    @Override
    public void onFinishedForgetUser() {
    }
    public void onFinishedAddParticipant(Participant participant, Integer status) {

    }

    @Override
    public void onFinishedProfile(ApiUser.Info dataUser) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("valid", 1);
        resultIntent.putExtra("jsonSportScenario",jsonSportScenario);
        setResult(Activity.RESULT_OK, resultIntent);
        progress.setBackgroundDrawable(null);
        finish();
    }

    @Override
    public void onFinishedProfilePqr(JSONObject response) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Activity Result: ",requestCode+" "+resultCode);
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                Integer authenticated = (Integer) data.getExtras().getInt("authenticated");
                if(authenticated.equals(1)){
                    Log.i(" Result fine: ",requestCode+" "+resultCode);

                    Globals globals = (Globals) getApplication();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("valid", 1);
                    resultIntent.putExtra("jsonSportScenario",jsonSportScenario);
                    setResult(Activity.RESULT_OK, resultIntent);
                    progress.setBackgroundDrawable(null);
                    finish();
                }else{
                    Log.i(" Result bad : ",requestCode+" "+resultCode);

                }
            }
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

}
