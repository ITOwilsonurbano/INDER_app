package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Eps.Eps;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;

import java.util.ArrayList;

public interface SportScenarioListener {
    public void onFinishedConnection(ArrayList<SportsScenarios> itemsSportsScenarios);
    public void onFinishedConnectionWebview(ArrayList<SportsScenarios> itemsSportsScenarios, String sportsScenarios);
}