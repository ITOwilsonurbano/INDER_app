package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.itosoftware.inderandroid.Api.SportsScenarios.ApiSportsScenarios;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Fragments.SearchAdvancedFragment;
import com.itosoftware.inderandroid.Fragments.SearchFragment;
import com.itosoftware.inderandroid.Fragments.SportDetailFragment;
import com.itosoftware.inderandroid.Fragments.SportsFragment;
import com.itosoftware.inderandroid.Interface.DialogClickListener;
import com.itosoftware.inderandroid.Interface.SportScenarioListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.MarkerItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rasuncion on 11/12/15.
 */
public class SportsActivity extends ActionBarActivity implements ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem> , DialogClickListener, SportScenarioListener {

    //Map Variables
    private GoogleMap mMap;
    private MapView mapView;
    private CameraUpdate cu;
    private MarkerItem clickedClusterItem;
    private ClusterManager<MarkerItem> clusterManager;
    ImageView progress;
    Globals globals;
    ActionBar actionBars;
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
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        globals = (Globals) getApplication();
        actionBars = getSupportActionBar();
        // Center the action bar title
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                //Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        TextView textView_TitleActionBar = (TextView) viewActionBar.findViewById(R.id.title_actionbar);

        actionBars.setCustomView(viewActionBar, params);
        actionBars.setDisplayShowCustomEnabled(true); // Se habilita custom para tomar el layout.
        actionBars.setDisplayShowTitleEnabled(false);

        //actionBars.setTitle("Detalle");
        textView_TitleActionBar.setText("Escenarios");

        actionBars.setDisplayHomeAsUpEnabled(true);
        actionBars.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD); // Oculta ActionBar

        progress = (ImageView)  findViewById(R.id.fragment_sport_progressbar);

        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        progress.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {

            }
        });

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMap = mapView.getMap();
        startMap();

        //Show Fragment Search
        btnSearch = (RelativeLayout) findViewById(R.id.fragment_sports_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showDialogSearch();
            }
        });

        btnSearchText = (TextView) findViewById(R.id.text_btn_sports_search);


        //Show Fragment Search Advanced
        btnSearchAdvance = (ImageView) findViewById(R.id.fragment_sports_search_advance);
        if (this.keyword == "Búsqueda de escenarios") {
            btnSearchAdvance.setVisibility(View.GONE);
        }
        btnSearchAdvance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showDialogSearchAdvanced();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        menu.findItem(R.id.action_search_reservation).setVisible(false);
        invalidateOptionsMenu();
        isAuthenticatedUser(globals.getUserIsAuthenticated());

        return super.onCreateOptionsMenu(menu);
    }

    public void isAuthenticatedUser(Boolean value){
        MenuItem menuItem = menu.findItem(R.id.logout_action);
        if(value){
            menuItem.setVisible(true);
        }else{
            menuItem.setVisible(false);
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("Is authenticated", globals.getUserIsAuthenticated().toString());
                try{
                    finish();
                }catch (Exception e){
                    Log.e("Error =>", e.toString());
                }
                return true;
            case R.id.logout_action :
                globals.logoutButton();
                isAuthenticatedUser(globals.getUserIsAuthenticated());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("valid", 0);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**  Start methods for show Search and Search Advanced  **/
    private void showDialogSearch() {
        SearchFragment dialog = new SearchFragment();
        //dialog.setTargetFragment(this, 0);
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    private void showDialogSearchAdvanced() {
        SearchAdvancedFragment dialog = new SearchAdvancedFragment();
//        dialog.setTargetFragment(this, 0);
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    /** End Methods for show Search and Search Advanced  **/

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();

    }

    private void startMap() {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(6.29139, -75.53611));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(4);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.29139, -75.53611), 4));
        //map.moveCamera(center);
        //map.animateCamera(zoom);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

//        try {
        View locationButton = ((View) mapView.findViewById(R.id.map).getParent()).findViewById(R.id.map);

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        //Button goes above card view
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 150);
//        }catch ()


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
        ApiSportsScenarios apiSportsScenarios = new ApiSportsScenarios(this);
        apiSportsScenarios.getSportsScenarios(params);
    }
    /**   End Methods of Interface DialogClickListener **/

    /**  Start methods callback Api  **/
    public void onFinishedConnection(ArrayList<SportsScenarios> itemsSportsScenarios){
        mMap.clear();
        //Builder For Zoom on Alls Markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //ClusterManager for Group the markers
        clusterManager = new ClusterManager<MarkerItem>(this, mMap);

        // Set listener to map
        mMap.setOnCameraChangeListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        mMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(clusterManager);

//        Set listener to ClusterManager
        clusterManager.setOnClusterItemInfoWindowClickListener(this);
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
            clusterManager.getMarkerCollection().setOnInfoWindowAdapter( new InfoWindowAdapter());
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
        progress.setBackgroundDrawable(null);


    }

    @Override
    public void onFinishedConnectionWebview(ArrayList<SportsScenarios> itemsSportsScenarios, String sportsScenarios) {

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
        dFragment.show(getSupportFragmentManager(), "Dialog Fragment");
    }

    //Adapter for manage info window
    public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View rootview;

        //Get layout info_window
        InfoWindowAdapter() {
            rootview = getLayoutInflater().inflate(R.layout.info_window, null);
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
}
