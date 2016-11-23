package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Zones.Zones;

import java.util.ArrayList;
import java.util.HashMap;

public interface ZonesListener {
    public void onfinishedLoadZones(ArrayList<Zones> zones);
    public void onErrorLoadZones();
}