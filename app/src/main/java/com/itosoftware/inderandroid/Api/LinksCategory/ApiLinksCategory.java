package com.itosoftware.inderandroid.Api.LinksCategory;

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
import com.itosoftware.inderandroid.Interface.CommunesListener;
import com.itosoftware.inderandroid.Interface.LinksCategoryListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrador on 9/11/15.
 */
public class ApiLinksCategory {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    LinksCategoryListener callback;
    Context context;

    public ApiLinksCategory(Fragment fragment){
        try {
            callback = (LinksCategoryListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement LinksCategoryListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public void getLinksCategory(HashMap params){
        final Integer page = (Integer) params.get("page");
        final Integer limit = (Integer) params.get("limit");

        String url = this.context.getString(R.string.url);
        String lnks_inder_list = this.context.getString(R.string.lnks_inder_list);

        url = url.concat(lnks_inder_list);
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
                LinksCategoryContainer linksCategoryContainer;
                Type listType = new TypeToken<LinksCategoryContainer>() {}.getType();
                linksCategoryContainer = gson.fromJson(response.toString(), listType);
                Integer total = linksCategoryContainer.getTotalItems();
                ArrayList<LinksCategory> itemsLinksCategory = linksCategoryContainer.getItems();
                Integer maxPages = total / limit;
                callback.onfinishedLoadLinksCategory(itemsLinksCategory, maxPages);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("ResponsError Links => ",error.toString());
            }
        });

        //Add request to queue
        fRequestQueue.add(jsonObjReq);
    }

}
