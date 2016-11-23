package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Etnias.Etnias;

import java.util.ArrayList;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface EtniasListener {
    public void onfinishedLoadEtnias(ArrayList<Etnias> etnias);
    public void onErrorLoadEtnias();
}
