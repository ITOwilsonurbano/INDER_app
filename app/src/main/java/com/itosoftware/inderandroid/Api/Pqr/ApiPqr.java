package com.itosoftware.inderandroid.Api.Pqr;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Api.GradeLevel.GradeLevel;
import com.itosoftware.inderandroid.Api.GradeLevel.GradeLevelContainer;
import com.itosoftware.inderandroid.Interface.PqrListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by esteban on 8/11/2015.
 */
public class ApiPqr {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    PqrListener callback;
    Context context;

    public ApiPqr(Fragment fragment) {
        try {
            callback = (PqrListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement PqrListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }


    public void sendRequest(final HashMap params, String url, String method, final String from) {

        int method_int = 0;

        if (method.equals("get")) {
            method_int = Request.Method.GET;
        } else if (method.equals("post")) {
            method_int = Request.Method.POST;
        }
        if (params.containsKey("page")) {
            String page = (String) params.get("page");
            url = url.concat(new String("?pagina="));
            url = url.concat(page);
        }
        if (params.containsKey("limit")) {
            String limit = (String) params.get("limit");
            url = url.concat(new String("&elementos_por_pagina="));
            url = url.concat(limit);
        }


        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        StringRequest strRequest = new StringRequest(method_int, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject response_json;
                        try {
                            response_json = new JSONObject(response);
                            callback.onfinishedTrue(response_json, from);
                        } catch (JSONException e) {
                            callback.onfinishedFalse(e.toString());
                            Log.e("Response Error => ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onfinishedFalse(error.toString());
                        Log.e("Response Error => ", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.pqr_client_id);
                String client_secret = context.getString(R.string.pqr_client_secret);
                // add headers <key,value>
                String credentials = client_id + ":" + client_secret;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authentication", auth);
                return headers;
            }
        };

            strRequest.setRetryPolicy(new DefaultRetryPolicy(
                    120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        fRequestQueue.add(strRequest);

    }

    public void cancelAll() {
        if (fRequestQueue != null) {
            fRequestQueue.cancelAll(context);
        }
    }

}






