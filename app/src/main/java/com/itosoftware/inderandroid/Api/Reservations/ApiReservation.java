package com.itosoftware.inderandroid.Api.Reservations;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
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

import com.itosoftware.inderandroid.Api.Countries.Countries;
import com.itosoftware.inderandroid.Api.Countries.CountriesContainer;
import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservationContainer;
import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;
import com.itosoftware.inderandroid.Api.News.New;

import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.ReservationsListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.Utils.GcmServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ApiReservation {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    ReservationsListener callback;
    Context context;

    Integer limit;
    Integer page;

    public ApiReservation(Fragment fragment){
        try {
            callback = (ReservationsListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement ReservationsListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public ApiReservation(Activity activity){
        try {
            callback = (ReservationsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling activity must implement ReservationsListener interface");
        }
        this.context = activity.getApplicationContext();
    }
    public ApiReservation(DialogFragment dialogFragment){
        try {
            callback = (ReservationsListener) dialogFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling DialogFragment must implement ReservationsListener interface");
        }
        this.context = dialogFragment.getContext();
    }
    public ApiReservation(GcmServiceHandler gcmServiceHandler){
        try {
            callback = (ReservationsListener) gcmServiceHandler;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling GcmServiceHandler must implement ReservationsListener interface");
        }
        this.context = gcmServiceHandler.getApplicationContext();
    }

    public void getFiltroReservations(HashMap params){

        page = 0;
        limit = 0;
        String numReservaId = "";
        String estado = "";
        String access_token = "";
        String date = "";

        String url = context.getString(R.string.url);
        String search_reservation = context.getString(R.string.search_reservation);

        url = url.concat(search_reservation);
        //Verify Params
        if(!params.get("page").equals(null)){
            url = url.concat(new String("?pagina="));
            page = (Integer) params.get("page");
            url = url.concat(page.toString());
        }
        if(!params.get("limit").equals(null)){
            url = url.concat(new String("&elementos_por_pagina="));
            limit = (Integer) params.get("limit");
            url = url.concat(limit.toString());
        }
        if(!params.get("date").equals("") && !params.get("date").equals(null)){
            url = url.concat(new String("&fecha="));
            date = params.get("date").toString();
            url = url.concat(date);
        }
        Log.d("Reserva numero",params.get("num_reserva_id").toString());
        if(!params.get("num_reserva_id").equals("") && !params.get("num_reserva_id").equals(null)){
            url = url.concat(new String("&numero_reserva="));
            numReservaId = params.get("num_reserva_id").toString();
            url = url.concat(numReservaId);
        }
        if(!params.get("estado").equals("") && !params.get("estado").equals(null)){
            url = url.concat(new String("&estado_reserva="));
            estado = params.get("estado").toString();
            url = url.concat(estado);
        }
        if(params.get("access_token") != "" && params.get("access_token") != null){
            url = url.concat(new String("&access_token="));
            access_token = params.get("access_token").toString();
            url = url.concat(access_token);
        }

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.i("Url Reservas => ", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();

                ReservationsContainer reservationsContainer;

                Type listType = new TypeToken<ReservationsContainer>() {
                }.getType();
                reservationsContainer = gson.fromJson(response.toString(), listType);

                ArrayList<Reservations> itemsReservations = reservationsContainer.getItems();

                Integer maxPages = reservationsContainer.getTotalItems() / limit;

                callback.onfinishedLoadReservations(itemsReservations,maxPages);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onfinishedLoadReservations(null,0);
                Log.i("Error Reservations => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

    public void createReservation(HashMap params) {

        final HashMap paramsReservation = new HashMap();

        String url = context.getString(R.string.url);
        String add_new_reservation = context.getString(R.string.add_new_reservation);
        url = url.concat(add_new_reservation);

        final SportsScenarios sport = (SportsScenarios) params.get("sportScenario");
        final String date = (String) params.get("date");
        final Integer sportId = sport.getId();
        final Integer user = 1;
        final Integer discipline = (Integer) params.get("discipline");
        final Integer subdivision = (Integer) params.get("subdivision");
        final String participants = (String) params.get("participants");

        paramsReservation.put("usuarioSimId", user.toString());
        paramsReservation.put("escenarioSimId", sportId.toString());
        paramsReservation.put("horario", date);
        paramsReservation.put("participantes", participants);
        paramsReservation.put("disciplinaSimId", discipline.toString());
        paramsReservation.put("subdivisionSimId", subdivision.toString());
        paramsReservation.put("estadoSimId", user.toString());


        Log.d("Reserva ",paramsReservation.toString());
        Log.d("Reserva Participants",participants);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "createReservation";
        fRequestQueue.cancelAll(TAG);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                Type listType = new TypeToken<Reservations>() {}.getType();
                Reservations reservationResponse = gson.fromJson(response.toString(), listType);

                callback.onCreateReservation(reservationResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramsReservation;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);
                // add headers <key,value>
                String credentials = client_id + ":" + client_secret;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };


        sr.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(sr);
    }


    public void getDates(HashMap params){

        Integer sport_id = (Integer) params.get("sport_escenario");
        String date = (String) params.get("date");

        String url = context.getString(R.string.url);
        String get_dates = context.getString(R.string.get_dates);

        url = url.concat(get_dates);
        url = url.replace("{fecha}",date);
        url = url.replace("{escenario_id}",sport_id.toString());


        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.i("Url Date => ", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                DatesReservationContainer datesReservationContainer;

                Type listType = new TypeToken<DatesReservationContainer>() {
                }.getType();
                datesReservationContainer = gson.fromJson(response.toString(), listType);

                Integer total = datesReservationContainer.getTotalItems();
                ArrayList<DatesReservation> itemsDates = datesReservationContainer.getItems();

                callback.onResponseDates(itemsDates);
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

    public void getReservarion(HashMap params){

        String id = (String) params.get("id").toString();

        String url = this.context.getString(R.string.url);
        String search_reservation_id = this.context.getString(R.string.search_reservation_id, id);

        url = url.concat(search_reservation_id);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.i("Url Id Reservation => ", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();

                Reservations reservations;
                Type listType = new TypeToken<Reservations>() {
                }.getType();
                reservations = gson.fromJson(response.toString(), listType);

                callback.onCreateReservation(reservations);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Response Error => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

    public void getTerminos(){


        String url = this.context.getString(R.string.url);
        String terminos = this.context.getString(R.string.terminos);

        url = url.concat(terminos);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.i("Url Terminos => ", url);

        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                callback.onFinishTerminos(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;
                if(error.networkResponse != null && error.networkResponse.data != null){
                    VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                    error = volleyError;
                }

                Log.e("Error status" , statusCode.toString());
                Log.e("Error label" , error.toString());

            }
        }){
//            @Override
//            protected Map<String,String> getParams(){
//                return paramsUser;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id  = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);

                // add headers <key,value>
                String credentials = client_id+":"+client_secret;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };


        //Add request to queue
        fRequestQueue.add(sr);

    }

    public void cancelReservation(HashMap params){


        String url = this.context.getString(R.string.url);
        String cancelReservation = this.context.getString(R.string.cancelReservation, params.get("id"));

        url = url.concat(cancelReservation);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.i("Url Terminos => ", url);

        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                callback.onCancelReservation(200);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;
                if(error.networkResponse != null && error.networkResponse.data != null){
                    VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                    error = volleyError;
                }
                callback.onCancelReservation(400);
                Log.e("Error status" , statusCode.toString());
                Log.e("Error label" , error.toString());

            }
        }){
//            @Override
//            protected Map<String,String> getParams(){
//                return paramsUser;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id  = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);

                // add headers <key,value>
                String credentials = client_id+":"+client_secret;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };


        //Add request to queue
        fRequestQueue.add(sr);

    }


}






