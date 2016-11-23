package com.itosoftware.inderandroid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.SettingsActivity;
import com.itosoftware.inderandroid.Api.Device.ApiDevice;
import com.itosoftware.inderandroid.Api.SportsScenarios.ApiSportsScenarios;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.DeviceListener;
import com.itosoftware.inderandroid.Interface.DialogClickListener;
import com.itosoftware.inderandroid.Interface.SportScenarioListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.MarkerItem;
import com.itosoftware.inderandroid.Utils.WebAppSportsInterface;
import android.view.View.OnClickListener;
import com.itosoftware.inderandroid.Interface.DeviceListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SportsFragment extends Fragment implements ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem> , DialogClickListener, SportScenarioListener, DeviceListener {

    //Map Variables
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

    public WebView mapWebView;
    public RelativeLayout btnGetGps;
    public WebAppSportsInterface webAppSportsInterface;
    static HashMap paramsaux;
    Location milocationActual;
    boolean siActualizarMiUbicacion = false;

    /**   Start Methods of Interface DialogClickListener **/
    @Override
    public String getLabel() {
        return this.keyword;
    }
    @Override
    public void setLabel(String keyword) {
        if(keyword.length() == 0){
            keyword = "Búsqueda de escenarios";
        }
        this.btnSearchText.setText(keyword);
        this.keyword = keyword;
        if(!keyword.equals("Búsqueda de escenarios") || (!getZone().equals("") && !getZone().equals("0") && !getZone().equals(null)) || (!getCommune().equals("") && !getCommune().equals("0") && !getCommune().equals(null)) || (!getNeighborhood().equals("") && !getNeighborhood().equals("0") && !getNeighborhood().equals(null))){
            btnSearchAdvance.setVisibility(View.VISIBLE);
        }else{
            btnSearchAdvance.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateDevice() {
        Log.d("SportFragment", "onCreateDevice");
    }
    @Override
    public String getZone(){
        return this.zone;
    }
    @Override
    public void setZone(String zone){
        this.zone = zone;
    }
    @Override
    public String getCommune(){
        return this.commune;
    }
    @Override
    public void setCommune(String commune){
        this.commune = commune;
    }
    @Override
    public String getNeighborhood(){
        return this.neighborhood;
    }
    @Override
    public void setNeighborhood(String neighborhood){
        this.neighborhood = neighborhood;
    }
    @Override
    public String getDiscipline(){
        return this.discipline;
    }
    @Override
    public void setDiscipline(String discipline){
        this.discipline = discipline;
    }
    @Override
    public void updateMap(HashMap params){
        progress.setBackgroundResource(R.drawable.progress_animation);
        // Showing progress
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();
        paramsaux = params;

        // Assign webclient.
        mapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                btnGetGps.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Call Api and get Sports Scenarios
                ApiSportsScenarios apiSportsScenarios = new ApiSportsScenarios(sportsFragment);
                apiSportsScenarios.getSportsScenarios(paramsaux);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("TAG", "failed: " + failingUrl + ", error code: " + errorCode + " [" + description + "]");
            }
        });

        mapWebView.loadUrl("file:///android_asset/map.html");

    }
    /**   End Methods of Interface DialogClickListener **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("SportsFragment","SportsFragment - onCreateView");
        super.onCreate(savedInstanceState);
        globals = (Globals) getActivity().getApplication();
        sportsFragment = this;

        Log.d("SportsActivity - ","Ejecutar registro del dispositivo");
        ApiDevice apiDevice = new ApiDevice(this);
        apiDevice.createDevice();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Get Main View
        rootview = inflater.inflate(R.layout.fragment_sports, container, false);

        progress = (ImageView)  rootview.findViewById(R.id.fragment_sport_progressbar);

//        Button button = (Button) rootview.findViewById(R.id.fragment_sports_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), SportsActivity.class);
//                getActivity().startActivityForResult(intent, 4);
//            }
//        });

        webAppSportsInterface = new WebAppSportsInterface(sportsFragment);
        mapWebView = (WebView) rootview.findViewById(R.id.map_webview);

        mapWebView.getSettings().setJavaScriptEnabled(true);
        mapWebView.addJavascriptInterface(webAppSportsInterface, "Android");

        // Showing progress
        progress.setBackgroundResource(R.drawable.progress_animation);
        // Showing progress
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        // Assign webclient.
        mapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Call Api and get Sports Scenarios
                HashMap params = new HashMap<>();
                params.put("page", 0);
                params.put("limit",-1);
                params.put("keyword",keyword);
                params.put("zone",zone);
                params.put("commune",commune);
                params.put("neighborhood", neighborhood);
                params.put("discipline",discipline);

                ApiSportsScenarios apiSportsScenarios = new ApiSportsScenarios(sportsFragment);
                apiSportsScenarios.getSportsScenarios(params);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("TAG", "failed: " + failingUrl + ", error code: " + errorCode + " [" + description + "]");
            }
        });

        mapWebView.loadUrl("file:///android_asset/map.html");

//        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
//        frameAnimation.start();

//        mainActivity = (MainActivity) getActivity();
//        mainActivity.isAutheticated(false);


        progress.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {

            }
        });

        //Get and Show Map View
//        mapView = (MapView) rootview.findViewById(R.id.map);
//        mapView.onCreate(savedInstanceState);
//
////        try {
////            MapsInitializer.initialize(getActivity());
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        //Get Map From MapView
//        map = mapView.getMap();

//        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(6.29139,-75.53611));
//        CameraUpdate zoom=CameraUpdateFactory.zoomTo(4);
//        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.29139, -75.53611), 4));
        //map.moveCamera(center);
        //map.animateCamera(zoom);
//        map.getUiSettings().setMapToolbarEnabled(false);
//        map.getUiSettings().setMyLocationButtonEnabled(true);

////        try {
//            View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
//
//            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//            //Button goes above card view
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            rlp.setMargins(0, 0, 30, 150);
////        }catch ()



        //Show Fragment Search
        btnSearch = (RelativeLayout) rootview.findViewById(R.id.fragment_sports_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showDialogSearch();
            }
        });

        btnSearchText = (TextView) rootview.findViewById(R.id.text_btn_sports_search);


        //Show Fragment Search Advanced
        btnSearchAdvance = (ImageView) rootview.findViewById(R.id.fragment_sports_search_advance);
        if (this.keyword == "Búsqueda de escenarios") {
            btnSearchAdvance.setVisibility(View.GONE);
        }
        btnSearchAdvance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showDialogSearchAdvanced();
            }
        });

        btnGetGps = (RelativeLayout) rootview.findViewById(R.id.btn_getgps);
        btnGetGps.setVisibility(View.GONE);

        /* Use the LocationManager class to obtain GPS locations */
        final LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final MyLocationListener mlocListener = new MyLocationListener();

        try {
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7, 2,(LocationListener) mlocListener);

        }
        catch (SecurityException e){

        }

		btnGetGps.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7, 2,(LocationListener) mlocListener);

                }
                catch (SecurityException e){

                }

                Log.i("LocationListener","agregado");

                if(milocationActual != null){
                    double cx_latitudActual = milocationActual.getLatitude();
                    double cy_longitudActual = milocationActual.getLongitude();

                    Log.e("Ok", "Coordenadas Enviadas " + cx_latitudActual + "," + cy_longitudActual);
                    webAppSportsInterface.mostrarMiUbicacion(mapWebView, cx_latitudActual, cy_longitudActual);
                    siActualizarMiUbicacion = true;
                }
            }

        });



        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mapView.onResume();
//        map.getUiSettings().setZoomControlsEnabled(true);
//        map.getUiSettings().setCompassEnabled(true);
//        map.getUiSettings().setRotateGesturesEnabled(true);
    }

    /**  Start methods for show Search and Search Advanced  **/
    private void showDialogSearch() {
        SearchFragment dialog = new SearchFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }
    private void showDialogSearchAdvanced() {
        SearchAdvancedFragment dialog = new SearchAdvancedFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }
    /** End Methods for show Search and Search Advanced  **/


    /**  Start methods callback Api  **/
    public void onFinishedConnection(ArrayList<SportsScenarios> itemsSportsScenarios){
//        map.clear();
//        //Builder For Zoom on Alls Markers
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//        //ClusterManager for Group the markers
//        clusterManager = new ClusterManager<MarkerItem>(getActivity(), map);
//
//        // Set listener to map
//        map.setOnCameraChangeListener(clusterManager);
//        map.setOnMarkerClickListener(clusterManager);
//        map.setInfoWindowAdapter(clusterManager.getMarkerManager());
//        map.setOnInfoWindowClickListener(clusterManager);
//
////        Set listener to ClusterManager
//        clusterManager.setOnClusterItemInfoWindowClickListener(this);
//        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
//            @Override
//            public boolean onClusterItemClick(MarkerItem item) {
//                clickedClusterItem = item;
//                return false;
//            }
//        });
//        Integer total = itemsSportsScenarios.size();
//        if(itemsSportsScenarios.size() != 0) {
//            clusterManager.clearItems();
//
        // Add MarkerItems to cluster
//            for (SportsScenarios scenario : itemsSportsScenarios) {
//                String name = "Placa Deportiva";
//                if (scenario.getInfo() != null && scenario.getInfo().size() != 0) {
//                    name = (String) scenario.getInfo().get("nombre");
//                }

//                MarkerItem item = new MarkerItem(scenario.getLatitude().doubleValue(), scenario.getLongitude().doubleValue(), name);
//                Log.i("coordenada", scenario.getLatitude().doubleValue() + " , " + scenario.getLongitude().doubleValue());

        //clusterManager.addItem(item);
//                dataMarkers.put(item.getPosition(), scenario);
        //builder.include(item.getPosition());
//            }

//
//            //Set adapter to info window
//            clusterManager.getMarkerCollection().setOnInfoWindowAdapter( new InfoWindowAdapter());
//            //Zoom on the markers
//            LatLngBounds bounds = builder.build();
//            cu = CameraUpdateFactory.newLatLngBounds(bounds,90);
//            try{
//                map.animateCamera(cu);
//            }
//            catch (IllegalStateException e){
//
//            }
//
//            clusterManager.cluster();
//
//        }
//        progress.setBackgroundDrawable(null);


    }

    @Override
    public void onFinishedConnectionWebview(ArrayList<SportsScenarios> itemsSportsScenarios, String sportsScenarios) {
        webAppSportsInterface.mostrarScenarios(mapWebView, sportsScenarios);
        Log.i("sportsConnectionWebView", sportsScenarios);

        if(itemsSportsScenarios.size() != 0) {
            int cont = 0;
            for (SportsScenarios scenario : itemsSportsScenarios) {
                String name = "Placa Deportiva";
                if (scenario.getInfo() != null && scenario.getInfo().size() != 0) {
                    name = (String) scenario.getInfo().get("nombre");
                }

                MarkerItem item = new MarkerItem(scenario.getLatitude().doubleValue(), scenario.getLongitude().doubleValue(), name);
                //Log.i("coordenada", scenario.getLatitude().doubleValue() + " , " + scenario.getLongitude().doubleValue());

                dataMarkers.put(cont, scenario);
                cont++;
            }
        }
        btnGetGps.setVisibility(View.VISIBLE);
        progress.setBackgroundDrawable(null);

    }

    /**  End methods callback Api  **/

    //Info Window
    @Override
    public void onClusterItemInfoWindowClick(MarkerItem item) {
        //Get Details Sport Scenario
        SportsScenarios sportsScenario = (SportsScenarios) dataMarkers.get(item.getPosition());

        //Send Information to SportDetailFragment
        Bundle args = new Bundle();
        args.putString("sportsScenario", sportsScenario.toString());

        // Open Dialog Detail Sport
        SportDetailFragment dFragment = new SportDetailFragment();
        dFragment.setArguments(args);
        dFragment.show(getFragmentManager(), "Dialog Fragment");
    }

    //Adapter for manage info window
    public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View rootview;

        //Get layout info_window
        InfoWindowAdapter() {
            rootview = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            TextView title = ((TextView) rootview.findViewById(R.id.info_window_title));
            title.setText(clickedClusterItem.getTitle().toUpperCase());

            return rootview;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    public HashMap getDataMarkers() {
        return dataMarkers;
    }

    public void setDataMarkers(HashMap dataMarkers) {
        this.dataMarkers = dataMarkers;
    }

    /* Class My Location Listener */
    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccin de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            String Text = "Mi ubicación actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            //messageTextView.setText(Text);
            Log.i("Ubicación gps",Text);
            sportsFragment.setLocation(loc);
            if(siActualizarMiUbicacion){
                webAppSportsInterface.actualizarMiUbicacion(mapWebView, loc.getLatitude(), loc.getLongitude());
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //messageTextView.setText("GPS Desactivado");
            Log.e("GPS","Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //messageTextView.setText("GPS Activado");
            Log.e("GPS","Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Este metodo se ejecuta cada vez que se detecta un cambio en el
            // status del proveedor de localizacin (GPS)
            // Los diferentes Status son:
            // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
            // TEMPORARILY_UNAVAILABLE -> Tempralmente no disponible pero se
            // espera que este disponible en breve
            // AVAILABLE -> Disponible
        }

    }/* End of Class MyLocationListener */

    public void setLocation(Location loc) {
        this.milocationActual = loc;
    }

    public void reloadMap(){
         keyword = "Búsqueda de escenarios";
         zone = "";
         commune = "";
         neighborhood = "";
         discipline = "";

        // Assign webclient.
        mapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                // Showing progress
                progress.setBackgroundResource(R.drawable.progress_animation);
                // Showing progress
                AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
                frameAnimation.start();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Call Api and get Sports Scenarios
                HashMap params = new HashMap<>();
                params.put("page", 0);
                params.put("limit",-1);
                params.put("keyword",keyword);
                params.put("zone",zone);
                params.put("commune",commune);
                params.put("neighborhood", neighborhood);
                params.put("discipline",discipline);

                ApiSportsScenarios apiSportsScenarios = new ApiSportsScenarios(sportsFragment);
                apiSportsScenarios.getSportsScenarios(params);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("TAG", "failed: " + failingUrl + ", error code: " + errorCode + " [" + description + "]");
            }
        });

        mapWebView.loadUrl("file:///android_asset/map.html");
    }

}

