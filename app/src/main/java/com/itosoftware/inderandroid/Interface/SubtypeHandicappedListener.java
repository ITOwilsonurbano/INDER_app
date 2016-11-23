package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.SubtypeHandicapped.SubtypeHandicapped;
import com.itosoftware.inderandroid.Api.TypeHandicapped.TypeHandicapped;

import java.util.ArrayList;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface SubtypeHandicappedListener {
    public void onfinishedLoadSubtypeHandicapped(ArrayList<SubtypeHandicapped> subtypeHandicapped);
    public void onErrorLoadSubtypeHandicapped();
}
