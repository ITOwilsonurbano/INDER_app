package com.itosoftware.inderandroid.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.itosoftware.inderandroid.Api.SportActivities.SportActivities;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Fragments.SportActivitiesFragment;
import com.itosoftware.inderandroid.Fragments.SportActivityDetailFragment;
import com.itosoftware.inderandroid.Fragments.SportDetailFragment;
import com.itosoftware.inderandroid.Fragments.SportsFragment;

/**
 * Created by oscar on 9/07/15.
 */
public class WebAppActivitiesInterface {
    Context mContext;
    SportActivitiesFragment mSportsActivitiesFragment;
    SportActivityDetailFragment dFragment;


    /** Instantiate the interface and set the context */
    public WebAppActivitiesInterface(SportActivitiesFragment f) {
        mContext = f.getContext();
        mSportsActivitiesFragment = f;
        dFragment = new SportActivityDetailFragment();
    }

    /** Show a dialog from the webview */
    @JavascriptInterface
    public void showDialog(String message) {
        new AlertDialog.Builder(mContext).setTitle("Alert").setMessage(message).setNeutralButton("Close", null).show();
    }

    /** Show SportDetailFragment from the webview */
    @JavascriptInterface
    public void onActivitiesItemInfoWindowClick(int position) {
        //Get Details Sport Scenario
        SportActivities sportActivities = (SportActivities) mSportsActivitiesFragment.getDataMarkers().get(position);
        //showDialog(sportsScenario.toString());

        //Send Information to SportDetailFragment
        Bundle args = new Bundle();
        args.putString("sportActivities", sportActivities.toString());

        // Open Dialog Detail Sport
        dFragment.setArguments(args);
        dFragment.show(mSportsActivitiesFragment.getFragmentManager(), "Dialog Fragment");
    }

    public boolean mostrarScenarios(WebView wvApp, String activities){
        String code = "javascript:window.mostrarMarcadores("+ activities +");"; // MEtodo javascript de assets/logica_activities,js
        Log.i("mostrarMarcadores", "webview");
        wvApp.loadUrl(code);

        try {
            // Se da tiempo para que el script se ejecute.
            Thread.sleep(500);
        }
        catch (InterruptedException e){
            Log.e(null, "Interrupted", e);
        }

        return true;
    }

    public boolean mostrarMiUbicacion(WebView wvApp, double latitude, double longitude){
        String code = "javascript:window.mostrarMiUbicacion("+ latitude +","+ longitude +");"; // MEtodo javascript de assets/logica_activities,js
        Log.i("mostrarMiUbicacion","webview");
        wvApp.loadUrl(code);

        try {
            // Se da tiempo para que el script se ejecute.
            Thread.sleep(500);
        }
        catch (InterruptedException e){
            Log.e(null, "Interrupted", e);
        }

        return true;
    }

    public boolean actualizarMiUbicacion(WebView wvApp, double latitude, double longitude){
        String code = "javascript:window.actualizarMiUbicacion("+ latitude +","+ longitude +");"; // MEtodo javascript de assets/logica_activities,js
        Log.i("actualizarMiUbicacion","webview");
        wvApp.loadUrl(code);

        try {
            // Se da tiempo para que el script se ejecute.
            Thread.sleep(500);
        }
        catch (InterruptedException e){
            Log.e(null, "Interrupted", e);
        }

        return true;
    }



}
