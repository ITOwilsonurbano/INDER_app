package com.itosoftware.inderandroid.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Activities.NewReservationActivity;
import com.itosoftware.inderandroid.Adapters.DateReservationAdapter;
import com.itosoftware.inderandroid.Adapters.HoursAdpater;
import com.itosoftware.inderandroid.Adapters.ShowParticipantsAdapter;
import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.Reservations.ApiReservation;
import com.itosoftware.inderandroid.Api.Reservations.Reservations;
import com.itosoftware.inderandroid.Interface.ReservationsListener;
import com.itosoftware.inderandroid.R;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HoursFragment extends DialogFragment implements ReservationsListener, AdapterView.OnItemClickListener {
    View rootView;

    ListView hoursList;
    ListView startHoursList;
    ArrayList<DatesReservation> list = new ArrayList<DatesReservation>();

    HashMap params = new HashMap();

    Integer itemSelected = 0;
    Integer itemsSelected = 0;
    Integer maxItemsSelected = 4;
    ArrayList<Integer> itemsSelectedArray;
    HoursAdpater adapter;
    ImageView progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hours, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        hoursList = (ListView) rootView.findViewById(R.id.fragment_hours_list);

        progress = (ImageView) rootView.findViewById(R.id.fragment_hours_progressbar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        itemsSelectedArray = new ArrayList();

        final NewReservationActivity newReservationActivity = (NewReservationActivity) getActivity();

        Button close = (Button) rootView.findViewById(R.id.fragment_button_cancel);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        Button accept = (Button) rootView.findViewById(R.id.fragment_button_done);
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                String date = newReservationActivity.getDate();
                ArrayList hours = new ArrayList();
                int  i = 0;
                int total = itemsSelectedArray.size();
                String dateString = "";
                String start = "";
                String end = "";
                for (Integer in : itemsSelectedArray){
                    DatesReservation dates = (DatesReservation) list.get(in);
                    HashMap params = new HashMap();
                    params.put("inicio", dates.getInicio());
                    params.put("fin", dates.getFin());
                    hours.add(params);

                    if(i == 0){
                        start = dates.getInicio();
                    }
                    end = dates.getFin();
                    i++;
                }
                Gson gson = new Gson();
                String hoursString = gson.toJson(hours);
                String datesString = (start+" "+end);
                Log.d("marches log",datesString);
                if(datesString.matches(" ")){
                    datesString = "Elija una Fecha";
                }
                newReservationActivity.setHours(hoursString, datesString);
                dismiss();
            }
        });

        DialogFragment hoursFragamet = this;

        ApiReservation apiReservation = new ApiReservation(hoursFragamet);
        apiReservation.getDates(params);

        // Do something else
        return rootView;
    }

    public void setData(String date, Integer idSport){
        params.put("date",date);
        params.put("sport_escenario", idSport);
    }

    //    Start Methods Implements ReservationsListener
    @Override
    public void onfinishedLoadReservations(ArrayList<Reservations> reservations, Integer maxPage) {

    }
    @Override
    public void onCreateReservation(Reservations reservation) {
    }
    @Override
    public void onResponseDates(ArrayList<DatesReservation> dates ) {
        list = dates;

        adapter = new HoursAdpater(getContext(), dates, itemsSelectedArray);
        hoursList.setAdapter(adapter);

        startHoursList = hoursList;

        hoursList.setOnItemClickListener(this);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

    }
    @Override
    public void onFinishTerminos(String terminos) {

    }
    @Override
    public void onCancelReservation(Integer code) {

    }

    Boolean isSelected = false;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("Almenos", itemsSelectedArray.toString());
        if(itemsSelectedArray.size() <= maxItemsSelected) {
            itemSelected = position;
            ArrayList<DatesReservation> newList = new ArrayList<DatesReservation>();
            Integer pos = 0;

            Integer itemSelectedAfter = 0;
            Integer itemSelectedMedio = null;
            Integer itemSelectedMedioOther = null;
            Integer itemSelectedBefore = 0;

            if(itemsSelectedArray.size() == 0){
                itemSelectedAfter = itemSelected + 1;
                if(itemSelected == 0){
                    itemSelectedBefore = 0;
                }else{
                    itemSelectedBefore  = itemSelected - 1;
                }
            }
            if(itemsSelectedArray.size() == 1){
                Integer lastItem = (Integer) itemsSelectedArray.get(0);
                Integer currentItem = itemSelected;
                if(lastItem < currentItem){
                    itemSelectedAfter = itemSelected + 1;
                    itemSelectedMedio = itemSelected - 1;
                    itemSelectedBefore = itemSelected - 2;
                }else{
                    itemSelectedBefore = itemSelected - 1;
                    itemSelectedMedio = itemSelected + 1;
                    itemSelectedAfter = itemSelected + 2;
                }
            }
            if(itemsSelectedArray.contains(itemSelected) && itemsSelectedArray.size() == 2 ){
                Integer firtsItem = (Integer) itemsSelectedArray.get(0);
                Integer lastItem = (Integer) itemsSelectedArray.get(1);
                Integer item = 0;
                if(firtsItem == itemSelected){
                    item = lastItem;
                }else{
                    item = firtsItem;
                }
                itemSelectedMedio = item;
                itemSelectedAfter = item + 1;
                if(item == 0){
                    itemSelectedBefore = 0;
                }else{
                    itemSelectedBefore  = item - 1;
                }
            }else if(itemsSelectedArray.size() == 2){
                Collections.sort(itemsSelectedArray);
                Integer firstItem = (Integer) itemsSelectedArray.get(0);
                Integer secondItem = (Integer) itemsSelectedArray.get(1);
                if(firstItem - 1 == itemSelected){
                    itemSelectedBefore = itemSelected -1;
                    itemSelectedMedio = firstItem;
                    itemSelectedMedioOther = secondItem;
                    itemSelectedAfter = secondItem + 1;
                }else{
                    itemSelectedBefore = firstItem -1;
                    itemSelectedMedio = firstItem;
                    itemSelectedMedioOther = secondItem;
                    itemSelectedAfter = itemSelected + 1;
                }
            }
            if(itemsSelectedArray.contains(itemSelected) && itemsSelectedArray.size() == 3 ){
                Collections.sort(itemsSelectedArray);
                Integer firstItem = (Integer) itemsSelectedArray.get(0);
                Integer secondItem = (Integer) itemsSelectedArray.get(1);
                Integer thirdItem = (Integer) itemsSelectedArray.get(2);
                if(secondItem == itemSelected){
                    Toast.makeText(getContext(), "Puede deseleccionar la primera o la tercera fecha que ya ha seleccionado", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    if(firstItem == itemSelected){
                        itemSelectedBefore = firstItem;
                        itemSelectedMedio = firstItem +1;
                        itemSelectedMedioOther = thirdItem +1;
                        itemSelectedAfter = thirdItem;
                    }else{
                        itemSelectedBefore = firstItem -1;
                        itemSelectedMedio = firstItem;
                        itemSelectedMedioOther = secondItem;
                        itemSelectedAfter = thirdItem;
                    }
                }
            }else if(itemsSelectedArray.size() == 3){
                Collections.sort(itemsSelectedArray);
                Integer firstItem = (Integer) itemsSelectedArray.get(0);
                Integer secondItem = (Integer) itemsSelectedArray.get(1);
                Integer thirdItem = (Integer) itemsSelectedArray.get(2);
                if(itemSelected < firstItem){
                    itemSelectedBefore = itemSelected;
                    itemSelectedAfter = thirdItem;
                    itemSelectedMedio = firstItem;
                    itemSelectedMedioOther = secondItem;
                }else{
                    itemSelectedBefore = firstItem;
                    itemSelectedAfter = itemSelected;
                    itemSelectedMedio = secondItem;
                    itemSelectedMedioOther = thirdItem;
                }
            }
            if(itemsSelectedArray.contains(itemSelected) && itemsSelectedArray.size() == 4){
                Collections.sort(itemsSelectedArray);
                Integer firstItem = (Integer) itemsSelectedArray.get(0);
                Integer secondItem = (Integer) itemsSelectedArray.get(1);
                Integer thirdItem = (Integer) itemsSelectedArray.get(2);
                Integer fourItem = (Integer) itemsSelectedArray.get(3);
                if(secondItem == itemSelected || thirdItem == itemSelected){
                    Toast.makeText(getContext(), "Puede deseleccionar la primera o la cuarta fecha que ya ha seleccionado", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    if(firstItem == itemSelected){
                        itemSelectedBefore = firstItem;
                        itemSelectedMedio = secondItem;
                        itemSelectedMedioOther = thirdItem;
                        itemSelectedAfter = fourItem + 1;
                    }else{
                        itemSelectedBefore = firstItem -1;
                        itemSelectedMedio = secondItem;
                        itemSelectedMedioOther = thirdItem;
                        itemSelectedAfter = fourItem;
                    }
                }
            }

            for (DatesReservation date : list) {
                DatesReservation newDate = new DatesReservation();
                isSelected = itemsSelectedArray.contains(pos);
                if(isSelected && itemSelected == pos){
                    itemsSelectedArray.remove(pos);
                    newDate.setDisponible(date.getDisponible());
                }else{
                    if ((itemSelected == pos && date.getDisponible() == true) ||
                            (itemSelectedBefore == pos && date.getDisponible() == true) ||
                            (itemSelectedAfter  == pos && date.getDisponible() == true) ||
                            (itemSelectedMedio != null && itemSelectedMedio == pos && date.getDisponible() == true) ||
                            (itemSelectedMedioOther != null && itemSelectedMedioOther == pos && date.getDisponible() == true) ||
                            (itemsSelectedArray.contains(pos) && date.getDisponible() == true)
                            ) {
                        newDate.setDisponible(true);
                    } else {
                        newDate.setDisponible(false);
                    }
                    if (itemSelected == pos ) {
                        itemsSelectedArray.add(pos);
                    }
                }
                newDate.setInicio(date.getInicio());
                newDate.setFin(date.getFin());
                newList.add(newDate);

                pos++;
            }
            if(itemsSelectedArray.size() == 0){
                adapter = new HoursAdpater(getContext(), list, itemsSelectedArray);
            }else{
                adapter = new HoursAdpater(getContext(), newList, itemsSelectedArray);
            }
            hoursList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
