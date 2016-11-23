package com.itosoftware.inderandroid.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.SportActivities.Offers;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import java.util.ArrayList;
import java.util.HashMap;

public class OffersAdapter extends ArrayAdapter<Offers> {

    private Context context;
    private ArrayList<Offers> datos;
    private int textviewResource;

    public OffersAdapter(Context context,  ArrayList<Offers> objects) {
        super(context, R.layout.offers_row, objects);
        this.context = context;
        this.datos = objects;
        this.textviewResource = R.layout.offers_row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(textviewResource, null);

        String labelName = this.datos.get(position).getNombre();
        TextView name = (TextView) item.findViewById(R.id.offers_row_name);
        name.setText(labelName);



        return item;
    }

}