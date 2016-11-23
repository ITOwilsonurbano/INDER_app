package com.itosoftware.inderandroid.Interface;

import java.util.HashMap;

public interface DialogReservationsClickListener {
    public String getLabel();
    public void setLabel(String numReserva);
    public String getEstado();
    public void setEstado(String estado);
    public String getDate();
    public void setDate(String date);
    public void updateReservations(HashMap params);


}