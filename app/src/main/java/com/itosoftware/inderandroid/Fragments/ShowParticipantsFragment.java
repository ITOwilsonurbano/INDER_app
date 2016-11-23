package com.itosoftware.inderandroid.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.itosoftware.inderandroid.Adapters.AddParticipantsAdapter;
import com.itosoftware.inderandroid.Adapters.ShowParticipantsAdapter;
import com.itosoftware.inderandroid.Api.ParticipantsDetailReservation.ApiParticipant;
import com.itosoftware.inderandroid.Api.ParticipantsDetailReservation.Participants;
import com.itosoftware.inderandroid.Api.Reservations.ApiReservation;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Interface.ReservationDetailListener;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShowParticipantsFragment extends DialogFragment {
    View rootView;
    ShowParticipantsAdapter adapter;
    ListView participantsList;



    ArrayList<Participants> participants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_show_participants, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        participantsList = (ListView) rootView.findViewById(R.id.fragment_show_participants_list);

        final ArrayList<String> list = new ArrayList<String>();
        for(Participants participant : getParticipants()){
            list.add("Identificación: "+ participant.getId() + "\nParticipante: " + participant.getNombre());
        }

        adapter = new ShowParticipantsAdapter(getContext(), R.layout.participant_row, list);

        participantsList.setAdapter(adapter);

        Button close = (Button) rootView.findViewById(R.id.fragment_show_participants_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        // Do something else
        return rootView;
    }

    public ArrayList<Participants> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participants> participants) {
        this.participants = participants;
    }

    /*@Override
    public void onfinishedLoadParticipants(ArrayList<Participants> participants, Integer maxPage) {
        if(!participants.isEmpty()){
            // Borrar Lista
            adapter.clear();
            for(Participants participant : participants){
                adapter.add(participant.getNombre());
            }
            participantsList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }*/
}