package com.itosoftware.inderandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowParticipantsAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
    private Context context;
    private ArrayList<String> datos;
    private int textviewResource;

    public ShowParticipantsAdapter(Context context, int textViewResourceId,  ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.datos = objects;
        this.textviewResource = textViewResourceId;

        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(textviewResource, null);

        String labelName = this.datos.get(position);
        TextView name = (TextView) item.findViewById(R.id.participant_row_name);
        name.setText(labelName);



        return item;
    }

}