package com.itosoftware.inderandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.User;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddParticipantsAdapter extends ArrayAdapter {


    private Context context;
    private ArrayList<Participant> datos;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public AddParticipantsAdapter(Context context, ArrayList datos) {
        super(context, R.layout.participant_row, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.datos = datos;
    }


    public void add(Participant participant){
        insert(participant, 0);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.participant_row, null);

        String firsName = (String) this.datos.get(position).getFirstName();
        String lastName = (String) this.datos.get(position).getLastName();

        String labelName = firsName + " - " + lastName;
        TextView name = (TextView) item.findViewById(R.id.participant_row_name);
        name.setText(labelName);



        return item;
    }

}