package com.itosoftware.inderandroid.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.NewReservationActivity;
import com.itosoftware.inderandroid.Activities.RegisterActivity;

import org.json.JSONArray;

import java.util.Calendar;

public  class DatePickerReservationsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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

    public void onDateSet(DatePicker view, int yearSelected, int monthSelected, int daySelected) {
        // Do something with the date chosen by the user

        final Calendar c = Calendar.getInstance();
        int yearCurrent = c.get(Calendar.YEAR);
        int monthCurrent = c.get(Calendar.MONTH);
        int dayCurrent = c.get(Calendar.DAY_OF_MONTH);

        Boolean show;

        if((yearSelected == yearCurrent && monthSelected == monthCurrent && daySelected >= dayCurrent) || (yearSelected == yearCurrent && monthSelected > monthCurrent) || yearSelected > yearCurrent ){
            show = true;
        }else{
            show = false;
        }

        monthSelected += 1; // Ya que es de 0 a 11
        String dateSelected = yearSelected + "-" + monthSelected + "-" + daySelected;

        NewReservationActivity newReservationActivity = (NewReservationActivity) getActivity();

        if(show){
            getDateButton().setText(dateSelected);
            newReservationActivity.reloadHours(dateSelected);
            newReservationActivity.setHours("","Elija una hora");
        }else{
            newReservationActivity.showMessage();
        }


    }

    public void setButton(Button button){
        dateButton = button;
    }

    public Button getDateButton(){
        return dateButton;
    }




}