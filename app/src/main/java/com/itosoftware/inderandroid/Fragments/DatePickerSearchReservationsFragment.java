package com.itosoftware.inderandroid.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.itosoftware.inderandroid.Activities.NewReservationActivity;

import java.util.Calendar;

public  class DatePickerSearchReservationsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Button dateButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        month += 1; // Ya que es de 0 a 11
        String dateSelected = year + "-" + month + "-" + day;

        getDateButton().setText(dateSelected);

    }

    public void setButton(Button button){
        dateButton = button;
    }

    public Button getDateButton(){
        return dateButton;
    }




}