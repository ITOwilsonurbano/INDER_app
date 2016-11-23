package com.itosoftware.inderandroid.Api.SportsScenarios;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by administrador on 5/11/15.
 */
public class SportsScenarios implements Serializable {

    Integer id = 0;
    String imagen_url = "";
    HashMap info = new HashMap();
    Double latitude = 0.0;
    Double longitude = 0.0;
    Boolean is_active = false;
    String descripcion = "";

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        if(id != null){
            this.id = id;
        }
    }

    public String getImagen_url() {
        return imagen_url;
    }
    public void setImagen_url(String imagen_url) {
        if(imagen_url != null) {
            this.imagen_url = imagen_url;
        }
    }

    public HashMap getInfo() {
        return info;
    }
    public void setInfo(HashMap info) {
        if(info != null){
            this.info = info;
        }
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        if (latitude != null) {
            this.latitude = latitude;
        }
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        if(longitude != null){
            this.longitude = longitude;
        }
    }

    public Boolean getIs_active() {
        return is_active;
    }
    public void setIs_active(Boolean is_active) {
        if(is_active != null) {
            this.is_active = is_active;
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {

        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }
}
