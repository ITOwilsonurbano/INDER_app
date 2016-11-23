package com.itosoftware.inderandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itosoftware.inderandroid.R;

import java.util.ArrayList;


public class AutoCompleteAdapter extends ArrayAdapter<String> {

    private ArrayList<String> items;
    private int viewResourceId;

    public AutoCompleteAdapter(Context context, int viewResourceId, ArrayList<String> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.viewResourceId = viewResourceId;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }

        TextView customerNameLabel = (TextView) v.findViewById(R.id.customerNameLabel);
        if (customerNameLabel != null) {
            customerNameLabel.setText(items.get(position));
        }

        /*if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.customerNameLabel);
            if (customerNameLabel != null) {
                customerNameLabel.setText(String.valueOf(customer.getName()));
            }
        } */
        return v;
    }
}
