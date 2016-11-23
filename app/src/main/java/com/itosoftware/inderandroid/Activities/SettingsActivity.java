package com.itosoftware.inderandroid.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itosoftware.inderandroid.R;

public class SettingsActivity extends ActionBarActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

        textView_TitleActionBar.setText("Configuraciones");

        SharedPreferences settings = this.getApplicationContext().getSharedPreferences("Preferences", 0);

        String access_tokens = settings.getString("access_token", null);
        String refresh_tokens = settings.getString("refresh_token", null);

        Boolean notification_news = settings.getBoolean("notification_news", true);
        CheckBox notificationsNews = (CheckBox) findViewById(R.id.activity_settings_row_one_check);
        notificationsNews.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean notificationsNews = b;
                saveNotifications("notification_news", notificationsNews);
                Log.d("Result Changed "," notification_news =>  "+notificationsNews.toString());
            }
        });
        if(notification_news == true){
            notificationsNews.setChecked(true);
        }else{
            notificationsNews.setChecked(false);
        }

        Boolean notification_reservation = settings.getBoolean("notification_reservations", false);
        LinearLayout sectionReservation = (LinearLayout) findViewById(R.id.activity_settings_row_two);
        CheckBox notificationsReservation = (CheckBox) findViewById(R.id.activity_settings_row_two_check);
        notificationsReservation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean notificationsReservations = b;
                saveNotifications("notification_reservations",notificationsReservations);
                Log.d("Result Changed "," notification_reservations => "+notificationsReservations.toString());
            }
        });
        if(notification_reservation == true){
            notificationsReservation.setChecked(true);
        }else{
            notificationsReservation.setChecked(false);
        }

        if(access_tokens == null){
            sectionReservation.setVisibility(View.GONE);
        }else{
            sectionReservation.setVisibility(View.VISIBLE);
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

    private void saveNotifications(String nameNotification,Boolean notification) {
        final SharedPreferences prefs = getApplicationContext().getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(nameNotification, notification);
        editor.commit();
    }

}
