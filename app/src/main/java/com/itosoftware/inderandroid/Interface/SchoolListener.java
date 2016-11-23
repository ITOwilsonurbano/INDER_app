package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Eps.Eps;
import com.itosoftware.inderandroid.Api.School.School;

import java.util.ArrayList;

public interface SchoolListener {
    public void onfinishedLoadSchools(ArrayList<School> schools);
    public void onErrorLoadSchools();
}