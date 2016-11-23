package com.itosoftware.inderandroid.Adapters;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.itosoftware.inderandroid.Utils.ApiConnection;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.R;


public class NewsAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<New> datos;

    private RequestQueue mRequestQueue;
    ImageLoader mImageLoader;

    public NewsAdapter(Context context, ArrayList datos) {
        super(context, R.layout.new_row, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.datos = datos;
        mImageLoader = ApiConnection.getInstance(this.getContext()).getImageLoader();


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.new_row, null);

           // Locate the Views in new_row.xml
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.networkImageView);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.progress = (ImageView) convertView.findViewById(R.id.progressbar);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mNetworkImageView.setImageUrl(this.datos.get(position).getImageUrl(), mImageLoader);
        if (holder.progress.getBackground() != null) {
            AnimationDrawable frameAnimation = (AnimationDrawable) holder.progress.getBackground();
            frameAnimation.start();
        }
        holder.mNetworkImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (holder.mNetworkImageView.getDrawable() != null) {
                    holder.progress.setVisibility(View.GONE);
                }
                else{
                    holder.mNetworkImageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.lista_noticias_broken) );
                    Log.i("Error", "Cargando imagen url.");
                }
            }
        });

        holder.title.setText(this.datos.get(position).getTitle());
        holder.date.setText(this.datos.get(position).getDate());
        holder.content.setText(this.datos.get(position).getSubtitle());

        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        NetworkImageView mNetworkImageView;
        ImageView progress;
        TextView title;
        TextView date;
        TextView content;
    }

}
