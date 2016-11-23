package com.itosoftware.inderandroid.Api.ParticipantsDetailReservation;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Api.Subdivisions.Subdivisions;

import java.io.Serializable;
import java.util.HashMap;


/**
 * Created by administrador on 13/10/15.
 */
public class Participants implements Serializable {

    public Integer id ;
    public String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Participants(HashMap params) {
        this.setId(Integer.parseInt(params.get("id").toString()));
        this.setNombre(params.get("nombre").toString());

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
