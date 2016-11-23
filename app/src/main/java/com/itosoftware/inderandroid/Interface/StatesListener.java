package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Etnias.Etnias;
import com.itosoftware.inderandroid.Api.States.States;

import java.util.ArrayList;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface StatesListener {
    public void onfinishedLoadStates(ArrayList<States> states);
    public void onErrorLoadStates();
}
