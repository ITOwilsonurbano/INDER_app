package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;
import com.itosoftware.inderandroid.Api.SportActivities.ApiActivitiesScenarios;
import com.itosoftware.inderandroid.Api.SportActivities.Offers;
import com.itosoftware.inderandroid.Api.SportActivities.ScheduleCategoryContainer;
import com.itosoftware.inderandroid.Api.SportActivities.ScheduleOfferContainer;
import com.itosoftware.inderandroid.Api.SportActivities.SportActivities;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.SportActivitiesListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.graphicstest.JustifiedTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class OfferDetailActivity extends ActionBarActivity implements SportActivitiesListener{

    ActionBar actionBars;
    TextView textView_TitleActionBar;
    Globals globals;
    TextView title;
    Menu menu;

    String hour = "";
    String contactName = "";
    String contactMail = "";
    String contactTel = "";
    String contactTel2 = "";
    String contactCel = "";
    String discipline = "";
    ImageView progress;
    TextView schedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        final Activity a = this;
        globals = (Globals) getApplication();
        String json = null;
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            json = bundle.getString("offer");
        }

        Integer id_sport_activity = bundle.getInt("id_sport_activity");
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

        textView_TitleActionBar.setText("Detalle de Oferta");
        actionBars.setDisplayHomeAsUpEnabled(true);

        actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        Gson gson = new Gson();
        Offers offers = gson.fromJson(json, Offers.class);


        progress = (ImageView) findViewById(R.id.factivity_offer_detail_progress_bar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        HashMap paramsSchedule = new HashMap();
        paramsSchedule.put("puntos_atencion_id",id_sport_activity);
        paramsSchedule.put("ofertas_id",offers.getId());
        ApiActivitiesScenarios apiActivitiesScenarios = new ApiActivitiesScenarios(this);
        apiActivitiesScenarios.getScheduleOffer(paramsSchedule);

        TextView title = (TextView) findViewById(R.id.activity_offer_detail_title);
        title.setText(offers.getNombre());

        schedule = (TextView) findViewById(R.id.activity_offer_detail_schedule);

        Disciplines discipline = (Disciplines) offers.getDisciplina();

        TextView disciplines = (TextView) findViewById(R.id.activity_offer_detail_discipline);
        if(discipline != null){
            this.discipline =  discipline.getNombre();
            if(this.discipline  != ""){
                disciplines.setText("Disciplina : "+this.discipline);
            }
        }

        HashMap contact = (HashMap) offers.getResponsable_oferta();

        TextView contact_name = (TextView) findViewById(R.id.activity_offer_detail_contact_name);
        if(contact.get("primer_nombre").toString() != ""  || contact.get("primer_apellido").toString() != ""){
            this.contactName =  contact.get("primer_nombre").toString() + " "+ contact.get("primer_apellido").toString();
            contact_name.setText("Nombre de responsable : "+this.contactName);
        }

        TextView contact_mail = (TextView) findViewById(R.id.activity_offer_detail_contact_mail);
        if(contact.get("email").toString() != "" ){
            this.contactMail =  contact.get("email").toString();
            contact_mail.setText("Correo : "+this.contactMail);
        }

        Button callButton = (Button) findViewById(R.id.content_call_button);
        LinearLayout callButtonOne = (LinearLayout) findViewById(R.id.activity_offer_detail_tel_one);
        Integer telefono = 0;
        if(contact.get("telefono1").toString().length() > 1){
            telefono = 1;
        }
        if(contact.get("telefono1").toString() != "" && telefono != 0 ){
            TextView contact_tel = (TextView) findViewById(R.id.activity_offer_detail_contact_tel);
            this.contactTel =  contact.get("telefono1").toString();
            contact_tel.setText(this.contactTel);
            callButtonOne.setVisibility(View.VISIBLE);

            callButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    new AlertDialog.Builder(a)
                            .setTitle("¿ Desea llamar ?")
                            .setMessage("Esta llamada puede tener costos y dependen de su operador de telefonía móvil")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactTel)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }else{
            callButtonOne.setVisibility(View.GONE);
        }

        Button callButtonTwo = (Button) findViewById(R.id.content_call_button_two);
        LinearLayout LinearCallButtonTwo = (LinearLayout) findViewById(R.id.activity_offer_detail_tel_two);
        Integer telefono2 = 0;
        if(contact.get("telefono2").toString().length() > 1){
            telefono2 = 1;
        }
        if(contact.get("telefono2").toString() != ""  && telefono2 != 0){
            TextView contact_tel = (TextView) findViewById(R.id.activity_offer_detail_contact_two);
            this.contactTel2 =  contact.get("telefono2").toString();
            contact_tel.setText(this.contactTel2);
            LinearCallButtonTwo.setVisibility(View.VISIBLE);

            callButtonTwo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    new AlertDialog.Builder(a)
                            .setTitle("¿ Desea llamar ?")
                            .setMessage("Esta llamada puede tener costos y dependen de su operador de telefonía móvil")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactTel2)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }else{
            LinearCallButtonTwo.setVisibility(View.GONE);
        }

        Button callButtonThree = (Button) findViewById(R.id.content_call_button_three);
        LinearLayout LinearCallButtonThree = (LinearLayout) findViewById(R.id.activity_offer_detail_tel_three);
        Integer celular = 0;
        if(contact.get("celular").toString().length() > 1){
            celular = 1;
        }
        if(contact.get("celular").toString() != "" && celular != 0){
            TextView contact_tel = (TextView) findViewById(R.id.activity_offer_detail_contact_tel_three);
            this.contactCel =  contact.get("celular").toString();
            contact_tel.setText(this.contactCel);
            LinearCallButtonThree.setVisibility(View.VISIBLE);

            callButtonThree.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    new AlertDialog.Builder(a)
                            .setTitle("¿ Desea llamar ?")
                            .setMessage("Esta llamada puede tener costos y dependen de su operador de telefonía móvil")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactCel)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }else{
            LinearCallButtonThree.setVisibility(View.GONE);
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
                try{
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

    @Override
    public void onFinishedConnection(ArrayList<SportActivities> itemsSportsActivities, Integer result) {

    }
    @Override
    public void onFinishedScheduleOffer(String scheduleOffer) {
        if(scheduleOffer != ""){
            hour = scheduleOffer;
        }else{
            hour = "En el momento no hay horario disponible.";
        }
        schedule.setText(hour);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
    }
    @Override
    public void onFinishedConnectionWebview(ArrayList<SportActivities> itemsSportsActivities, String sportsActivities, Integer result) {

    }
}
