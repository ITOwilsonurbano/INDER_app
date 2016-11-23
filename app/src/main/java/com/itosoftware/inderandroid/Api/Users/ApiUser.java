package com.itosoftware.inderandroid.Api.Users;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Fragments.PqrFragment;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.Interface.NeighborhoodListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.Utils.GcmServiceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by administrador on 9/11/15.
 */
public class ApiUser {
    private ApiConnection apiConnection;
    protected RequestQueue fRequestQueue;
    private Globals g;
    Integer statusCode;
    ApiUserListener callback;
    Context context;
    Fragment fragment;

    public ApiUser(Activity activity) {
        try {
            callback = (ApiUserListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement ApiUserListener interface");
        }
        this.context = activity.getApplicationContext();
        g = (Globals) activity.getApplication();
    }

    public ApiUser(Fragment fragment) {
        this.fragment = fragment;
        try {
            callback = (ApiUserListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement ApiUserListener interface");
        }
        this.context = fragment.getActivity().getApplicationContext();
        g = (Globals) fragment.getActivity().getApplication();
    }

    public ApiUser(GcmServiceHandler gcmServiceHandler) {
        try {
            callback = (ApiUserListener) gcmServiceHandler;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement ApiUserListener interface");
        }
        this.context = gcmServiceHandler.getApplicationContext();
        g = (Globals) gcmServiceHandler.getApplication();
    }


    public void addParticipant(final HashMap params) {

        String identification = (String) params.get("identification");

        final HashMap paramsUser = new HashMap();

        String url = context.getString(R.string.url);
        String add_participants = context.getString(R.string.add_participants, identification);
        url = url.concat(add_participants);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "addParticipant";
        fRequestQueue.cancelAll(TAG);

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                HashMap user;

                Type listType = new TypeToken<HashMap>() {
                }.getType();
                user = gson.fromJson(response.toString(), listType);

                Random r = new Random();
                Integer id = r.nextInt(50000 - 0);

                HashMap participantParams = new HashMap<>();
                participantParams.put("user_id", id);
                participantParams.put("first_name", user.get("primer_nombre").toString());
                participantParams.put("last_name", user.get("primer_apellido").toString());
                participantParams.put("email", user.get("email").toString());
                participantParams.put("identification", new Integer(user.get("id").toString().replace(".0", "")));

                callback.onFinishedAddParticipant(new Participant(participantParams), 200);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                    error = volleyError;
                }
                Log.e("Error status", statusCode.toString());
                Log.e("Error label", error.toString());
                switch (statusCode) {
                    case 404:
                        //user or password invalids
                        callback.onFinishedAddParticipant(null, 404);
                        break;
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramsUser;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);
                // add headers <key,value>
                String credentials = client_id + ":" + client_secret;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };


        sr.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(sr);

    }


    public void authenticatedUser(HashMap params) {

        String username = (String) params.get("username");
        String password = (String) params.get("password");

        final HashMap paramsUser = new HashMap();

        final SharedPreferences prefs = context.getSharedPreferences("Preferences", 0);
        String registrationId = prefs.getString("registration_id", "");

        paramsUser.put("username", username);
        paramsUser.put("password", password);
        paramsUser.put("device_id", registrationId);
        paramsUser.put("device_type", "android");

        String url = context.getString(R.string.url);
        String user_authenticated = context.getString(R.string.user_authenticated);
        url = url.concat(user_authenticated);

        Log.d("username", username);
        Log.d("password", password);
        Log.d("device_id", registrationId);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "authenticatedUser";
        fRequestQueue.cancelAll(TAG);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response on login", response);

                Gson gson = new Gson();
                UserContainer userContainer;

                Type listType = new TypeToken<UserContainer>() {
                }.getType();
                userContainer = gson.fromJson(response.toString(), listType);

                callback.onFinishedAuthentication(userContainer, true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                    error = volleyError;
                }

                Log.e("Error status", statusCode.toString());
                Log.e("Error label", error.toString());

                switch (statusCode) {
                    case 400:
                        //user or password invalids
                        callback.onFinishedAuthentication(null, false);
                        break;
                    case 401:
                        //Expired token
                        break;
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramsUser;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);

                // add headers <key,value>
                String credentials = client_id + ":" + client_secret;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };


        sr.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(sr);

    }

    public void registerUser(HashMap params) {

        final HashMap paramsUser = params;

        Integer countParams = paramsUser.size();
        Log.d("countParams => ", countParams.toString());

        String url = context.getString(R.string.url);
        String user_authenticated = context.getString(R.string.registro_usuario);
        url = url.concat(user_authenticated);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "registerUser";
        fRequestQueue.cancelAll(TAG);

        final SharedPreferences prefs = context.getSharedPreferences("Preferences", 0);
        String registrationId = prefs.getString("registration_id", "");

        paramsUser.put("device_id", registrationId);
        paramsUser.put("device_type", "android");

        Log.d("Register => ", "paramsUser : " + paramsUser.toString());

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                HashMap hashMap;
//
                Type listType = new TypeToken<HashMap>() {
                }.getType();
                hashMap = gson.fromJson(response.toString(), listType);

                callback.onFinishedRegister(true, hashMap);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    VolleyError volleyError = new VolleyError(new String(error.networkResponse.data));
                    error = volleyError;
                }

                Log.e("Error status", statusCode.toString());
                Log.e("Error label", error.toString());

                switch (statusCode) {
                    case 400:
                        //user or password invalids

                        String[] errores = error.toString().split(",\"errors\":");
                        String errorString = errores[1].replace("[", "");
                        errorString = errorString.replace("]}", "");

                        HashMap data = new HashMap();
                        data.put("message", errorString);
                        callback.onFinishedRegister(false, data);
                        break;
                    case 401:
                        //Expired token
                        break;
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return paramsUser;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);

                // add headers <key,value>
                String credentials = client_id + ":" + client_secret;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                1,
                1f));

        sr.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(sr);

    }

    public String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    public void displayMessage(String toastString) {
        Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
    }

    public void forgetUser(HashMap params) {

        final HashMap paramsUser = params;

        String url = context.getString(R.string.url);
        String forget_user = context.getString(R.string.zones_list);
        url = url.concat(forget_user);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "forgetUser";
        fRequestQueue.cancelAll(TAG);
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(), new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onFinishedForgetUser();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;

                Log.e("Response Error => ", error.toString());
                Log.e("Response Code Error => ", statusCode.toString());

            }
        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return paramsUser;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);
                String auth = "Basic "
                        + Base64.encodeToString((client_id
                        + ":" + client_secret).getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        jsonObjReq.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }


    public void forgetPassword(HashMap params) {

        final HashMap paramsUser = params;

        String url = context.getString(R.string.url);
        String forget_password = context.getString(R.string.zones_list);
        url = url.concat(forget_password);

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "forgetPassword";
        fRequestQueue.cancelAll(TAG);
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(), new Response.Listener<JSONObject>() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onFinishedForgetPassword();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;

                Log.e("Response Error => ", error.toString());
                Log.e("Response Code Error => ", statusCode.toString());

            }
        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return paramsUser;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);
                String auth = "Basic "
                        + Base64.encodeToString((client_id
                        + ":" + client_secret).getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        jsonObjReq.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }


    public void getProfile(HashMap params) {

        final HashMap paramsUser = params;

        String url = context.getString(R.string.url);
        String profile = context.getString(R.string.profile);
        url = url + profile + params.get("token");

        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "profile";
        fRequestQueue.cancelAll(TAG);

        Log.d("urlprofile", url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d("Response Profile raw", response.toString());
                    Gson gson = new Gson();
                    ResponseProfile responseData;
                    Type listType = new TypeToken<ResponseProfile>() {
                    }.getType();
                    responseData = gson.fromJson(response.toString(), listType);
                    g.setIsAuthorizedToReserve((Boolean) responseData.getAutorizado());

                    Log.d("Response Profile ", " User id => " + responseData.getInfo().getId().toString());
                    Log.d("Response Profile ", " data => " + responseData.toString());
                    if (fragment != null && fragment instanceof PqrFragment) {
                        if (fragment instanceof PqrFragment) {
                            callback.onFinishedProfilePqr(response);
                        } else {
                            callback.onFinishedProfile(responseData.getInfo());
                        }

                    } else {
                        callback.onFinishedProfile(responseData.getInfo());
                    }
                }catch (Exception e){
                    Log.e("Process Login",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;

                Log.e("Response Error => ", error.toString());
                Log.e("Response Code Error => ", statusCode.toString());
                if (error instanceof AuthFailureError) {
                    Log.e("splash", "auth");
                    refreshToken(paramsUser, "getProfile");
                }
            }
        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return paramsUser;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);
                String auth = "Basic "
                        + Base64.encodeToString((client_id
                        + ":" + client_secret).getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        jsonObjReq.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(jsonObjReq);

    }

    private void refreshToken(final HashMap<String, String> params, final String from) {
        String url = context.getString(R.string.url);
        String token = context.getString(R.string.refresh_token);
        url = url + token;
        final HashMap<String, String> paramsRefresh = new HashMap<>();
        paramsRefresh.put("grant_type", "refresh_token");
        paramsRefresh.put("refresh_token", g.getRefreshToken());
        //Create a request queue
        apiConnection = apiConnection.getInstance(context);

        //Get the request queue
        fRequestQueue = apiConnection.getRequestQueue();
        String TAG = "refresh";
        fRequestQueue.cancelAll(TAG);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("prueba_splash_refresh", "post:" + response.toString());
                JSONObject jsonObject = new JSONObject();
                if (from.equals("getProfile")) {
                    try {
                        HashMap parameters = new HashMap<>();
                        parameters.put("token", jsonObject.getString("access_token"));

                        jsonObject = new JSONObject(response);
                        g.setUserIsAuthenticated(true);
                        g.setToken(jsonObject.getString("access_token"));
                        g.setRefreshToken(jsonObject.getString("refresh_token"));
                        SharedPreferences settings = context.getSharedPreferences("Preferences", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("access_token", jsonObject.getString("access_token"));
                        editor.putString("refresh_token", jsonObject.getString("refresh_token"));
                        editor.commit();
                        getProfile(parameters);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Integer statusCode = error.networkResponse.statusCode;

                Log.e("Response Error => ", error.toString());
                Log.e("Response Code Error => ", statusCode.toString());
                g.logoutButton();
                Info info = new Info();
                callback.onFinishedProfile(info);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paramsRefresh;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String client_id = context.getString(R.string.client_id);
                String client_secret = context.getString(R.string.client_secret);
                String auth = "Basic "
                        + Base64.encodeToString((client_id
                        + ":" + client_secret).getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };
        sr.setTag(TAG);

        //Add request to queue
        fRequestQueue.add(sr);

    }


    class ResponseProfile implements Serializable {
        public Boolean autorizado;
        public Info info;

        public Boolean getAutorizado() {
            return autorizado;
        }

        public void setAutorizado(Boolean autorizado) {
            this.autorizado = autorizado;
        }

        public Info getInfo() {
            return info;
        }

        public void setInfo(Info info) {
            this.info = info;
        }

        @Override
        public String toString() {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        }
    }

    public class Info {
        public String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
