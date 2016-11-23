
package com.itosoftware.inderandroid.Fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.R;

import java.io.Console;
import java.util.HashMap;


public class RegisterStepFourFragment extends Fragment  implements AdapterView.OnItemSelectedListener{
    View rootView;

    View rootViewOne;
    View rootViewTwo;
    View rootViewThree;

    RegisterActivity registerActivity;

    //Properties Spinner Newsletters
    protected Spinner newslettersSpinner;
    protected HashMap<Integer, Integer> newslettersCodes;
    protected HashMap<Integer, String> newslettersNames;

    public ImageView progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register_step_four, container, false);
        rootViewOne = inflater.inflate(R.layout.fragment_register_step_one, container, false);
        rootViewTwo = inflater.inflate(R.layout.fragment_register_step_two, container, false);
        rootViewThree = inflater.inflate(R.layout.fragment_register_step_three, container, false);

        registerActivity = (RegisterActivity) getActivity();

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

        progress = (ImageView) rootView.findViewById(R.id.fragment_register_four_progress_bar);
        progress.setVisibility(View.GONE);

        //Spinner Newsletters
        newslettersSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_four_newsletters);
        newslettersCodes = new HashMap<Integer, Integer>();
        newslettersNames = new HashMap<Integer, String>();
        InderSpinnerAdapter adapterNewsletters = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
        adapterNewsletters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterNewsletters.add("Recibir Boletines Inder");
        int position = 1;
        String[] newsletters = getResources().getStringArray(R.array.spinner_yes_no);
        for (int i = 0; i <= newsletters.length - 1; i++) {
            newslettersCodes.put(position, position);
            newslettersNames.put(position, newsletters[i]);
            adapterNewsletters.add(newsletters[i]);
            position++;
        }
        newslettersSpinner.setAdapter(adapterNewsletters);
        newslettersSpinner.setOnItemSelectedListener(this);

        Button buttonTermsConditions = (Button) rootView.findViewById(R.id.fragment_register_four_button_terms_conditions);
        buttonTermsConditions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TermsConditionsFragment dFragment = new TermsConditionsFragment();
                // Show DialogFragment
                dFragment.show(fm, "Terminos y Condiciones ");
            }
        });


        Button next = (Button ) rootView.findViewById(R.id.fragment_register_four_button_next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                int errors = 0;

                progress.setBackgroundResource(R.drawable.progress_animation);
                progress.setVisibility(View.VISIBLE);
                AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
                frameAnimation.start();

                EditText username = (EditText) rootView.findViewById(R.id.fragment_register_four_username);
                EditText password = (EditText) rootView.findViewById(R.id.fragment_register_four_password);
                EditText repeatPassword = (EditText) rootView.findViewById(R.id.fragment_register_four_repeat_password);
                EditText email = (EditText) rootView.findViewById(R.id.fragment_register_four_email);
                CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.fragment_register_four_terms_conditions);

                username.setError(null);
                password.setError(null);
                repeatPassword.setError(null);
                email.setError(null);
                checkBox.setError(null);

                // Validate Username
                String usernameText = username.getText().toString();
                if(usernameText.matches("")){
                    username.setError("Ingresa un nombre de usuario");
                    errors ++;
                }
                else{
                    registerActivity.setUsername(usernameText);
                }
                // Validate password
                String passwordText = password.getText().toString();
                if(passwordText.matches("")){
                    password.setError("Ingresa una contraseña");
                    errors ++;
                }
                // Validate repeat password
                String repeatPasswordText = repeatPassword.getText().toString();
                if(repeatPasswordText.matches("")){
                    repeatPassword.setError("Repite la contraseña");
                    errors ++;
                }else{
                    if(!repeatPassword.getText().toString().equals(password .getText().toString())){
                        repeatPassword.setError("Las Contraseñas no coinciden");
                        errors ++;
                    }
                    else{
                        registerActivity.setPassword(passwordText);
                    }
                }

                // Validate Email
                String emailText = email.getText().toString();
                if(emailText.matches("")){
                    email.setError("Ingresa un correo.");
                    errors ++;
                }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    email.setError("Ingresa un correo valido");
                    errors ++;
                }
                else{
                    registerActivity.setMail(emailText);
                }

                // Validate checkbox
                if(!checkBox.isChecked()){
                    checkBox.setError("Acepte los términos y condiciones");
                    errors ++;
                }

                //Register
                if(errors == 0) {
                    HashMap parameters = new HashMap();

                    Integer errorsOthers = 0;
                    Integer pageErrorsOthers = 3;

                    //Get Data Step One
                    if(registerActivity.getDocumentType() != null){
                        parameters.put("tipoDocumento",registerActivity.getDocumentType().toString());
                        Log.i("tipoDocumento",registerActivity.getDocumentType()+"");
                    }else{
                        ((TextView)registerActivity.registerStepOneFragment.documentTypeSpinner.getSelectedView()).setError("Selecciona un tipo de documento");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 0;
                        }
                    }
                    if(registerActivity.getNumDocument() != null && registerActivity.getNumDocument() != "") {
                        parameters.put("Id", registerActivity.getNumDocument().toString());
                        Log.i("Id", registerActivity.getNumDocument() + "");
                    }else{
                        registerActivity.registerStepOneFragment.numDocument.setError("Ingresa tu primer nombre");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 0;
                        }
                    }
                    if(registerActivity.getFirstNameOne() != null) {
                        parameters.put("primerNombre", registerActivity.getFirstNameOne().toString());
                        Log.i("primerNombre", registerActivity.getFirstNameOne() + "");
                    }else{
                        registerActivity.registerStepOneFragment.firstName.setError("Ingresa tu primer nombre");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 0;
                        }
                    }
                    if(registerActivity.getFirstNameTwo() != null) {
                        parameters.put("segundoNombre", registerActivity.getFirstNameTwo().toString());
                        Log.i("segundoNombre", registerActivity.getFirstNameTwo() + "");
                    }
                    if(registerActivity.getFirstLastNameOne() != null) {
                        parameters.put("primerApellido", registerActivity.getFirstLastNameOne().toString());
                        Log.i("primerApellido", registerActivity.getFirstLastNameOne() + "");
                    }else{
                        registerActivity.registerStepOneFragment.firstLastName.setError("Ingresa tu primer apellido ");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 0;
                        }
                    }
                    if(registerActivity.getFirstLastNameTwo() != null) {
                        parameters.put("segundoApellido", registerActivity.getFirstLastNameTwo().toString());
                        Log.i("segundoApellido", registerActivity.getFirstLastNameTwo() + "");
                    }
                    if(registerActivity.getBornDate() != null) {
                        parameters.put("fechaNacimiento", registerActivity.getBornDate().toString());
                        Log.i("fechaNacimiento", registerActivity.getBornDate() + "");
                    }else{
                        registerActivity.registerStepOneFragment.btnBornDate.setBackgroundResource(R.drawable.rounded_error);
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 0;
                        }
                    }
                    if(registerActivity.getSex() != null) {
                        parameters.put("genero", registerActivity.getSex().toString());
                        Log.i("genero", registerActivity.getSex() + "");
                    }else{
                        ((TextView)registerActivity.registerStepOneFragment.sexSpinner.getSelectedView()).setError("Selecciona un sexo");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 0;
                        }
                    }
                    if(registerActivity.getEtnia() != null) {
                        parameters.put("etnia", registerActivity.getEtnia().toString());
                        Log.i("etnia", registerActivity.getEtnia() + "");
                    }
                    if(registerActivity.getDisplaced() != null) {
                        parameters.put("desplazado", registerActivity.getDisplaced().toString());
                        Log.i("desplazado", registerActivity.getDisplaced() + "");
                    }
                    if(registerActivity.getDeclarant() != null) {
                        parameters.put("tipoDesplazadoDeclarante", registerActivity.getDeclarant().toString());
                        Log.i("tipoDesplazadoDeclarant", registerActivity.getDeclarant() + "");
                    }
                    if(registerActivity.getRecognition() != null) {
                        parameters.put("tipoDesplazadoDeclaranteReconocido", registerActivity.getRecognition().toString());
                        Log.i("tipoDesplazadoReconocid", registerActivity.getRecognition() + "");
                    }
                    if(registerActivity.getHandicapped() != null) {
                        parameters.put("discapacidad", registerActivity.getHandicapped().toString());
                        Log.i("discapacidad", registerActivity.getHandicapped() + "");
                    }
                    if(registerActivity.getHandicappedType() != null) {
                        parameters.put("tipoDiscapacidad", registerActivity.getHandicappedType().toString());
                        Log.i("tipoDiscapacidad", registerActivity.getHandicappedType() + "");
                    }
                    if(registerActivity.getHandicappedSubtype() != null) {
                        parameters.put("subtipoDiscapacidad", registerActivity.getHandicappedSubtype().toString());
                        Log.i("subtipoDiscapacidad", registerActivity.getHandicappedSubtype() + "");
                    }
                    if(registerActivity.getBoosHeadFamily() != null) {
                        parameters.put("jefeCabezaHogar", registerActivity.getBoosHeadFamily().toString());
                        Log.i("jefeCabezaHogar", registerActivity.getBoosHeadFamily() + "");
                    }

                    //Get Data Step Two
                    if(registerActivity.getCountry() != null) {
                        parameters.put("pais", registerActivity.getCountry().toString());
                        Log.i("pais", registerActivity.getCountry() + "");
                    }else{
                        ((TextView)registerActivity.registerStepTwoFragment.countrySpinner.getSelectedView()).setError("Selecciona un país");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 1;
                        }
                    }
                    if(registerActivity.getState() != null) {
                        parameters.put("departamento", registerActivity.getState().toString());
                        Log.i("departamento", registerActivity.getState() + "");
                    }else{
                        ((TextView)registerActivity.registerStepTwoFragment.departmentSpinner.getSelectedView()).setError("Selecciona un departamento");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 1;
                        }
                    }
                    if(registerActivity.getTown() != null) {
                        parameters.put("municipio", registerActivity.getTown().toString());
                        Log.i("municipio", registerActivity.getTown() + "");
                    }else{
                        ((TextView)registerActivity.registerStepTwoFragment.townSpinner.getSelectedView()).setError("Selecciona una ciudad");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 1;
                        }
                    }
                    if(registerActivity.getZone() != null) {
                        parameters.put("zona", registerActivity.getZone().toString());
                        Log.i("zona", registerActivity.getZone() + "");
                    }else{
                        ((TextView)registerActivity.registerStepTwoFragment.zoneSpinner.getSelectedView()).setError("Selecciona una zona");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 1;
                        }
                    }
                    if(registerActivity.getCommune() != null) {
                        parameters.put("comuna", registerActivity.getCommune().toString());
                        Log.i("comuna", registerActivity.getCommune() + "");
                    }else{
                        ((TextView)registerActivity.registerStepTwoFragment.communeSpinner.getSelectedView()).setError("Selecciona una cumuna");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 1;
                        }
                    }
                    if(registerActivity.getNeighborhood() != null) {
                        parameters.put("barrio", registerActivity.getNeighborhood().toString());
                        Log.i("barrio", registerActivity.getNeighborhood() + "");
                    }else{
                        ((TextView)registerActivity.registerStepTwoFragment.neighborhoodSpinner.getSelectedView()).setError("Selecciona un barrio");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 1;
                        }
                    }
                    if(registerActivity.getStratum() != null) {
                        parameters.put("estrato", registerActivity.getStratum().toString());
                        Log.i("estrato", registerActivity.getStratum() + "");
                    }
                    if(registerActivity.getAddress() != null && !registerActivity.getAddress().matches("Dirección")) {
                        parameters.put("direccion", registerActivity.getAddress().toString());
                        Log.i("direccion", registerActivity.getAddress() + "");
                    }
                    if(registerActivity.getPhone() != null) {
                        parameters.put("telefono1", registerActivity.getPhone().toString());
                        Log.i("telefono1", registerActivity.getPhone() + "");
                    }else{
                        registerActivity.registerStepTwoFragment.phone.setError("Ingresa un número de telefono fijo");
                        errorsOthers ++;
                        if(pageErrorsOthers == 3){
                            pageErrorsOthers = 1;
                        }
                    }
                    if(registerActivity.getPhoneOptinal() != null) {
                        parameters.put("telefono2", registerActivity.getPhoneOptinal().toString());
                        Log.i("telefono2", registerActivity.getPhoneOptinal() + "");
                    }
                    if(registerActivity.getCelular() != null) {
                        parameters.put("celular", registerActivity.getCelular().toString());
                        Log.i("celular", registerActivity.getCelular() + "");
                    }

                    //Get Data Step Three
                    if(registerActivity.getEps() != null) {
                        parameters.put("eps", registerActivity.getEps().toString());
                        Log.i("eps", registerActivity.getEps() + "");
                    }
                    if(registerActivity.getEconomyActivity() != null) {
                        parameters.put("actividadEconomica", registerActivity.getEconomyActivity().toString());
                        Log.i("actividadEconomica", registerActivity.getEconomyActivity() + "");
                    }
                    if(registerActivity.getGradeLevel() != null) {
                        parameters.put("nivelEscolaridad", registerActivity.getGradeLevel().toString());
                        Log.i("nivelEscolaridad", registerActivity.getGradeLevel() + "");
                    }
                    if(registerActivity.getLastYearApprobate() != null) {
                        parameters.put("ultimoAnioAprobado", registerActivity.getLastYearApprobate().toString());
                        Log.i("ultimoAnioAprobado", registerActivity.getLastYearApprobate() + "");
                    }
                    if(registerActivity.getSchoolType() != null) {
                        parameters.put("tipoEstablecimientoEducativo", registerActivity.getSchoolType().toString());
                        Log.i("tipoEstaEducat", registerActivity.getSchoolType() + "");
                    }
                    if(registerActivity.getSchool() != null) {
                        //parameters.put("establecimiento_educativo_id", registerActivity.getSchool());
                    }

                    //Get Data Step Four
                    if(registerActivity.getUsername() != null) {
                        parameters.put("username", registerActivity.getUsername().toString());
                        Log.i("username", registerActivity.getUsername() + "");
                    }
                    if(registerActivity.getPassword() != null) {
                        parameters.put("password", registerActivity.getPassword().toString());
                        Log.i("password", registerActivity.getPassword() + "");
                    }
                    if(registerActivity.getMail() != null) {
                        parameters.put("email", registerActivity.getMail().toString());
                        Log.i("email", registerActivity.getMail() + "");
                    }


                    if(registerActivity.getSchool() != null) {
                        //parameters.put("recibir_boletines", registerActivity.getSchool());
                    }
                    Log.i("separador", "---------------------------------------------------");
                    if(errorsOthers > 0){
                        registerActivity.pager.setCurrentItem(pageErrorsOthers);
                        progress.setBackgroundDrawable(null);
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Los campos marcados son obligatorios", Toast.LENGTH_LONG).show();
                    }else{
                        ApiUser apiUser = new ApiUser(registerActivity);
                        apiUser.registerUser(parameters);
                    }
                }else{
                    progress.setBackgroundDrawable(null);
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Los campos marcados son obligatorios", Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("Click ","Spinner");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}

