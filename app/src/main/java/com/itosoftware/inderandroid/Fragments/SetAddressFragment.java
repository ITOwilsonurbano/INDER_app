package com.itosoftware.inderandroid.Fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itosoftware.inderandroid.Adapters.InderSpinnerAdapter;
import com.itosoftware.inderandroid.Interface.SetAddressListener;
import com.itosoftware.inderandroid.R;

import java.util.HashMap;


public class SetAddressFragment extends DialogFragment {

    View rootView;

    private SetAddressListener callback;

    //Properties Spinner Main Street Type
    protected Spinner mainStreetTypeSpinner;
    protected HashMap<Integer, String> mainStreetTypeCodes;
    protected HashMap<Integer, String> mainStreetTypeNames;

    //Properties Spinner Main Street Letter
    protected Spinner mainStreetLetterSpinner;
    protected HashMap<Integer, String> mainStreetLetterCodes;
    protected HashMap<Integer, String> mainStreetLetterNames;

    //Properties Spinner Main Street Coordinate
    protected Spinner mainStreetCoordinateSpinner;
    protected HashMap<Integer, String> mainStreetCoordinateCodes;
    protected HashMap<Integer, String> mainStreetCoordinateNames;

    //Properties Spinner Second Street Letter
    protected Spinner secondStreetLetterSpinner;
    protected HashMap<Integer, String> secondStreetLetterCodes;
    protected HashMap<Integer, String> secondStreetLetterNames;

    //Properties Spinner Second Street Coordinate
    protected Spinner secondStreetCoordinateSpinner;
    protected HashMap<Integer, String> secondStreetCoordinateCodes;
    protected HashMap<Integer, String> secondStreetCoordinateNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callback = (SetAddressListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement SetAddressListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_set_address, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Bundle bundle = getArguments();
        String bundleMainStreetType = "";
        String bundleNumbrerMainStreetText = "";
        String bundleMainStreetLetter = "";
        String bundleMainStreetCoordinate = "";
        String bundleNumbrerSecondStreetText = "";
        String bundleSecondStreetLetter = "";
        String bundleSecondStreetCoordinate = "";
        String bundleNumbrerHouseText = "";
        String bundleComplementText = "";
        if( bundle.containsKey("mainStreetType")){
            bundleMainStreetType = bundle.getString("mainStreetType");
        }
        if( bundle.containsKey("numberMainStreetText")){
            bundleNumbrerMainStreetText = bundle.getString("numberMainStreetText");
        }
        if( bundle.containsKey("mainStreetLetter")){
            bundleMainStreetLetter = bundle.getString("mainStreetLetter");
        }
        if( bundle.containsKey("mainStreetCoordinate")){
            bundleMainStreetCoordinate = bundle.getString("mainStreetCoordinate");
        }
        if( bundle.containsKey("numberSecondStreetText")){
            bundleNumbrerSecondStreetText = bundle.getString("numberSecondStreetText");
        }
        if( bundle.containsKey("secondStreetLetter")){
            bundleSecondStreetLetter = bundle.getString("secondStreetLetter");
        }
        if( bundle.containsKey("secondStreetCoordinate")){
            bundleSecondStreetCoordinate = bundle.getString("secondStreetCoordinate");
        }
        if( bundle.containsKey("numberHouseText")){
            bundleNumbrerHouseText = bundle.getString("numberHouseText");
        }
        if( bundle.containsKey("complementText")){
            bundleComplementText = bundle.getString("complementText");
        }

        //Close Keyword
        ScrollView scroll = (ScrollView) rootView.findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    return true;
                }
                return false;
            }
        });

        //Spinner Main Street
        int selectedItemMainStreet = 0;
        mainStreetTypeSpinner = (Spinner) rootView.findViewById(R.id.fragment_set_address_main_street);
        mainStreetTypeCodes = new HashMap<Integer, String>();
        mainStreetTypeNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterMainStreetType = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapterMainStreetType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMainStreetType.add("Tipo de calle");
        int position = 1;
        String[] mainStreetLocalCodes = getResources().getStringArray(R.array.main_street_codes);
        String[] mainStreetLocalNames = getResources().getStringArray(R.array.main_street_names);
        for (int i = 0; i <= mainStreetLocalNames.length - 1; i++) {
            mainStreetTypeCodes.put(position, mainStreetLocalCodes[i]);
            mainStreetTypeNames.put(position, mainStreetLocalNames[i]);
            adapterMainStreetType.add(mainStreetLocalNames[i]);
            if(mainStreetLocalCodes[i].equals(bundleMainStreetType)){
                selectedItemMainStreet = position;
            }
            position++;
        }
        mainStreetTypeSpinner.setAdapter(adapterMainStreetType);
        mainStreetTypeSpinner.setSelection(selectedItemMainStreet);

        //Spinner Main Street Letter
        int selectedItemMainStreetLetter = 0;
        mainStreetLetterSpinner = (Spinner) rootView.findViewById(R.id.fragment_set_address_letter_main_street);
        mainStreetLetterCodes = new HashMap<Integer, String>();
        mainStreetLetterNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterMainStreetLetter = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapterMainStreetLetter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMainStreetLetter.add("Letra calle principal");
        position = 1;
        String[] mainStreetLetterLocalNames = getResources().getStringArray(R.array.letter);
        for (int i = 0; i <= mainStreetLetterLocalNames.length - 1; i++) {
            mainStreetLetterCodes.put(position, mainStreetLetterLocalNames[i]);
            mainStreetLetterNames.put(position, mainStreetLetterLocalNames[i]);
            adapterMainStreetLetter.add(mainStreetLetterLocalNames[i]);
            if(mainStreetLetterLocalNames[i].equals(bundleMainStreetLetter)){
                selectedItemMainStreetLetter = position;
            }
            position++;
        }
        mainStreetLetterSpinner.setAdapter(adapterMainStreetLetter);
        mainStreetLetterSpinner.setSelection(selectedItemMainStreetLetter);

        //Spinner Main Street Coordinate
        int selectedItemMainStreetCoordinate = 0;
        mainStreetCoordinateSpinner = (Spinner) rootView.findViewById(R.id.fragment_set_address_coordinate_main_street);
        mainStreetCoordinateCodes = new HashMap<Integer, String>();
        mainStreetCoordinateNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterMainStreetCoordinate = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapterMainStreetCoordinate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMainStreetCoordinate.add("Coordenada calle principal");
        position = 1;
        String[] mainStreetCoordinatesLocalCodes = getResources().getStringArray(R.array.cordinate);
        for (int i = 0; i <= mainStreetCoordinatesLocalCodes.length - 1; i++) {
            mainStreetCoordinateCodes.put(position, mainStreetCoordinatesLocalCodes[i].toUpperCase());
            mainStreetCoordinateNames.put(position, mainStreetCoordinatesLocalCodes[i]);
            adapterMainStreetCoordinate.add(mainStreetCoordinatesLocalCodes[i]);
            if(mainStreetCoordinatesLocalCodes[i].equals(bundleMainStreetCoordinate)){
                selectedItemMainStreetCoordinate = position;
            }
            position++;
        }
        mainStreetCoordinateSpinner.setAdapter(adapterMainStreetCoordinate);
        mainStreetCoordinateSpinner.setSelection(selectedItemMainStreetCoordinate);

        //Spinner Second Street Letter
        int selectedItemSecondStreetLetter = 0;
        secondStreetLetterSpinner = (Spinner) rootView.findViewById(R.id.fragment_set_address_letter_second_street);
        secondStreetLetterCodes = new HashMap<Integer, String>();
        secondStreetLetterNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterSecondStreetLetter = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapterSecondStreetLetter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSecondStreetLetter.add("Letra calle secundaria");
        position = 1;
        String[]secondStreetLetterLocalNames = getResources().getStringArray(R.array.letter);
        for (int i = 0; i <= secondStreetLetterLocalNames.length - 1; i++) {
            secondStreetLetterCodes.put(position, secondStreetLetterLocalNames[i]);
            secondStreetLetterNames.put(position, secondStreetLetterLocalNames[i]);
            adapterSecondStreetLetter.add(secondStreetLetterLocalNames[i]);
            if(secondStreetLetterLocalNames[i].equals(bundleSecondStreetLetter)){
                selectedItemSecondStreetLetter = position;
            }
            position++;
        }
        secondStreetLetterSpinner.setAdapter(adapterSecondStreetLetter);
        secondStreetLetterSpinner.setSelection(selectedItemSecondStreetLetter);

        //Spinner Second Street Coordinate
        int selectedItemSecondStreetCoordinate = 0;
        secondStreetCoordinateSpinner = (Spinner) rootView.findViewById(R.id.fragment_set_address_coordinate_second_street);
        secondStreetCoordinateCodes = new HashMap<Integer, String>();
        secondStreetCoordinateNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterSecondStreetCoordinate = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapterSecondStreetCoordinate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSecondStreetCoordinate.add("Coordenada calle secundaria");
        position = 1;
        String[] secondStreetCoordinatesLocalNames = getResources().getStringArray(R.array.cordinate);
        for (int i = 0; i <= secondStreetCoordinatesLocalNames.length - 1; i++) {
            secondStreetCoordinateCodes.put(position, secondStreetCoordinatesLocalNames[i].toUpperCase());
            secondStreetCoordinateNames.put(position, secondStreetCoordinatesLocalNames[i]);
            adapterSecondStreetCoordinate.add(secondStreetCoordinatesLocalNames[i]);
            if(secondStreetCoordinatesLocalNames[i].equals(bundleSecondStreetCoordinate)){
                selectedItemMainStreetCoordinate = position;
            }
            position++;
        }
        secondStreetCoordinateSpinner.setAdapter(adapterSecondStreetCoordinate);
        secondStreetCoordinateSpinner.setSelection(selectedItemMainStreetCoordinate);

        final EditText numberMainStreet = (EditText) rootView.findViewById(R.id.fragment_set_address_number_street_main);
        numberMainStreet.setText(bundleNumbrerMainStreetText);

        final EditText numberSecondStreet = (EditText) rootView.findViewById(R.id.fragment_set_address_number_street_second);
        numberSecondStreet.setText(bundleNumbrerSecondStreetText);

        final EditText numberHouse = (EditText) rootView.findViewById(R.id.fragment_set_address_home_number);
        numberHouse.setText(bundleNumbrerHouseText);

        final EditText complement = (EditText) rootView.findViewById(R.id.fragment_set_address_complement);
        complement.setText(bundleComplementText);


        Button close = (Button) rootView.findViewById(R.id.fragment_set_address_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        Button cancel = (Button) rootView.findViewById(R.id.fragment_set_address_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        Button done = (Button) rootView.findViewById(R.id.fragment_set_address_button_done);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                int errors = 0;

                //Main Street Type
                Integer posMainStreetType = mainStreetTypeSpinner.getSelectedItemPosition();
                if(posMainStreetType == 0){
                    ((TextView)mainStreetTypeSpinner.getSelectedView()).setError("Selecciona un tipo de calle principal");
                    errors ++;
                }

                //Number Main Street
                String numberMainStreetText = numberMainStreet.getText().toString();
                if(numberMainStreetText.matches("")){
                    numberMainStreet.setError("Escriba un número de calle principal");
                    errors ++;
                }

                //Number Second Street
                String numberSecondStreetText = numberSecondStreet.getText().toString();
                if(numberSecondStreetText.matches("")){
                    numberSecondStreet.setError("Escriba un número de calle secundaria");
                    errors ++;
                }

                //Number House
                String numberHouseText = numberHouse.getText().toString();
                if(numberHouseText.matches("")){
                    numberHouse.setError("Escriba un número de casa");
                    errors ++;
                }

                if(errors == 0){
                    String mainStreetType = mainStreetTypeCodes.get(posMainStreetType).toString();

                    Integer posMainStreetLetter = mainStreetLetterSpinner.getSelectedItemPosition();
                    String mainStreetLetter = "";
                    if(posMainStreetLetter != 0){
                        mainStreetLetter = mainStreetLetterCodes.get(posMainStreetLetter).toString();
                    }

                    Integer posMainStreetCoordinate = mainStreetCoordinateSpinner.getSelectedItemPosition();
                    String mainStreetCoordinate = "";
                    if(posMainStreetCoordinate != 0){
                        mainStreetCoordinate = mainStreetCoordinateCodes.get(posMainStreetCoordinate).toString();
                    }

                    Integer posSecondStreetLetter = secondStreetLetterSpinner.getSelectedItemPosition();
                    String secondStreetLetter = "";
                    if(posSecondStreetLetter != 0){
                        secondStreetLetter = secondStreetLetterCodes.get(posSecondStreetLetter).toString();
                    }

                    Integer posSecondStreetCoordinate = secondStreetCoordinateSpinner.getSelectedItemPosition();
                    String secondStreetCoordinate = "";
                    if(posSecondStreetCoordinate != 0){
                        secondStreetCoordinate = secondStreetCoordinateCodes.get(posSecondStreetCoordinate).toString();
                    }

                    String complementText = complement.getText().toString();

                    String address = mainStreetType + " " + numberMainStreetText +  " " + mainStreetLetter + " " + mainStreetCoordinate + " # "+ numberSecondStreetText + " "+secondStreetLetter + " " + secondStreetCoordinate +" - " + numberHouseText + "  " + complementText;

                    Bundle paramsAddress = new Bundle();

                    paramsAddress.putString("mainStreetType",mainStreetType);
                    paramsAddress.putString("numberMainStreetText", numberMainStreetText);
                    paramsAddress.putString("mainStreetLetter", mainStreetLetter);
                    paramsAddress.putString("mainStreetCoordinate", mainStreetCoordinate);
                    paramsAddress.putString("numberSecondStreetText", numberSecondStreetText);
                    paramsAddress.putString("secondStreetLetter", secondStreetLetter);
                    paramsAddress.putString("secondStreetCoordinate", secondStreetCoordinate);
                    paramsAddress.putString("numberHouseText", numberHouseText);
                    paramsAddress.putString("complementText", complementText);


                    callback.setAddressButton(address, paramsAddress);

                    dismiss();
                }else{
                    Toast.makeText(getContext(), "Los campos marcados son obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Do something else
        return rootView;
    }
}