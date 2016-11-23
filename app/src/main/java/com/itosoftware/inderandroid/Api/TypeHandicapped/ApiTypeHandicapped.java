package com.itosoftware.inderandroid.Api.TypeHandicapped;


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
import com.itosoftware.inderandroid.Interface.TypeHandicappedListener;
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
public class ApiTypeHandicapped {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    TypeHandicappedListener callback;
    Context context;

    public ApiTypeHandicapped(Fragment fragment){
        try {
            callback = (TypeHandicappedListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement TypeHandicapped interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public void getTypesHandicapped (HashMap params){

        Integer page = (Integer) params.get("page");
        Integer limit = (Integer) params.get("limit");

        String url = context.getString(R.string.url);
        String zones_list = context.getString(R.string.handicapped_type_list);

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

                TypeHandicappedContainer typeHandicappedContainer;

                Type listType = new TypeToken<TypeHandicappedContainer>() {
                }.getType();
                typeHandicappedContainer = gson.fromJson(response.toString(), listType);

                ArrayList<TypeHandicapped> itemsTypeHandicapped = typeHandicappedContainer.getItems();

                callback.onfinishedLoadTypeHandicapped(itemsTypeHandicapped);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorLoadTypeHandicapped();
                Log.e("Response Error => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

}






