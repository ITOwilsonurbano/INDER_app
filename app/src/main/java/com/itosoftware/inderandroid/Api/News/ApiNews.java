package com.itosoftware.inderandroid.Api.News;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Interface.NewsListener;
import com.itosoftware.inderandroid.Interface.ZonesListener;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.Fragments.NewsFragment;
import com.itosoftware.inderandroid.R;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.itosoftware.inderandroid.Utils.GcmServiceHandler;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ApiNews {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    NewsListener callback;
    Context context;

    public ApiNews(Activity activity){
        try {
            callback = (NewsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement NewsListener interface");
        }
        this.context = activity.getApplicationContext();
    }

    public ApiNews(Fragment fragment){
        try {
            callback = (NewsListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement NewsListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }
    public ApiNews(GcmServiceHandler gcmServiceHandler, Context context){
        try {
            callback = (NewsListener) gcmServiceHandler;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement NewsListener interface");
        }
        this.context = context;
    }

    public void getNews (HashMap params){

        Integer page = (Integer) params.get("page");
        final Integer limit = (Integer) params.get("limit");

        String url = this.context.getString(R.string.url);
        String news_list = this.context.getString(R.string.news_list);

        url = url.concat(news_list);
        url = url.concat(new String("?pagina="));
        url = url.concat(page.toString());
        url = url.concat(new String("&elementos_por_pagina="));
        url = url.concat(limit.toString());

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.i("Url => ", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                NewContainer newContainer;

                Type listType = new TypeToken<NewContainer>() {
                }.getType();
                newContainer = gson.fromJson(response.toString(), listType);

                Integer total = newContainer.getTotalItems();
                ArrayList<New> itemsNews = newContainer.getItems();

                Integer maxPages = newContainer.getTotalItems() / limit;

                callback.onfinishedNews(itemsNews, maxPages);

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

    public void getNew(HashMap params){

        String id = (String) params.get("id").toString();

        String url = this.context.getString(R.string.url);
        String new_id = this.context.getString(R.string.new_id, id);

        url = url.concat(new_id);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();

        Log.i("Url Id Notice => ", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                New news;

                Type listType = new TypeToken<New>() {
                }.getType();
                news = gson.fromJson(response.toString(), listType);

                callback.onfinishedNew(news);

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

}







