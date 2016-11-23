package com.itosoftware.inderandroid.Api.Pqr;

import com.google.gson.Gson;

/**
 * Created by itofelipeparra on 30/06/16.
 */
public class PqrSpinnerClass {
    Integer id = 0;
    String nombre = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id != null) {
            this.id = id;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null) {
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
