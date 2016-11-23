package com.itosoftware.inderandroid.Fragments;

/**
 * Created by itofelipeparra on 24/06/16.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Activities.MainActivity;
import com.itosoftware.inderandroid.Api.Pqr.Pqr;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.graphicstest.JustifiedTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DialogFragmentPqrFilter extends DialogFragment implements View.OnClickListener,View.OnFocusChangeListener {

    private View back;
    View rootView;
    EditText numero_solicitud;
    EditText identificacion;
    EditText fecha_desde_txt;
    EditText fecha_hasta2_txt;
    ImageView fecha_desde_img;
    ImageView fecha_hasta2_img;
    ImageView fecha_desde_delete;
    ImageView fecha_hasta2_delete;
    Button filtrar;

    public DialogFragmentPqrFilter() {
        // Required empty public constructor
    }

    public void setRootView(View rootView){
        this.rootView = rootView;
    }
    public View getRootView(){
        return this.rootView ;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        rootView = inflater.inflate(R.layout.fragment_dialog_pqr_filter, container, false);
        setRootView(rootView);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        back = rootView.findViewById(R.id.back);
        back.setOnClickListener(this);
        filtrar = (Button) rootView.findViewById(R.id.filtrar_pqrs);
        filtrar.setOnClickListener(this);
        numero_solicitud = (EditText) rootView.findViewById(R.id.numero_solicitud);
        identificacion = (EditText) rootView.findViewById(R.id.identificacion);
        fecha_desde_txt = (EditText) rootView.findViewById(R.id.fecha_desde_txt);
        disableEditText(fecha_desde_txt);
        fecha_hasta2_txt = (EditText) rootView.findViewById(R.id.fecha_hasta2_txt);
        disableEditText(fecha_hasta2_txt);

        fecha_desde_img = (ImageView) rootView.findViewById(R.id.fecha_desde_img);
        fecha_hasta2_img = (ImageView) rootView.findViewById(R.id.fecha_hasta2_img);

        fecha_desde_delete = (ImageView) rootView.findViewById(R.id.fecha_desde_delete);
        fecha_hasta2_delete  = (ImageView) rootView.findViewById(R.id.fecha_hasta2_delete);

        fecha_desde_txt.setOnFocusChangeListener(this);
        fecha_hasta2_txt.setOnFocusChangeListener(this);
        fecha_desde_img.setOnClickListener(this);
        fecha_hasta2_img.setOnClickListener(this);
        fecha_desde_delete.setOnClickListener(this);
        fecha_hasta2_delete.setOnClickListener(this);
        return rootView;
    }
    private void disableEditText(EditText editText) {
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newdatePickerFragment = new DatePickerFragment();

        if (v instanceof EditText)
            newdatePickerFragment.setFieldId((EditText) v);
        else if (v instanceof ImageView && v == fecha_hasta2_img)
            newdatePickerFragment.setFieldId(fecha_hasta2_txt);
        else if (v instanceof ImageView && v == fecha_desde_img)
            newdatePickerFragment.setFieldId(fecha_desde_txt);

        newdatePickerFragment.setMainActivity((MainActivity) getActivity());
        FragmentManager fm = getActivity().getSupportFragmentManager();
        newdatePickerFragment.show(fm, "datePicker");
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag() != null ? v.getTag().toString(): "";
        if (tag.equals("back")) {
            dismiss();
        }
        if (tag.equals("filtrar_pqrs")) {
            String error = "";
            Intent intent = new Intent();
            intent.putExtra("numero_solicitud",numero_solicitud.getText().toString());
            intent.putExtra("identificacion",identificacion.getText().toString());
            String textoFechaDesde = fecha_desde_txt.getText().toString();
            String textoFechaHasta = fecha_hasta2_txt.getText().toString();
            if (!textoFechaDesde.equals("") && !textoFechaHasta.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha_desde = null;
                Date fecha_hasta = null;
                try {
                    fecha_desde = sdf.parse(fecha_desde_txt.getText().toString());
                    fecha_hasta = sdf.parse(fecha_hasta2_txt.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (fecha_desde.after(fecha_hasta)) {
                    error = "La fecha Desde no puede ser mayor a la fecha Hasta";
                }
            }
            if (error.equals("")){
                intent.putExtra("fecha_desde_txt",fecha_desde_txt.getText().toString());
                intent.putExtra("fecha_hasta2_txt",fecha_hasta2_txt.getText().toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(),1,intent);
                dismiss();
            }else{
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        }
        if (v == fecha_hasta2_img ||
                v == fecha_desde_img){
            showDatePickerDialog(v);
        }
        if(v == fecha_desde_delete){
            fecha_desde_txt.setText("");
        }
        if(v == fecha_hasta2_delete){
            fecha_hasta2_txt.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if( (v == fecha_hasta2_txt && hasFocus) || (v == fecha_desde_txt && hasFocus)){
            showDatePickerDialog(v);
        }
    }
}