package com.itosoftware.inderandroid.Api.Countries;


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
import com.itosoftware.inderandroid.Interface.CountriesListener;
import com.itosoftware.inderandroid.Interface.ZonesListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by esteban on 8/11/2015.
 */
public class ApiCountries {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    CountriesListener callback;
    Context context;

    public ApiCountries(Fragment fragment){
        try {
            callback = (CountriesListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement CountriesListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public void getCountries (HashMap params){

        Integer page = (Integer) params.get("page");
        Integer limit = (Integer) params.get("limit");

        String url = context.getString(R.string.url);
        String zones_list = context.getString(R.string.countries_list);

        url = url.concat(zones_list);
        url = url.concat(new String("?pagina="));
        url = url.concat(page.toString());
        url = url.concat(new String("&elementos_por_pagina="));
        url = url.concat(limit.toString());

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                CountriesContainer countriesContainer;

                Type listType = new TypeToken<CountriesContainer>() {
                }.getType();
                countriesContainer = gson.fromJson(response.toString(), listType);

                Integer total = countriesContainer.getTotalItems();
                ArrayList<Countries> itemsCountries = countriesContainer.getItems();

                callback.onfinishedLoadCountries(itemsCountries);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorLoadCountries();
                Log.e("Response Error => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

}






