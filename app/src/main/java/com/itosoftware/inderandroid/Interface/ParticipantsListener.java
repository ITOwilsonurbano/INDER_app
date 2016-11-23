package com.itosoftware.inderandroid.Interface;

import com.itosoftware.inderandroid.Api.Eps.Eps;
import com.itosoftware.inderandroid.Api.Users.Participant;

import java.util.ArrayList;

public interface ParticipantsListener {
    public void onCloseFragment(ArrayList<Participant> participants);
}