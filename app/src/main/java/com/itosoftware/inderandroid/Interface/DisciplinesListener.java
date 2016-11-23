package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;
import com.itosoftware.inderandroid.Api.Eps.Eps;

import java.util.ArrayList;

public interface DisciplinesListener {
    public void onfinishedLoadDisciplines(ArrayList<Disciplines> disciplines);
    public void onErrorLoadDisciplines();
}