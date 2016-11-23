package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.Disciplines.Disciplines;

import java.util.ArrayList;

public interface DatesReservationListener {
    public void onfinishedLoadDatesReservation(ArrayList<DatesReservation> DatesReservation);
}