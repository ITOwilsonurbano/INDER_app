package com.itosoftware.inderandroid.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.itosoftware.inderandroid.Activities.NewReservationActivity;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.R;

import org.w3c.dom.Text;


public class ResponseReservationFragment extends DialogFragment {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_response_reservation, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final NewReservationActivity newReservationActivity = (NewReservationActivity) getActivity();
        Reservations reservation = newReservationActivity.getReservation();

        String status = reservation.getStatus();

        Log.d("statusstatus",status);

        TextView statusView = (TextView) rootView.findViewById(R.id.fragment_response_reservation_status);
        if(status.equals("rechazado")) {
            statusView.setText("Rechazada");
            statusView.setTextColor(getContext().getResources().getColor(R.color.Rojo));
        }
        if(status.equals("pendiente")) {
            statusView.setText("En Proceso");
            statusView.setTextColor(getContext().getResources().getColor(R.color.Naranja));
        }
        if(status.equals("aprobado")) {
            statusView.setText("Aprobada");
            statusView.setTextColor(getContext().getResources().getColor(R.color.Verde));
        }


        TextView date = (TextView) rootView.findViewById(R.id.fragment_response_reservation_date);
        String startDate = reservation.getInicio();
        String endDate = reservation.getFin();
        String[] endDates = endDate.split(" ");
        String dateText = startDate + " - " + endDates[1];

        date.setText(dateText);


        Button buttonFinish = (Button) rootView.findViewById(R.id.fragment_response_reservation_button_finish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newReservationActivity.changeSportArena();
            }
        });

        Button buttonOther = (Button) rootView.findViewById(R.id.fragment_response_reservation_button_other);
        buttonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newReservationActivity.setHours("","Elija una hora");
                dismiss();
            }
        });

        // Do something else
        return rootView;
    }

//    @Override
//    public Dialog onCreateDialog(final Bundle savedInstanceState) {
//
//        // the content
//        final RelativeLayout root = new RelativeLayout(getActivity());
//        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        // creating the fullscreen dialog
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(root);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        return dialog;
//    }
}
