package com.itosoftware.inderandroid.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.clustering.ClusterManager;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.MarkerItem;

import java.util.HashMap;

/**
 * Created by rasuncion on 11/12/15.
 */
public class SportsMapFragment extends SupportMapFragment {

    private GoogleMap map;
    private MapView mapView;
    private CameraUpdate cu;
    private MarkerItem clickedClusterItem;
    private ClusterManager<MarkerItem> clusterManager;
    ImageView progress;
    Globals globals;
    MainActivity mainActivity;
    SportsFragment sportsFragment;

    //Main View
    public View rootview;
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

    public SportsMapFragment() {
        super();

    }

    public static SportsMapFragment newInstance() {
        SportsMapFragment fragment = new SportsMapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("SportsMapFragment","SportsMapFragment - onCreateView");
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);

        if(mapView!=null)
        {
            map = mapView.getMap();

            map.getUiSettings().setMyLocationButtonEnabled(false);

            map.setMyLocationEnabled(true);

            map.getUiSettings().setZoomControlsEnabled(true);
        }

        return view;

    }

    @Override
    public void onResume()
    {
        mapView.onResume();

//        super.onResume();
    }
    @Override
    public void onDestroy()
    {
//        super.onDestroy();

        mapView.onDestroy();
    }
    @Override
    public void onLowMemory()
    {
//        super.onLowMemory();

        mapView.onLowMemory();
    }

}
