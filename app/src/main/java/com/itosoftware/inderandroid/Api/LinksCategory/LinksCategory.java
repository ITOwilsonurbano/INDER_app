package com.itosoftware.inderandroid.Api.LinksCategory;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrador on 9/11/15.
 */
public class LinksCategory implements Serializable {
    Integer id = 0;
    String nombre = "";
    ArrayList<HashMap> enlaces = new ArrayList<HashMap>();

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

    public ArrayList getEnlaces() {
        return enlaces;
    }
    public void setEnlaces(ArrayList<HashMap> enlaces) {
        if(enlaces != null){
            this.enlaces = enlaces;
        }
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
