package com.itosoftware.inderandroid.Api.Communes;

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
import com.itosoftware.inderandroid.Api.Zones.ZonesContainer;
import com.itosoftware.inderandroid.Interface.CommunesListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrador on 9/11/15.
 */
public class ApiCommunes{

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    CommunesListener callback;
    Context context;

    public ApiCommunes(Fragment fragment){
        try {
            callback = (CommunesListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement CommunesListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public void getCommunesByZone(HashMap params){

        Integer page = (Integer) params.get("page");
        Integer limit = (Integer) params.get("limit");

        String url = context.getString(R.string.url);
        String communes_list = context.getString(R.string.communes_list);

        url = url.concat(communes_list);

        url = url.concat(new String("?pagina="));
        url = url.concat(page.toString());
        url = url.concat(new String("&elementos_por_pagina="));
        url = url.concat(limit.toString());

        String countryId = params.get("country_id").toString();
        String stateId = params.get("departamento_id").toString();
        String townId = params.get("town_id").toString();
        String zoneId = (String) params.get("zone_id").toString();

        url = url.replace("{pais_id}",countryId);
        url = url.replace("{departamento_id}",stateId);
        url = url.replace("{municipio_id}",townId);
        url = url.replace("{zona_id}",zoneId);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                CommunesContainer communesContainer;

                Type listType = new TypeToken<CommunesContainer>() {
                }.getType();
                communesContainer = gson.fromJson(response.toString(), listType);

                Integer total = communesContainer.getTotalItems();
                ArrayList<Communes> itemsZones = communesContainer.getItems();

                callback.onfinishedLoadCommunes(itemsZones);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorLoadCommunes();
                Log.e("Response Error => ", error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

}
