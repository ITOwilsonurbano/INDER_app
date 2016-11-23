package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.VolleyImageView;
import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.ParticipantsDetailReservation.ApiParticipant;
import com.itosoftware.inderandroid.Api.ParticipantsDetailReservation.Participants;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Fragments.ShowParticipantsFragment;
import com.itosoftware.inderandroid.Interface.ReservationDetailListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rasuncion on 3/12/15.
 */
public class ReservationDetailActivity extends ActionBarActivity implements ReservationDetailListener {

    ActionBar actionBars;
    int reserva_id;

    ListView listView;
    MainActivity mainActivity;
    Globals globals;
    ReservationDetailActivity reservationDetailActivity;
    Menu menu;
    String type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reservation_detail);

        reservationDetailActivity = this;
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

        //actionBars.setTitle("Detalle");
        textView_TitleActionBar.setText("Reserva");

        actionBars.setDisplayHomeAsUpEnabled(true);
        actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD); // Oculta ActionBar

        String json = null;
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            json = bundle.getString("reservation");
        }

        type = bundle.getString("type");

        Gson gson = new Gson();
        Reservations reservation = gson.fromJson(json, Reservations.class);

        SportsScenarios sportsScenario = reservation.getSportId();

        ImageLoader mImageLoader;
        final NetworkImageView mNetworkImageView;
        mNetworkImageView = (NetworkImageView) findViewById(R.id.reservation_detail_image);
        mImageLoader = ApiConnection.getInstance(getBaseContext()).getImageLoader();
        mNetworkImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (mNetworkImageView.getDrawable() == null) {
                    mNetworkImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.detalle_reserva_broken) );
                    Log.i("Error","Cargando imagen url.");
                }
            }
        });
        mNetworkImageView.setImageUrl(sportsScenario.getImagen_url(), mImageLoader);
        Log.i("url imagen",sportsScenario.getImagen_url());

        TextView numberReservation = (TextView) findViewById(R.id.reservation_detail_number);
        numberReservation.setText("Reserva Nº 0000" + reservation.getId().toString());
        reserva_id = reservation.getId();

        TextView status = (TextView) findViewById(R.id.reservation_detail_status);
        TextView sportName = (TextView) findViewById(R.id.reservation_detail_sportname);

        Log.i("Status: ", reservation.getStatus());

        if(reservation.getStatus().equals("rechazado")){
            status.setText("Rechazada");
            status.setTextColor(getBaseContext().getResources().getColor(R.color.Rojo));
            sportName.setTextColor(getBaseContext().getResources().getColor(R.color.Rojo));
        }
        if(reservation.getStatus().equals("pendiente")){
            status.setText("En Proceso");
            status.setTextColor(getBaseContext().getResources().getColor(R.color.Naranja));
            sportName.setTextColor(getBaseContext().getResources().getColor(R.color.Naranja));
        }
        if(reservation.getStatus().equals("aprobado")){
            status.setText("Aprobada");
            status.setTextColor(getBaseContext().getResources().getColor(R.color.Verde));
            sportName.setTextColor(getBaseContext().getResources().getColor(R.color.Verde));
        }

        if(sportsScenario.getInfo().get("nombre") != null){
            sportName.setText(sportsScenario.getInfo().get("nombre").toString());
        }

        if(sportsScenario.getInfo().get("direccion") != null){
            TextView sportAddress = (TextView) findViewById(R.id.reservation_detail_sport_address);
            sportAddress.setText(sportsScenario.getInfo().get("direccion").toString());
        }

        if(sportsScenario.getInfo().get("telefono") != null){
            TextView sportPhone = (TextView) findViewById(R.id.reservation_detail_sport_phone);
            sportPhone.setText(sportsScenario.getInfo().get("telefono").toString());
        }

        TextView date = (TextView) findViewById(R.id.reservation_detail_date);
        String startDate = reservation.getInicio();
        String endDate = reservation.getFin();
        String[] endDates = endDate.split(" ");
        String dateText = startDate + " - " + endDates[1];
        date.setText(dateText);

        TextView discipline = (TextView) findViewById(R.id.reservation_detail_discipline);
        discipline.setText(reservation.getDiscipline().getNombre());

        TextView subdivision = (TextView) findViewById(R.id.reservation_detail_subdivision);
        subdivision.setText(reservation.getSubdivision().getNombre());

        LinearLayout showPeersButton = (LinearLayout) findViewById(R.id.reservation_detail_button_show_peers);
        showPeersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                HashMap params = new HashMap<>();
                params.put("page", 1);
                params.put("limit", 20);
                params.put("reserva_id", reserva_id); // reserva_id

                ApiParticipant apiReservation = new ApiParticipant(reservationDetailActivity);
                apiReservation.getParticipantsReservation(params);

            }
        });

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
    public void onfinishedLoadParticipants(ArrayList<Participants> participants, Integer maxPage) {
        FragmentManager fm = getSupportFragmentManager();
        ShowParticipantsFragment dFragment = new ShowParticipantsFragment();
        dFragment.setParticipants(participants);
        // Show DialogFragment
        dFragment.show(fm, "Participantes");
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
                        mainActivity.putExtra("openTab", 3);
                        startActivity(mainActivity);
                    }
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
                finish();
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
