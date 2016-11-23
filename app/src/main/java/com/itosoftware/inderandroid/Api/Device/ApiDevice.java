package com.itosoftware.inderandroid.Api.Device;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.itosoftware.inderandroid.Api.Countries.Countries;
import com.itosoftware.inderandroid.Api.Countries.CountriesContainer;
import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservationContainer;
import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;
import com.itosoftware.inderandroid.Api.News.New;

import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.CountriesListener;
import com.itosoftware.inderandroid.Interface.DeviceListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.Utils.GcmServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApiDevice {

    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;

    DeviceListener callback;
    Context context;

    public ApiDevice(Activity activity){
        try {
            callback = (DeviceListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling activity must implement DeviceListener interface");
        }
        this.context = activity.getApplicationContext();
    }

    public ApiDevice(Fragment fragment){
        try {
            callback = (DeviceListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DeviceListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
    }

    public void createDevice() {
        Log.d("ApiDevice - ","createDevice");
        final HashMap paramsDevice = new HashMap();

        String url = context.getString(R.string.url);
        String add_new_device = context.getString(R.string.add_new_device);
        url = url.concat(add_new_device);

        final SharedPreferences prefs = context.getSharedPreferences("Preferences", 0);
        String registrationId = prefs.getString("registration_id", "");

        paramsDevice.put("tipo", "android");
        paramsDevice.put("identificador", registrationId);


        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "createDevice";
        fRequestQueue.cancelAll(TAG);


        Log.d("Registra Dispositivo",registrationId);
        Log.d("URL: ",url);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("createDevice: ","onResponse");
                Gson gson = new Gson();

                callback.onCreateDevice();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("createDevice: ","onErrorResponse");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramsDevice;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);
                // add headers <key,value>
                String credentials = client_id + ":" + client_secret;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(),Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        sr.setTag(TAG);
        //Add request to queue
        fRequestQueue.add(sr);
    }

}
