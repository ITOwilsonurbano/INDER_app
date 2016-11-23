package com.itosoftware.inderandroid.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Api.SportActivities.ApiActivitiesScenarios;
import com.itosoftware.inderandroid.Api.SportActivities.SportActivities;
import com.itosoftware.inderandroid.Api.SportsScenarios.ApiSportsScenarios;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.DialogClickListener;
import com.itosoftware.inderandroid.Interface.SportActivitiesListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.MarkerItem;
import com.itosoftware.inderandroid.Utils.WebAppActivitiesInterface;
import com.itosoftware.inderandroid.Utils.WebAppSportsInterface;

import java.util.ArrayList;
import java.util.HashMap;


public class SportActivitiesFragment extends Fragment implements ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem> , DialogClickListener, SportActivitiesListener {

    //Map Variables
    private GoogleMap map;
    private MapView mapView;
    private CameraUpdate cu;
    private MarkerItem clickedClusterItem;
    private ClusterManager clusterManager;
    ImageView progress;
    Globals globals;
    MainActivity mainActivity;
    RelativeLayout relativeLayout;
    TextView message;
    TextView messageTwo;
    SportActivitiesFragment sportActivitiesFragment;

    //Main View
    public View rootview;
    public HashMap dataMarkers = new HashMap<>();
    public static final int ID = 2;

    //Search Variables
    RelativeLayout btnSearch;
    TextView btnSearchText;
    ImageView btnSearchAdvance;
    String keyword = "Búsqueda de ofertas";
    String zone = "";
    String commune = "";
    String neighborhood = "";
    String discipline = "";

    public WebView mapWebView;
    public RelativeLayout btnGetGps;
    public WebAppActivitiesInterface webAppActivitiesInterface;
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
            keyword = "Búsqueda de ofertas";
        }
        this.btnSearchText.setText(keyword);
        this.keyword = keyword;
//        if(!keyword.equals("Buscar") || (!getZone().equals("") && !getZone().equals("0") && !getZone().equals(null)) || (!getCommune().equals("") && !getCommune().equals("0") && !getCommune().equals(null)) || (!getNeighborhood().equals("") && !getNeighborhood().equals("0") && !getNeighborhood().equals(null))){
//            btnSearchAdvance.setVisibility(View.VISIBLE);
//        }else{
            btnSearchAdvance.setVisibility(View.GONE);
//        }
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
                // Dismiss the progress
                ApiActivitiesScenarios apiActivitiesScenarios = new ApiActivitiesScenarios(sportActivitiesFragment);
                apiActivitiesScenarios.getActivities(paramsaux);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("TAG", "failed: " + failingUrl + ", error code: " + errorCode + " [" + description + "]");
            }
        });

        mapWebView.loadUrl("file:///android_asset/map_activities.html");

    }
    /**   End Methods of Interface DialogClickListener **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sportActivitiesFragment = this;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Get Main View
        rootview = inflater.inflate(R.layout.fragment_sport_activities, container, false);

        relativeLayout = (RelativeLayout) rootview.findViewById(R.id.fragment_sport_activities_content_sports_search);
        message = (TextView) rootview.findViewById(R.id.fragment_sport_activities_no_activities);
        messageTwo = (TextView) rootview.findViewById(R.id.fragment_sport_activities_no_activities_two);

        String href = "<a href='http://www.inder.gov.co'>www.inder.gov.co</p>";

        messageTwo.setText(Html.fromHtml(href));
        messageTwo.setMovementMethod(LinkMovementMethod.getInstance());

        relativeLayout.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        messageTwo.setVisibility(View.GONE);

        progress = (ImageView)  rootview.findViewById(R.id.fragment_sport_activities_progressbar);

        progress.setBackgroundResource(R.drawable.progress_animation);
        // Showing progress
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        mainActivity = (MainActivity) getActivity();

        progress.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {

            }
        });

        webAppActivitiesInterface = new WebAppActivitiesInterface(sportActivitiesFragment);
        mapWebView = (WebView) rootview.findViewById(R.id.map_webview);

        mapWebView.getSettings().setJavaScriptEnabled(true);
        mapWebView.addJavascriptInterface(webAppActivitiesInterface, "Android");


        // Assign webclient.
        mapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Call Api and get Sports Scenarios
                HashMap params = new HashMap<>();
                params.put("page", 1);
                params.put("limit",100);
                params.put("keyword",keyword);
                params.put("zone",zone);
                params.put("commune",commune);
                params.put("neighborhood", neighborhood);
                params.put("discipline",discipline);

                ApiActivitiesScenarios apiActivitiesScenarios = new ApiActivitiesScenarios(sportActivitiesFragment);
                apiActivitiesScenarios.getActivities(params);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("TAG", "failed: " + failingUrl + ", error code: " + errorCode + " [" + description + "]");
            }
        });

        mapWebView.loadUrl("file:///android_asset/map_activities.html");

//        //Get and Show Map View
//        mapView = (MapView) rootview.findViewById(R.id.map_activities);
//        mapView.onCreate(savedInstanceState);
//        mapView.onResume();
//        try {
//            MapsInitializer.initialize(getContext().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //Get Map From MapView
//        map = mapView.getMap();
//        CameraUpdate center=
//                CameraUpdateFactory.newLatLng(new LatLng(6.29139,
//                        -75.53611));
//        CameraUpdate zoom=CameraUpdateFactory.zoomTo(4 );
//
//        map.moveCamera(center);
//        map.animateCamera(zoom);
//
//        map.getUiSettings().setMapToolbarEnabled(false);
//        map.setMyLocationEnabled(true);
//        map.getUiSettings().setMyLocationButtonEnabled(true);
//
////        try {
//        View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
//
//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//        //Button goes above card view
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        rlp.setMargins(0, 0, 30, 150);
////        }catch ()
//
//        //Call Api and get Sports Scenarios
//        HashMap params = new HashMap<>();
//        params.put("page", 1);
//        params.put("limit",100);
//        params.put("keyword",keyword);
//        params.put("zone",zone);
//        params.put("commune",commune);
//        params.put("neighborhood", neighborhood);
//        params.put("discipline",discipline);
//
//        ApiActivitiesScenarios apiActivitiesScenarios = new ApiActivitiesScenarios(this);
//        apiActivitiesScenarios.getActivities(params);

        //Show Fragment Search
        btnSearch = (RelativeLayout) rootview.findViewById(R.id.fragment_sport_activities_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showDialogSearch();
            }
        });

        btnSearchText = (TextView) rootview.findViewById(R.id.text_btn_sports_activities_search);

        //Show Fragment Search Advanced
        btnSearchAdvance = (ImageView) rootview.findViewById(R.id.fragment_sport_activities_search_advance);
        if(this.keyword == "Búsqueda de ofertas"){
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

        btnGetGps.setOnClickListener(new View.OnClickListener() {

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
                    webAppActivitiesInterface.mostrarMiUbicacion(mapWebView, cx_latitudActual, cy_longitudActual);
                    siActualizarMiUbicacion = true;
                }
            }

        });

        return rootview;
    }
    @Override
    public void onResume() {
        super.onResume();

        globals = (Globals) getActivity().getApplication();
//        map.getUiSettings().setZoomControlsEnabled(true);
//        map.getUiSettings().setCompassEnabled(true);
//        map.getUiSettings().setRotateGesturesEnabled(true);
    }

    /**  Start methods for show Search and Search Advanced  **/
    private void showDialogSearch() {
        SearchActivitiesFragment dialog = new SearchActivitiesFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }
    private void showDialogSearchAdvanced() {
        SearchAdvancedActivitiesFragment dialog = new SearchAdvancedActivitiesFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "dialog");
    }
    /** End Methods for show Search and Search Advanced  **/

    /**  Start methods callback Api  **/
    @Override
    public void onFinishedConnection(ArrayList<SportActivities> itemsSportsActivities, Integer status) {
//        if(status == 200){
//            relativeLayout.setVisibility(View.VISIBLE);
//            mapView.setVisibility(View.VISIBLE);
//            message.setVisibility(View.GONE);
//
//            map.clear();
//            //Builder For Zoom on Alls Markers
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//            //ClusterManager for Group the markers
//            clusterManager = new ClusterManager<MarkerItem>(getContext(), map);
//
//            // Set listener to map
//            map.setOnCameraChangeListener(clusterManager);
//            map.setOnMarkerClickListener(clusterManager);
//            map.setInfoWindowAdapter(clusterManager.getMarkerManager());
//            map.setOnInfoWindowClickListener(clusterManager);
//
//            //Set listener to ClusterManager
//            clusterManager.setOnClusterItemInfoWindowClickListener(this);
//            clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
//                @Override
//                public boolean onClusterItemClick(MarkerItem item) {
//                    clickedClusterItem = item;
//                    return false;
//                }
//            });
//            if(itemsSportsActivities.size() > 0){
//                // Add MarkerItems to cluster
//                for( SportActivities activity : itemsSportsActivities ){
//                    String name = "";
//                    if(activity.getInfo() != null){
//                        HashMap info = activity.getInfo();
//                        name = (String) info.get("nombre");
//                    }
//                    MarkerItem item = new MarkerItem(activity.getLatitude(), activity.getLongitude(), name);
//                    clusterManager.addItem(item);
//                    dataMarkers.put(item.getPosition(), activity);
//                    builder.include(item.getPosition());
//                }
//                //Set adapter to info window
//                clusterManager.getMarkerCollection().setOnInfoWindowAdapter( new InfoWindowAdapter());
//                //Zoom on the markers
//                LatLngBounds bounds = builder.build();
//                cu = CameraUpdateFactory.newLatLngBounds(bounds, 30);
//                try{
//                    map.animateCamera(cu);
//                }
//                catch (IllegalStateException e){
//
//                }
//            }
//        }else if(status == 400){
//            relativeLayout.setVisibility(View.GONE);
//            mapView.setVisibility(View.GONE);
//            message.setVisibility(View.VISIBLE);
//        }
//
//        progress.setBackgroundDrawable(null);

    }
    @Override
    public void onFinishedScheduleOffer(String scheduleOffer) {

    }

    @Override
    public void onFinishedConnectionWebview(ArrayList<SportActivities> itemsSportsActivities, String sportsActivities, Integer status) {
        if(status == 200){
            relativeLayout.setVisibility(View.VISIBLE);
            mapWebView.setVisibility(View.VISIBLE);
            btnGetGps.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
            messageTwo.setVisibility(View.GONE);

            webAppActivitiesInterface.mostrarScenarios(mapWebView, sportsActivities);
            Log.i("sportsConnectionWebView", sportsActivities);

            if(itemsSportsActivities.size() > 0){
                int cont = 0;
                for( SportActivities activity : itemsSportsActivities ){
                    String name = "";
                    if(activity.getInfo() != null){
                        HashMap info = activity.getInfo();
                        name = (String) info.get("nombre");
                    }
                    MarkerItem item = new MarkerItem(activity.getLatitude(), activity.getLongitude(), name);
                    //Log.i("coordenada", activity.getLatitude().doubleValue() + " , " + activity.getLongitude().doubleValue());
                    dataMarkers.put(cont, activity);
                    cont++;
                }
            }
            else if(itemsSportsActivities.size() == 0){
                relativeLayout.setVisibility(View.GONE);
                mapWebView.setVisibility(View.GONE);
                btnGetGps.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                messageTwo.setVisibility(View.VISIBLE);
            }
        }
        else if(status == 400){
            relativeLayout.setVisibility(View.GONE);
            mapWebView.setVisibility(View.GONE);
            btnGetGps.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            messageTwo.setVisibility(View.VISIBLE);

        }

        progress.setBackgroundDrawable(null);
    }

    /**  End methods callback Api  **/

    //Info Window
    @Override
    public void onClusterItemInfoWindowClick(MarkerItem item) {
        //Get Details Sport Scenario
        SportActivities sportActivities = (SportActivities) dataMarkers.get(item.getPosition());

        //Send Information to SportDetailFragment
        Bundle args = new Bundle();
        args.putString("sportActivities", sportActivities.toString());

        // Open Dialog Detail Sport
        SportActivityDetailFragment dFragment = new SportActivityDetailFragment();
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
            //Log.i("Ubicación gps",Text);
            sportActivitiesFragment.setLocation(loc);
            if(siActualizarMiUbicacion){
                webAppActivitiesInterface.actualizarMiUbicacion(mapWebView, loc.getLatitude(), loc.getLongitude());
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
            Log.e("GPS", "Activado");
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
        keyword = "Búsqueda de ofertas";
        zone = "";
        commune = "";
        neighborhood = "";
        discipline = "";

        // Assign webclient.
        mapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Showing progress
                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
                frameAnimation.start();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Call Api and get Sports Scenarios
                HashMap params = new HashMap<>();
                params.put("page", 1);
                params.put("limit",100);
                params.put("keyword",keyword);
                params.put("zone",zone);
                params.put("commune",commune);
                params.put("neighborhood", neighborhood);
                params.put("discipline",discipline);

                ApiActivitiesScenarios apiActivitiesScenarios = new ApiActivitiesScenarios(sportActivitiesFragment);
                apiActivitiesScenarios.getActivities(params);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("TAG", "failed: " + failingUrl + ", error code: " + errorCode + " [" + description + "]");
            }
        });

        mapWebView.loadUrl("file:///android_asset/map_activities.html");
    }

}
