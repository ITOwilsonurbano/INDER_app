package com.itosoftware.inderandroid.Api.Users;

import android.support.v4.app.Fragment;

import com.itosoftware.inderandroid.Interface.UserListener;

import java.util.HashMap;


/**
 * Created by administrador on 13/10/15.
 */
public class User extends UserProfile{

    UserListener callback;
//    GlobalClass globalClass;

    String username = "";
    String token = "";
    String refresh_token  = "";

    public User(HashMap params, Fragment fragment){
        super(params);
        try {
            callback = (UserListener) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement UserListener interface");
        }
//        this.globalClass = (GlobalClass) callback.getApplication();
    }


    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getToken(){
        return this.token;
    }
    public void setToken(String token){
        this.token = token;
//        this.globalClass.setTokenUser(token);
    }

    public String getRefreshToken(){
        return this.refresh_token;
    }
    public void setRefreshToken(String refresh_token){
        this.refresh_token = refresh_token;
    }


    //CUSTOM
    public void clearUserInformation(){
        //CLEAR USER INFO
//        user_information.authenticated_user = nil
//        this.globalClass.setTokenUser("");
        this.username = "";
        this.token  = "";
        this.refresh_token  = "";
        //profile
        this.user_id = null;
        this.email = "";
        this.first_name  = "";
        this.last_name  = "";
        this.identification  = null;
    }

}
