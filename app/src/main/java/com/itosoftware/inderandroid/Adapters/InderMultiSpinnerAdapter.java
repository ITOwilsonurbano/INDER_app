package com.itosoftware.inderandroid.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rasuncion on 5/11/15.
 */
public class InderMultiSpinnerAdapter extends ArrayAdapter<String> {

    // Initialise custom font, for example:
    Typeface font = Typeface.createFromAsset(getContext().getAssets(),"HelveticaNeueLTStd-LtCn.otf");
    private Context context;
    private List<String> datos;
    private int resource;

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), HelveticaNeueLTStd-LtCn);

    public InderMultiSpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.context = context;
        this.datos = items;
        this.resource = resource;
    }

    public InderMultiSpinnerAdapter(Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
    }

    public InderMultiSpinnerAdapter(Activity activity, int spinner_item) {
        super(activity, spinner_item);
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.multispinner_row, parent, false);
        Log.i("getDropDownView","multispinnerAdapter");
        TextView label=(TextView)row.findViewById(R.id.textView1);
        label.setText(getItem(position));

        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(font);

        return view;
    }

}
