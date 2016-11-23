package com.itosoftware.inderandroid.Interface;

import org.json.JSONObject;

import java.util.ArrayList;

public interface PqrListener {
    public void onfinishedTrue(JSONObject response,String from);
    public void onfinishedFalse(String error);
}