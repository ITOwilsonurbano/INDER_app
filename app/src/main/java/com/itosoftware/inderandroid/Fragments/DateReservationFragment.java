package com.itosoftware.inderandroid.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;


import com.itosoftware.inderandroid.Activities.NewReservationActivity;
import com.itosoftware.inderandroid.Adapters.DateReservationAdapter;
import com.itosoftware.inderandroid.Adapters.SectionListAdapter;
import com.itosoftware.inderandroid.ListView.SectionListView;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.SectionListItem;


public class DateReservationFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    View rootView;
    NewReservationActivity newReservationActivity;

    SectionListItem[] datesArray = {
            new SectionListItem(1,"10:00 AM - 12:00 M", "Nov-05-2015"), //
            new SectionListItem(2,"2:00 PM - 4:00 PM", "Nov-05-2015"), //
            new SectionListItem(3,"4:00 PM - 6:00 PM", "Nov-05-2015"), //
            new SectionListItem(4,"6:00 PM - 8:00 PM", "Nov-05-2015"), //
            new SectionListItem(5,"8:00 PM - 10:00 PM", "Nov-05-2015"), //
            new SectionListItem(6,"08:00 AM - 10:00 AM", "Nov-06-2015"), //
            new SectionListItem(7,"10:00 AM - 12:00 M", "Nov-06-2015"), //
            new SectionListItem(8,"2:00 PM - 4:00 PM", "Nov-06-2015"), //
            new SectionListItem(9,"6:00 PM - 8:00 PM", "Nov-06-2015"), //
            new SectionListItem(10,"08:00 AM - 10:00 AM", "Nov-09-2015"), //
            new SectionListItem(11,"10:00 AM - 12:00 M", "Nov-09-2015"), //
            new SectionListItem(12,"2:00 PM - 4:00 PM", "Nov-09-2015"), //
            new SectionListItem(13,"4:00 PM - 6:00 PM", "Nov-09-2015"), //
            new SectionListItem(14,"6:00 PM - 8:00 PM", "Nov-09-2015"), //
            new SectionListItem(15,"8:00 PM - 10:00 PM", "Nov-09-2015"), //
            new SectionListItem(16,"08:00 AM - 10:00 AM", "Nov-10-2015"), //
            new SectionListItem(17,"10:00 AM - 12:00 M", "Nov-10-2015"), //
            new SectionListItem(18,"2:00 PM - 4:00 PM", "Nov-10-2015"), //
            new SectionListItem(19,"4:00 PM - 6:00 PM", "Nov-10-2015"), //
            new SectionListItem(20,"6:00 PM - 8:00 PM", "Nov-10-2015"), //
            new SectionListItem(21,"8:00 PM - 10:00 PM", "Nov-10-2015"), //
            new SectionListItem(22,"08:00 AM - 10:00 AM", "Nov-12-2015"), //
            new SectionListItem(23,"2:00 PM - 4:00 PM", "Nov-12-2015"), //
            new SectionListItem(24,"08:00 AM - 10:00 AM", "Nov-13-2015"), //
            new SectionListItem(25,"10:00 AM - 12:00 M", "Nov-13-2015"), //
            new SectionListItem(26,"2:00 PM - 4:00 PM", "Nov-13-2015"), //
            new SectionListItem(27,"4:00 PM - 6:00 PM", "Nov-13-2015"), //
            new SectionListItem(28,"6:00 PM - 8:00 PM", "Nov-13-2015"), //
            new SectionListItem(29,"10:00 AM - 12:00 M", "Nov-14-2015"), //
            new SectionListItem(30,"4:00 PM - 6:00 PM", "Nov-14-2015"), //
            new SectionListItem(31,"8:00 PM - 10:00 PM", "Nov-14-2015"), //
            new SectionListItem(32,"08:00 AM - 10:00 AM", "Nov-15-2015"), //
            new SectionListItem(33,"10:00 AM - 12:00 M", "Nov-15-2015"), //
            new SectionListItem(34,"2:00 PM - 4:00 PM", "Nov-15-2015"), //
            new SectionListItem(35,"4:00 PM - 6:00 PM", "Nov-15-2015"), //
            new SectionListItem(36,"6:00 PM - 8:00 PM", "Nov-15-2015"), //
            new SectionListItem(37,"8:00 PM - 10:00 PM", "Nov-15-2015"), //
            new SectionListItem(0,"08:00 AM - 10:00 AM", "Nov-05-2015"), //
    };

    private DateReservationAdapter arrayAdapter;
    private SectionListAdapter sectionAdapter;
    private SectionListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_date_reservation, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        newReservationActivity = (NewReservationActivity) getActivity();

        arrayAdapter = new DateReservationAdapter(getContext(), R.id.date_reservation_row_date_reservation,  datesArray);
        sectionAdapter = new SectionListAdapter(inflater,arrayAdapter);

        listView = (SectionListView) rootView.findViewById(R.id.section_list_view);
        listView.setAdapter(sectionAdapter);
        listView.setOnItemClickListener(this);

        Button close = (Button) rootView.findViewById(R.id.fragment_date_reservation_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Integer pos = i;
        sectionAdapter.getItemPositions().get(i).toString();
        SectionListItem item = datesArray[sectionAdapter.getItemPositions().get(i)];

        String date = (String)item.getSection();
        String hour = (String)item.getItem();
        String label = "Fecha : "+date+" - Hora : "+ hour;

        newReservationActivity.reservationDateId = item.getId();
        newReservationActivity.updateTextDate(label);
        dismiss();
    }
}