package com.itosoftware.inderandroid.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.NewsDetailActivity;
import com.itosoftware.inderandroid.Activities.OfferDetailActivity;
import com.itosoftware.inderandroid.Adapters.OffersAdapter;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.SportActivities.ApiActivitiesScenarios;
import com.itosoftware.inderandroid.Api.SportActivities.Offers;
import com.itosoftware.inderandroid.Api.SportActivities.SportActivities;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.SportActivitiesListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import java.util.ArrayList;
import java.util.HashMap;


public class SportActivityDetailFragment extends DialogFragment implements AdapterView.OnItemClickListener  {

    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    MainActivity mainActivity;

    private String image_url = "http://hotelbolivarianaplaza.com/v2/media/k2/items/cache/7f2cd38b7681e6e2ef83b5a7a5385264_XL.jpg";
    private String titulo = "";
    public String address = "";

    private String horario = "";
    private ArrayList<Offers> offers;
    OffersAdapter adapter;
    Integer id_sport_activity;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sport_activity_detail, container, false);

        mainActivity = (MainActivity) getActivity();

        String json = null;
        Bundle bundle = getArguments();
        if(bundle != null){
            json = bundle.getString("sportActivities");
        }
        Gson gson = new Gson();
        final SportActivities sportActivities = gson.fromJson(json, SportActivities.class);
        id_sport_activity = sportActivities.getId();
        image_url = sportActivities.getImagen_url();

        if(sportActivities.getInfo() != null){
            HashMap info = sportActivities.getInfo();
            if((String) info.get("nombre").toString() != "" && (String) info.get("nombre") != null){
                this.titulo = (String) info.get("nombre").toString();
            }
            if((String) info.get("direccion").toString() != "" ){
                this.address = (String) info.get("direccion");
            }
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageLoader mImageLoader;
        final NetworkImageView mNetworkImageView;
        mNetworkImageView = (NetworkImageView) rootView.findViewById(R.id.fragment_sports_activity_detail_image);
        mImageLoader = ApiConnection.getInstance(this.getContext()).getImageLoader();
        mNetworkImageView.setImageUrl(image_url, mImageLoader);
        final ImageView progress = (ImageView) rootView.findViewById(R.id.fragment_sports_activity_detail_progressbar);
        if (progress.getBackground() != null) {
            AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
            frameAnimation.start();
        }
        mNetworkImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (mNetworkImageView.getDrawable() != null) {
                    progress.setVisibility(View.GONE);
                }
                else{
                    mNetworkImageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.detalle_oferta_broken) );
                    Log.i("Error", "Cargando imagen url.");
                }
            }
        });

        TextView title = (TextView) rootView.findViewById(R.id.fragment_sports_activity_detail_title);
        title.setText(this.titulo);

        TextView address = (TextView) rootView.findViewById(R.id.fragment_sports_activity_detail_address);
        address.setText("Dirección : " + this.address);

        listView  = (ListView) rootView.findViewById(R.id.list_offers);


        this.offers = sportActivities.getOfertas();
        Integer size = this.offers.size();

        adapter = new OffersAdapter(getContext().getApplicationContext(), this.offers);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        Button close = (Button) rootView.findViewById(R.id.fragment_sports_activity_detail_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        // Do something else
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        try{
            Intent offerDetailIntent = new Intent(getActivity(), OfferDetailActivity.class );
            offerDetailIntent.putExtra("offer", offers.get(position).toString());
            offerDetailIntent.putExtra("id_sport_activity", id_sport_activity);
            Log.d("offer_id",offers.get(position).getId().toString());
            Log.d("id_sport_activity",id_sport_activity.toString());
            startActivity(offerDetailIntent);
        }catch (Exception e){
            Log.e("Error =>", e.toString());
        }
    }
}