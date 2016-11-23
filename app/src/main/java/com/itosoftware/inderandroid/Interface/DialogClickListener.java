package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Fragments.SportsFragment;

import java.util.HashMap;

public interface DialogClickListener {
    public String getLabel();
    public void setLabel(String zone);
    public String getZone();
    public void setZone(String zone);
    public String getCommune();
    public void setCommune(String commune);
    public String getNeighborhood();
    public void setNeighborhood(String neighborhood);
    public String getDiscipline();
    public void setDiscipline(String discipline);
    public void updateMap(HashMap paramss);


}