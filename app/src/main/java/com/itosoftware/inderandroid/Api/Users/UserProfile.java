package com.itosoftware.inderandroid.Api.Users;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by administrador on 9/11/15.
 */
public class UserProfile implements Serializable {

    public Integer user_id ;
    public String first_name;
    public String last_name;
    public String email;
    public Integer identification;

    public Integer getIdentification() {
        return identification;
    }
    public void setIdentification(Integer identification) {
        this.identification = identification;
    }

    public Integer getId() {
        return user_id;
    }
    public void setId(Integer id) {
        this.user_id = id;
    }

    public String getFirstName() {
        return first_name;
    }
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return last_name;
    }
    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public UserProfile(HashMap params) {
        this.setId((Integer) params.get("user_id"));
        this.setFirstName((String) params.get("first_name"));
        this.setLastName((String) params.get("last_name"));
        this.setEmail((String) params.get("email"));
        this.setIdentification((Integer) params.get("identification"));
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        // convert java object to JSON format,
        // and returned as JSON formatted string
        String json = gson.toJson(this);
        return json;
    }
}
