package com.itosoftware.inderandroid.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.Recommended.Recommended;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.Utils.CircularNetworkImageView;

import java.util.ArrayList;

/**
 * Created by administrador on 23/10/15.
 */

public class RecommendedAdpater extends ArrayAdapter{

        private Context context;
        private ArrayList<Recommended> datos;
        private RequestQueue mRequestQueue;
        ImageLoader mImageLoader;

    //Constructor to initialize values
        public RecommendedAdpater(Context context, ArrayList itemsRecommended) {
            super(context, R.layout.new_row, itemsRecommended);
            this.context   = context;
            this.datos     = itemsRecommended;
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
                convertView = inflater.inflate(R.layout.item_recommeded, null);

                holder = new ViewHolder();

                holder.mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.item_recomended_image);
                holder.titulo = (TextView) convertView.findViewById(R.id.item_recomended_description);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mNetworkImageView.setImageUrl(this.datos.get(position).getImageUrl(), mImageLoader);
            if (holder.mNetworkImageView.getBackground() != null) {
                AnimationDrawable frameAnimation = (AnimationDrawable) holder.mNetworkImageView.getBackground();
                frameAnimation.start();
            }
            holder.mNetworkImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    if (holder.mNetworkImageView.getDrawable() != null) {
                        holder.mNetworkImageView.setBackgroundDrawable(null);
                    }
                    else{
                        holder.mNetworkImageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.lista_recomendados_broken) );
                        Log.i("Error", "Cargando imagen url.");
                    }
                }
            });

            Integer size = this.datos.get(position).getTitulo().length();
            String labelTitle;
            if(size > 90){
                labelTitle = this.datos.get(position).getTitulo().substring(0, 90) + "...";
            }else{
                labelTitle = this.datos.get(position).getTitulo();
            }

            holder.titulo.setText(labelTitle);

            return convertView;
        }

    /*private view holder class*/
    private class ViewHolder {
        NetworkImageView mNetworkImageView;
        TextView titulo;
    }




}