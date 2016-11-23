package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.EconomyActivity.EconomyActivity;
import com.itosoftware.inderandroid.Api.Zones.Zones;

import java.util.ArrayList;

public interface EconomyActivityListener {
    public void onfinishedLoadEconomyActivity(ArrayList<EconomyActivity> economyActivity);
    public void onErrorLoadEconomyActivity();
}