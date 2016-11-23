package com.itosoftware.inderandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.SectionListItem;

public class DateReservationAdapter extends ArrayAdapter<SectionListItem> {

    private final SectionListItem[] items;
    Context context;

    public DateReservationAdapter(final Context context, int textViewResourceId, final SectionListItem[] items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView,
                        final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.date_reservation_row, null);
        }
        final SectionListItem currentItem = items[position];
        if (currentItem != null) {
            final TextView textView = (TextView) view
                    .findViewById(R.id.date_reservation_row_date_reservation);
            if (textView != null) {
                textView.setText(currentItem.item.toString());
            }
        }
        return view;
    }

}