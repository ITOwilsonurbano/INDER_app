package com.itosoftware.inderandroid.Api.SportActivities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrador on 9/12/15.
 */
public class ScheduleCategoryContainer {
    String nombre;
    ArrayList<HashMap> horario;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<HashMap> getHorario() {
        return horario;
    }
    public void setHorario(ArrayList<HashMap> horario) {
        this.horario = horario;
    }
}


