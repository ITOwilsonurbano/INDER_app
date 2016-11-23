package com.itosoftware.inderandroid.Api.SportsScenarios;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Api.Recommended.Recommended;
import com.itosoftware.inderandroid.Api.Recommended.RecommendedContainer;
import com.itosoftware.inderandroid.Fragments.RecommendedFragment;
import com.itosoftware.inderandroid.Fragments.SearchFragment;
import com.itosoftware.inderandroid.Fragments.SportsFragment;
import com.itosoftware.inderandroid.Interface.CommunesListener;
import com.itosoftware.inderandroid.Interface.SportScenarioListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrador on 5/11/15.
 */
public class ApiSportsScenarios implements Serializable {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    SportScenarioListener callback;
    Context context;

    public ApiSportsScenarios(Fragment fragment){
        try {
            callback = (SportScenarioListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SportScenarioListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public ApiSportsScenarios(FragmentActivity fragmentActivity){
        try {
            callback = (SportScenarioListener) fragmentActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SportScenarioListener interface");
        }
        this.context = fragmentActivity.getApplicationContext();
    }

    public void getSportsScenarios (HashMap params){

        Integer page;
        Integer limit;
        String keyword;
        String zone;
        String commune;
        String neighborhood;
        String discipline;

        String url = this.context.getString(R.string.url);
        String sports_list = this.context.getString(R.string.scenarios_list);
        url = url.concat(sports_list);

        //Verify Params
        if(!params.get("page").equals(null)){
            url = url.concat(new String("?pagina="));
            page = (Integer) params.get("page");
            url = url.concat(page.toString());
        }
        if(!params.get("limit").equals(null)){
            url = url.concat(new String("&elementos_por_pagina="));
            limit = (Integer) params.get("limit");
            url = url.concat(limit.toString());
        }
        if(!params.get("keyword").equals(null) && !params.get("keyword").equals("") && !params.get("keyword").equals("Búsqueda de escenarios")){
            url = url.concat(new String("&keyword="));
            keyword = (String) params.get("keyword");
            url = url.concat(keyword.toString());
        }
        if(!params.get("zone").equals(null) && !params.get("zone").equals("") && !params.get("zone").equals("0")){
            url = url.concat(new String("&zona_id="));
            zone = (String) params.get("zone");
            url = url.concat(zone);
        }
        if(!params.get("commune").equals(null) && !params.get("commune").equals("") && !params.get("commune").equals("0")){
            url = url.concat(new String("&comuna_id="));
            commune = (String) params.get("commune");
            url = url.concat(commune);
        }
        if(!params.get("neighborhood").equals(null) && !params.get("neighborhood").equals("") && !params.get("neighborhood").equals("0")){
            url = url.concat(new String("&barrio_id="));
            neighborhood = (String) params.get("neighborhood");
            url = url.concat(neighborhood);
        }
        if(!params.get("discipline").equals(null) && !params.get("discipline").equals("") && !params.get("discipline").equals("0")){
            url = url.concat(new String("&actividad_id="));
            discipline = (String) params.get("discipline");
            url = url.concat(discipline);
        }

        Log.d("URL Apis", url);
        Log.d("URL Apis", url);
        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "getSportsScenarios";
        fRequestQueue.cancelAll(TAG);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                SportsScenariosContainer sportsScenariosContainer;

                Type listType = new TypeToken<SportsScenariosContainer>() {
                }.getType();
                sportsScenariosContainer = gson.fromJson(response.toString(), listType);

                Integer total = sportsScenariosContainer.getTotalItems();
                ArrayList<SportsScenarios> itemsSportsScenarios = sportsScenariosContainer.getItems();

                //callback.onFinishedConnection(itemsSportsScenarios);
                Log.i("sports cargados",response.toString());
                callback.onFinishedConnectionWebview(itemsSportsScenarios,response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response Error => ","Errorsesase en los escenarios");
                Log.i("Response Error => ",error.toString());
            }
        });

        jsonObjReq.setTag(TAG);

        int socketTimeout = 40000;//40 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        //Add request to queue
        fRequestQueue.add(jsonObjReq);
    }

    public void getSportsScenariosAutoComplete (HashMap params){
        Integer page;
        Integer limit;
        String keyword;

        String url = this.context.getString(R.string.url);
        String sports_list = this.context.getString(R.string.scenarios_list);
        url = url.concat(sports_list);

        //Verify Params
        if(params.get("page") != null){
            url = url.concat(new String("?pagina="));
            page = (Integer) params.get("page");
            url = url.concat(page.toString());
        }
        if(params.get("limit") != null){
            url = url.concat(new String("&elementos_por_pagina="));
            limit = (Integer) params.get("limit");
            url = url.concat(limit.toString());
        }
        if(params.get("keyword") != null){
            url = url.concat(new String("&keyword="));
            keyword = (String) params.get("keyword");
            url = url.concat(keyword.toString());
            Log.d("Key",keyword);
        }

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "getSportsScenariosAutoComplete";
        fRequestQueue.cancelAll(TAG);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Gson gson = new Gson();
                Object container = gson.toJson(response.toString());

                SportsScenariosContainer sportsScenariosContainer;

                Type listType = new TypeToken<SportsScenariosContainer>() {
                }.getType();

                sportsScenariosContainer = gson.fromJson(response.toString(), listType);

                Integer total = sportsScenariosContainer.getTotalItems();
                ArrayList<SportsScenarios> itemsSportsScenarios = sportsScenariosContainer.getItems();
                callback.onFinishedConnection(itemsSportsScenarios);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response Error => ","Error en los recomendados");
                Log.i("Response Error => ",error.toString());
            }
        });

        jsonObjReq.setTag(TAG);

        int socketTimeout = 40000;//40 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }


}
