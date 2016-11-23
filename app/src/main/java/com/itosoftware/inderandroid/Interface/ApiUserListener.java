package com.itosoftware.inderandroid.Interface;

import android.app.Application;

import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.UserContainer;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rasuncion on 11/11/15.
 */
public interface ApiUserListener {
    public void onFinishedAuthentication( UserContainer userContainer, Boolean authenticated);
    public void onFinishedRegister(Boolean success, HashMap data);
    public void onFinishedForgetPassword();
    public void onFinishedForgetUser();
    public void onFinishedAddParticipant(Participant participant, Integer status);
    public void onFinishedProfile(ApiUser.Info responseData);
    public void onFinishedProfilePqr(JSONObject response);
}
