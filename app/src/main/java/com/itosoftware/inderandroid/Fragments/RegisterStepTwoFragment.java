package com.itosoftware.inderandroid.Fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Activities.RegisterActivity;
import com.itosoftware.inderandroid.Adapters.InderSpinnerAdapter;
import com.itosoftware.inderandroid.Api.Communes.ApiCommunes;
import com.itosoftware.inderandroid.Api.Communes.Communes;
import com.itosoftware.inderandroid.Api.Countries.ApiCountries;
import com.itosoftware.inderandroid.Api.Countries.Countries;
import com.itosoftware.inderandroid.Api.DocumentType.ApiDocumentTypes;
import com.itosoftware.inderandroid.Api.Neighborhood.ApiNeighborhood;
import com.itosoftware.inderandroid.Api.Neighborhood.Neighborhood;
import com.itosoftware.inderandroid.Api.States.ApiStates;
import com.itosoftware.inderandroid.Api.States.States;
import com.itosoftware.inderandroid.Api.Towns.ApiTowns;
import com.itosoftware.inderandroid.Api.Towns.Towns;
import com.itosoftware.inderandroid.Api.Zones.ApiZones;
import com.itosoftware.inderandroid.Api.Zones.Zones;
import com.itosoftware.inderandroid.Interface.CommunesListener;
import com.itosoftware.inderandroid.Interface.CountriesListener;
import com.itosoftware.inderandroid.Interface.NeighborhoodListener;
import com.itosoftware.inderandroid.Interface.SetAddressListener;
import com.itosoftware.inderandroid.Interface.StatesListener;
import com.itosoftware.inderandroid.Interface.TownsListener;
import com.itosoftware.inderandroid.Interface.ZonesListener;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.HashMap;


public class RegisterStepTwoFragment extends Fragment implements AdapterView.OnItemSelectedListener, CountriesListener, StatesListener,TownsListener, ZonesListener, CommunesListener, NeighborhoodListener, SetAddressListener {
    View rootView;
    ActionBar actionBars;
    RegisterActivity registerActivity;

    //Properties Spinner Country    fragment_register_two_country
    protected Spinner countrySpinner;
    protected HashMap<Integer, Integer> countryCodes;
    protected HashMap<Integer, String> countryNames;
    InderSpinnerAdapter adapterCountry;

    //Properties Spinner Department    fragment_register_two_department
    protected Spinner departmentSpinner;
    protected HashMap<Integer, Integer> departmentCodes;
    protected HashMap<Integer, String> departmentNames;
    InderSpinnerAdapter adapterDepartment;

    //Properties Spinner Town  fragment_register_two_town
    protected Spinner townSpinner;
    protected HashMap<Integer, Integer> townCodes;
    protected HashMap<Integer, String> townNames;
    InderSpinnerAdapter adapterTown;

    //Properties Spinner Zone fragment_register_two_zone
    protected Spinner zoneSpinner;
    protected HashMap<Integer, Integer> zoneCodes;
    protected HashMap<Integer, String> zoneNames;
    InderSpinnerAdapter adapterZones;

    //Properties Spinner Commune fragment_register_two_commune
    protected Spinner communeSpinner;
    protected HashMap<Integer, Integer> communeCodes;
    protected HashMap<Integer, String> communeNames;
    InderSpinnerAdapter adapterCommunes;

    //Properties Spinner Stratum fragment_register_two_stratum
    protected Spinner stratumSpinner;
    protected HashMap<Integer, Integer> stratumCodes;
    protected HashMap<Integer, String> stratumNames;

    //Properties Spinner Neighborhood fragment_register_two_neighborhood
    protected Spinner neighborhoodSpinner;
    protected HashMap<Integer, Integer>neighborhoodCodes;
    protected HashMap<Integer, String> neighborhoodNames;
    InderSpinnerAdapter adapterNeighborhood;

    public EditText phone;

    //Button Address
    Button addressButton;
    Bundle paramsAddressDialog;

    Integer codeCountry = 49;
    Integer codeDepartment = 744;
    Integer codeTown = 73;
    Integer codeZone = 0;
    Integer codeCommune = 0;
    Integer codeNeighborhood = 0;
    RegisterStepTwoFragment fragment;

    ImageView progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        paramsAddressDialog = new Bundle();
        rootView = inflater.inflate(R.layout.fragment_register_step_two, container, false);
        registerActivity = (RegisterActivity) getActivity();

         fragment = this;

        ScrollView scroll = (ScrollView) rootView.findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(registerActivity.getCurrentFocus().getWindowToken(), 0);
                    registerActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    return true;
                }
                return false;
            }
        });

        progress = (ImageView) rootView.findViewById(R.id.fragment_register_two_progress_bar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        //Spinner Country
        HashMap paramsCountry = new HashMap<>();
        paramsCountry.put("page", 0);
        paramsCountry.put("limit", -1);
        ApiCountries apiCountries = new ApiCountries(this);
        apiCountries.getCountries(paramsCountry);
        countrySpinner = (Spinner) rootView.findViewById(R.id.fragment_register_two_country);
        countryCodes = new HashMap<Integer, Integer>();
        countryNames = new HashMap<Integer, String>();
        adapterCountry = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCountry.add("País");
        countrySpinner.setAdapter(adapterCountry);

        //Spinner Department
        departmentSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_two_department);
        departmentCodes = new HashMap<Integer, Integer>();
        departmentNames = new HashMap<Integer, String>();
        adapterDepartment = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDepartment.add("Departamento");
        departmentSpinner.setAdapter(adapterDepartment);

        //Spinner Town
        townSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_two_town);
        townCodes = new HashMap<Integer, Integer>();
        townNames = new HashMap<Integer, String>();
        adapterTown = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterTown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTown.add("Ciudad");
        townSpinner.setAdapter(adapterTown);

        //Spinner Zone
        zoneSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_two_zone);
        zoneCodes = new HashMap<Integer, Integer>();
        zoneNames = new HashMap<Integer, String>();
        adapterZones = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterZones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterZones.add("Zona");
        zoneSpinner.setAdapter(adapterZones);

        //Spinner Commune
        communeSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_two_commune);
        communeCodes = new HashMap<Integer, Integer>();
        communeNames = new HashMap<Integer, String>();
        adapterCommunes = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterCommunes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCommunes.add("Comuna");
        communeSpinner.setAdapter(adapterCommunes);

        //Spinner Stratum
        stratumSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_two_stratum);
        stratumCodes = new HashMap<Integer, Integer>();
        stratumNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterStratum = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterStratum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterStratum.add("Estrato");
        for (Integer position = 1; position <= 6; position++) {
            stratumCodes.put(position, position);
            stratumNames.put(position, position.toString());
            adapterStratum.add(position.toString());
        }
        stratumSpinner.setAdapter(adapterStratum);

        //Spinner Neighborhood
        neighborhoodSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_two_neighborhood);
        neighborhoodCodes = new HashMap<Integer, Integer>();
        neighborhoodNames = new HashMap<Integer, String>();
        adapterNeighborhood = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterNeighborhood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterNeighborhood.add("Barrio");
        neighborhoodSpinner.setAdapter(adapterNeighborhood);

        addressButton = (Button) rootView.findViewById(R.id.fragment_register_two_address);
        addressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                SetAddressFragment dFragment = new SetAddressFragment();
                dFragment.setTargetFragment(fragment,0);

                dFragment.setArguments(paramsAddressDialog);
                dFragment.show(getFragmentManager(), "Dialog Fragment");
            }
        });

        //Edit Text Phone
        phone = (EditText) rootView.findViewById(R.id.fragment_register_two_phone);

        //Validate fields on click next step
        Button next = (Button) rootView.findViewById(R.id.fragment_register_two_button_next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                int errors = 0;

                // Country
                Integer posCountry = countrySpinner.getSelectedItemPosition();
                if(posCountry == 0){
                    ((TextView)countrySpinner.getSelectedView()).setError("Selecciona un país");
                    errors ++;
                }
                // State
                Integer posDepartament = departmentSpinner.getSelectedItemPosition();
                if(posDepartament == 0){
                    ((TextView)departmentSpinner.getSelectedView()).setError("Selecciona un departamento");
                    errors ++;
                }
                // Town
                Integer posTown = townSpinner.getSelectedItemPosition();
                if(posTown == 0){
                    ((TextView)townSpinner.getSelectedView()).setError("Selecciona una ciudad");
                    errors ++;
                }

                // Zone
                Integer posZone = zoneSpinner.getSelectedItemPosition();
                if(posZone == 0){
                    ((TextView)zoneSpinner.getSelectedView()).setError("Selecciona una zona");
                    errors ++;
                }

                // Commune
                Integer posCommune = communeSpinner.getSelectedItemPosition();
                if(posCommune == 0){
                    ((TextView)communeSpinner.getSelectedView()).setError("Selecciona una comuna");
                    errors ++;
                }

                // Neighborhood
                Integer posNeighborhood = neighborhoodSpinner.getSelectedItemPosition();
                if(posNeighborhood == 0){
                    ((TextView)neighborhoodSpinner.getSelectedView()).setError("Selecciona un barrio");
                    errors ++;
                }

                // Home Phone
                String phoneText = phone.getText().toString();
                if(phoneText.matches("")){
                    phone.setError("Ingresa un número de telefono fijo");
                    errors ++;
                }
                //Next Fragment Step two
                if(errors == 0) {
                    phone.setError(null);

                    //Country
                    posCountry = countrySpinner.getSelectedItemPosition();
                    if(posCountry != 0){
                        registerActivity.setCountry(countryCodes.get(posCountry).toString());
                    }
                    //Departament
                    posDepartament = departmentSpinner.getSelectedItemPosition();
                    if(posDepartament != 0){
                        registerActivity.setState(departmentCodes.get(posDepartament).toString());
                    }
                    //Town
                    posTown = townSpinner.getSelectedItemPosition();
                    if(posTown != 0){
                        registerActivity.setTown(townCodes.get(posTown).toString());
                    }
                    //Zone
                    posZone = zoneSpinner.getSelectedItemPosition();
                    if(posZone != 0){
                        registerActivity.setZone(zoneCodes.get(posZone).toString());
                    }
                    //Commune
                    posCommune = communeSpinner.getSelectedItemPosition();
                    if(posCommune != 0){
                        registerActivity.setCommune(communeCodes.get(posCommune).toString());
                    }
                    //Stratum
                    Integer posStratum = stratumSpinner.getSelectedItemPosition();
                    if(posStratum != 0){
                        registerActivity.setStratum(stratumCodes.get(posStratum).toString());
                    }
                    //Neighborhood
                    posNeighborhood = neighborhoodSpinner.getSelectedItemPosition();
                    if(posNeighborhood != 0){
                        registerActivity.setNeighborhood(neighborhoodCodes.get(posNeighborhood).toString());
                    }
                    //Phone One
                    registerActivity.setPhone(phoneText);
                    //Phone Two
                    EditText phoneTwo = (EditText) rootView.findViewById(R.id.fragment_register_two_phone_optional);
                    String phoneTwoText = phoneTwo.getText().toString();
                    if(!phoneTwoText.matches("")){
                        registerActivity.setPhoneOptinal(phoneTwoText);
                    }
                    //Celular
                    EditText celular = (EditText) rootView.findViewById(R.id.fragment_register_two_celphone);
                    String celularText = celular.getText().toString();
                    if(!celularText.matches("")){
                        registerActivity.setCelular(celularText);
                    }
                    //Address
                    if(!addressButton.getText().toString().equals("Direccion")) {
                        registerActivity.setAddress(addressButton.getText().toString());
                    }


                    registerActivity.pager.setCurrentItem(registerActivity.pager.getCurrentItem() + 1);

                }else{
                    Toast.makeText(getContext(), "Los campos marcados son obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                try{

                    for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                        getFragmentManager().popBackStack();
                    }

                }catch (Exception e){
                    Log.e("Error =>", e.toString());
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            case R.id.fragment_register_two_country:
                progress.setVisibility(View.VISIBLE);
                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimationCountry = (AnimationDrawable) progress.getBackground();
                frameAnimationCountry.start();

                codeCountry = 0;
                if(countrySpinner.getSelectedItemPosition() != 0){
                    codeCountry = (Integer) countryCodes.get(countrySpinner.getSelectedItemPosition());
                }
                adapterDepartment.clear();
                adapterDepartment.add("Departamento");
                adapterDepartment.notifyDataSetChanged();

                adapterTown.clear();
                adapterTown.add("Ciudad");
                adapterTown.notifyDataSetChanged();

                adapterZones.clear();
                adapterZones.add("Zona");
                adapterZones.notifyDataSetChanged();

                adapterCommunes.clear();
                adapterCommunes.add("Comuna");
                adapterCommunes.notifyDataSetChanged();

                adapterNeighborhood.clear();
                adapterNeighborhood.add("Barrio");
                adapterNeighborhood.notifyDataSetChanged();
                if(countrySpinner.getSelectedItemPosition() != 0){
                    HashMap paramsDepartament = new HashMap<>();
                    paramsDepartament.put("page", 0);
                    paramsDepartament.put("limit", -1);
                    paramsDepartament.put("country_id", codeCountry);
                    ApiStates apiStates = new ApiStates(this);
                    apiStates.getStates(paramsDepartament);
                }else{
                    progress.setBackgroundDrawable(null);
                    progress.setVisibility(View.GONE);
                }
                break;
            case R.id.fragment_register_two_department:
                progress.setVisibility(View.VISIBLE);
                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimationState = (AnimationDrawable) progress.getBackground();
                frameAnimationState.start();

                codeDepartment = 0;
                if(departmentSpinner.getSelectedItemPosition() != 0){
                    codeDepartment = (Integer) departmentCodes.get(departmentSpinner.getSelectedItemPosition());
                }
                adapterTown.clear();
                adapterTown.add("Ciudad");
                adapterTown.notifyDataSetChanged();

                adapterZones.clear();
                adapterZones.add("Zona");
                adapterZones.notifyDataSetChanged();

                adapterCommunes.clear();
                adapterCommunes.add("Comuna");
                adapterCommunes.notifyDataSetChanged();

                adapterNeighborhood.clear();
                adapterNeighborhood.add("Barrio");
                adapterNeighborhood.notifyDataSetChanged();
                if(departmentSpinner.getSelectedItemPosition() != 0){
                    HashMap paramsTown = new HashMap<>();
                    paramsTown.put("page", 0);
                    paramsTown.put("limit", -1);
                    paramsTown.put("country_id", codeCountry);
                    paramsTown.put("departamento_id", codeDepartment);
                    ApiTowns apiTowns = new ApiTowns(this);
                    apiTowns.getTowns(paramsTown);
                }else{
                    progress.setBackgroundDrawable(null);
                    progress.setVisibility(View.GONE);
                }
                break;
            case R.id.fragment_register_two_town:
                progress.setVisibility(View.VISIBLE);
                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimationTown = (AnimationDrawable) progress.getBackground();
                frameAnimationTown.start();

                codeTown = 0;
                if(townSpinner.getSelectedItemPosition() != 0){
                    codeTown = (Integer) townCodes.get(townSpinner.getSelectedItemPosition());
                }
                adapterZones.clear();
                adapterZones.add("Zona");
                adapterZones.notifyDataSetChanged();

                adapterCommunes.clear();
                adapterCommunes.add("Comuna");
                adapterCommunes.notifyDataSetChanged();

                adapterNeighborhood.clear();
                adapterNeighborhood.add("Barrio");
                adapterNeighborhood.notifyDataSetChanged();
                if(townSpinner.getSelectedItemPosition() != 0){
                    HashMap paramsZones = new HashMap<>();
                    paramsZones.put("page", 0);
                    paramsZones.put("limit", -1);
                    paramsZones.put("country_id", codeCountry);
                    paramsZones.put("departamento_id", codeDepartment);
                    paramsZones.put("town_id", codeTown);
                    ApiZones apiTowns = new ApiZones(this);
                    apiTowns.getZones(paramsZones);
                }else{
                    progress.setBackgroundDrawable(null);
                    progress.setVisibility(View.GONE);
                }
                break;
            case R.id.fragment_register_two_zone:

                progress.setVisibility(View.VISIBLE);
                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimationZone = (AnimationDrawable) progress.getBackground();
                frameAnimationZone.start();

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
                if(zoneSpinner.getSelectedItemPosition() != 0){
                    HashMap paramsCommune = new HashMap<>();
                    paramsCommune.put("page", 0);
                    paramsCommune.put("limit", -1);
                    paramsCommune.put("country_id", codeCountry);
                    paramsCommune.put("departamento_id", codeDepartment);
                    paramsCommune.put("town_id", codeTown);
                    paramsCommune.put("zone_id", codeZone);
                    ApiCommunes apiCommunes = new ApiCommunes(this);
                    apiCommunes.getCommunesByZone(paramsCommune);
                }else{
                    progress.setBackgroundDrawable(null);
                    progress.setVisibility(View.GONE);
                }

                break;
            case R.id.fragment_register_two_commune:

                progress.setVisibility(View.VISIBLE);
                progress.setBackgroundResource(R.drawable.progress_animation);
                AnimationDrawable frameAnimationCommune = (AnimationDrawable) progress.getBackground();
                frameAnimationCommune.start();

                codeCommune = 0;
                if(communeSpinner.getSelectedItemPosition() != 0){
                    codeCommune = (Integer) communeCodes.get(communeSpinner.getSelectedItemPosition());
                }
                adapterNeighborhood.clear();
                adapterNeighborhood.add("Barrio");
                adapterNeighborhood.notifyDataSetChanged();
                if(communeSpinner.getSelectedItemPosition() != 0) {
                    HashMap paramsNeighborhood = new HashMap<>();
                    paramsNeighborhood.put("page", 0);
                    paramsNeighborhood.put("limit", -1);
                    paramsNeighborhood.put("country_id", codeCountry);
                    paramsNeighborhood.put("departamento_id", codeDepartment);
                    paramsNeighborhood.put("town_id", codeTown);
                    paramsNeighborhood.put("zone_id", codeZone);
                    paramsNeighborhood.put("commune_id", codeCommune);
                    ApiNeighborhood apiNeighborhood = new ApiNeighborhood(this);
                    apiNeighborhood.getNeighborhoodsByCommunes(paramsNeighborhood);
                }else{
                    progress.setBackgroundDrawable(null);
                    progress.setVisibility(View.GONE);
                }
                break;
            case R.id.fragment_search_advanced_neighborhood:
                if(neighborhoodSpinner.getSelectedItemPosition() != 0){
                    codeNeighborhood = (Integer) neighborhoodCodes.get(neighborhoodSpinner.getSelectedItemPosition());
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    Integer selectedCountry;
    //    Start Methods Implements CountriesListener
    @Override
    public void onfinishedLoadCountries(ArrayList<Countries> countries) {
        int i = 1;
        selectedCountry = 0;
        adapterCountry.clear();
        adapterCountry.add("País");
        for (Countries countrie : countries) {
            String name =  (String) countrie.getNombre();
            adapterCountry.add(name);
            countryCodes.put(i,countrie.getId());
            countryNames.put(i,countrie.getNombre());
            if(countrie.getId() == 49){
                selectedCountry = i;
            }
            i++;
        }
        adapterCountry.add("Otro");
        countryCodes.put(i,0);
        countryNames.put(i,"Otro");

        adapterCountry.notifyDataSetChanged();
        countrySpinner.setOnItemSelectedListener(this);
        countrySpinner.setAdapter(adapterCountry);
        countrySpinner.setSelection(selectedCountry);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onErrorLoadCountries() {
        Toast.makeText(getContext(), "Error al cargar Países.",Toast.LENGTH_LONG).show();
    }

    //    End Methods Implements CountriesListener
    Integer selectedState;
    //    Start Methods Implements StatesListener
    @Override
    public void onfinishedLoadStates(ArrayList<States> states) {
        int i = 1;
        selectedState = 0;
        adapterDepartment.clear();
        adapterDepartment.add("Departamento");
        for (States state : states) {
            String name =  (String) state.getNombre();
            adapterDepartment.add(name);
            departmentCodes.put(i,state.getId());
            departmentNames.put(i,state.getNombre());
            if(state.getId() == 744 && countryCodes.get(selectedCountry) == 49){
                selectedState = i;
            }
            i++;
        }
        adapterDepartment.add("Otro");
        departmentCodes.put(i,0);
        departmentNames.put(i,"Otro");

        adapterDepartment.notifyDataSetChanged();
        departmentSpinner.setOnItemSelectedListener(this);
        departmentSpinner.setAdapter(adapterDepartment);
        departmentSpinner.setSelection(selectedState);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

    }

    @Override
    public void onErrorLoadStates() {
        Toast.makeText(getContext(), "Error al cargar Departamentos.",Toast.LENGTH_LONG).show();
    }

    //    End Methods Implements StatesListener
    Integer selectedTown;
    //    Start Methods Implements StatesListener
    @Override
    public void onfinishedLoadTowns(ArrayList<Towns> towns) {
        int i = 1;
        selectedTown = 0;
        adapterTown.clear();
        adapterTown.add("Ciudad");
        for (Towns town : towns) {
            String name =  (String) town.getNombre();
            adapterTown.add(name);
            townCodes.put(i,town.getId());
            townNames.put(i,town.getNombre());
            if(town.getId() == 73 && departmentCodes.get(selectedState) == 744){
                selectedTown = i;
            }
            i++;
        }
        adapterTown.add("Otro");
        townCodes.put(i,0);
        townNames.put(i,"Otro");
        adapterTown.notifyDataSetChanged();
        townSpinner.setOnItemSelectedListener(this);
        townSpinner.setAdapter(adapterTown);
        townSpinner.setSelection(selectedTown);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

    }

    @Override
    public void onErrorLoadTowns() {
        Toast.makeText(getContext(), "Error al cargar Ciudades.",Toast.LENGTH_LONG).show();
    }
    //    End Methods Implements StatesListener

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
            i++;
        }
        adapterZones.add("Otro");
        zoneCodes.put(i,0);
        zoneNames.put(i,"Otro");
        adapterZones.notifyDataSetChanged();
        zoneSpinner.setOnItemSelectedListener(this);
        zoneSpinner.setAdapter(adapterZones);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
        Log.d("Zonas por aqui","Si aqui son las zonas");

    }

    @Override
    public void onErrorLoadZones() {
        Toast.makeText(getContext(), "Error al cargar Zonas.",Toast.LENGTH_LONG).show();
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
            i++;
        }
        adapterCommunes.add("Otro");
        communeCodes.put(i,0);
        communeNames.put(i,"Otro");
        adapterCommunes.notifyDataSetChanged();
        communeSpinner.setOnItemSelectedListener(this);
        communeSpinner.setAdapter(adapterCommunes);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

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
            i++;
        }
        adapterNeighborhood.add("Otro");
        neighborhoodCodes.put(i,0);
        neighborhoodNames.put(i,"Otro");
        adapterNeighborhood.notifyDataSetChanged();
        neighborhoodSpinner.setOnItemSelectedListener(this);
        neighborhoodSpinner.setAdapter(adapterNeighborhood);
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onErrorLoadNeighborhood() {
        Toast.makeText(getContext(), "Error al cargar Barrios.",Toast.LENGTH_LONG).show();
    }
    /**   End Methods of Interface NeighborhoodListener **/

    /**   Start Methods of Interface SetAddressListener **/
    @Override
    public void setAddressButton(String address, Bundle paramsAddress) {
        addressButton.setText(address);
        paramsAddressDialog = paramsAddress;
    }
    /**   End Methods of Interface SetAddressListener **/

}
