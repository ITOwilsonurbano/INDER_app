package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Eps.Eps;
import com.itosoftware.inderandroid.Api.TypeSchool.TypeSchools;

import java.util.ArrayList;

public interface TypeSchoolListener {
    public void onfinishedTypeSchools(ArrayList<TypeSchools> typeSchools);
    public void onErrorLoadTypeSchools();
}