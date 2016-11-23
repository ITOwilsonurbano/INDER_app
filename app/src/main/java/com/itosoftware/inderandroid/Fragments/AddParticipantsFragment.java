package com.itosoftware.inderandroid.Fragments;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.itosoftware.inderandroid.Activities.NewReservationActivity;
import com.itosoftware.inderandroid.Adapters.AddParticipantsAdapter;
import com.itosoftware.inderandroid.Adapters.ShowParticipantsAdapter;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.User;
import com.itosoftware.inderandroid.Api.Users.UserContainer;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.Interface.EpsListener;
import com.itosoftware.inderandroid.Interface.ParticipantsListener;
import com.itosoftware.inderandroid.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddParticipantsFragment extends DialogFragment implements AdapterView.OnItemLongClickListener, ApiUserListener {
    View rootView;
    AddParticipantsAdapter adapter;
    Integer contador = 10;
    ArrayList<Participant> itemsUsers;
    EditText numDocumentText;
    int positionDelete;
    NewReservationActivity callback;
    ImageView progress;


    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

        positionDelete = pos;

        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Participante")
                .setMessage("¿ Esta usted seguro que desea eliminar este participante ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        itemsUsers.remove(positionDelete);
                        adapter.notifyDataSetChanged();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_participants, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        callback = (NewReservationActivity) getActivity();

        itemsUsers = callback.getParticipants();

        ListView participantsList = (ListView) rootView.findViewById(R.id.fragment_add_participants_list);

        adapter = new AddParticipantsAdapter(getContext(), itemsUsers);
        participantsList.setAdapter(adapter);
        participantsList.setOnItemLongClickListener(this);

        numDocumentText = (EditText) rootView.findViewById(R.id.fragment_add_participants_add);
        progress = (ImageView) rootView.findViewById(R.id.fragment_add_participants_progressbar);
        progress.setVisibility(View.GONE);

        final AddParticipantsFragment addParticipantsFragment = this;

        //Add new Participant
        Button add = (Button) rootView.findViewById(R.id.fragment_add_participants_add_button);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String numDocumentTexto = numDocumentText.getText().toString();
                if(!numDocumentTexto.matches("")){
                    if(callback.maxParticipants() > itemsUsers.size()){
                        progress.setBackgroundResource(R.drawable.progress_animation);
                        progress.setVisibility(View.VISIBLE);
                        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
                        frameAnimation.start();

                        HashMap params = new HashMap();
                        params.put("identification",numDocumentTexto);

                        ApiUser apiUser = new ApiUser(addParticipantsFragment);
                        apiUser.addParticipant(params);
                    }else{
                        Toast.makeText(getContext(), "El número de participantes ha llegado al maximo", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Log.e("To have","Error");
                    numDocumentText.setError("Ingrese un número de documento");
                    Toast.makeText(getContext(), "Ingrese un número de documento", Toast.LENGTH_LONG).show();
                }

            }
        });


        Button close = (Button) rootView.findViewById(R.id.fragment_add_participants_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                callback.updateParticipants(itemsUsers);
                dismiss();
            }
        });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyboard(getActivity(), numDocumentText.getWindowToken());
                return false;
            }
        });


        // Do something else
        return rootView;
    }

    public static void closeKeyboard(Context c, IBinder windowToken){
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    @Override
    public void onFinishedAuthentication(UserContainer userContainer, Boolean authenticated) {

    }
    @Override
    public void onFinishedRegister(Boolean success, HashMap data) {

    }
    @Override
    public void onFinishedForgetPassword() {

    }
    @Override
    public void onFinishedForgetUser() {

    }

    @Override
    public void onFinishedProfile(ApiUser.Info dataUser) {

    }

    @Override
    public void onFinishedProfilePqr(JSONObject response) {

    }

    @Override
    public void onFinishedAddParticipant(Participant participant, Integer status) {
        if(status == 200){
            itemsUsers.add(participant);
            progress.setBackgroundDrawable(null);
            progress.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(numDocumentText.getWindowToken(), 0);
        }
        if(status == 404){
            progress.setBackgroundDrawable(null);
            progress.setVisibility(View.GONE);
            numDocumentText.setError("Ingrese un número de documento valido");
            Toast.makeText(getContext(), "Ingrese un número de documento valido", Toast.LENGTH_LONG).show();
        }
    }
}