package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.SportActivities.SportActivities;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;

import java.util.ArrayList;

public interface SportActivitiesListener {
    public void onFinishedConnection(ArrayList<SportActivities> itemsSportsActivities, Integer result);
    public void onFinishedScheduleOffer(String scheduleOffer);
    public void onFinishedConnectionWebview(ArrayList<SportActivities> itemsSportsActivities, String sportsActivities, Integer result);
}