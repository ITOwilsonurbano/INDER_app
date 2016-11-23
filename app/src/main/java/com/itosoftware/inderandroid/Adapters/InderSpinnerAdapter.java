package com.itosoftware.inderandroid.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itosoftware.inderandroid.Activities.RegisterActivity;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rasuncion on 5/11/15.
 */
public class InderSpinnerAdapter extends ArrayAdapter<String> {

    // Initialise custom font, for example:
    Typeface font = Typeface.createFromAsset(getContext().getAssets(),"HelveticaNeueLTStd-LtCn.otf");

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), HelveticaNeueLTStd-LtCn);

    public InderSpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    public InderSpinnerAdapter(Activity activity, int spinner_item) {
        super(activity, spinner_item);
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.row_spinner, parent, false);
        TextView label=(TextView)row.findViewById(R.id.textview_row_spinner);
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
