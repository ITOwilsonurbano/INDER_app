package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Etnias.Etnias;
import com.itosoftware.inderandroid.Api.Towns.Towns;

import java.util.ArrayList;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface TownsListener {
    public void onfinishedLoadTowns(ArrayList<Towns> towns);
    public void onErrorLoadTowns();
}
