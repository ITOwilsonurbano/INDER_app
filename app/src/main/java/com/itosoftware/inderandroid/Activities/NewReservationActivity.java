package com.itosoftware.inderandroid.Activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.itosoftware.inderandroid.Adapters.DateReservationAdapter;
import com.itosoftware.inderandroid.Adapters.InderSpinnerAdapter;
import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.Disciplines.ApiDisciplines;
import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;
import com.itosoftware.inderandroid.Api.Reservations.ApiReservation;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Api.States.ApiStates;
import com.itosoftware.inderandroid.Api.Subdivisions.ApiSubdivisions;
import com.itosoftware.inderandroid.Api.Subdivisions.Subdivisions;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Fragments.AddParticipantsFragment;
import com.itosoftware.inderandroid.Fragments.DatePickerFragment;
import com.itosoftware.inderandroid.Fragments.DatePickerReservationsFragment;
import com.itosoftware.inderandroid.Fragments.DateReservationFragment;
import com.itosoftware.inderandroid.Fragments.HoursFragment;
import com.itosoftware.inderandroid.Fragments.ResponseReservationFragment;
import com.itosoftware.inderandroid.Fragments.SportDetailFragment;
import com.itosoftware.inderandroid.Fragments.TermsConditionsFragment;
import com.itosoftware.inderandroid.Interface.DisciplinesListener;
import com.itosoftware.inderandroid.Interface.ReservationsListener;
import com.itosoftware.inderandroid.Interface.SubdivisionsListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class NewReservationActivity extends ActionBarActivity implements SubdivisionsListener, DisciplinesListener, ReservationsListener, AdapterView.OnItemSelectedListener{

    ActionBar actionBar;
    FragmentManager fm = getSupportFragmentManager();

    Integer codeDiscipline = 0;

    //Sport Scenario Choosed
    SportsScenarios sportScenario;

    //Properties Spinner Discipline
    protected android.widget.Spinner disciplineSpinner;
    protected HashMap<Integer, Integer> disciplineCodes;
    protected HashMap<Integer, String> disciplineNames;
    protected InderSpinnerAdapter adapterDiscipline;


    //Properties Spinner Subdivision
    protected android.widget.Spinner subdivisionSpinner;
    protected HashMap<Integer, Integer> subdivisionCodes;
    protected HashMap<Integer, String> subdivisionNames;
    protected InderSpinnerAdapter adapterSubdivision;

    // Properties date reservation
    Button reservationDate;
    Button reservationHour;
    public Integer reservationDateId;

    //Properties dates
    protected android.widget.Spinner datesSpinner;
    protected HashMap<Integer, Integer> datesCodes;
    protected HashMap<Integer, DatesReservation> datesNames;
    protected InderSpinnerAdapter adapterDates;
    String date;
    String hours = "";

    // Properties Add Participants
    Button addParticipants;

    //Participants Array Adapter
    ArrayList<Participant> participants;
    String maximoPersonas = "";

    //Reservation
    Reservations reservationNew;

    ImageView progress;

    protected CharSequence[] colours = { "Red", "Green", "Blue", "Yellow", "Orange", "Purple" };

    protected ArrayList<CharSequence> selectedColours = new ArrayList<CharSequence>();

    Menu menu;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);

        globals = (Globals) getApplication();

        Intent extras = getIntent();
        String jsonSportScenario = extras.getStringExtra("sportScenario");
        String authorized = globals.getIsAuthorizedToReserve().toString();

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

        textView_TitleActionBar.setText("Estás Reservando");

        RelativeLayout layoutAuthorized = (RelativeLayout) findViewById(R.id.authorized);
        RelativeLayout layoutNoAuthorized = (RelativeLayout) findViewById(R.id.no_authorized);

        if(authorized == "true"){
            layoutAuthorized.setVisibility(View.VISIBLE);
            layoutNoAuthorized.setVisibility(View.GONE);

            progress = (ImageView) findViewById(R.id.fragment_new_reservation_progress_bar);
            progress.setBackgroundResource(R.drawable.progress_animation);
            AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
            frameAnimation.start();

            Gson gson = new Gson();
            sportScenario = gson.fromJson(jsonSportScenario, SportsScenarios.class);

                //actionBar.setTitle("Estas Reservando");

                ImageLoader mImageLoader;
                final NetworkImageView mNetworkImageView;
                mNetworkImageView = (NetworkImageView) findViewById(R.id.fragment_new_reservation_sport_image);
                mImageLoader = ApiConnection.getInstance(getApplicationContext()).getImageLoader();
                mNetworkImageView.setImageUrl(sportScenario.getImagen_url(), mImageLoader);

                mNetworkImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                        if (mNetworkImageView.getDrawable() == null) {
                            mNetworkImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.detalle_escenario_broken));
                            Log.i("Error", "Cargando imagen url.");
                        }
                    }
                });

                if (sportScenario.getInfo().get("nombre") != null) {
                    TextView name = (TextView) findViewById(R.id.fragment_new_reservation_sport_name);
                    name.setText(sportScenario.getInfo().get("nombre").toString());
                }

                if (sportScenario.getInfo().get("direccion") != null) {
                    TextView direccion = (TextView) findViewById(R.id.fragment_new_reservation_sport_address);
                    direccion.setText(sportScenario.getInfo().get("direccion").toString());
                }

                if (sportScenario.getInfo().get("maximo_personas") != null) {
                    maximoPersonas = (String) sportScenario.getInfo().get("maximo_personas").toString();
                }

    //        if(sportScenario.getInfo().get("telefono") != null){
    //            TextView telefono = (TextView) findViewById(R.id.fragment_new_reservation_sport_phone);
    //            telefono.setText(sportScenario.getInfo().get("telefono").toString());
    //        }

                //Spinner Discipline
                HashMap paramsDisciplines = new HashMap<>();
                paramsDisciplines.put("page", 0);
                paramsDisciplines.put("limit", -1);
                paramsDisciplines.put("escenario_id", sportScenario.getId());
                ApiDisciplines apiDisciplines = new ApiDisciplines(this);
                apiDisciplines.getDisciplines(paramsDisciplines);
                disciplineSpinner = (Spinner) findViewById(R.id.fragment_new_reservation_discipline);
                disciplineCodes = new HashMap<Integer, Integer>();
                disciplineNames = new HashMap<Integer, String>();
                adapterDiscipline = new InderSpinnerAdapter(this, android.R.layout.simple_spinner_item);
                adapterDiscipline.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterDiscipline.add("Disciplina");
                disciplineSpinner.setAdapter(adapterDiscipline);
                disciplineSpinner.setOnItemSelectedListener(this);

                //Spinner Subdivisions
                subdivisionSpinner = (Spinner) findViewById(R.id.fragment_new_reservation_subdivision);
                subdivisionCodes = new HashMap<Integer, Integer>();
                subdivisionNames = new HashMap<Integer, String>();
                adapterSubdivision = new InderSpinnerAdapter(this, android.R.layout.simple_spinner_item);
                adapterSubdivision.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterSubdivision.add("Subdivisión");
                subdivisionSpinner.setAdapter(adapterSubdivision);

                //Change Sport Arena
                TextView reservationChangeSport = (TextView) findViewById(R.id.fragment_new_reservation_button_change_sport);
                reservationChangeSport.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        changeSportArena();
                    }
                });

    //            reservationChangeSport.setVisibility(View.GONE);

                //Select Date
                reservationDate = (Button) findViewById(R.id.fragment_new_reservation_date);
                reservationDate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DatePickerReservationsFragment dFragment = new DatePickerReservationsFragment();
                        dFragment.setButton(reservationDate);
                        // Show DialogFragment
                        dFragment.show(fm, "Dialog Fragment");
                    }
                });

                //Select Hour
                reservationHour = (Button) findViewById(R.id.fragment_new_reservation_hour);
                reservationHour.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        String date = reservationDate.getText().toString();
                        if (date.equals("Elija un dìa")) {
                            reservationHour.setBackgroundResource(R.drawable.rounded_error);
                            Toast.makeText(getApplicationContext(), "Seleccione una fecha primero", Toast.LENGTH_LONG).show();
                        } else {
                            reservationHour.setBackgroundResource(R.drawable.rounded_verde);
                            HoursFragment dFragment = new HoursFragment();
                            dFragment.setData(date, sportScenario.getId());
                            // Show DialogFragment
                            dFragment.show(fm, "Dialog Fragment");
                        }
                    }
                });

                //Add Participants
                participants = new ArrayList<Participant>();
                addParticipants = (Button) findViewById(R.id.fragment_new_reservation_add_participants);
                addParticipants.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {

                        updateParticipants(participants);

                        AddParticipantsFragment dFragment = new AddParticipantsFragment();
                        dFragment.show(fm, "Dialog Fragment");
                    }
                });

                //Terms and Conditions
                Button buttonTermsConditions = (Button) findViewById(R.id.fragment_new_reservation_button_terms_conditions);
                buttonTermsConditions.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        FragmentManager fm = getSupportFragmentManager();
                        TermsConditionsFragment dFragment = new TermsConditionsFragment();
                        // Show DialogFragment
                        dFragment.show(fm, "Términos y Condiciones ");
                    }
                });
                //Create a new Reservation
                final NewReservationActivity newReservationActivity = this;
                RelativeLayout newResevationRelativeLayout = (RelativeLayout) findViewById(R.id.fragment_new_reservation_button_create);
                newResevationRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer errors = 0;
                        CheckBox checkBox = (CheckBox) findViewById(R.id.fragment_new_reservation_terms_conditions);

                        // Disciplina
                        Integer posDiscipline = disciplineSpinner.getSelectedItemPosition();
                        if (posDiscipline == 0) {
                            ((TextView) disciplineSpinner.getSelectedView()).setError("Selecciona una disciplina");
                            errors++;
                        }

                        // Subdivision
                        Integer posSubdivision = subdivisionSpinner.getSelectedItemPosition();
//                        if (posSubdivision == 0) {
//                            ((TextView) subdivisionSpinner.getSelectedView()).setError("Selecciona una Subdivision");
//                            errors++;
//                        }

                        //Date
                        date = (String) reservationDate.getText().toString();
                        if (date.equals("Elija un dìa")) {
                            reservationDate.setBackgroundResource(R.drawable.rounded_error);
                            errors++;
                        } else {
                            reservationDate.setBackgroundResource(R.drawable.rounded_verde);
                        }

                        //Date
                        if (hours.equals("")) {
                            reservationHour.setBackgroundResource(R.drawable.rounded_error);
                            errors++;
                        } else {
                            reservationHour.setBackgroundResource(R.drawable.rounded_verde);
                        }

                        //Date
                        String participantsText = addParticipants.getText().toString();
                        if (participantsText.equals("")) {
                            addParticipants.setBackgroundResource(R.drawable.rounded_error);
                            errors++;
                        } else {
                            addParticipants.setBackgroundResource(R.drawable.rounded_verde);
                        }

                        //Terms and Conditions
                        if (!checkBox.isChecked()) {
                            checkBox.setError("Acepte los terminos y condiciones");
                            errors++;
                        } else {
                            checkBox.setError(null);
                        }

                        //Validation
                        if (errors == 0) {
                            HashMap parameterReservation = new HashMap();

                            parameterReservation.put("sportScenario", sportScenario);
                            parameterReservation.put("discipline", disciplineCodes.get(posDiscipline));
                            parameterReservation.put("date", hours);
                            parameterReservation.put("subdivision", subdivisionCodes.get(posSubdivision));

                            ArrayList participantsId = new ArrayList();
                            int posParti = 0;
                            for (Participant participant : participants) {
                                HashMap participantnew = new HashMap();
                                participantnew.put("id", participant.getId());
                                participantsId.add(posParti, participantnew);
                                posParti++;
                            }
                            Gson gson = new Gson();
                            String participantes = gson.toJson(participantsId);
                            parameterReservation.put("participants", participantes);

                            ApiReservation apiReservation = new ApiReservation(newReservationActivity);
                            apiReservation.createReservation(parameterReservation);
                        } else {
                            Toast.makeText(getApplicationContext(), "Los campos marcados son obligatorios", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                TextView messageNoAutorized = (TextView) findViewById(R.id.message_no_authorized);
                layoutAuthorized.setVisibility(View.GONE);
                layoutNoAuthorized.setVisibility(View.VISIBLE);
                messageNoAutorized.setText("Usted no esta autorizado para realizar reservas por favor comuniquese con Inder.");
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Hide keyboard by touching anywhere on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }

    public void updateTextDate(String date){
        reservationDate.setText(date);
    }
    public void reloadHours(String date){
        HashMap params = new HashMap();

        params.put("sport_escenario",sportScenario.getId());
        params.put("date",date);
        
        ApiReservation apiReservation = new ApiReservation(this);
        apiReservation.getDates(params);
    }

    public void updateParticipants(ArrayList<Participant> arrayParticipants){
        participants = arrayParticipants;
        Integer numParticipants = participants.size();
        if(numParticipants == 0){
            addParticipants.setText("Ver/Agregar Participantes");
        }else{
            addParticipants.setText("Hay seleccionados "+numParticipants+" participante(s)");
        }
    }
    public ArrayList<Participant> getParticipants(){
        return participants;
    }
    public Integer maxParticipants(){
        maximoPersonas = maximoPersonas.replace(".0", "");
        Integer max = Integer.parseInt(maximoPersonas);
        return max;
    }

    //    Start Methods Implements SubdivisionsListener
    @Override
    public void onfinishedLoadSubdivisions(ArrayList<Subdivisions> subdivisions) {
        int i = 1;
        Integer selectedItem = 0;
        adapterSubdivision.clear();
        adapterSubdivision.add("Subdivisión");
        for (Subdivisions subdivision : subdivisions) {
            String name =  (String) subdivision.getNombre();
            adapterSubdivision.add(name);
            subdivisionCodes.put(i,subdivision.getId());
            subdivisionNames.put(i,subdivision.getNombre());
            i++;
        }
        adapterSubdivision.notifyDataSetChanged();
        subdivisionSpinner.setAdapter(adapterSubdivision);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onErrorLoadSubdivisions() {
        Toast.makeText(getBaseContext(), "Error al cargar Subdivisiones.",Toast.LENGTH_LONG).show();
    }
    //    End Methods Implements SubdivisionsListener

    //    Start Methods Implements DisciplinesListener
    @Override
    public void onfinishedLoadDisciplines(ArrayList<Disciplines> disciplines) {
        int i = 1;
        Integer selectedItem = 0;
        adapterDiscipline.clear();
        adapterDiscipline.add("Disciplina");
        for (Disciplines discipline : disciplines) {
            String name =  (String) discipline.getNombre();
            adapterDiscipline.add(name);
            disciplineCodes.put(i,discipline.getId());
            disciplineNames.put(i,discipline.getNombre());
            i++;
        }
        adapterDiscipline.notifyDataSetChanged();
        disciplineSpinner.setAdapter(adapterDiscipline);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onErrorLoadDisciplines() {
        Toast.makeText(getBaseContext(), "Error al cargar Disciplinas.",Toast.LENGTH_LONG).show();
    }
    //    End Methods Implements DisciplinesListener

    //    Start Methods Implements ReservationsListener
    @Override
    public void onfinishedLoadReservations(ArrayList<Reservations> reservations, Integer maxPage) {

    }
    @Override
    public void onCreateReservation(Reservations reservation) {
        reservationNew = reservation;
        FragmentManager fm = getSupportFragmentManager();
        ResponseReservationFragment dFragment = new ResponseReservationFragment();
        // Show DialogFragment
        dFragment.show(fm, "ResponseReservationFragment");

    }
    @Override
    public void onResponseDates(ArrayList<DatesReservation> dates ) {

    }
    @Override
    public void onFinishTerminos(String terminos) {

    }
    @Override
    public void onCancelReservation(Integer code) {

    }
    //    End Methods Implements ReservationsListener

    public Reservations getReservation(){
        return reservationNew;
    }

    public void changeSportArena(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("valid", 1);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public String getDate(){
        return this.date;
    }

    public void setHours(String hour, String dateString){
        this.hours = hour;
        reservationHour.setText(dateString);
    }


    public void showMessage(){
        Toast.makeText(getBaseContext(), "La fecha de reserva no puede ser inferior a la actual", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.fragment_new_reservation_discipline:
                progress.setVisibility(View.VISIBLE);
                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimationCountry = (AnimationDrawable) progress.getBackground();
                frameAnimationCountry.start();

                codeDiscipline = 0;
                if (disciplineSpinner.getSelectedItemPosition() != 0) {
                    codeDiscipline = (Integer) disciplineCodes.get(disciplineSpinner.getSelectedItemPosition());
                }
                adapterSubdivision.clear();
                adapterSubdivision.add("Subdivisión");
                adapterSubdivision.notifyDataSetChanged();

                if (disciplineSpinner.getSelectedItemPosition() != 0) {
                    HashMap paramsSubdivisions = new HashMap<>();
                    paramsSubdivisions.put("page", 0);
                    paramsSubdivisions.put("limit", -1);
                    paramsSubdivisions.put("discipline_id", codeDiscipline);
                    paramsSubdivisions.put("sport_scenario_id", sportScenario.getId());
                    ApiSubdivisions apiSubdivisions = new ApiSubdivisions(this);
                    apiSubdivisions.getSubdivisions(paramsSubdivisions);
                } else {
                    progress.setBackgroundDrawable(null);
                    progress.setVisibility(View.GONE);
                }
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
