package com.itosoftware.inderandroid.Api.ParticipantsDetailReservation;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Api.Reservations.ReservationsContainer;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Interface.ReservationDetailListener;
import com.itosoftware.inderandroid.Interface.ReservationsListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ApiParticipant {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    ReservationDetailListener callback;
    Context context;

    public ApiParticipant(Fragment fragment){
        try {
            callback = (ReservationDetailListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public ApiParticipant(Activity activity){
        try {
            callback = (ReservationDetailListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling activity must implement interface");
        }
        this.context = activity.getApplicationContext();
    }

    public void getParticipantsReservation(HashMap params){

        String reserva_id = params.get("reserva_id").toString();
        //String reserva_id = "1";

        String url = context.getString(R.string.url);
        String search_participants = context.getString(R.string.search_participants);

        url = url.concat(search_participants);
        url = url.replace("{reserva_id}",reserva_id);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.i("Url Participants => ", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();

                ParticipantsContainer participantsContainer;

                Type listType = new TypeToken<ParticipantsContainer>() {
                }.getType();
                participantsContainer = gson.fromJson(response.toString(), listType);

                ArrayList<Participants> itemsReservations = participantsContainer.getItems();

                callback.onfinishedLoadParticipants(itemsReservations, 7);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Error Reservations => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }



}






