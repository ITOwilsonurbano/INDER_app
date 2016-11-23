package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.ParticipantsDetailReservation.Participants;

import java.util.ArrayList;

public interface ReservationDetailListener {
    public void onfinishedLoadParticipants(ArrayList<Participants> participants, Integer maxPage);

}