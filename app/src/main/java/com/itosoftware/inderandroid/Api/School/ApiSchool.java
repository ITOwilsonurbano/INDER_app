package com.itosoftware.inderandroid.Api.School;


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
import com.itosoftware.inderandroid.Interface.SchoolListener;
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
public class ApiSchool {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    SchoolListener callback;
    Context context;

    public ApiSchool(Fragment fragment){
        try {
            callback = (SchoolListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SchoolListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public void getSchools(HashMap params){

        Integer page = (Integer) params.get("page");
        Integer limit = (Integer) params.get("limit");


        String url = context.getString(R.string.url);
        String school_list = context.getString(R.string.schools_list);

        url = url.concat(school_list);
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
                SchoolContainer zonesContainer;

                Type listType = new TypeToken<SchoolContainer>() {
                }.getType();
                zonesContainer = gson.fromJson(response.toString(), listType);

                ArrayList<School> itemsSchools = zonesContainer.getItems();

                callback.onfinishedLoadSchools(itemsSchools);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                 callback.onErrorLoadSchools();
                Log.e("Response Error => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

}






