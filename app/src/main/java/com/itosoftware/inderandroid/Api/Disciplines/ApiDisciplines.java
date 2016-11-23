package com.itosoftware.inderandroid.Api.Disciplines;


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
import com.itosoftware.inderandroid.Interface.DisciplinesListener;
import com.itosoftware.inderandroid.Interface.DocumentTypesListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by esteban on 8/11/2015.
 */
public class ApiDisciplines {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    DisciplinesListener callback;
    Context context;

    public ApiDisciplines(Fragment fragment){
        try {
            callback = (DisciplinesListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DisciplinesListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public ApiDisciplines(Activity activity){
        try {
            callback = (DisciplinesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DisciplinesListener interface");
        }
        this.context = activity.getApplicationContext();
    }

    public void getDisciplines (HashMap params){

        Integer page = (Integer) params.get("page");
        Integer limit = (Integer) params.get("limit");
        Integer scenario = (Integer) params.get("escenario_id");

        String url = context.getString(R.string.url);
        String disciplines_list = context.getString(R.string.disciplines_list, scenario);

        url = url.concat(disciplines_list);
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

                DisciplinesContainer disciplinesContainer;

                Type listType = new TypeToken<DisciplinesContainer>() {
                }.getType();
                disciplinesContainer = gson.fromJson(response.toString(), listType);

                ArrayList<Disciplines> itemsDisciplines = disciplinesContainer.getItems();

                callback.onfinishedLoadDisciplines(itemsDisciplines);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorLoadDisciplines();
                Log.e("Response Error => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

}






