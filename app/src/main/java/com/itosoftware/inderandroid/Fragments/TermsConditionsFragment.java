package com.itosoftware.inderandroid.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.Reservations.ApiReservation;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Interface.ReservationsListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.graphicstest.JustifiedTextView;

import java.util.ArrayList;


public class TermsConditionsFragment extends DialogFragment implements ReservationsListener {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_terms_conditions, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = (TextView) rootView.findViewById(R.id.fragment_terms_conditions_title);
        title.setText("Términos y Condiciones");

        ApiReservation apiReservation = new ApiReservation(this);
        apiReservation.getTerminos();

//        content.setTextColor(android.R.color.white);
//        content.setTextSize(0,17);


        Button close = (Button) rootView.findViewById(R.id.fragment_terms_conditions_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        // Do something else
        return rootView;
    }

    @Override
    public void onfinishedLoadReservations(ArrayList<Reservations> reservations, Integer maxPage) {

    }

    @Override
    public void onCreateReservation(Reservations reservation) {

    }

    @Override
    public void onResponseDates(ArrayList<DatesReservation> date) {

    }

    @Override
    public void onFinishTerminos(String terminos) {
        JustifiedTextView content = (JustifiedTextView) rootView.findViewById(R.id.fragment_terms_conditions_content);
        String contenido = terminos;
        content.setText(contenido);
        content.setLineSpacing(0);
        content.setAlignment(Paint.Align.LEFT);
        content.setTypeFace(Typeface.createFromAsset(getActivity().getBaseContext().getAssets(), "HelveticaNeueLTStd-LtCn.otf"));
    }
    @Override
    public void onCancelReservation(Integer code) {

    }
}