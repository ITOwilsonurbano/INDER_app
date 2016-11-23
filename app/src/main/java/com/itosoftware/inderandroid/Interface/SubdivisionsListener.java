package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Eps.Eps;
import com.itosoftware.inderandroid.Api.Subdivisions.Subdivisions;

import java.util.ArrayList;

public interface SubdivisionsListener {
    public void onfinishedLoadSubdivisions(ArrayList<Subdivisions> subdivisions);
    public void onErrorLoadSubdivisions();
}