package com.itosoftware.inderandroid.Fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.itosoftware.inderandroid.Adapters.InderSpinnerAdapter;
import com.itosoftware.inderandroid.Api.Zones.ApiZones;
import com.itosoftware.inderandroid.Interface.DialogClickListener;
import com.itosoftware.inderandroid.Interface.DialogReservationsClickListener;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by rasuncion on 26/11/15.
 */
public class SearchAdvancedReservationFragment extends DialogFragment {

    private DialogReservationsClickListener callback;

    protected EditText numeroReserva;
    Button date;
    Spinner estadoSpinner;
    List<String> estados;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            callback = (DialogReservationsClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_advanced_reservation, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Transparente)));

        //Button Close
        Button close = (Button) rootView.findViewById(R.id.fragment_search_advanced_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        // Número de Reserva
        numeroReserva = (EditText) rootView.findViewById(R.id.fragment_search_advanced_numreserva);
        numeroReserva.setFocusableInTouchMode(true);
        numeroReserva.requestFocus();

        //Spinner Estados
        estadoSpinner = (Spinner) rootView.findViewById(R.id.fragment_search_advanced_estado);
        estados = new ArrayList<String>();
        estados.add("Estado");
        estados.add("Rechazada");
        estados.add("En Proceso");
        estados.add("Aprobada");
        InderSpinnerAdapter adapterEstados = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item,estados);
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoSpinner.setAdapter(adapterEstados);

        // Button DATE
        date = (Button) rootView.findViewById(R.id.fragment_search_advanced_reservation_date);
        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DatePickerSearchReservationsFragment newdatePickerFragment = new DatePickerSearchReservationsFragment();
                newdatePickerFragment.setButton(date);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                newdatePickerFragment.show(fm, "datePicker");
            }
        });

        if(!callback.getLabel().matches("")){
            numeroReserva.setText(callback.getLabel().toString());
        }
        if(!callback.getEstado().matches("")){
            String status = callback.getEstado();
            if(status == "rechazado"){
                estadoSpinner.setSelection(1);
            }
            if(status == "pendiente"){
                estadoSpinner.setSelection(2);
            }
            if(status == "aprobado"){
                estadoSpinner.setSelection(3);
            }
        }
        if(!callback.getDate().matches("")){
            date.setText(callback.getDate().toString());
        }



        RelativeLayout search = (RelativeLayout) rootView.findViewById(R.id.fragment_search_advanced_button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!numeroReserva.getText().equals("Número")) {
                    String num = numeroReserva.getText().toString();
                    callback.setLabel(num);
                }else {
                    callback.setLabel("");
                }

                if (estadoSpinner.getSelectedItemPosition() != 0) {
                    String estado = estados.get(estadoSpinner.getSelectedItemPosition());
                    if(estado.equals("Rechazada")){
                        estado = "rechazado";
                    }
                    else if(estado.equals("En Proceso")){
                        estado = "pendiente";
                    }
                    else if(estado.equals("Aprobada")){
                        estado = "aprobado";
                    }
                    callback.setEstado(estado);
                } else {
                    callback.setEstado("");
                }

                if (!date.getText().toString().equals("Seleccione una fecha")) {
                    callback.setDate(date.getText().toString());
                } else {
                    callback.setDate("");
                }

                HashMap params = new HashMap<>();
                params.put("page", 1);
                params.put("limit", 10);
                params.put("num_reserva_id", callback.getLabel());
                params.put("estado", callback.getEstado());
                params.put("date", callback.getDate());

                callback.updateReservations(params);
                dismiss();

            }
        });

        // Do something else*/
        return rootView;
    }

}
