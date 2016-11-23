package com.itosoftware.inderandroid.Api.SportActivities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Api.CommonContainer;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Interface.SportActivitiesListener;
import com.itosoftware.inderandroid.Interface.SportScenarioListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by administrador on 5/11/15.
 */
public class ApiActivitiesScenarios implements Serializable {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    SportActivitiesListener callback;
    Context context;

    public ApiActivitiesScenarios(Fragment fragment){
        try {
            callback = (SportActivitiesListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SportActivitiesListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public ApiActivitiesScenarios(Activity activity){
        try {
            callback = (SportActivitiesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling activity must implement SportActivitiesListener interface");
        }
        this.context = activity.getApplicationContext();
    }

    public void getActivities (HashMap params){

        Integer page;
        Integer limit;
        String keyword;
        String zone;
        String commune;
        String neighborhood;
        String discipline;

        String url = this.context.getString(R.string.url);
        String sports_list = this.context.getString(R.string.offers_list);
        url = url.concat(sports_list);

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
        if(!params.get("keyword").equals(null) && !params.get("keyword").equals("") && !params.get("keyword").equals("Búsqueda de ofertas")){
            url = url.concat(new String("&nombre="));
            keyword = (String) params.get("keyword");
            url = url.concat(keyword.toString());
        }
        if(!params.get("zone").equals(null) && !params.get("zone").equals("") && !params.get("zone").equals("0")){
            url = url.concat(new String("&zona_id="));
            zone = (String) params.get("zone");
            url = url.concat(zone);
        }
        if(!params.get("commune").equals(null) && !params.get("commune").equals("") && !params.get("commune").equals("0")){
            url = url.concat(new String("&comuna_id="));
            commune = (String) params.get("commune");
            url = url.concat(commune);
        }
        if(!params.get("neighborhood").equals(null) && !params.get("neighborhood").equals("") && !params.get("neighborhood").equals("0")){
            url = url.concat(new String("&barrio_id="));
            neighborhood = (String) params.get("neighborhood");
            url = url.concat(neighborhood);
        }
//        if(!params.get("discipline").equals(null) && !params.get("discipline").equals("") && !params.get("discipline").equals("0")){
//            url = url.concat(new String("&actividad_deportiva_id="));
//            discipline = (String) params.get("discipline");
//            url = url.concat(discipline);
//        }


        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "getSportActivities";
        fRequestQueue.cancelAll(TAG);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                SportActivitiesContainer sportsScenariosContainer;
                Type listType = new TypeToken<SportActivitiesContainer>() {
                }.getType();
                sportsScenariosContainer = gson.fromJson(response.toString(), listType);
                ArrayList<SportActivities> itemsSportsScenarios = sportsScenariosContainer.getItems();
                //callback.onFinishedConnection(itemsSportsScenarios, 200);
                callback.onFinishedConnectionWebview(itemsSportsScenarios,response.toString(),200);
                Log.i("Response True => ","True en las Actividades");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //callback.onFinishedConnection(null, 400);
                callback.onFinishedConnectionWebview(null,"",400);
                Log.i("Response Error => ","Activities ");
                Log.i("Response Error => ",error.toString());
            }
        });

        jsonObjReq.setTag(TAG);

        int socketTimeout = 40000;//40 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        //Add request to queue
        fRequestQueue.add(jsonObjReq);
    }

    public void getActivitiesAutoComplete (HashMap params){
        Integer page;
        Integer limit;
        String keyword;

        String url = this.context.getString(R.string.url);
        String sports_list = this.context.getString(R.string.offers_list);
        url = url.concat(sports_list);

        //Verify Params
        if(params.get("page") != null){
            url = url.concat(new String("?pagina="));
            page = (Integer) params.get("page");
            url = url.concat(page.toString());
        }
        if(params.get("limit") != null){
            url = url.concat(new String("&elementos_por_pagina="));
            limit = (Integer) params.get("limit");
            url = url.concat(limit.toString());
        }
        if(params.get("keyword") != null){
            url = url.concat(new String("&keyword="));
            keyword = (String) params.get("keyword");
            url = url.concat(keyword.toString());
            Log.d("Key",keyword);
        }

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "getSportsScenariosAutoComplete";
        fRequestQueue.cancelAll(TAG);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                SportActivitiesContainer sportsScenariosContainer;

                Type listType = new TypeToken<SportActivitiesContainer>() {
                }.getType();
                sportsScenariosContainer = gson.fromJson(response.toString(), listType);

                Integer total = sportsScenariosContainer.getTotalItems();
                ArrayList<SportActivities> itemsSportsScenarios = sportsScenariosContainer.getItems();
                callback.onFinishedConnection(itemsSportsScenarios, 200);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFinishedConnection(null, 400);
                Log.i("Response Error => ","Error en los recomendados");
                Log.i("Response Error => ",error.toString());
            }
        });

        jsonObjReq.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

    public void getScheduleOffer(HashMap params){
        Integer puntos_atencion_id = (Integer) params.get("puntos_atencion_id");
        Integer ofertas_id = (Integer) params.get("ofertas_id");

        String url = context.getString(R.string.url);
        String schedule_offer = context.getString(R.string.schedule_offer, puntos_atencion_id,ofertas_id);
        url = url.concat(schedule_offer);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "getScheduleOffer";
        fRequestQueue.cancelAll(TAG);

        Log.d("Url getScheduleOffer",url);

        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                ArrayList<HashMap>  schedules;

                Type listType = new TypeToken<ArrayList<HashMap> >() {
                }.getType();
                schedules = gson.fromJson(response.toString(), listType);
                String hour = "";
                for (HashMap item : schedules){
                    hour += item.get("dia").toString() +" de "+item.get("inicio").toString()+ " a " +item.get("fin").toString()+ "\n";
                }
                callback.onFinishedScheduleOffer(hour);
//                callback.onFinishedAddParticipant(new Participant(participantParams), 200);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;
                if(error.networkResponse != null && error.networkResponse.data != null){
                    VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                    error = volleyError;
                }
                callback.onFinishedScheduleOffer("");
            }

        }){
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



        sr.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(sr);
    }


}
