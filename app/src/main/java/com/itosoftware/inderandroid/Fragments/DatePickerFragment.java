package com.itosoftware.inderandroid.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.RegisterActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public  class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private RegisterActivity registerActivity;


    public void setRegisterActivity(RegisterActivity registerActivity){
        this.registerActivity = registerActivity;
    }

    private MainActivity mainActivity;
    private EditText field;


    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

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

        if((yearSelected == yearCurrent && monthSelected == monthCurrent && daySelected > dayCurrent) || (yearSelected == yearCurrent && monthSelected > monthCurrent) || yearSelected > yearCurrent ){
            show = false;
        }else{
            show = true;
        }

        monthSelected += 1; // Ya que es de 0 a 11

        String dateSelected = yearSelected + "-" + monthSelected + "-" + daySelected;
        if (getRegisterActivity() != null)
            getRegisterActivity().setDateSelected(dateSelected, show);

        if (getMainActivity() != null)
            getMainActivity().setDateSelected(dateSelected,getFieldId());

    }

    public RegisterActivity getRegisterActivity(){
        return registerActivity;
    }
    public MainActivity getMainActivity(){
        return mainActivity;
    }


    public void setFieldId(EditText field) {
        this.field = field;
    }
    public EditText getFieldId() {
        return this.field;
    }
}