
    package com.itosoftware.inderandroid.Fragments;

    import android.graphics.drawable.AnimationDrawable;
    import android.os.Bundle;
    import android.support.v4.app.Fragment;
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
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.RelativeLayout;
    import android.widget.ScrollView;
    import android.widget.Spinner;
    import android.widget.Toast;

    import com.itosoftware.inderandroid.Activities.RegisterActivity;
    import com.itosoftware.inderandroid.Adapters.InderSpinnerAdapter;
    import com.itosoftware.inderandroid.Api.DocumentType.ApiDocumentTypes;
    import com.itosoftware.inderandroid.Api.EconomyActivity.ApiEconomyActivity;
    import com.itosoftware.inderandroid.Api.EconomyActivity.EconomyActivity;
    import com.itosoftware.inderandroid.Api.Eps.ApiEps;
    import com.itosoftware.inderandroid.Api.Eps.Eps;
    import com.itosoftware.inderandroid.Api.GradeLevel.ApiGradeLevel;
    import com.itosoftware.inderandroid.Api.GradeLevel.GradeLevel;
    import com.itosoftware.inderandroid.Api.School.ApiSchool;
    import com.itosoftware.inderandroid.Api.School.School;
    import com.itosoftware.inderandroid.Api.States.ApiStates;
    import com.itosoftware.inderandroid.Api.TypeSchool.ApiTypeSchool;
    import com.itosoftware.inderandroid.Api.TypeSchool.TypeSchools;
    import com.itosoftware.inderandroid.Interface.EconomyActivityListener;
    import com.itosoftware.inderandroid.Interface.EpsListener;
    import com.itosoftware.inderandroid.Interface.GradeLevelListener;
    import com.itosoftware.inderandroid.Interface.SchoolListener;
    import com.itosoftware.inderandroid.Interface.TypeSchoolListener;
    import com.itosoftware.inderandroid.R;

    import java.util.ArrayList;
    import java.util.HashMap;


    public class RegisterStepThreeFragment extends Fragment implements AdapterView.OnItemSelectedListener, EpsListener, EconomyActivityListener, GradeLevelListener, SchoolListener, TypeSchoolListener{

        View rootView;
        RegisterActivity registerActivity;
        //Properties Spinner Eps
        protected Spinner epsSpinner;
        protected HashMap<Integer, Integer> epsCodes;
        protected HashMap<Integer, String> epsNames;
        InderSpinnerAdapter adapterEps;

        //Properties Spinner Economy Activity
        protected Spinner economyActivitySpinner;
        protected HashMap<Integer, Integer> economyActivityCodes;
        protected HashMap<Integer, String> economyActivityNames;
        InderSpinnerAdapter adapterEconomyActivity;

        //Properties Spinner Grade Level
        protected Spinner gradeLevelSpinner;
        protected HashMap<Integer, Integer> gradeLevelCodes;
        protected HashMap<Integer, String> gradeLevelNames;
        InderSpinnerAdapter adapterGradeLevel;

        //Properties Spinner Last Year Approbate
        protected Spinner lastYearApprobateSpinner;
        protected HashMap<Integer, Integer> lastYearApprobateCodes;
        protected HashMap<Integer, String> lastYearApprobateNames;

        //Properties Spinner School Type
        protected Spinner schoolTypeSpinner;
        protected HashMap<Integer, Integer> schoolTypeCodes;
        protected HashMap<Integer, String> schoolTypeNames;
        InderSpinnerAdapter adapterSchoolType;

        //Properties Spinner School
        protected Spinner schoolSpinner;
        protected HashMap<Integer, Integer> schoolCodes;
        protected HashMap<Integer, String> schoolNames;
        InderSpinnerAdapter adapterSchool;

        ImageView progress;
        LinearLayout linearLayoutRowSeven;
        LinearLayout linearLayoutRowFour;
        LinearLayout linearLayoutRowFive;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_register_step_three, container, false);
            registerActivity = (RegisterActivity) getActivity();

            linearLayoutRowSeven = (LinearLayout) rootView.findViewById(R.id.fragment_register_three_row_seven);
            linearLayoutRowFour = (LinearLayout) rootView.findViewById(R.id.fragment_register_three_row_four);
            linearLayoutRowFive = (LinearLayout) rootView.findViewById(R.id.fragment_register_three_row_five);

            progress = (ImageView) rootView.findViewById(R.id.fragment_register_three_progress_bar);
            progress.setBackgroundResource(R.drawable.progress_animation);
            AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
            frameAnimation.start();
            //Spinner Eps
            HashMap paramsEps = new HashMap<>();
            paramsEps.put("page", 1);
            paramsEps.put("limit", 800);
            ApiEps apiEps = new ApiEps(this);
            apiEps.getEps(paramsEps);
            epsSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_three_eps);
            epsCodes = new HashMap<Integer, Integer>();
            epsNames = new HashMap<Integer, String>();
            adapterEps = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
            adapterEps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterEps.add("Eps");
            epsSpinner.setAdapter(adapterEps);

            //Spinner Economy Activity
            HashMap paramsEconomy = new HashMap<>();
            paramsEconomy.put("page", 1);
            paramsEconomy.put("limit", 800);
            ApiEconomyActivity apiEconomyActivity = new ApiEconomyActivity(this);
            apiEconomyActivity.getEconomyActivity(paramsEconomy);
            economyActivitySpinner = (Spinner) rootView.findViewById(R.id.fragment_register_three_economy_activity);
            economyActivityCodes = new HashMap<Integer, Integer>();
            economyActivityNames = new HashMap<Integer, String>();
            adapterEconomyActivity = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
            adapterEconomyActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterEconomyActivity.add("Actividad Economica Principal");
            economyActivitySpinner.setAdapter(adapterEconomyActivity);

            //Spinner Grade Level
            HashMap paramsGradeLevel = new HashMap<>();
            paramsGradeLevel.put("page", 1);
            paramsGradeLevel.put("limit", 800);
            ApiGradeLevel apiGradeLevel = new ApiGradeLevel(this);
            apiGradeLevel.getGradeLevels(paramsGradeLevel);
            gradeLevelSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_three_grade_level);
            gradeLevelCodes = new HashMap<Integer, Integer>();
            gradeLevelNames = new HashMap<Integer, String>();
            adapterGradeLevel = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
            adapterGradeLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterGradeLevel.add("Nivel de Escolaridad");
            gradeLevelSpinner.setAdapter(adapterGradeLevel);

            //Spinner Last Year Approbate
            lastYearApprobateSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_three_last_year_approbate);
            lastYearApprobateCodes = new HashMap<Integer, Integer>();
            lastYearApprobateNames = new HashMap<Integer, String>();
            InderSpinnerAdapter adapterLastYearApprobate = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
            adapterLastYearApprobate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterLastYearApprobate.add("Ultimo Año Aprobado");
            for (Integer position = 1; position <= 11; position++) {
                lastYearApprobateCodes.put(position, position);
                lastYearApprobateNames.put(position, position.toString());
                adapterLastYearApprobate.add(position.toString());
            }
            lastYearApprobateSpinner.setAdapter(adapterLastYearApprobate);

            //Spinner School Type
            HashMap paramsSchoolType = new HashMap<>();
            paramsSchoolType.put("page", 0);
            paramsSchoolType.put("limit", -1);
            ApiTypeSchool apiTypeSchool = new ApiTypeSchool(this);
            apiTypeSchool.getTypeSchool(paramsSchoolType);
            schoolTypeSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_three_school_type);
            schoolTypeCodes = new HashMap<Integer, Integer>();
            schoolTypeNames = new HashMap<Integer, String>();
            adapterSchoolType = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
            adapterSchoolType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterSchoolType.add("Tipo de establecimiento educativo");
            schoolTypeSpinner.setAdapter(adapterSchoolType);

            //Spinner School
            HashMap paramsSchool = new HashMap<>();
            paramsSchool.put("page", 1);
            paramsSchool.put("limit", 800);
            ApiSchool apiSchool = new ApiSchool(this);
            apiSchool.getSchools(paramsSchool);
            schoolSpinner = (Spinner) rootView.findViewById(R.id.fragment_register_three_school);
            schoolCodes = new HashMap<Integer, Integer>();
            schoolNames = new HashMap<Integer, String>();
            adapterSchool = new InderSpinnerAdapter(registerActivity, android.R.layout.simple_spinner_item);
            adapterSchool.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterSchool.add("Establecimiento educativo");
            schoolSpinner.setAdapter(adapterSchool);

            Button next = (Button) rootView.findViewById(R.id.fragment_register_three_button_next);
            next.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    // Eps
                    Integer posEps = epsSpinner.getSelectedItemPosition();
                    if(posEps != 0){
                        registerActivity.setEps(epsCodes.get(posEps).toString());
                    }
                    // Economy Activity
                    Integer posEconomy = economyActivitySpinner.getSelectedItemPosition();
                    if(posEconomy != 0){
                        registerActivity.setEconomyActivity(economyActivityCodes.get(posEconomy).toString());
                    }
                    // Grade Level
                    Integer posGrade = gradeLevelSpinner.getSelectedItemPosition();
                    if(posGrade != 0){
                        registerActivity.setGradeLevel(gradeLevelCodes.get(posGrade).toString());
                    }
                    // Last Year
                    Integer posLastYear = lastYearApprobateSpinner.getSelectedItemPosition();
                    if(posLastYear != 0){
                        Log.i("Last Year Selected: ",lastYearApprobateCodes.get(posLastYear).toString());
                        registerActivity.setLastYearApprobate(lastYearApprobateCodes.get(posLastYear).toString());
                    }
                    // Type School
                    Integer posTypeSchool = schoolTypeSpinner.getSelectedItemPosition();
                    if(posTypeSchool != 0){
                        registerActivity.setSchoolType(schoolTypeCodes.get(posTypeSchool).toString());
                    }
                    // School
                    Integer posSchool = schoolSpinner.getSelectedItemPosition();
                    if(posSchool != 0){
                        registerActivity.setSex(schoolCodes.get(posSchool).toString());
                    }

                    registerActivity.pager.setCurrentItem(registerActivity.pager.getCurrentItem() + 1);
                }
            });

            return rootView;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getId()) {
                case R.id.fragment_register_three_school_type:
                    Integer posTypeSchool = schoolTypeSpinner.getSelectedItemPosition();
                    if(posTypeSchool != 0){
                        if(schoolTypeNames.get(posTypeSchool) != null && schoolTypeNames.get(posTypeSchool).toString().matches("NINGUNO")){

                            linearLayoutRowSeven.setVisibility(View.GONE);
                            linearLayoutRowFour.setVisibility(View.GONE);
                            linearLayoutRowFive.setVisibility(View.GONE);
                            gradeLevelSpinner.setSelection(0);
                            schoolSpinner.setSelection(0);
                            lastYearApprobateSpinner.setSelection(0);
                        }else{
                            linearLayoutRowSeven.setVisibility(View.VISIBLE);
                            linearLayoutRowFour.setVisibility(View.VISIBLE);
                            linearLayoutRowFive.setVisibility(View.VISIBLE);
                        }
                    }

                    break;
            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        //    Start Methods Implements EpsListener
        @Override
        public void onfinishedLoadEps(ArrayList<Eps> epses) {
            int i = 1;
            Integer selectedItem = 0;
            adapterEps.clear();
            adapterEps.add("Eps");
            for (Eps eps : epses) {
                String name =  (String) eps.getNombre();
                adapterEps.add(name);
                epsCodes.put(i,eps.getId());
                epsNames.put(i,eps.getNombre());
                i++;
            }
            adapterEps.notifyDataSetChanged();
            epsSpinner.setOnItemSelectedListener(this);
            epsSpinner.setAdapter(adapterEps);
        }

        @Override
        public void onErrorLoadEps() {
            Toast.makeText(getContext(), "Error al cargar Eps.",Toast.LENGTH_LONG).show();
        }
        //    End Methods Implements EpsListener

        //    Start Methods Implements EconomyActivityListener
        @Override
        public void onfinishedLoadEconomyActivity(ArrayList<EconomyActivity> economyActivities) {
            int i = 1;
            Integer selectedItem = 0;
            adapterEconomyActivity.clear();
            adapterEconomyActivity.add("Actividad Economica");
            for (EconomyActivity economyActivity : economyActivities) {
                String name =  (String) economyActivity.getNombre();
                adapterEconomyActivity.add(name);
                economyActivityCodes.put(i,economyActivity.getId());
                economyActivityNames.put(i,economyActivity.getNombre());
                i++;
            }
            adapterEconomyActivity.notifyDataSetChanged();
            economyActivitySpinner.setOnItemSelectedListener(this);
            economyActivitySpinner.setAdapter(adapterEconomyActivity);
        }

        @Override
        public void onErrorLoadEconomyActivity() {
            Toast.makeText(getContext(), "Error al cargar Actividades Económicas.",Toast.LENGTH_LONG).show();
        }
        //    End Methods Implements EconomyActivityListener

        //    Start Methods Implements GradeLevelListener
        @Override
        public void onfinishedLoadGradeLevels(ArrayList<GradeLevel> gradeLevels) {
            int i = 1;
            Integer selectedItem = 0;
            adapterGradeLevel.clear();
            adapterGradeLevel.add("Nivel de Escolaridad");
            for (GradeLevel gradeLevel : gradeLevels) {
                String name =  (String) gradeLevel.getNombre();
                adapterGradeLevel.add(name);
                gradeLevelCodes.put(i,gradeLevel.getId());
                gradeLevelNames.put(i,gradeLevel.getNombre());
                i++;
            }
            adapterGradeLevel.notifyDataSetChanged();
            gradeLevelSpinner.setOnItemSelectedListener(this);
            gradeLevelSpinner.setAdapter(adapterGradeLevel);
        }

        @Override
        public void onErrorLoadGradeLevels() {
            Toast.makeText(getContext(), "Error al cargar Niveles de Escolaridad.",Toast.LENGTH_LONG).show();
        }
        //    End Methods Implements GradeLevelListener

        //    Start Methods Implements SchoolListener

        @Override
        public void onErrorLoadSchools() {
            Toast.makeText(getContext(), "Error al cargar Establecimientos educativos.",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onfinishedLoadSchools(ArrayList<School> schools) {
            int i = 1;
            Integer selectedItem = 0;
            adapterSchool.clear();
            adapterSchool.add("Establecimiento Educativo");
            for (School school : schools) {
                String name =  (String) school.getNombre();
                adapterSchool.add(name);
                schoolCodes.put(i,school.getId());
                schoolNames.put(i,school.getNombre());
                i++;
            }
            adapterSchool.notifyDataSetChanged();
            schoolSpinner.setOnItemSelectedListener(this);
            schoolSpinner.setAdapter(adapterSchool);
        }




        //    End Methods Implements SchoolListener

        //    Start Methods Implements TypeSchoolListener

        @Override
        public void onfinishedTypeSchools(ArrayList<TypeSchools> typeSchools) {
            int i = 1;
            Integer selectedItem = 0;
            adapterSchoolType.clear();
            adapterSchoolType.add("Tipo de establecimiento educativo");
            for (TypeSchools typeSchool : typeSchools) {
                String name =  (String) typeSchool.getNombre();
                adapterSchoolType.add(name);
                schoolTypeCodes.put(i,typeSchool.getId());
                schoolTypeNames.put(i,typeSchool.getNombre());
                i++;
            }
            adapterSchoolType.notifyDataSetChanged();
            schoolTypeSpinner.setOnItemSelectedListener(this);
            schoolTypeSpinner.setAdapter(adapterSchoolType);
            progress.setBackgroundDrawable(null);
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onErrorLoadTypeSchools() {
            Toast.makeText(getContext(), "Error al cargar Tipos de establecimientos educativos.", Toast.LENGTH_LONG).show();
        }




        //    End Methods Implements TypeSchoolListener
    }
