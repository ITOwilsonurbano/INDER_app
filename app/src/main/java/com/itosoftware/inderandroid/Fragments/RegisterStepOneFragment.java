package com.itosoftware.inderandroid.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.text.Layout;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Activities.ForgetPasswordActivity;
import com.itosoftware.inderandroid.Activities.RegisterActivity;
import com.itosoftware.inderandroid.Adapters.InderSpinnerAdapter;
import com.itosoftware.inderandroid.Api.DocumentType.ApiDocumentTypes;
import com.itosoftware.inderandroid.Api.DocumentType.DocumentTypes;
import com.itosoftware.inderandroid.Api.Etnias.ApiEtnias;
import com.itosoftware.inderandroid.Api.Etnias.Etnias;
import com.itosoftware.inderandroid.Api.SubtypeHandicapped.ApiSubtypeHandicapped;
import com.itosoftware.inderandroid.Api.SubtypeHandicapped.SubtypeHandicapped;
import com.itosoftware.inderandroid.Api.TypeHandicapped.ApiTypeHandicapped;
import com.itosoftware.inderandroid.Api.TypeHandicapped.TypeHandicapped;
import com.itosoftware.inderandroid.Interface.DocumentTypesListener;
import com.itosoftware.inderandroid.Interface.EtniasListener;
import com.itosoftware.inderandroid.Interface.SubtypeHandicappedListener;
import com.itosoftware.inderandroid.Interface.TypeHandicappedListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.graphicstest.FontCheckbox;
import com.itosoftware.inderandroid.graphicstest.MultiSpinner;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class RegisterStepOneFragment extends Fragment implements AdapterView.OnItemSelectedListener, DocumentTypesListener, EtniasListener, TypeHandicappedListener, SubtypeHandicappedListener, MultiSpinner.MultiSpinnerListener {

    View rootView;

    RegisterActivity registerActivity;
    ActionBar actionBar;

    //Properties Spinner Document Type
    public Spinner documentTypeSpinner;
    public HashMap<Integer, Integer> documentTypeCodes;
    public HashMap<Integer, String> documentTypeNames;
    public InderSpinnerAdapter adapterDocumentType;

    //Properties Spinner Sex
    public Spinner sexSpinner;
    public HashMap<Integer, String> sexCodes;
    public HashMap<Integer, String> sexNames;

    //Properties Spinner Etnia
    public Spinner etniaSpinner;
    public HashMap<Integer, Integer> etniaCodes;
    public HashMap<Integer, String> etniaNames;
    public InderSpinnerAdapter adapterEtnia;

    //Properties Spinner Displaced
    public Spinner displacedSpinner;
    public HashMap<Integer, Boolean> displacedCodes;
    public HashMap<Integer, String> displacedNames;

    //Checkbox Declarant
    public FontCheckbox declarantCheckbox;

    //Checkbox Recognition
    public FontCheckbox recognitionCheckbox;

    //Properties Spinner Handicapped
    public Spinner handicappedSpinner;
    public HashMap<Integer, Boolean> handicappedCodes;
    public HashMap<Integer, String> handicappedNames;

    //Properties Spinner Handicapped Type
    public MultiSpinner handicappedTypeMultiSpinner;
    public boolean[] selectedhandicappedTypeMultiSpinner;
    public ArrayList<HashMap> handicappedTypeSelected;
    public HashMap<Integer, Integer> handicappedTypeCodes;
    public HashMap<Integer, String> handicappedTypeNames;

    //Properties Spinner Handicapped Subtype
    public MultiSpinner handicappedSubtypeMultiSpinner;
    public boolean[] selectedhandicappedSubtypeMultiSpinner;
    public ArrayList<HashMap> handicappedSubTypeSelected;
    public HashMap<Integer, Integer> handicappedSubtypeCodes;
    public HashMap<Integer, String> handicappedSubtypeNames;

    //Properties Spinner Boss Head Home
    public Spinner bossHeadHomeSpinner;
    public HashMap<Integer, Boolean> bossHeadHomeCodes;
    public HashMap<Integer, String> bossHeadHomeNames;

    //Properties EditText
    public EditText numDocument;
    public EditText firstName;
    public EditText firstLastName ;

    Button btnBornDate;

    ImageView progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_step_one, container, false);
        registerActivity = (RegisterActivity) getActivity();

        ScrollView scroll = (ScrollView) rootView.findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    try{
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(registerActivity.getCurrentFocus().getWindowToken(), 0);
                        registerActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        return true;
                    }catch (Exception e ){
                        Log.e("Error Scroll",e.getMessage());
                    }
                }
                return false;
            }
        });

        progress = (ImageView) rootView.findViewById(R.id.fragment_register_one_progress_bar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        //Spinner Document Type
        HashMap paramsDocumentType = new HashMap<>();
        paramsDocumentType.put("page", 0);
        paramsDocumentType.put("limit", -1);
        ApiDocumentTypes apiDocumentTypes = new ApiDocumentTypes(this);
        apiDocumentTypes.getDocumentTypes(paramsDocumentType);
        documentTypeSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_one_document_type);
        documentTypeCodes = new HashMap<Integer, Integer>();
        documentTypeNames = new HashMap<Integer, String>();
        adapterDocumentType = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterDocumentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDocumentType.add("Tipo de Documento");
        documentTypeSpinner.setAdapter(adapterDocumentType);

        //Spinner Sex
        sexSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_one_sex);
        sexCodes = new HashMap<Integer, String>();
        sexNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterSex = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSex.add("Sexo");
        sexCodes.put(1, "M");
        sexNames.put(1, "Masculino");
        adapterSex.add("Masculino");
        sexCodes.put(2, "F");
        sexNames.put(2, "Femenino");
        adapterSex.add("Femenino");
        sexSpinner.setAdapter(adapterSex);
        sexSpinner.setOnItemSelectedListener(this);

        //Spinner Etnia
        HashMap paramsEtnia = new HashMap<>();
        paramsEtnia.put("page", 0);
        paramsEtnia.put("limit", -1);
        ApiEtnias apiEtnias = new ApiEtnias(this);
        apiEtnias.getEtnias(paramsEtnia);
        etniaSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_one_etnia);
        etniaCodes = new HashMap<Integer, Integer>();
        etniaNames = new HashMap<Integer, String>();
        adapterEtnia = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterEtnia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterEtnia.add("Etnia");
        etniaSpinner.setAdapter(adapterEtnia);

        //Spinner Displaced
        displacedSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_one_displaced);
        displacedCodes = new HashMap<Integer, Boolean>();
        displacedNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterDisplaced = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterDisplaced.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDisplaced.add("Desplazado");
        displacedCodes.put(1, true);
        displacedNames.put(1, "Si");
        adapterDisplaced.add("Si");
        displacedCodes.put(2, false);
        displacedNames.put(2, "No");
        adapterDisplaced.add("No");
        displacedSpinner.setAdapter(adapterDisplaced);
        displacedSpinner.setOnItemSelectedListener(this);

        //Checkbox Declarant
        declarantCheckbox = (FontCheckbox) rootView.findViewById(R.id.fragment_register_one_declarant);

        //Checkbox Recognition
        recognitionCheckbox = (FontCheckbox) rootView.findViewById(R.id.fragment_register_one_recognition);

        //Spinner Handicapped
        handicappedSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_one_handicapped);
        handicappedCodes = new HashMap<Integer, Boolean>();
        handicappedNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterHandicapped = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterHandicapped.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterHandicapped.add("Discapacitado");
        handicappedCodes.put(1, true);
        handicappedNames.put(1, "Si");
        adapterHandicapped.add("Si");
        handicappedCodes.put(2, false);
        handicappedNames.put(2, "No");
        adapterHandicapped.add("No");
        handicappedSpinner.setAdapter(adapterHandicapped);
        handicappedSpinner.setOnItemSelectedListener(this);

        //Spinner Handicapped Type
        HashMap paramsHandicappedType = new HashMap<>();
        paramsHandicappedType.put("page", 0);
        paramsHandicappedType.put("limit", -1);
        ApiTypeHandicapped apiTypeHandicapped = new ApiTypeHandicapped(this);
        apiTypeHandicapped.getTypesHandicapped(paramsHandicappedType);
        handicappedTypeMultiSpinner = (MultiSpinner) rootView.findViewById(R.id.fragment_register_one_type_handicapped);
        handicappedTypeMultiSpinner.setNombreMultiSpinner("HandicappedType");
        handicappedTypeCodes = new HashMap<Integer, Integer>();
        handicappedTypeNames = new HashMap<Integer, String>();
        handicappedTypeSelected = new ArrayList<HashMap>();

        //Spinner Handicapped Subtype
        HashMap paramsHandicappedSubtype = new HashMap<>();
        paramsHandicappedSubtype.put("page", 0);
        paramsHandicappedSubtype.put("limit", -1);
        ApiSubtypeHandicapped apiSubtypeHandicapped = new ApiSubtypeHandicapped(this);
        apiSubtypeHandicapped.getSubtypesHandicapped(paramsHandicappedSubtype);
        handicappedSubtypeMultiSpinner = (MultiSpinner) rootView.findViewById(R.id.fragment_register_one_subtype_handicapped);
        handicappedSubtypeMultiSpinner.setNombreMultiSpinner("HandicappedSubtype");
        handicappedSubtypeCodes = new HashMap<Integer, Integer>();
        handicappedSubtypeNames = new HashMap<Integer, String>();
        handicappedSubTypeSelected = new ArrayList<HashMap>();

        //Spinner Handicapped
        bossHeadHomeSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_one_boss_head_home);
        bossHeadHomeCodes = new HashMap<Integer, Boolean>();
        bossHeadHomeNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterBossHeadHome = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterBossHeadHome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterBossHeadHome.add("Jefe Cabeza de Hogar");
        bossHeadHomeCodes.put(1, true);
        bossHeadHomeNames.put(1, "Si");
        adapterBossHeadHome.add("Si");
        bossHeadHomeCodes.put(2, false);
        bossHeadHomeNames.put(2, "No");
        adapterBossHeadHome.add("No");
        bossHeadHomeSpinner.setAdapter(adapterBossHeadHome);
        bossHeadHomeSpinner.setOnItemSelectedListener(this);

        //Document Number
        numDocument = (EditText) rootView.findViewById(R.id.fragment_register_one_num_document);

        //First Name One
        firstName = (EditText) rootView.findViewById(R.id.fragment_register_one_name_one);

        //First Last Name One
        firstLastName = (EditText) rootView.findViewById(R.id.fragment_register_one_last_name_one);

        //Button date
        btnBornDate = (Button) rootView.findViewById(R.id.fragment_register_one_born_date);

        Button next = (Button) rootView.findViewById(R.id.fragment_register_one_button_next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                int errors = 0;

                // Document type
                Integer posTypeDocument = documentTypeSpinner.getSelectedItemPosition();
                if(posTypeDocument == 0){
                    ((TextView)documentTypeSpinner.getSelectedView()).setError("Selecciona un tipo de documento");
                    errors ++;
                }
                //Document number
                String numDocumentText = numDocument.getText().toString();
                if(numDocumentText.matches("")){
                    numDocument.setError("Ingresa tu numero de documento");
                    errors ++;
                }
                //First Name One
                String firstNameText = firstName.getText().toString();
                if(firstNameText.matches("")){
                    firstName.setError("Ingresa tu primer nombre");
                    errors ++;
                }
                //First Last Name One
                String firstLastNameText = firstLastName.getText().toString();
                if(firstLastNameText.matches("")){
                    firstLastName.setError("Ingresa tu primer apellido");
                    errors ++;
                }
                // Sex
                Integer posSex = sexSpinner.getSelectedItemPosition();
                if(posSex == 0){
                    ((TextView)sexSpinner.getSelectedView()).setError("Selecciona un sexo");
                    errors ++;
                }

                //Date
                String btnBornDateText = btnBornDate.getText().toString();
                if(btnBornDateText.equals("Fecha de Nacimiento")){
                    btnBornDate.setBackgroundResource(R.drawable.rounded_error);
                    errors ++;
                }else{
                    btnBornDate.setBackgroundResource(R.drawable.rounded_verde);
                }
                //Next Fragment Step two
                if(errors == 0) {
                    firstLastName.setError(null);
                    firstName.setError(null);
                    numDocument.setError(null);

                    // Document type
                    registerActivity.setDocumentType(documentTypeCodes.get(posTypeDocument).toString());
                    //Document number
                    registerActivity.setNumDocument(numDocumentText);
                    //First Name One
                    registerActivity.setFirstNameOne(firstNameText);
                    //First Name Two
                    EditText firstNameTwo = (EditText) rootView.findViewById(R.id.fragment_register_one_name_two);
                    String firstNameTwoText = firstNameTwo.getText().toString();
                    if(!firstNameTwoText.matches("")){
                        registerActivity.setFirstNameTwo(firstNameText);
                    }
                    //First Last Name One
                    registerActivity.setFirstLastNameOne(firstLastNameText);
                    //First Last Name Two
                    EditText firstLastNameTwo = (EditText) rootView.findViewById(R.id.fragment_register_one_last_name_two);
                    String firstLastNameTwoText = firstLastNameTwo.getText().toString();
                    if(!firstNameTwoText.matches("")){
                        registerActivity.setFirstLastNameTwo(firstLastNameTwoText);
                    }
                    //Born Date
                    btnBornDate = (Button) rootView.findViewById(R.id.fragment_register_one_born_date);
                    btnBornDateText = btnBornDate.getText().toString();
                    if(!btnBornDateText.matches("") && !btnBornDateText.matches("Fecha de Nacimiento")){
                        registerActivity.setBornDate(btnBornDateText);
                    }
                    // Sex
                    posSex = sexSpinner.getSelectedItemPosition();
                    if(posSex != 0){
                        registerActivity.setSex(sexCodes.get(posSex));
                    }
                    // Etnia
                    Integer posEtnia = etniaSpinner.getSelectedItemPosition();
                    if(posEtnia != 0){
                        registerActivity.setEtnia(etniaCodes.get(posSex).toString());
                    }
                    // Displaced
                    Integer posDisplaced = displacedSpinner.getSelectedItemPosition();
                    if(posDisplaced != 0){
                        registerActivity.setDisplaced(displacedCodes.get(posDisplaced));
                    }
                    // Declarant
                    Boolean isDeclarant =  declarantCheckbox.isChecked();
                    if(isDeclarant){
                        registerActivity.setDeclarant(isDeclarant);
                    }
                    // Recognition
                    Boolean isRecognition =  recognitionCheckbox.isChecked();
                    if(isRecognition){
                        registerActivity.setRecognition(isRecognition);
                    }
                    // Handicapped
                    Integer posHandicapped = handicappedSpinner.getSelectedItemPosition();
                    if(posHandicapped != 0){
                        registerActivity.setHandicapped(handicappedCodes.get(posHandicapped));
                    }
                    else if(posHandicapped == 0){ // Discapacitado
                        handicappedTypeSelected.clear();
                        handicappedSubTypeSelected.clear();
                    }
                    // Handicapped Type
                    if(handicappedTypeSelected.size() != 0){
                        Gson gson = new Gson();
                        String json = gson.toJson(handicappedTypeSelected);
                        registerActivity.setHandicappedType(json);
                        Log.i("TipoDiscapacidad", registerActivity.getHandicappedType());
                    }
                    // Handicapped Subtype
                    if(handicappedSubTypeSelected.size() != 0){
                        Gson gson = new Gson();
                        String json = gson.toJson(handicappedSubTypeSelected);
                        registerActivity.setHandicappedSubtype(json);
                        Log.i("SubTipoDiscapacidad", registerActivity.getHandicappedSubtype());
                    }
                    // Boos Head Family
                    Integer posBoosHeadFamily = bossHeadHomeSpinner.getSelectedItemPosition();
                    if(posBoosHeadFamily != 0){
                        registerActivity.setBoosHeadFamily(bossHeadHomeCodes.get(posBoosHeadFamily));
                    }


                    ((TextView)documentTypeSpinner.getSelectedView()).setError(null);
                    registerActivity.pager.setCurrentItem(registerActivity.pager.getCurrentItem() + 1);
                }else{
                    Toast.makeText(getContext(),"Los campos marcados son obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("Click up","Register One");
                try{
                    getActivity().finish();

                }catch (Exception e){
                    Log.e("Error =>", e.toString());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout rowTwelve =(LinearLayout)rootView.findViewById(R.id.fragment_register_one_row_twelve);
        LinearLayout rowFourteen =(LinearLayout)rootView.findViewById(R.id.fragment_register_one_row_fourteen);
        LinearLayout rowFiveteen =(LinearLayout)rootView.findViewById(R.id.fragment_register_one_row_fiveteen);

        Integer pos;
        switch (parent.getId()) {
            case R.id.fragment_register_one_displaced:
                pos = (Integer) position;
                if(pos == 1){
                    rowTwelve.setVisibility(LinearLayout.VISIBLE);
                }else{
                    rowTwelve.setVisibility(LinearLayout.GONE);
                }
                break;
            case R.id.fragment_register_one_handicapped:
                pos = (Integer) position;
                if(pos == 0){ // Discapacitado
                    handicappedTypeSelected.clear();
                    handicappedSubTypeSelected.clear();
                }
                if(pos == 1){ // Si
                    rowFourteen.setVisibility(View.VISIBLE);
                    rowFiveteen.setVisibility(View.VISIBLE);
                    handicappedTypeMultiSpinner.reinitItems();
                    handicappedSubtypeMultiSpinner.reinitItems();
                }else{ // No
                    rowFourteen.setVisibility(View.GONE);
                    rowFiveteen.setVisibility(View.GONE);
                    handicappedTypeSelected.clear();
                    handicappedSubTypeSelected.clear();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setBornDate(String date){
        Button btnBornDate = (Button) rootView.findViewById(R.id.fragment_register_one_born_date);
        btnBornDate.setText(date);
    }

    //    Start Methods Implements DocumentTypesListener
    @Override
    public void onfinishedLoadDocumentTypes(ArrayList<DocumentTypes> documentTypes) {
        int i = 1;
        Integer selectedItem = 0;
        adapterDocumentType.clear();
        adapterDocumentType.add("Tipo de documento");
        for (DocumentTypes documentType : documentTypes) {
            String name =  (String) documentType.getNombre();
            adapterDocumentType.add(name);
            documentTypeCodes.put(i, documentType.getId());
            documentTypeNames.put(i, documentType.getNombre());
            i++;
        }
        adapterDocumentType.notifyDataSetChanged();
        documentTypeSpinner.setOnItemSelectedListener(this);
        documentTypeSpinner.setAdapter(adapterDocumentType);
    }

    @Override
    public void onErrorLoadDocumentTypes() {
        Toast.makeText(getContext(), "Error al cargar Tipos de documento.",Toast.LENGTH_LONG).show();
    }
    //    End Methods Implements DocumentTypesListener

    //    Start Methods Implements EtniasListener
    @Override
    public void onfinishedLoadEtnias(ArrayList<Etnias> etnias) {
        int i = 1;
        Integer selectedItem = 0;
        adapterEtnia.clear();
        adapterEtnia.add("Etnia");
        for (Etnias etnia : etnias) {
            String name =  (String) etnia.getNombre();
            adapterEtnia.add(name);
            etniaCodes.put(i,etnia.getId());
            etniaNames.put(i,etnia.getNombre());
            i++;
        }
        adapterEtnia.notifyDataSetChanged();
        etniaSpinner.setOnItemSelectedListener(this);
        etniaSpinner.setAdapter(adapterEtnia);
    }

    @Override
    public void onErrorLoadEtnias() {
        Toast.makeText(getContext(), "Error al cargar Etnias.",Toast.LENGTH_LONG).show();
    }
    //    End Methods Implements EtniasListener

    //    Start Methods Implements TypeHandicappedListener
    @Override
    public void onfinishedLoadTypeHandicapped(ArrayList<TypeHandicapped> typesHandicapped) {
        int i = 1;
        ArrayList<String> items = new ArrayList<String>();
        for (TypeHandicapped typeHandicapped : typesHandicapped) {
            String name =  (String) typeHandicapped.getNombre();
            items.add(name);
            handicappedTypeCodes.put(i,typeHandicapped.getId());
            handicappedTypeNames.put(i,typeHandicapped.getNombre());
            i++;
        }
        handicappedTypeMultiSpinner.setItems(items,this);

        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onErrorLoadTypeHandicapped() {
        Toast.makeText(getContext(), "Error al cargar Tipos de Discapacidad.",Toast.LENGTH_LONG).show();
    }
    //    End Methods Implements TypeHandicappedListener


    //    Start Methods Implements SubtypeHandicappedListener
    @Override
    public void onfinishedLoadSubtypeHandicapped(ArrayList<SubtypeHandicapped> subtypesHandicapped) {
        int i = 1;
        ArrayList<String> items = new ArrayList<String>();
        for (SubtypeHandicapped subtypeHandicapped : subtypesHandicapped) {
            String name =  (String) subtypeHandicapped.getNombre();
            items.add(name);
            handicappedSubtypeCodes.put(i,subtypeHandicapped.getId());
            handicappedSubtypeNames.put(i,subtypeHandicapped.getNombre());
            i++;
        }
        handicappedSubtypeMultiSpinner.setItems(items,this);

        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onErrorLoadSubtypeHandicapped() {
        Toast.makeText(getContext(), "Error al cargar Subtipos de Discapacidad.",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemsSelectedMultiSpinner(boolean[] selected, String nombreMultiSpinner) {
        switch (nombreMultiSpinner){
            case "HandicappedType":
                selectedhandicappedTypeMultiSpinner = selected;
                handicappedTypeSelected.clear();
                for(int i=0;i<selectedhandicappedTypeMultiSpinner.length;i++){
                    if (selectedhandicappedTypeMultiSpinner[i] == true) {
                        HashMap id = new HashMap();
                        id.put("id",handicappedTypeCodes.get(i + 1).toString());
                        handicappedTypeSelected.add(id);
                    }
                }
                break;
            case "HandicappedSubtype":
                selectedhandicappedSubtypeMultiSpinner = selected;
                handicappedSubTypeSelected.clear();
                for(int i=0;i<selectedhandicappedSubtypeMultiSpinner.length;i++){
                    if (selectedhandicappedSubtypeMultiSpinner[i] == true) {
                        HashMap id = new HashMap();
                        id.put("id",handicappedSubtypeCodes.get(i + 1).toString());
                        handicappedSubTypeSelected.add(id);
                    }
                }
                break;
        }

    }
    //    End Methods Implements SubtypeHandicappedListener

}

