package com.itosoftware.inderandroid.Api.Zones;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by edgar on 8/11/2015.
 */
public class Zones implements Serializable {
    Integer id = 0;
    String nombre = "";

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        if(id != null){
            this.id = id;
        }
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        if(nombre != null) {
            this.nombre = nombre;
        }
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
