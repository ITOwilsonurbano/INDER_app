package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Eps.Eps;
import com.itosoftware.inderandroid.Api.GradeLevel.GradeLevel;

import java.util.ArrayList;

public interface GradeLevelListener {
    public void onfinishedLoadGradeLevels(ArrayList<GradeLevel> gradeLevels);
    public void onErrorLoadGradeLevels();
}