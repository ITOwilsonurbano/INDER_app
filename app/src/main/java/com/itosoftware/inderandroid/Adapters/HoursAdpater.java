package com.itosoftware.inderandroid.Adapters;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.itosoftware.inderandroid.Api.DatesReservation.DatesReservation;
import com.itosoftware.inderandroid.Api.Recommended.Recommended;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import java.util.ArrayList;

/**
 * Created by administrador on 23/10/15.
 */

public class HoursAdpater extends ArrayAdapter{

        private Context context;
        private ArrayList<DatesReservation> datos;
        private RequestQueue mRequestQueue;
        ImageLoader mImageLoader;
        ArrayList itemsSelectedArrayGreen;

    //Constructor to initialize values
        public HoursAdpater(Context context, ArrayList dates,  ArrayList itemsSelectedArray) {
            super(context, R.layout.hour_row, dates);
            this.context   = context;
            this.datos     = dates;
            this.itemsSelectedArrayGreen = itemsSelectedArray;

            mImageLoader = ApiConnection.getInstance(this.getContext()).getImageLoader();
        }

        @Override
        public int getCount() {
            // Number of times getView method call depends upon gridValues.length
            return datos.size();
        }
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.hour_row, null);

                holder = new ViewHolder();
                holder.date = (TextView) convertView.findViewById(R.id.hour_row_text);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            String start =  (String) this.datos.get(position).getInicio();
            String end =  (String) this.datos.get(position).getFin();
            String[] starts = start.split(" ");
            String[] ends = end.split(" ");
            String date =starts[1] +" - "+ ends[1];

            holder.date.setText(date);

            Boolean status = this.datos.get(position).getDisponible();

            if(status == false){
                convertView.setBackgroundResource(R.color.NegroTransparente);
            }else{
                if(itemsSelectedArrayGreen.contains(position)){
                    convertView.setBackgroundResource(R.color.Verde);
                }else{
                    convertView.setBackgroundResource(R.color.Blanco);
                }
            }



            return convertView;
        }
    @Override
    public boolean isEnabled(int position) {
        Boolean status = this.datos.get(position).getDisponible();
        if (status == true) {
            return true;
        }else{
            return false;
        }
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView date;
    }




}