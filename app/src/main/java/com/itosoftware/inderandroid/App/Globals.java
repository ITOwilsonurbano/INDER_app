package com.itosoftware.inderandroid.App;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.widget.ImageView;

import com.itosoftware.inderandroid.R;

/**
 * Created by administrador on 19/11/15.
 */
public class Globals extends Application {

    private Boolean userIsAuthenticated = false;
    private String token = "";
    private String refreshToken = "";
    private Boolean verButtonSearchReservation = false;
    private Boolean verButtonReloadMap = false;
    private Boolean isAuthorizedToReserve = false;
    private Boolean siReservationFragment = false;
    private Boolean siSwiping = true;


    public Boolean getVerButtonReloadMap() {
        return verButtonReloadMap;
    }

    public void setVerButtonReloadMap(Boolean verButtonReloadMap) {
        this.verButtonReloadMap = verButtonReloadMap;
    }

    public Boolean getUserIsAuthenticated() {
        return userIsAuthenticated;
    }

    public void setUserIsAuthenticated(Boolean userIsAuthenticated) {
        if (!userIsAuthenticated.equals(null)) {
            this.userIsAuthenticated = userIsAuthenticated;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {

        this.token = token;

    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {

        this.refreshToken = refreshToken;

    }

    public void logoutButton() {
        this.setUserIsAuthenticated(false);
        this.setRefreshToken(null);
        this.setToken(null);
        this.setIsAuthorizedToReserve(false);
        SharedPreferences settings = this.getApplicationContext().getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("access_token", null);
        editor.putString("refresh_token", null);
        editor.commit();

    }

    public Boolean getVerButtonSearchReservation() {
        return verButtonSearchReservation;
    }

    public void setVerButtonSearchReservation(Boolean verButtonSearchReservation) {
        this.verButtonSearchReservation = verButtonSearchReservation;
    }

    public Boolean getSiReservationFragment() {
        return siReservationFragment;
    }

    public void setSiReservationFragment(Boolean siReservationFragment) {
        this.siReservationFragment = siReservationFragment;
    }

    public Boolean getSiSwiping() {
        return siSwiping;
    }

    public void setSiSwiping(Boolean siSwiping) {
        this.siSwiping = siSwiping;
    }

    public Boolean getIsAuthorizedToReserve() {
        return isAuthorizedToReserve;
    }

    public void setIsAuthorizedToReserve(Boolean isAuthorizedToReserve) {
        this.isAuthorizedToReserve = isAuthorizedToReserve;
    }
}
