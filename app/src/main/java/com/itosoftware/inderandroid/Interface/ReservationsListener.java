package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.Eps.Eps;
import com.itosoftware.inderandroid.Api.Reservations.ApiReservation;

import com.itosoftware.inderandroid.Api.Reservations.Reservations;

import java.util.ArrayList;
import java.util.HashMap;

public interface ReservationsListener {
    public void onfinishedLoadReservations(ArrayList<Reservations> reservations, Integer maxPage);
    public void onCreateReservation(Reservations reservation);
    public void onResponseDates(ArrayList<DatesReservation> date);
    public void onFinishTerminos(String terminos);
    public void onCancelReservation(Integer code);

}