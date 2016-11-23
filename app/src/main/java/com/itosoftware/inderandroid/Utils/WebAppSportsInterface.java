package com.itosoftware.inderandroid.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Fragments.SportDetailFragment;
import com.itosoftware.inderandroid.Fragments.SportsFragment;

/**
 * Created by oscar on 9/07/15.
 */
public class WebAppSportsInterface {
    Context mContext;
    SportsFragment mSportsFragment;
    SportDetailFragment dFragment;


    /** Instantiate the interface and set the context */
    public WebAppSportsInterface(SportsFragment f) {
        mContext = f.getContext();
        mSportsFragment = f;
        dFragment = new SportDetailFragment();
    }

    /** Show a dialog from the webview */
    @JavascriptInterface
    public void showDialog(String message) {
        new AlertDialog.Builder(mContext).setTitle("Alert").setMessage(message).setNeutralButton("Close", null).show();
    }

    /** Show SportDetailFragment from the webview */
    @JavascriptInterface
    public void onSportsItemInfoWindowClick(int position) {
        //Get Details Sport Scenario
        SportsScenarios sportsScenario = (SportsScenarios) mSportsFragment.getDataMarkers().get(position);
        //showDialog(sportsScenario.toString());

        //Send Information to SportDetailFragment
        Bundle args = new Bundle();
        args.putString("sportsScenario", sportsScenario.toString());

        // Open Dialog Detail Sport
        dFragment.setArguments(args);
        dFragment.show(mSportsFragment.getFragmentManager(), "Dialog Fragment");
    }

    public boolean mostrarScenarios(WebView wvApp, String scenarios){
        String code = "javascript:window.mostrarMarcadores("+ scenarios +");"; // MEtodo javascript de assets/logica,js
        Log.i("mostrarMarcadoresE", "webview");
        wvApp.loadUrl(code);

//        try {
//            // Se da tiempo para que el script se ejecute.
//            Thread.sleep(500);
//        }
//        catch (InterruptedException e){
//            Log.e(null, "Interrupted", e);
//        }

        return true;
    }

    public boolean mostrarMiUbicacion(WebView wvApp, double latitude, double longitude){
        String code = "javascript:window.mostrarMiUbicacion("+ latitude +","+ longitude +");"; // MEtodo javascript de assets/logica,js
        Log.i("mostrarMiUbicacion","webview");
        wvApp.loadUrl(code);

//        try {
//            // Se da tiempo para que el script se ejecute.
//            Thread.sleep(500);
//        }
//        catch (InterruptedException e){
//            Log.e(null, "Interrupted", e);
//        }

        return true;
    }

    public boolean actualizarMiUbicacion(WebView wvApp, double latitude, double longitude){
        String code = "javascript:window.actualizarMiUbicacion("+ latitude +","+ longitude +");"; // MEtodo javascript de assets/logica,js
        Log.i("actualizarMiUbicacion","webview");
        wvApp.loadUrl(code);

//        try {
//            // Se da tiempo para que el script se ejecute.
//            Thread.sleep(500);
//        }
//        catch (InterruptedException e){
//            Log.e(null, "Interrupted", e);
//        }

        return true;
    }



}
