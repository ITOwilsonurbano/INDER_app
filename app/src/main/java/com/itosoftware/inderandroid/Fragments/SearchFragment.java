package com.itosoftware.inderandroid.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.itosoftware.inderandroid.Adapters.AutoCompleteAdapter;
import com.itosoftware.inderandroid.Adapters.ShowParticipantsAdapter;
import com.itosoftware.inderandroid.Api.SportsScenarios.ApiSportsScenarios;
import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
import com.itosoftware.inderandroid.Interface.DialogClickListener;
import com.itosoftware.inderandroid.Interface.SportScenarioListener;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchFragment extends DialogFragment implements AdapterView.OnItemClickListener, SportScenarioListener {

    View rootView;
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> sportsSecnariosNames;
    ArrayAdapter<String> adapter;
    SearchFragment searchFragment;

    Context context;

    private DialogClickListener callback;

    ImageView progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchFragment = this;
        try {
            callback = (DialogClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Transparente)));

        progress = (ImageView) rootView.findViewById(R.id.fragment_search_progress_bar);
        progress.setBackgroundResource(R.drawable.progress_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) progress.getBackground();
        frameAnimation.start();

        /*Button close = (Button) rootView.findViewById(R.id.fragment_search_advanced_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });*/
        Button cancel = (Button) rootView.findViewById(R.id.fragment_search_cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            // Oculta Teclado
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dismiss();
            }
        });
        final ArrayList<String> list = new ArrayList<String>();

        this.context = getContext();


        //adapter = new ShowParticipantsAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        adapter = new AutoCompleteAdapter(this.context , R.layout.autocomplete_adapter, list);

        autoCompleteTextView =(AutoCompleteTextView) rootView.findViewById(R.id.fragment_search_text);
        // Muestra Teclado.
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setThreshold(0);
        final SearchFragment searchFragment = this;

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {

                    progress.setVisibility(View.VISIBLE);
                    progress.setBackgroundResource(R.drawable.progress_animation);
                    AnimationDrawable frameAnimationCountry = (AnimationDrawable) progress.getBackground();
                    frameAnimationCountry.start();

                    sportsSecnariosNames = new ArrayList<String>();
                    //Call Api and get Sports Scenarios
                    HashMap params = new HashMap<>();
                    params.put("page", 1);
                    params.put("limit", -1);
                    params.put("keyword", s.toString());
                    ApiSportsScenarios apiSportsScenarios = new ApiSportsScenarios(searchFragment);
                    apiSportsScenarios.getSportsScenariosAutoComplete(params);
                }
            }
        });

        if(callback.getLabel() != "Búsqueda de escenarios"){
            autoCompleteTextView.setText(callback.getLabel());
        }

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callback.setLabel(autoCompleteTextView.getText().toString());
                    HashMap params = new HashMap<>();
                    params.put("page", 1);
                    params.put("limit",-1);
                    params.put("keyword", autoCompleteTextView.getText().toString());
                    if(!callback.getNeighborhood().equals(null)){
                        params.put("neighborhood", callback.getNeighborhood());
                    }
                    if(!callback.getCommune().equals(null)){
                        params.put("commune", callback.getCommune());
                    }
                    if(!callback.getZone().equals(null)){
                        params.put("zone", callback.getZone());
                    }
                    if(!callback.getDiscipline().equals(null)){
                        params.put("discipline", callback.getDiscipline());
                    }
                    callback.updateMap(params);
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        Button deleteText = (Button) rootView.findViewById(R.id.fragment_search_delete_text_button);
        deleteText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                autoCompleteTextView.setText("");
            }
        });

        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

        // Do something else
        return rootView;
    }

    //Start Implement Sport Scenario Listener
    public void onFinishedConnection(ArrayList<SportsScenarios> sportsScenarios){
        for (SportsScenarios sportScenario : sportsScenarios) {
            String name =  (String) sportScenario.getInfo().get("nombre");
            sportsSecnariosNames.add(name);
        }
        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

        adapter = new AutoCompleteAdapter(this.context , R.layout.autocomplete_adapter, sportsSecnariosNames);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onFinishedConnectionWebview(ArrayList<SportsScenarios> itemsSportsScenarios, String sportsScenarios) {

    }
    //End Implement Sport Scenario Listener

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Call Api and get Sports Scenarios
        callback.setLabel(autoCompleteTextView.getText().toString());
        HashMap params = new HashMap<>();
        params.put("page", 1);
        params.put("limit",-1);
        params.put("keyword", parent.getItemAtPosition(position).toString());
        if(!callback.getNeighborhood().equals(null)){
            params.put("neighborhood", callback.getNeighborhood());
        }
        if(!callback.getCommune().equals(null)){
            params.put("commune", callback.getCommune());
        }
        if(!callback.getZone().equals(null)){
            params.put("zone", callback.getZone());
        }
        if(!callback.getDiscipline().equals(null)){
            params.put("discipline", callback.getDiscipline());
        }
        callback.updateMap(params);

        // Oculta Teclado
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress.setBackgroundDrawable(null);
        progress.setVisibility(View.GONE);

        dismiss();
    }



}