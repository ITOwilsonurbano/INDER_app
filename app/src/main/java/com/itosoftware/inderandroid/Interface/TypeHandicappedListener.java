package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Neighborhood.Neighborhood;
import com.itosoftware.inderandroid.Api.TypeHandicapped.TypeHandicapped;

import java.util.ArrayList;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface TypeHandicappedListener {
    public void onfinishedLoadTypeHandicapped(ArrayList<TypeHandicapped> typeHandicapped);
    public void onErrorLoadTypeHandicapped();
}
