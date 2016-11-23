package com.itosoftware.inderandroid.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Activities.ForgetPasswordActivity;
import com.itosoftware.inderandroid.Activities.LoginActivity;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.NewReservationActivity;
import com.itosoftware.inderandroid.Api.Communes.Communes;
import com.itosoftware.inderandroid.Api.Neighborhood.Neighborhood;
import com.itosoftware.inderandroid.Api.News.New;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenariosContainer;
import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.UserContainer;
import com.itosoftware.inderandroid.Api.Zones.Zones;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.ApiConnection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;


public class SportDetailFragment extends DialogFragment implements ApiUserListener {

    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    MainActivity mainActivity;

    private String image_url = "http://hotelbolivarianaplaza.com/v2/media/k2/items/cache/7f2cd38b7681e6e2ef83b5a7a5385264_XL.jpg";
    private String address = "";
    private String name = "";
    private String telphone = "";
    private String content = "";
    private String zona = "";
    private String comuna = "";
    private String barrio = "";
    private String capacidad = "";
    SportDetailFragment sportDetailFragment;
    SportsScenarios sportScenario;
    ImageView progress;
    Globals globalApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("SportsDetailFragment","SportsDetailFragment - onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_sport_detail, container, false);

//        mainActivity = (MainActivity) getActivity();
//        mainActivity.sportDetailFragment = this;

        sportDetailFragment = this;

        globalApplication = (Globals) getActivity().getApplication();

        String json = null;
        Bundle bundle = getArguments();
        if(bundle != null){
            json = bundle.getString("sportsScenario");
        }
        Gson gson = new Gson();
        sportScenario = gson.fromJson(json, SportsScenarios.class);

        image_url = sportScenario.getImagen_url();

        if(sportScenario.getInfo() != null && sportScenario.getInfo().size() != 0){
            if((String) sportScenario.getInfo().get("nombre") != "" && (String) sportScenario.getInfo().get("nombre") != null){
                this.name = ((String) sportScenario.getInfo().get("nombre")).toUpperCase();
            }
            if((String) sportScenario.getInfo().get("direccion") != "" && (String) sportScenario.getInfo().get("direccion") != null){
                this.address = "Dirección: " + (String) sportScenario.getInfo().get("direccion");
            }
//           if((String) sportScenario.getInfo().get("telefono") != "" && (String) sportScenario.getInfo().get("telefono") != null){
//                this.telphone = "Teléfono: " + sportScenario.getInfo().get("telefono").toString();
//            }
            if(sportScenario.getDescripcion() != "" && sportScenario.getDescripcion() != null){
                this.content = "Descripción: " +sportScenario.getDescripcion();
            }

            try{
                if(sportScenario.getInfo().get("maximo_personas").toString() != "" && sportScenario.getInfo().get("maximo_personas") != null){
                    this.capacidad = "Máximo de Personas: "+sportScenario.getInfo().get("maximo_personas").toString();
                }
            }
            catch (java.lang.NullPointerException e){

            }

            Zones zone;
            Type listType = new TypeToken<Zones>() {
            }.getType();

            try {
                if(sportScenario.getInfo().get("zona") != null){
                    String z = sportScenario.getInfo().get("zona").toString();
                    zone = gson.fromJson(z, listType);
                    if(zone.getNombre() != "" && zone.getNombre() != null){
                        this.zona = "Ubicación: "+zone.getNombre();
                    }
                }
            }
            catch (com.google.gson.JsonSyntaxException e){

            }

            Communes commune;
            Type listType2 = new TypeToken<Communes>() {
            }.getType();

            try {
                if (sportScenario.getInfo().get("comuna") != null){
                    String c = sportScenario.getInfo().get("comuna").toString();
                    commune = gson.fromJson(c, listType2);
                    if(commune.getNombre() != "" && commune.getNombre() != null){
                        this.comuna = commune.getNombre();
                    }
                }
            }
            catch (com.google.gson.JsonSyntaxException e){

            }

            Neighborhood neighborhood;
            Type listType3 = new TypeToken<Neighborhood>() {
            }.getType();

            try {
                if(sportScenario.getInfo().get("barrio") != null){
                    String n = sportScenario.getInfo().get("barrio").toString();
                    neighborhood = gson.fromJson(n, listType3);
                    if(neighborhood.getNombre() != "" && neighborhood.getNombre() != null){
                        this.barrio = neighborhood.getNombre();
                    }
                }
            }
            catch (com.google.gson.JsonSyntaxException e){

            }
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageLoader mImageLoader;
        final NetworkImageView mNetworkImageView;
        mNetworkImageView = (NetworkImageView) rootView.findViewById(R.id.fragment_sports_detail_image);
        mImageLoader = ApiConnection.getInstance(this.getContext()).getImageLoader();
        mNetworkImageView.setImageUrl(image_url, mImageLoader);
        progress = (ImageView) rootView.findViewById(R.id.fragment_sports_detail_progressbar);
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
                    mNetworkImageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.detalle_escenario_broken) );
                    Log.i("Error", "Cargando imagen url.");
                }
            }
        });

        TextView title = (TextView) rootView.findViewById(R.id.fragment_sports_detail_title);
        title.setText(this.name);

        TextView address = (TextView) rootView.findViewById(R.id.fragment_sports_detail_address);
        address.setText(this.address);

        TextView telphone = (TextView) rootView.findViewById(R.id.fragment_sports_detail_telphone);
        telphone.setText(this.telphone.toString());

        TextView capacidad = (TextView) rootView.findViewById(R.id.fragment_sports_detail_capacidad);
        capacidad.setText(this.capacidad.toString());

        TextView ubicacion = (TextView) rootView.findViewById(R.id.fragment_sports_detail_ubicacion);
        ubicacion.setText(this.zona + " - " + this.comuna + " - " + this.barrio);

        TextView content = (TextView) rootView.findViewById(R.id.fragment_sports_detail_content);
        content.setText(this.content);


        RelativeLayout doReservation = (RelativeLayout) rootView.findViewById(R.id.fragment_sports_detail_reservation_button);
        doReservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                progress.setVisibility(View.VISIBLE);
                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimationZone = (AnimationDrawable) progress.getBackground();
                frameAnimationZone.start();

                if(globalApplication.getUserIsAuthenticated().equals(true)){
                    HashMap params = new HashMap();
                    params.put("token",globalApplication.getToken());

                    ApiUser apiUser = new ApiUser(sportDetailFragment);
                    apiUser.getProfile(params);

                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("sportScenario", sportScenario.toString());
                    getActivity().startActivityForResult(intent, 1);
                    progress.setBackgroundDrawable(null);
                    progress.setVisibility(View.GONE);
                }
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.content_reservation_button);
        if(sportScenario.getIs_active() == true){
            relativeLayout.setVisibility(View.VISIBLE);
        }else{
            relativeLayout.setVisibility(View.GONE);
        }

        Button close = (Button) rootView.findViewById(R.id.fragment_sports_detail_close_button);
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
    public void onFinishedAuthentication(UserContainer userContainer, Boolean authenticated) {

    }

    @Override
    public void onFinishedRegister(Boolean success, HashMap data) {

    }

    @Override
    public void onFinishedForgetPassword() {

    }

    @Override
    public void onFinishedForgetUser() {

    }

    @Override
    public void onFinishedAddParticipant(Participant participant, Integer status) {

    }

    @Override
    public void onFinishedProfile(ApiUser.Info dataUser) {

        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

        if(globalApplication.getUserIsAuthenticated().equals(true)){
            Intent intent = new Intent(getActivity(),  NewReservationActivity.class);
            intent.putExtra("sportScenario", sportScenario.toString());
            getActivity().startActivityForResult(intent, 2);
        }else{
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("sportScenario", sportScenario.toString());
            getActivity().startActivityForResult(intent, 1);
        }


    }

    @Override
    public void onFinishedProfilePqr(JSONObject response) {

    }
}