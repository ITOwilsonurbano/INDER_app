package com.itosoftware.inderandroid.Fragments;

import com.itosoftware.inderandroid.Activities.LoginActivity;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.ReservationDetailActivity;
import com.itosoftware.inderandroid.Adapters.ReservationAdapter;

import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;

import com.itosoftware.inderandroid.Api.Reservations.ApiReservation;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.DialogReservationsClickListener;
import com.itosoftware.inderandroid.Interface.ReservationsListener;
import com.itosoftware.inderandroid.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

public class ReservationFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, ReservationsListener, DialogReservationsClickListener, AdapterView.OnItemLongClickListener {

    private ArrayList<Reservations> reservations;

    Integer page = new Integer(1);
    Integer limit = new Integer(10);
    Integer maxPage = new Integer(1);

    ActionBar actionBars;
    ListView listView;
    int currentLastVisible;
    int currentVisibleItemCount;
    Integer currentPage = new Integer(1);
    int positionDelete;

    ImageView progress;

    ReservationAdapter adapter;

    Globals globals;
    MainActivity mainActivity;

    RelativeLayout reservationLogin;
    String keyword = "";
    String estado = "";
    String date = "";

    Boolean siDetailFragment = false;
    ReservationDetailFragment reservationDetailFragment;
    Menu menu;
    View rootview;
    TextView message;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HashMap params = new HashMap<>();
        params.put("page", page);
        params.put("limit", limit);
        params.put("num_reserva_id", getLabel());
        params.put("estado", getEstado());
        params.put("date", getDate());

        mainActivity = (MainActivity) this.getActivity();
        actionBars = mainActivity.getActionBarActivity();
        globals = (Globals) getActivity().getApplication();

        params.put("access_token", globals.getToken());

        ApiReservation apiReservation = new ApiReservation(this);
        apiReservation.getFiltroReservations(params);
        reservationDetailFragment = new ReservationDetailFragment();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();
        this.menu = menu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootview = inflater.inflate(R.layout.fragment_reservation, container, false);

        message = (TextView) rootview.findViewById(R.id.fragment_reservation_message);
        message.setVisibility(View.GONE);

        reservationLogin = (RelativeLayout)  rootview.findViewById(R.id.fragment_reservation_login);
        listView  = (ListView) rootview.findViewById(R.id.reservations_list);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
        listView.setOnItemLongClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.fragment_reservation_swipe);

        progress = (ImageView) rootview.findViewById(R.id.fragment_reservation_progressbar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        this.reservations = new ArrayList<Reservations>();
        adapter = new ReservationAdapter(getContext().getApplicationContext(), this.reservations);

        Button buttonLogin = (Button) rootview.findViewById(R.id.fragment_reservation_button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(intent, 3);
            }
        });

        final ApiReservation apiReservation = new  ApiReservation(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                page = 1;
                HashMap params = new HashMap<>();
                params.put("page", page);
                params.put("limit",limit);
                params.put("num_reserva_id", getLabel());
                params.put("estado", getEstado());
                params.put("date", getDate());
                params.put("access_token", globals.getToken());

                apiReservation.getFiltroReservations(params);
            }
        });

        removeList(globals.getUserIsAuthenticated());

        return rootview;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getId() == listView.getId()) {
            if ((currentLastVisible + 1) == listView.getCount()) {
                if(currentPage == page){
                    if(maxPage >= page) {
                        page++;
                    }
                    if(maxPage >= page){
                        progress.setBackgroundResource(R.drawable.progress_animation);
                        progress.setVisibility(View.VISIBLE);
                        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
                        frameAnimation.start();

                        HashMap params = new HashMap<>();
                        params.put("page", page);
                        params.put("limit",limit);
                        params.put("num_reserva_id", getLabel());
                        params.put("estado", getEstado());
                        params.put("date", getDate());
                        params.put("access_token", globals.getToken());

                        ApiReservation apiReservation = new ApiReservation(this);
                        apiReservation.getFiltroReservations(params);
                    }
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        currentLastVisible = listView.getLastVisiblePosition();
        currentVisibleItemCount = visibleItemCount;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        try{
            // Create fragment and give it an argument specifying the article it should show

            Intent reservationDetailIntent = new Intent((MainActivity) getActivity(), ReservationDetailActivity.class );
            reservationDetailIntent.putExtra("reservation", reservations.get(position).toString());
            reservationDetailIntent.putExtra("type", "activity");
            ((MainActivity) getActivity()).startActivityForResult(reservationDetailIntent, 4);

        }catch (Exception e){
            Log.e("Error =>", e.toString());
        }
    }

    Boolean active = false;
    // Start implements ReservationsListener
    @Override
    public void onfinishedLoadReservations(ArrayList<Reservations> reservations, Integer maxPage) {
        if(reservations != null){
            message.setVisibility(View.GONE);
            this.maxPage = maxPage;
            this.currentPage = this.page;
            if(!reservations.isEmpty()){
                // Borrar Lista
                adapter.clear();
                for(Reservations reservation : reservations){
                    adapter.add(reservation);
                }
                int index = listView.getFirstVisiblePosition();
                View v = listView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                listView.setAdapter(adapter);
                listView.setSelectionFromTop(index, top);
                adapter.notifyDataSetChanged();
                Log.i("Lista","reservas actualizada.");
            }
        }else{
            if(globals.getUserIsAuthenticated() == true){
                message.setVisibility(View.VISIBLE);
            }else{
                active = true;
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

    }

    @Override
    public void onCreateReservation(Reservations reservation) {

    }
    @Override
    public void onResponseDates(ArrayList<DatesReservation> date) {

    }

    @Override
    public void onFinishTerminos(String terminos) {

    }

    @Override
    public void onCancelReservation(Integer code) {
        if(code == 200){
            reservations.get(positionDelete).setStatus("rechazado");
            adapter.notifyDataSetChanged();
        }else if(code == 400){
            Toast.makeText(getContext(), "Su solicitud de cancelar reserva no puede ser procesada", Toast.LENGTH_LONG).show();
        }

    }
    //End implements ReservationsListener

    @Override
    public void onResume() {
        super.onResume();
        removeList(globals.getUserIsAuthenticated());
        if(active == true){
            message.setVisibility(View.VISIBLE);
        }
    }

    public void removeList(Boolean isAuthenticated){
        if(isAuthenticated){
            listView.setVisibility(View.VISIBLE);
            reservationLogin.setVisibility(View.GONE);
            if(globals.getSiReservationFragment()){
                // Muestra Botón buscar reserva luego de login en lista de reservas, si se cerro sesion en este fragmento antes del login.
                globals.setVerButtonSearchReservation(true);
            }
        }else{
            listView.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            reservationLogin.setVisibility(View.VISIBLE);
            globals.setVerButtonSearchReservation(false); // Cuando cierra sesion se oculta el botón.
        }
    }

    public void showDialogSearchAdvanced() {
        SearchAdvancedReservationFragment dialogSearchAdvanced = new SearchAdvancedReservationFragment();
        dialogSearchAdvanced.setTargetFragment(this, 0);
        dialogSearchAdvanced.show(getFragmentManager(), "dialog");
    }

    @Override
    public String getLabel() {
        return this.keyword;
    }

    @Override
    public void setLabel(String numReserva) {
        this.keyword = numReserva;
    }

    @Override
    public String getEstado() {
        return this.estado;
    }

    @Override
    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void updateReservations(HashMap params) {
        progress.setBackgroundResource(R.drawable.progress_animation);
        progress.setVisibility(View.VISIBLE);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();
        Log.i("progress","progress");

        if(params.get("access_token") == null){
            params.put("access_token", globals.getToken());
        }

        ApiReservation apiReservation = new ApiReservation(this);
        apiReservation.getFiltroReservations(params);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

        positionDelete = pos;
        final HashMap paramsDeleteReservation = new HashMap();
        paramsDeleteReservation.put("id", reservations.get(pos).getId());
        final ReservationFragment fragment = this;

        new AlertDialog.Builder(getContext())
                .setTitle("Cancelar Reserva")
                .setMessage("¿ Esta usted seguro que desea cancelar esta reserva ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ApiReservation apiReservation = new ApiReservation(fragment);
                        apiReservation.cancelReservation(paramsDeleteReservation);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.ic_error)
                .show();


        return true;
    }


}

