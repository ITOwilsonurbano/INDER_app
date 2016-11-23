package com.itosoftware.inderandroid.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itosoftware.inderandroid.Adapters.InderSpinnerAdapter;
import com.itosoftware.inderandroid.Api.Communes.ApiCommunes;
import com.itosoftware.inderandroid.Api.Communes.Communes;
import com.itosoftware.inderandroid.Api.Neighborhood.ApiNeighborhood;
import com.itosoftware.inderandroid.Api.Neighborhood.Neighborhood;
import com.itosoftware.inderandroid.Api.SportsScenarios.ApiSportsScenarios;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Api.Zones.ApiZones;
import com.itosoftware.inderandroid.Api.Zones.Zones;
import com.itosoftware.inderandroid.Interface.CommunesListener;
import com.itosoftware.inderandroid.Interface.DialogClickListener;
import com.itosoftware.inderandroid.Interface.NeighborhoodListener;
import com.itosoftware.inderandroid.Interface.ZonesListener;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchAdvancedActivitiesFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, ZonesListener, CommunesListener, NeighborhoodListener {

    View rootView;
    private DialogClickListener callback;

    //Properties Spinner Zone fragment_register_two_zone
    protected Spinner zoneSpinner;
    protected HashMap<Integer, Integer> zoneCodes;
    protected HashMap<Integer, String> zoneNames;
    ArrayAdapter<String> adapterZones;

    //Properties Spinner Commune fragment_register_two_commune
    protected Spinner communeSpinner;
    protected HashMap<Integer, Integer> communeCodes;
    protected HashMap<Integer, String> communeNames;
    ArrayAdapter<String> adapterCommunes;

    //Properties Spinner Neighborhood fragment_register_two_neighborhood
    protected Spinner neighborhoodSpinner;
    protected HashMap<Integer, Integer>neighborhoodCodes;
    protected HashMap<Integer, String> neighborhoodNames;
    ArrayAdapter<String> adapterNeighborhood;

    Integer codeZone = 0;
    Integer codeCommune = 0;
    Integer codeNeighborhood = 0;
    //Properties Spinner Discipline
//    protected android.widget.Spinner disciplineSpinner;
//    protected HashMap<Integer, Integer> disciplineCodes;
//    protected HashMap<Integer, String> disciplineNames;



    protected EditText general;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (DialogClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Search Advanced ","ActivityActivityActivityActivity");

        rootView = inflater.inflate(R.layout.fragment_search_advanced_activities, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Transparente)));

        //Button Close
        Button close = (Button) rootView.findViewById(R.id.fragment_search_advanced_activities_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        //General
        general = (EditText) rootView.findViewById(R.id.fragment_search_advanced_activities_general);
        String label = callback.getLabel();
        if(label != "Buscar"){
            general.setText(label);
        }

        //Spinner Zone
        HashMap params = new HashMap<>();
        params.put("page", 0);
        params.put("limit", -1);
        params.put("country_id", getContext().getString(R.string.colombia_id));
        params.put("departamento_id", getContext().getString(R.string.antioquia_id));
        params.put("town_id", getContext().getString(R.string.medellin_id));
        ApiZones apiZones = new ApiZones(this);
        apiZones.getZones(params);
        zoneSpinner = (Spinner) rootView.findViewById(R.id.fragment_search_advanced_activities_zone);
        zoneCodes = new HashMap<Integer, Integer>();
        zoneNames = new HashMap<Integer, String>();
        adapterZones = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapterZones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterZones.add("Zona");
        zoneSpinner.setAdapter(adapterZones);

        //Spinner Commune
        communeSpinner = (Spinner) rootView.findViewById(R.id.fragment_search_advanced_activities_commune);
        communeCodes = new HashMap<Integer, Integer>();
        communeNames = new HashMap<Integer, String>();
        adapterCommunes = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapterCommunes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCommunes.add("Comuna");
        communeSpinner.setAdapter(adapterCommunes);

        //Spinner Neighborhood
        neighborhoodSpinner = (Spinner) rootView.findViewById(R.id.fragment_search_advanced_activities_neighborhood);
        neighborhoodCodes = new HashMap<Integer, Integer>();
        neighborhoodNames = new HashMap<Integer, String>();
        adapterNeighborhood = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapterNeighborhood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterNeighborhood.add("Barrio");
        neighborhoodSpinner.setAdapter(adapterNeighborhood);

        //Spinner Discipline
//        disciplineSpinner = (Spinner) rootView.findViewById(R.id.fragment_search_advanced_discipline);
//        disciplineCodes = new HashMap<Integer, Integer>();
//        disciplineNames = new HashMap<Integer, String>();
//        ArrayAdapter<String> adapterDiscipline = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
//        adapterDiscipline.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapterDiscipline.add("Actividad Deportiva");
//        position = 1;
//        String[] disciplines = getResources().getStringArray(R.array.spinner_discipline);
//        for (int i = 0; i <= disciplines.length - 1; i++) {
//            disciplineCodes.put(position, position);
//            disciplineNames.put(position, disciplines[i]);
//            adapterDiscipline.add(disciplines[i]);
//            position++;
//        }
//        disciplineSpinner.setAdapter(adapterDiscipline);
//        disciplineSpinner.setOnItemSelectedListener(this);
//        if (!callback.getDiscipline().equals(null)) {
//            int spinnerPosition = adapterDiscipline.getPosition(callback.getDiscipline());
//            disciplineSpinner.setSelection(spinnerPosition);
//        }

        RelativeLayout search = (RelativeLayout) rootView.findViewById(R.id.fragment_search_advanced_activities_button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.setLabel(general.getText().toString());
                if (zoneSpinner.getSelectedItemPosition() != 0) {
                    Integer code = (Integer) zoneCodes.get(zoneSpinner.getSelectedItemPosition());
                    callback.setZone(code.toString());
                } else {
                    Integer code = 0;
                    callback.setZone(code.toString());
                }
                if (communeSpinner.getSelectedItemPosition() != 0) {
                    Integer code = (Integer) communeCodes.get(communeSpinner.getSelectedItemPosition());
                    callback.setCommune(code.toString());
                } else {
                    Integer code = 0;
                    callback.setCommune(code.toString());
                }
                if (neighborhoodSpinner.getSelectedItemPosition() != 0) {
                    Integer code = (Integer) neighborhoodCodes.get(neighborhoodSpinner.getSelectedItemPosition());
                    callback.setNeighborhood(code.toString());
                } else {
                    Integer code = 0;
                    callback.setNeighborhood(code.toString());
                }
                HashMap params = new HashMap<>();
                params.put("page", 1);
                params.put("limit", 50);
                params.put("keyword", general.getText().toString());
                if (!callback.getNeighborhood().equals(null) && !callback.getNeighborhood().equals(0)) {
                    params.put("neighborhood", callback.getNeighborhood());
                }
                if (!callback.getCommune().equals(null) && !callback.getCommune().equals(0)) {
                    params.put("commune", callback.getCommune());
                }
                if (!callback.getZone().equals(null) && !callback.getZone().equals(0)) {
                    params.put("zone", callback.getZone());
                }
                callback.updateMap(params);
//                callback.setDiscipline(disciplineSpinner.getSelectedItem().toString());
                dismiss();
            }
        });

        // Do something else
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.fragment_search_advanced_activities_zone:
                codeZone = 0;
                if(zoneSpinner.getSelectedItemPosition() != 0){
                    codeZone = (Integer) zoneCodes.get(zoneSpinner.getSelectedItemPosition());
                }
                adapterCommunes.clear();
                adapterCommunes.add("Comuna");
                adapterCommunes.notifyDataSetChanged();

                adapterNeighborhood.clear();
                adapterNeighborhood.add("Barrio");
                adapterNeighborhood.notifyDataSetChanged();
                if(codeZone != 0){
                    HashMap paramsCommune = new HashMap<>();
                    paramsCommune.put("page", 0);
                    paramsCommune.put("limit", -1);
                    paramsCommune.put("country_id", getContext().getString(R.string.colombia_id));
                    paramsCommune.put("departamento_id", getContext().getString(R.string.antioquia_id));
                    paramsCommune.put("town_id", getContext().getString(R.string.medellin_id));
                    paramsCommune.put("zone_id", codeZone.toString());
                    ApiCommunes apiCommunes = new ApiCommunes(this);
                    apiCommunes.getCommunesByZone(paramsCommune);
                }

                break;
            case R.id.fragment_search_advanced_activities_commune:
                codeCommune = 0;
                if(communeSpinner.getSelectedItemPosition() != 0){
                    codeCommune = (Integer) communeCodes.get(communeSpinner.getSelectedItemPosition());
                }
                adapterNeighborhood.clear();
                adapterNeighborhood.add("Barrio");
                adapterNeighborhood.notifyDataSetChanged();
                if(codeCommune != 0) {
                    HashMap paramsNeighborhood = new HashMap<>();
                    paramsNeighborhood.put("page", 0);
                    paramsNeighborhood.put("limit", -1);
                    paramsNeighborhood.put("country_id", getContext().getString(R.string.colombia_id));
                    paramsNeighborhood.put("departamento_id", getContext().getString(R.string.antioquia_id));
                    paramsNeighborhood.put("town_id", getContext().getString(R.string.medellin_id));
                    paramsNeighborhood.put("zone_id", codeZone.toString());
                    paramsNeighborhood.put("commune_id", codeCommune.toString());
                    ApiNeighborhood apiNeighborhood = new ApiNeighborhood(this);
                    apiNeighborhood.getNeighborhoodsByCommunes(paramsNeighborhood);
                }
                break;
            case R.id.fragment_search_advanced_activities_neighborhood:
                if(neighborhoodSpinner.getSelectedItemPosition() != 0){
                    codeNeighborhood = (Integer) neighborhoodCodes.get(neighborhoodSpinner.getSelectedItemPosition());
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    /**   Start Methods of Interface ZonesListener **/
    @Override
    public void onfinishedLoadZones(ArrayList<Zones> zones) {
        int i = 1;
        Integer selectedItem = 0;
        adapterZones.clear();
        adapterZones.add("Zona");
        for (Zones zone : zones) {
            String name =  (String) zone.getNombre();
            adapterZones.add(name);
            zoneCodes.put(i,zone.getId());
            zoneNames.put(i,zone.getNombre());
            if(zone.getId().toString().equals(callback.getZone())){
                selectedItem = i;
            }
            i++;
        }
        adapterZones.notifyDataSetChanged();
        zoneSpinner.setOnItemSelectedListener(this);
        zoneSpinner.setAdapter(adapterZones);
        if (!callback.getZone().equals(null) && !callback.getZone().equals("") && codeZone == 0) {
            zoneSpinner.setSelection(selectedItem);
        }
    }

    @Override
    public void onErrorLoadZones() {
        Toast.makeText(getContext(), "Error al cargar Zonas.", Toast.LENGTH_LONG).show();
    }
    /**   End Methods of Interface ZonesListener **/

    /**   Start Methods of Interface CommunesListener **/
    @Override
    public void onfinishedLoadCommunes(ArrayList<Communes> communes) {
        int i = 1;
        Integer selectedItem = 0;
        adapterCommunes.clear();
        adapterCommunes.add("Comuna");
        for (Communes commune : communes) {
            String name =  (String) commune.getNombre();
            adapterCommunes.add(name);
            communeCodes.put(i,commune.getId());
            communeNames.put(i,commune.getNombre());
            if(commune.getId().toString().equals(callback.getCommune())){
                selectedItem = i;
            }
            i++;
        }
        adapterCommunes.notifyDataSetChanged();
        communeSpinner.setOnItemSelectedListener(this);
        communeSpinner.setAdapter(adapterCommunes);

        if (!callback.getCommune().equals(null) && !callback.getCommune().equals("") && codeCommune == 0) {
            communeSpinner.setSelection(selectedItem);
        }
    }

    @Override
    public void onErrorLoadCommunes() {
        Toast.makeText(getContext(), "Error al cargar Comunas.",Toast.LENGTH_LONG).show();
    }
    /**   End Methods of Interface CommunesListener **/

    /**   Start Methods of Interface NeighborhoodListener **/
    @Override
    public void onfinishedLoadNeighborhood(ArrayList<Neighborhood> neighborhoods) {
        Log.d("Neighborhood","Neighborhood");
        int i = 1;
        Integer selectedItem = 0;
        adapterNeighborhood.clear();
        adapterNeighborhood.add("Barrio");
        for (Neighborhood neighborhood : neighborhoods) {
            String name =  (String) neighborhood.getNombre();
            adapterNeighborhood.add(name);
            neighborhoodCodes.put(i,neighborhood.getId());
            neighborhoodNames.put(i,neighborhood.getNombre());
            if(callback.getNeighborhood().equals(neighborhood.getId().toString())){
                selectedItem = i;
            }
            i++;
        }
        adapterNeighborhood.notifyDataSetChanged();
        neighborhoodSpinner.setOnItemSelectedListener(this);
        neighborhoodSpinner.setAdapter(adapterNeighborhood);
        if (!callback.getNeighborhood().equals(null) && !callback.getNeighborhood().equals("") && codeNeighborhood == 0) {
            neighborhoodSpinner.setSelection(selectedItem);
        }
    }

    @Override
    public void onErrorLoadNeighborhood() {
        Toast.makeText(getContext(), "Error al cargar Barrios.",Toast.LENGTH_LONG).show();
    }
    /**   End Methods of Interface NeighborhoodListener **/
}