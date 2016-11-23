package com.itosoftware.inderandroid.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Api.SportsScenarios.ApiSportsScenarios;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.SportScenarioListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.MarkerItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rasuncion on 11/12/15.
 */
public class CustomMapFragment extends SupportMapFragment implements SportScenarioListener {

    private static final String LOG_TAG = "CustomMapFragment";
    private GoogleMap mMap;
    private int layoutId;

    private CameraUpdate cu;
    private MarkerItem clickedClusterItem;
    private ClusterManager<MarkerItem> clusterManager;
    ImageView progress;

    //Main View
    public HashMap dataMarkers = new HashMap<>();
    public static final int ID = 2;

    //Search Variables
    RelativeLayout btnSearch;
    TextView btnSearchText;
    ImageView btnSearchAdvance;
    String keyword = "Búsqueda de escenarios";
    String zone = "";
    String commune = "";
    String neighborhood = "";
    String discipline = "";

    public CustomMapFragment() {
        super();

    }


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup arg1,
                             Bundle arg2) {
        return super.onCreateView(mInflater, arg1, arg2);
    }

    @Override
    public void onInflate(Activity arg0, AttributeSet arg1, Bundle arg2) {
        super.onInflate(arg0, arg1, arg2);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMap = getMap();
        start();
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.draggable(true);
//        markerOptions.position(new LatLng(23.231251f, 71.648437f));
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
//        mapView.addMarker(markerOptions);
    }



//    private void setUpMapIfNeeded() {
//        if (mMap != null) {
//            return;
//        }
//        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//        if (mMap != null) {
//            start();
//        }
//    }

    private void start() {

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(6.29139, -75.53611));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(4);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.29139, -75.53611), 4));
        //map.moveCamera(center);
        //map.animateCamera(zoom);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

////        try {
//        View locationButton = ((View) ((MapView) findViewById(R.id.map)).findViewById(1).getParent()).findViewById(2);
//
//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//        //Button goes above card view
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        rlp.setMargins(0, 0, 30, 150);
////        }catch ()


        //Call Api and get Sports Scenarios
        HashMap params = new HashMap<>();
        params.put("page", 0);
        params.put("limit",-1);
        params.put("keyword",keyword);
        params.put("zone",zone);
        params.put("commune",commune);
        params.put("neighborhood", neighborhood);
        params.put("discipline",discipline);

        ApiSportsScenarios apiSportsScenarios = new ApiSportsScenarios(this);
        apiSportsScenarios.getSportsScenarios(params);
    }

    @Override
    public void onFinishedConnection(ArrayList<SportsScenarios> itemsSportsScenarios) {
        mMap.clear();
        //Builder For Zoom on Alls Markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //ClusterManager for Group the markers
        clusterManager = new ClusterManager<MarkerItem>(getActivity(), mMap);

        // Set listener to map
        mMap.setOnCameraChangeListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        mMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(clusterManager);

//        Set listener to ClusterManager
        //clusterManager.setOnClusterItemInfoWindowClickListener((ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem>) this);
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
            @Override
            public boolean onClusterItemClick(MarkerItem item) {
                clickedClusterItem = item;
                return false;
            }
        });
        Integer total = itemsSportsScenarios.size();
        if(itemsSportsScenarios.size() != 0){
            clusterManager.clearItems();

            // Add MarkerItems to cluster
            for( SportsScenarios scenario : itemsSportsScenarios ){
                String name = "Placa Deportiva";
                if(scenario.getInfo() != null && scenario.getInfo().size() != 0){
                    name = (String) scenario.getInfo().get("nombre");
                }

                MarkerItem item = new MarkerItem(scenario.getLatitude().doubleValue(), scenario.getLongitude().doubleValue(), name);
                //Log.i("coordenada", scenario.getLatitude().doubleValue() + " , " + scenario.getLongitude().doubleValue());

                clusterManager.addItem(item);
                dataMarkers.put(item.getPosition(), scenario);
                builder.include(item.getPosition());
            }

            //Set adapter to info window
            //clusterManager.getMarkerCollection().setOnInfoWindowAdapter( new InfoWindowAdapter());
            //Zoom on the markers
            LatLngBounds bounds = builder.build();
            cu = CameraUpdateFactory.newLatLngBounds(bounds,90);
            try{
                mMap.animateCamera(cu);
            }
            catch (IllegalStateException e){

            }

            clusterManager.cluster();

        }
    }

    @Override
    public void onFinishedConnectionWebview(ArrayList<SportsScenarios> itemsSportsScenarios, String sportsScenarios) {

    }


//    @Override
//    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
//        View v = super.onCreateView(arg0, arg1, arg2);
//        Fragment fragment = getParentFragment();
//        if (fragment != null && fragment instanceof OnMapReadyListener) {
//            ((OnMapReadyListener) fragment).onMapReady();
//        }
//        return v;
//    }




}