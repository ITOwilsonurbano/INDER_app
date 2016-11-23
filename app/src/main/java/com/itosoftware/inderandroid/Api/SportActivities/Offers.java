package com.itosoftware.inderandroid.Api.SportActivities;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by administrador on 11/12/15.
 */
public class Offers implements Serializable  {

    public ScheduleOfferContainer horarios;
    public Integer id;
    public String nombre;
    public HashMap responsable_oferta;
    public Disciplines disciplina;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public HashMap getResponsable_oferta() {
        return responsable_oferta;
    }

    public void setResponsable_oferta(HashMap responsable_oferta) {
        this.responsable_oferta = responsable_oferta;
    }

    public Disciplines getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplines disciplina) {
        this.disciplina = disciplina;
    }

    public ScheduleOfferContainer getHorarios() {
        return horarios;
    }

    public void setHorarios(ScheduleOfferContainer horarios) {
        this.horarios = horarios;
    }

    @Override
    public String toString() {

        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }
}
