package com.itosoftware.inderandroid.Api.Recommended;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.News.NewContainer;
import com.itosoftware.inderandroid.Fragments.NewsFragment;
import com.itosoftware.inderandroid.Fragments.RecommendedFragment;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;




public class ApiRecommended {

    RecommendedFragment recommendedFragment;

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    Context context;


    public ApiRecommended(RecommendedFragment recommendedFragment){
        this.recommendedFragment = recommendedFragment;
        this.context = recommendedFragment.getActivity().getApplicationContext();
    }

    public void getRecommended (HashMap params){

        Integer page = (Integer) params.get("page");
        final Integer limit = (Integer) params.get("limit");

        String url = this.recommendedFragment.getString(R.string.url);
        String news_list = this.recommendedFragment.getString(R.string.recommended_list);

        url = url.concat(news_list);
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

                RecommendedContainer recommendedContainer;

                Type listType = new TypeToken<RecommendedContainer>() {
                }.getType();
                recommendedContainer = gson.fromJson(response.toString(), listType);

                Integer total = recommendedContainer.getTotalItems();
                ArrayList<Recommended> itemsRecommended = recommendedContainer.getItems();

                Integer maxPages = recommendedContainer.getTotalItems() / limit;

                recommendedFragment.onFinishedConnection(itemsRecommended, maxPages);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response Error => ","Error en los recomendados");
                Log.i("Response Error => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

}






