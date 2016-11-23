package com.itosoftware.inderandroid.Api.Reservations;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Api.Subdivisions.Subdivisions;

import java.io.Serializable;
import java.util.HashMap;


/**
 * Created by administrador on 13/10/15.
 */
public class Reservations implements Serializable {

    public Integer id ;
    public SportsScenarios escenario ;
    public Disciplines disciplina;
    public Subdivisions subdivision;
    public String inicio;
    public String fin;
    public String estado;
    public String participants;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public SportsScenarios getSportId() { return this.escenario; }
    public void setSportId(SportsScenarios sportId) { this.escenario = sportId; }

    public Disciplines getDiscipline() { return disciplina; }
    public void setDiscipline(Disciplines discipline) { this.disciplina = disciplina; }

    public Subdivisions getSubdivision() {
        return subdivision;
    }
    public void setSubdivision(Subdivisions subdivision) {
        this.subdivision = subdivision;
    }

    public String getStatus() { return estado; }
    public void setStatus(String estado) { this.estado = estado; }

    public String getParticipants() {
        return participants;
    }
    public void setParticipants(String participants) {
        this.participants = participants;
    }

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

    public Reservations(HashMap params) {
        this.setId(Integer.parseInt(params.get("id").toString()));
        this.setSportId((SportsScenarios) params.get("sport_id"));
        this.setInicio((String) params.get("date_start"));
        this.setFin((String) params.get("date_end"));
        this.setDiscipline((Disciplines) params.get("discipline"));
        this.setSubdivision((Subdivisions) params.get("subdivision"));
        this.setStatus((String) params.get("status"));
        this.setParticipants((String) params.get("participants"));
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
