package com.itosoftware.inderandroid.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Api.ParticipantsDetailReservation.ApiParticipant;
import com.itosoftware.inderandroid.Api.ParticipantsDetailReservation.Participants;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.ReservationDetailListener;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ReservationDetailFragment extends Fragment implements ReservationDetailListener{

    View rootView;
    ActionBar actionBars;
    TextView textView_TitleActionBar;
    int reserva_id;
    ReservationDetailFragment reservationDetailFragment;

    ListView listView;
    MainActivity mainActivity;
    Globals globals;
    Boolean siDetailFragment = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        globals = (Globals) getActivity().getApplication();

        mainActivity = (MainActivity) this.getActivity();
        actionBars = mainActivity.getActionBarActivity();
        textView_TitleActionBar = mainActivity.getTextView_TitleActionBar();

        //actionBars.setTitle("Detalle");
        textView_TitleActionBar.setText("Reserva");

        actionBars.setDisplayHomeAsUpEnabled(true);
        actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD); // Oculta ActionBar
        getListView().setVisibility(View.GONE); // Oculta lista de reservas

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();
        // Ocultar Botón Buscar Reserva.
        menu.findItem(R.id.action_search_reservation).setVisible(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reservationDetailFragment = this;
        globals.setSiSwiping(false);
        String json = null;
        Bundle bundle = getArguments();

        if(bundle != null){
            json = bundle.getString("reservation");
        }

        Gson gson = new Gson();
        Reservations reservation = gson.fromJson(json, Reservations.class);

        SportsScenarios sportsScenario = reservation.getSportId();

        rootView = inflater.inflate(R.layout.fragment_reservation_detail, container, false);

        ImageLoader mImageLoader;
        NetworkImageView mNetworkImageView;
        mNetworkImageView = (NetworkImageView) rootView.findViewById(R.id.reservation_detail_image);
        mImageLoader = ApiConnection.getInstance(this.getContext()).getImageLoader();
        mNetworkImageView.setImageUrl(sportsScenario.getImagen_url(), mImageLoader);

        TextView numberReservation = (TextView) rootView.findViewById(R.id.reservation_detail_number);
        numberReservation.setText("Reserva Nº 0000" + reservation.getId().toString());
        reserva_id = reservation.getId();

        TextView status = (TextView) rootView.findViewById(R.id.reservation_detail_status);
        TextView sportName = (TextView) rootView.findViewById(R.id.reservation_detail_sportname);

        Log.i("Status: ",reservation.getStatus());

        if(reservation.getStatus().equals("rechazado")){
            status.setText("Rechazada");
            status.setTextColor(getContext().getResources().getColor(R.color.Rojo));
            sportName.setTextColor(getContext().getResources().getColor(R.color.Rojo));
        }
        if(reservation.getStatus().equals("pendiente")){
            status.setText("En Proceso");
            status.setTextColor(getContext().getResources().getColor(R.color.Naranja));
            sportName.setTextColor(getContext().getResources().getColor(R.color.Naranja));
        }
        if(reservation.getStatus().equals("aprobado")){
            status.setText("Aprobada");
            status.setTextColor(getContext().getResources().getColor(R.color.Verde));
            sportName.setTextColor(getContext().getResources().getColor(R.color.Verde));
        }

        if(sportsScenario.getInfo().get("nombre") != null){
            sportName.setText(sportsScenario.getInfo().get("nombre").toString());
        }

        if(sportsScenario.getInfo().get("direccion") != null){
            TextView sportAddress = (TextView) rootView.findViewById(R.id.reservation_detail_sport_address);
            sportAddress.setText(sportsScenario.getInfo().get("direccion").toString());
        }

        if(sportsScenario.getInfo().get("telefono") != null){
            TextView sportPhone = (TextView) rootView.findViewById(R.id.reservation_detail_sport_phone);
            sportPhone.setText(sportsScenario.getInfo().get("telefono").toString());
        }

        TextView date = (TextView) rootView.findViewById(R.id.reservation_detail_date);
        String startDate = reservation.getInicio();
        String endDate = reservation.getFin();
        String[] endDates = endDate.split(" ");
        String dateText = startDate + " - " + endDates[1];
        date.setText(dateText);

        TextView discipline = (TextView) rootView.findViewById(R.id.reservation_detail_discipline);
        discipline.setText(reservation.getDiscipline().getNombre());

        TextView subdivision = (TextView) rootView.findViewById(R.id.reservation_detail_subdivision);
        subdivision.setText(reservation.getSubdivision().getNombre());

        LinearLayout showPeersButton = (LinearLayout) rootView.findViewById(R.id.reservation_detail_button_show_peers);
        showPeersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                HashMap params = new HashMap<>();
                params.put("page", 1);
                params.put("limit", 20);
                params.put("reserva_id", reserva_id); // reserva_id

                ApiParticipant apiReservation = new ApiParticipant(reservationDetailFragment);
                apiReservation.getParticipantsReservation(params);

            }
        });

//                reservation_detail_button_show_peers

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try{
                    // Create fragment and give it an argument specifying the article it should show
                    actionBars.setTitle("Reservas");
                    actionBars.setDisplayHomeAsUpEnabled(false);
                    actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); // Muestra ActionBar
                    setSiDetailFragment(false);
                    getListView().setVisibility(View.VISIBLE); // Muestra lista de reservas
                    globals.setSiSwiping(true);
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

    @Override
    public void onfinishedLoadParticipants(ArrayList<Participants> participants, Integer maxPage) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ShowParticipantsFragment dFragment = new ShowParticipantsFragment();
        dFragment.setParticipants(participants);
        // Show DialogFragment
        dFragment.show(fm, "Participantes");
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public Boolean getSiDetailFragment() {
        return siDetailFragment;
    }

    public void setSiDetailFragment(Boolean siDetailFragment) {
        this.siDetailFragment = siDetailFragment;
    }

    public void activarBtnHome(){
        try{
            // Create fragment and give it an argument specifying the article it should show
            actionBars.setTitle("Reservas");
            actionBars.setDisplayHomeAsUpEnabled(false);
            actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); // Muestra ActionBar
            setSiDetailFragment(false);
            for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                getFragmentManager().popBackStack();
            }
        }catch (Exception e){
            Log.e("Error =>", e.toString());
        }

    }

}



