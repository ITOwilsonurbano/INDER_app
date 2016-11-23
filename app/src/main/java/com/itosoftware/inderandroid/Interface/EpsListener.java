package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.EconomyActivity.EconomyActivity;
import com.itosoftware.inderandroid.Api.Eps.Eps;

import java.util.ArrayList;

public interface EpsListener {
    public void onfinishedLoadEps(ArrayList<Eps> eps);
    public void onErrorLoadEps();
}