package com.itosoftware.inderandroid.Api.Subdivisions;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Interface.DocumentTypesListener;
import com.itosoftware.inderandroid.Interface.SubdivisionsListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by esteban on 8/11/2015.
 */
public class ApiSubdivisions {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    SubdivisionsListener callback;
    Context context;

    public ApiSubdivisions(Fragment fragment){
        try {
            callback = (SubdivisionsListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SubdivisionsListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public ApiSubdivisions(Activity activity){
        try {
            callback = (SubdivisionsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SubdivisionsListener interface");
        }
        this.context = activity.getApplicationContext();
    }


    public void getSubdivisions (HashMap params){

        Integer page = (Integer) params.get("page");
        Integer limit = (Integer) params.get("limit");
        Integer discipline_id = (Integer) params.get("discipline_id");
        Integer sport_scenario_id = (Integer) params.get("sport_scenario_id");

        String url = context.getString(R.string.url);
        String subdivisions_list = context.getString(R.string.subdivisions_list,discipline_id,sport_scenario_id);

        url = url.concat(subdivisions_list);
        url = url.concat(new String("?pagina="));
        url = url.concat(page.toString());
        url = url.concat(new String("&elementos_por_pagina="));
        url = url.concat(limit.toString());

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.d("Subdivisionsurl => ",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                SubdivisionsContainer subdivisionsContainer;

                Type listType = new TypeToken<SubdivisionsContainer>() {
                }.getType();
                subdivisionsContainer = gson.fromJson(response.toString(), listType);

                ArrayList<Subdivisions> itemsSubdivisions = subdivisionsContainer.getItems();

                callback.onfinishedLoadSubdivisions(itemsSubdivisions);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorLoadSubdivisions();
                Log.e("Response Error => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

}






