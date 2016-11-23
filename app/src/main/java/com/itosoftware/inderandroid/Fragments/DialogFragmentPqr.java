package com.itosoftware.inderandroid.Fragments;

/**
 * Created by itofelipeparra on 24/06/16.
 */

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.itosoftware.inderandroid.Activities.DocumentoActivity;
import com.itosoftware.inderandroid.Api.Pqr.Pqr;
import com.itosoftware.inderandroid.R;


public class DialogFragmentPqr extends DialogFragment implements View.OnClickListener {

    private View back;
    private TextView numero_proceso;
    private TextView numero_solicitud;
    private TextView identificacion;
    private TextView fecha_registro;
    private TextView fecha_estimada_respuesta;
    private TextView fecha_respuesta;
    private TextView estado;
    private TextView funcionario_responsable;
    private ImageView documento;
    private TextView documento_text;
    public DialogFragmentPqr() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_pqr, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(this);
        numero_proceso = (TextView)view.findViewById(R.id.numero_proceso);
        numero_solicitud = (TextView)view.findViewById(R.id.numero_solicitud);
        identificacion = (TextView)view.findViewById(R.id.identificacion);
        fecha_registro = (TextView)view.findViewById(R.id.fecha_registro);
        fecha_estimada_respuesta = (TextView)view.findViewById(R.id.fecha_estimada_respuesta);
        fecha_respuesta = (TextView)view.findViewById(R.id.fecha_respuesta);
        estado = (TextView)view.findViewById(R.id.estado);
        funcionario_responsable = (TextView)view.findViewById(R.id.funcionario_responsable);
        documento_text = (TextView)view.findViewById(R.id.documento_text);
        documento = (ImageView) view.findViewById(R.id.documento);
        String object = getArguments().getString("object");
        Gson gson = new Gson();

        Pqr pqr = gson.fromJson(object, Pqr.class);

        numero_proceso.setText(pqr.getNumero_proceso());
        numero_solicitud.setText(pqr.getNumero_solicitud());
        identificacion.setText(pqr.getIdentificacion());
        fecha_registro.setText(pqr.getFecha_registro());
        fecha_estimada_respuesta.setText(pqr.getFecha_estimada_respuesta());
        fecha_respuesta.setText(pqr.getFecha_respuesta());
        estado.setText(pqr.getEstado());
        funcionario_responsable.setText(pqr.getFuncionario_responsable());
        if (pqr.getDocumento().equals("")){
            documento_text.setVisibility(View.VISIBLE);
            documento.setVisibility(View.GONE);
        }
        documento.setTag(pqr.getDocumento());
        documento.setOnClickListener(this);

        return view;
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();
        if (tag.equals("back")) {
            dismiss();
        }
        if (v == documento){
            Intent intent = new Intent(getContext(),DocumentoActivity.class );
            Bundle b = new Bundle();

            String object = getArguments().getString("object");
            Gson gson = new Gson();

            Pqr pqr = gson.fromJson(object, Pqr.class);

            b.putString("documento",pqr.getDocumento());
            intent.putExtras(b);
            Toast.makeText(getActivity(), "Descargando Documento...", Toast.LENGTH_LONG).show();
            getContext().startActivity(intent);
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

}