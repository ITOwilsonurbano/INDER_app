package com.itosoftware.inderandroid.Api.DatesReservation;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by edgar on 8/11/2015.
 */
public class DatesReservation implements Serializable {
    String inicio;
    String fin;
    Boolean disponible;

    public String getInicio() {
        return inicio;
    }
    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }
    public void setFin(String fin) {
        this.fin = fin;
    }

    public Boolean getDisponible() {
        return disponible;
    }
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}
