package com.itosoftware.inderandroid.Fragments;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itosoftware.inderandroid.Adapters.InderSpinnerAdapter;
import com.itosoftware.inderandroid.Api.Pqr.ApiPqr;
import com.itosoftware.inderandroid.Api.Pqr.PqrSpinnerClass;
import com.itosoftware.inderandroid.Api.Users.ApiUser;
import com.itosoftware.inderandroid.Api.Users.Participant;
import com.itosoftware.inderandroid.Api.Users.UserContainer;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.Interface.ApiUserListener;
import com.itosoftware.inderandroid.Interface.PqrListener;
import com.itosoftware.inderandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class PqrFragment extends Fragment implements View.OnClickListener, PqrListener, AdapterView.OnItemSelectedListener, ApiUserListener {
    View rootView;
    View fragment_pqr_end;
    ImageView progress;
    View email_zone;
    View personal_info_zone;
    View back;
    Button upload;
    ImageView upload_image;
    TextView filename;
    TextView mensaje;
    TextView radio_anonymous_ttl;
    TextView id_number;
    TextView names;
    TextView phone;
    TextView mobile;
    TextView radio_email_ttl;
    TextView email;
    TextView address;
    View uploated_zone;
    View delete_upload_file;
    RadioGroup radio_anonymous;
    RadioGroup radio_email;
    Spinner spinner_ciudades;
    Spinner spinner_comunas;
    Spinner spinner_tipos_solicitud;
    Spinner spinner_tematicas;
    Spinner spinner_tipos_documento;
    ArrayList<PqrSpinnerClass> tipos_solicitud;
    ArrayList<PqrSpinnerClass> ciudades;
    ArrayList<PqrSpinnerClass> comunas;
    ArrayList<PqrSpinnerClass> tematicas;
    ArrayList<PqrSpinnerClass> tipos_documento;
    View comuna_zone;
    ApiPqr api;
    Boolean error_anonymous = false;
    Boolean comuna_error = false;
    Boolean tipos_documento_error = false;
    Boolean error_email = false;
    private String path_file;
    private String base_64 = "";
    private boolean is_file = false;
    private static String TAG = "PqrFragment";
    private ProgressDialog progressDialog;
    ApiUser api_user;
    Globals g;
    boolean user_info_loaded=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        g = (Globals) getActivity().getApplication();
        rootView = inflater.inflate(R.layout.fragment_pqr, container, false);

        radio_anonymous = (RadioGroup) rootView.findViewById(R.id.radio_anonymous);
        radio_anonymous.check(R.id.radio_no);
        radio_anonymous.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_anonymous_ttl.setError(null);
                switch (checkedId) {
                    case R.id.radio_yes:
                        personal_info_zone.setVisibility(View.GONE);
                        break;
                    case R.id.radio_no:
                        personal_info_zone.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        AppCompatRadioButton rb = (AppCompatRadioButton) rootView.findViewById(R.id.radio_yes);
        AppCompatRadioButton rb1 = (AppCompatRadioButton) rootView.findViewById(R.id.radio_no);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{

                        ContextCompat.getColor(getActivity(), R.color.Gris)
                        , ContextCompat.getColor(getActivity(), R.color.Verde)
                }
        );
        CompoundButtonCompat.setButtonTintList(rb, colorStateList);
        CompoundButtonCompat.setButtonTintList(rb1, colorStateList);

        radio_email = (RadioGroup) rootView.findViewById(R.id.radio_email);

        radio_email.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_email_ttl.setError(null);
                address.setError(null);
                switch (checkedId) {
                    case R.id.radio_email_yes:
                        email_zone.setVisibility(View.VISIBLE);
                        address.setHint("Dirección de la residencia");
                        break;
                    case R.id.radio_email_no:
                        email_zone.setVisibility(View.GONE);
                        addAsteriskEditText(R.id.address);
                        break;
                }
            }
        });

        AppCompatRadioButton rb2 = (AppCompatRadioButton) rootView.findViewById(R.id.radio_email_yes);
        AppCompatRadioButton rb3 = (AppCompatRadioButton) rootView.findViewById(R.id.radio_email_no);
        ColorStateList colorStateListEmail = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{

                        ContextCompat.getColor(getActivity(), R.color.Gris)
                        , ContextCompat.getColor(getActivity(), R.color.Verde)
                }
        );
        CompoundButtonCompat.setButtonTintList(rb2, colorStateListEmail);
        CompoundButtonCompat.setButtonTintList(rb3, colorStateListEmail);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeueLTStd-LtCn.otf");
        rb.setTypeface(font);
        rb1.setTypeface(font);
        rb2.setTypeface(font);
        rb3.setTypeface(font);

        fragment_pqr_end = rootView.findViewById(R.id.fragment_pqr_end);
        fragment_pqr_end.setOnClickListener(this);
        back = rootView.findViewById(R.id.back);
        back.setOnClickListener(this);
        upload = (Button) rootView.findViewById(R.id.upload);
        upload.setOnClickListener(this);
        addAsterisk(R.id.radio_anonymous_ttl);
        addAsteriskEditText(R.id.id_number);
        addAsteriskEditText(R.id.names);
        addAsteriskEditText(R.id.phone);
        addAsterisk(R.id.radio_email_ttl);
        addAsteriskEditText(R.id.email);
        personal_info_zone = rootView.findViewById(R.id.personal_info_zone);
//        personal_info_zone.setVisibility(View.GONE);
        email_zone = rootView.findViewById(R.id.fragment_user_12);
        email_zone.setVisibility(View.GONE);
        upload_image = (ImageView) rootView.findViewById(R.id.upload_image);
        filename = (TextView) rootView.findViewById(R.id.filename);
        uploated_zone = rootView.findViewById(R.id.uploated_zone);
        uploated_zone.setVisibility(View.GONE);
        delete_upload_file = rootView.findViewById(R.id.delete_upload_file);
        delete_upload_file.setOnClickListener(this);
        mensaje = (TextView) rootView.findViewById(R.id.mensaje);
        comuna_zone = rootView.findViewById(R.id.fragment_pqr4);
        comuna_zone.setVisibility(View.GONE);

        if (path_file != null) {
            setUploadImage(path_file);
        }
        if (api == null) {
            api = new ApiPqr(this);
        }
        spinner_ciudades = (Spinner) rootView.findViewById(R.id.spinner_ciudades);
        spinner_ciudades.setOnItemSelectedListener(this);
        spinner_comunas = (Spinner) rootView.findViewById(R.id.spinner_comunas);
        spinner_comunas.setOnItemSelectedListener(this);
        spinner_tipos_solicitud = (Spinner) rootView.findViewById(R.id.spinner_tipos_solicitud);
        spinner_tematicas = (Spinner) rootView.findViewById(R.id.spinner_tematicas);
        spinner_tipos_documento = (Spinner) rootView.findViewById(R.id.spinner_tipos_documento);
        spinner_tipos_documento.setOnItemSelectedListener(this);
        radio_anonymous_ttl = (TextView) rootView.findViewById(R.id.radio_anonymous_ttl);
        id_number = (TextView) rootView.findViewById(R.id.id_number);
        names = (TextView) rootView.findViewById(R.id.names);
        phone = (TextView) rootView.findViewById(R.id.phone);
        mobile = (TextView) rootView.findViewById(R.id.mobile);
        radio_email_ttl = (TextView) rootView.findViewById(R.id.radio_email_ttl);
        email = (TextView) rootView.findViewById(R.id.email);
        address = (TextView) rootView.findViewById(R.id.address);

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Enviando PQR");
        }
        api_user = new ApiUser(this);



        return rootView;
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();
        if (tag.equals("end")) {
            Log.w(TAG, "end");
            hydeSoftKeyboard();
            attempSend();

        } else if (tag.equals("back")) {
            Log.w(TAG, "back");
            hydeSoftKeyboard();
            ((UserAtentionFragment) getParentFragment()).showMenuFragment(2);
        } else if (tag.equals("upload")) {
            Log.w(TAG, "upload");
            DialogFragmentPqrUploadSelector dialog = new DialogFragmentPqrUploadSelector();
            dialog.show(getChildFragmentManager(), "Diag");
        } else if (tag.equals("delete_upload_file")) {
            Log.w(TAG, "delete_upload_file");
            path_file = null;
            base_64 = "";
            uploated_zone.setVisibility(View.GONE);
            upload.setVisibility(View.VISIBLE);
        }
    }

    private void addAsterisk(int id) {
        TextView text_view = (TextView) rootView.findViewById(id);
        int length = text_view.getText().toString().length();
        final SpannableStringBuilder sb = new SpannableStringBuilder(text_view.getText().toString() + "*");
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(229, 34, 34));
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, length, length + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, length, length + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text_view.setText(sb);
    }

    private void addAsteriskEditText(int id) {
        TextView text_view = (TextView) rootView.findViewById(id);
        int length = text_view.getHint().toString().length();
        final SpannableStringBuilder sb = new SpannableStringBuilder(text_view.getHint().toString() + "*");
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(229, 34, 34));
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, length, length + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, length, length + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text_view.setHint(sb);
    }

    public void hydeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public void setUploadImage(String dir) {
        if (!is_file) {
            File imgFile = new File(dir);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                upload_image.setImageBitmap(myBitmap);

            }
        } else {
            upload_image.setImageResource(R.drawable.ic_file_single);
        }
        String[] parts = dir.split("/");
        filename.setText(parts[parts.length - 1]);
        upload.setVisibility(View.GONE);
        uploated_zone.setVisibility(View.VISIBLE);
    }

    public void setBase_64(String base_64) {
        this.base_64 = base_64;
    }

    public void setPath_file(String path_file) {
        this.path_file = path_file;
    }

    public void attempSend() {
        radio_email_ttl.setError(null);
        email.setError(null);
        address.setError(null);
        error_anonymous = false;
        comuna_error = false;
        tipos_documento_error = false;
        error_email = false;
        radio_anonymous_ttl.setError(null);
        int id_radio = radio_anonymous.getCheckedRadioButtonId();
        View radioButton = radio_anonymous.findViewById(id_radio);
        String anonymous_value = null;
        int id_radio_email = radio_email.getCheckedRadioButtonId();
        View radioButtonEmail = radio_email.findViewById(id_radio_email);
        String email_value = null;
        ArrayList errores = new ArrayList();

        mensaje.setError(null);
        id_number.setError(null);
        names.setError(null);
        phone.setError(null);

        if (radioButton != null) {
            anonymous_value = radioButton.getTag().toString();
        }

        if (radioButtonEmail != null) {
            email_value = radioButton.getTag().toString();
        }


        if (anonymous_value == null) {
            radio_anonymous_ttl.setError("");
            error_anonymous = true;
            errores.add("error");
        } else {
            if (anonymous_value.equals("no")) {
                if (spinner_tipos_documento.getSelectedItemPosition() == 0) {
                    if (spinner_tipos_documento.getSelectedItemPosition() == 0) {
                        ((TextView)spinner_tipos_documento.getSelectedView()).setError("");
                        tipos_documento_error = true;
                    }
                }
                if (id_number.getText().toString().trim().equals("")) {
                    id_number.setError("Ingrese su número de identificación");
                }
                if (names.getText().toString().trim().equals("")) {
                    names.setError("Ingrese su nombre completo");
                }
                if (phone.getText().toString().trim().equals("")) {
                    phone.setError("Ingrese su teléfono fijo");
                }
                if (email_value == null) {
                    radio_email_ttl.setError("");
                    errores.add("error");
                    error_email = true;
                } else {
                    if (radioButtonEmail.getId() == R.id.radio_email_yes) {
                        if (email.getText().toString().trim().equals("")) {
                            email.setError("Ingrese su correo electrónico");
                            errores.add("error");
                        } else if (!isValidEmail(email.getText().toString())) {
                            email.setError("Correo electrónico no valido");
                            errores.add("error");
                        }
                    } else if (radioButtonEmail.getId() == R.id.radio_email_no) {
                        if (address.getText().toString().trim().equals("")) {
                            address.setError("Ingresa tu dirección");
                            errores.add("error");
                        }
                    }
                }
            }
        }

        if (spinner_ciudades.getSelectedItemPosition() > 0) {
            int cantidadComunas = spinner_comunas.getAdapter().getCount();
            if (cantidadComunas > 1 && spinner_comunas.getSelectedItemPosition() == 0) {
                ((TextView)spinner_comunas.getSelectedView()).setError("");
                comuna_error = true;
                errores.add("error");
            }
        }

        if (errores.size() > 0) {
            Toast.makeText(getActivity(), "Por favor ingrese información en los campos obligatorios.", Toast.LENGTH_LONG).show();
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            progressDialog.show();
            HashMap params = new HashMap();
            params.put("mensaje", mensaje.getText().toString());

            if (spinner_ciudades.getSelectedItemPosition() > 0) {
                String ciudad_id = ciudades.get(spinner_ciudades.getSelectedItemPosition() - 1).getId().toString();
                params.put("ciudad_id", ciudad_id);
                if (spinner_comunas.getSelectedItemPosition() > 0) {
                    String comuna_id = comunas.get(spinner_comunas.getSelectedItemPosition() - 1).getId().toString();
                    params.put("comuna_id", comuna_id);
                }else{
                    params.put("comuna_id", "");
                }
            }else{
                params.put("ciudad_id", "");
            }

            if (spinner_tipos_solicitud.getSelectedItemPosition() > 0) {
                String tipo_solicitud_id = tipos_solicitud.get(spinner_tipos_solicitud.getSelectedItemPosition() - 1).getId().toString();
                params.put("tipo_solicitud_id", tipo_solicitud_id);
            }else{
                params.put("tipo_solicitud_id", "");
            }

            if (spinner_tematicas.getSelectedItemPosition() > 0) {
                String tematica_id = tematicas.get(spinner_tematicas.getSelectedItemPosition() - 1).getId().toString();
                params.put("tematica_id", tematica_id);
            }else{
                params.put("tematica_id", "");
            }

            params.put("archivo_base64", base_64);

            if (spinner_tipos_documento.getSelectedItemPosition() > 0) {
                String tipo_identificacion_id = tipos_documento.get(spinner_tipos_documento.getSelectedItemPosition() - 1).getId().toString();
                params.put("tipo_identificacion_id", tipo_identificacion_id);
            }else{
                params.put("tipo_identificacion_id", "");
            }

            params.put("numero_identificacion", id_number.getText().toString());

            params.put("nombre", names.getText().toString());

            params.put("telefono_fijo", phone.getText().toString());

            params.put("telefono_celular", mobile.getText().toString());

            params.put("direccion", address.getText().toString());

            params.put("correo_electronico", email.getText().toString());

            String url = this.getString(R.string.url_pqr) + this.getString(R.string.pqr_send);
            api.sendRequest(params, url, "post", "pqr_send");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        if (path_file != null) {
            outState.putString("path_file", path_file);
        }
        if (base_64 != null) {
            outState.putString("base_64", base_64);
        }
        if (is_file == true) {
            outState.putBoolean("is_file", is_file);
        }
        if (ciudades != null) {
            Gson gson = new Gson();
            String object = gson.toJson(ciudades);
            outState.putString("ciudades", object.toString());
        }
        if (comunas != null) {
            Gson gson = new Gson();
            String object = gson.toJson(comunas);
            outState.putString("comunas", object.toString());
        }
        if (tipos_solicitud != null) {
            Gson gson = new Gson();
            String object = gson.toJson(tipos_solicitud);
            outState.putString("tipos_solicitud", object.toString());
        }
        if (tematicas != null) {
            Gson gson = new Gson();
            String object = gson.toJson(tematicas);
            outState.putString("tematicas", object.toString());
        }
        if (tipos_documento != null) {
            Gson gson = new Gson();
            String object = gson.toJson(tipos_documento);
            outState.putString("tipos_documento", object.toString());
        }
        if (error_anonymous) {
            outState.putBoolean("error_anonymous", true);
        }
        if (comuna_error) {
            outState.putBoolean("comuna_error", true);
        }
        if (tipos_documento_error) {
            outState.putBoolean("tipos_documento_error", true);
        }
        if (error_email) {
            outState.putBoolean("error_email", true);
        }
        if (user_info_loaded) {
            outState.putBoolean("user_info_loaded", true);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("path_file")) {
                path_file = savedInstanceState.getString("path_file");
                if (path_file != null) {
                    setUploadImage(path_file);
                }
            }
            if (savedInstanceState.containsKey("base_64")) {
                base_64 = savedInstanceState.getString("base_64");
                if (base_64 != null) {
                    setUploadImage(base_64);
                }
            }
            if (savedInstanceState.containsKey("is_file")) {
                is_file = savedInstanceState.getBoolean("is_file");
                if (path_file != null) {
                    setUploadImage(path_file);
                }
            }
            if (savedInstanceState.containsKey("ciudades")) {
                ciudades = stringToObjects(savedInstanceState.getString("ciudades"));
            }
            if (savedInstanceState.containsKey("comunas")) {
                comunas = stringToObjects(savedInstanceState.getString("comunas"));
            }
            if (savedInstanceState.containsKey("tipos_solicitud")) {
                tipos_solicitud = stringToObjects(savedInstanceState.getString("tipos_solicitud"));
            }
            if (savedInstanceState.containsKey("tematicas")) {
                tematicas = stringToObjects(savedInstanceState.getString("tematicas"));
            }
            if (savedInstanceState.containsKey("tipos_documento")) {
                tipos_documento = stringToObjects(savedInstanceState.getString("tipos_documento"));
            }
            if (savedInstanceState.containsKey("error_anonymous")) {
                error_anonymous = savedInstanceState.getBoolean("error_anonymous");
            }
            if (savedInstanceState.containsKey("comuna_error")) {
                comuna_error = savedInstanceState.getBoolean("comuna_error");
            }
            if (savedInstanceState.containsKey("tipos_documento_error")) {
                tipos_documento_error = savedInstanceState.getBoolean("tipos_documento_error");
            }
            if (savedInstanceState.containsKey("error_email")) {
                error_email = savedInstanceState.getBoolean("error_email");
            }
            if (savedInstanceState.containsKey("user_info_loaded")) {
                user_info_loaded = savedInstanceState.getBoolean("user_info_loaded");
            }
        }


        if (ciudades == null) {
            HashMap params = new HashMap();
            params.put("page", "1");
            params.put("limit", "200");
            String url = this.getString(R.string.url_pqr) + this.getString(R.string.pqr_states);
            api.sendRequest(params, url, "get", "ciudades");
        } else {
            loadSpinner("Municipios", spinner_ciudades, ciudades);
        }


        if (tipos_solicitud == null) {
            HashMap params = new HashMap();
            params.put("page", "1");
            params.put("limit", "200");
            String url = this.getString(R.string.url_pqr) + this.getString(R.string.pqr_solicitude_type);
            api.sendRequest(params, url, "get", "tipos_solicitud");
        } else {
            loadSpinner("Tipos de solicitud", spinner_tipos_solicitud, tipos_solicitud);
        }
        if (tematicas == null) {
            HashMap params = new HashMap();
            params.put("page", "1");
            params.put("limit", "200");
            String url = this.getString(R.string.url_pqr) + this.getString(R.string.pqr_themes);
            api.sendRequest(params, url, "get", "tematicas");
        } else {
            loadSpinner("Temáticas", spinner_tematicas, tematicas);
        }
        if (tipos_documento == null) {
            HashMap params = new HashMap();
            params.put("page", "1");
            params.put("limit", "200");
            String url = this.getString(R.string.url_pqr) + this.getString(R.string.pqr_document_types);
            api.sendRequest(params, url, "get", "tipos_documento");
        } else {
            loadSpinner("Tipos de documento", spinner_tipos_documento, tipos_documento);
        }

        if (error_anonymous) {
            radio_anonymous_ttl.setError("");
        }
        if (comuna_error) {
            if ((spinner_comunas.getSelectedView()) != null){
                ((TextView)spinner_comunas.getSelectedView()).setError("");
            }
        }
        if (tipos_documento_error) {
            if ((spinner_tipos_documento.getSelectedView())!= null ){
                ((TextView)spinner_tipos_documento.getSelectedView()).setError("");
            }
        }
        if (error_email) {
            radio_email_ttl.setError("");
        }

    }


    private ArrayList<PqrSpinnerClass> stringToObjects(String string_json) {
        ArrayList<PqrSpinnerClass> array = new ArrayList<PqrSpinnerClass>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<PqrSpinnerClass>>() {
        }.getType();
        array = gson.fromJson(string_json, collectionType);
        return array;
    }

    private void loadSpinner(String first_value, Spinner spinner, ArrayList<PqrSpinnerClass> items) {
        InderSpinnerAdapter adapter = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapter.add(first_value);
        for (PqrSpinnerClass object : items) {
            adapter.add(object.getNombre());
        }
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
    }


    public void setIs_file(boolean is_file) {
        this.is_file = is_file;
    }


    @Override
    public void onfinishedTrue(JSONObject response, String from) {
        if (this.isAdded()) {
            if (from.equals("ciudades")) {
                Log.w(TAG, "respuesta:" + response.toString());
                try {
                    JSONArray items = (JSONArray) response.get("items");
                    String jsonString = "[";
                    for (int i = 0, size = items.length(); i < size; i++) {
                        JSONObject ciudad = items.getJSONObject(i);
                        String convertedString = null;
                        try {
                            convertedString =  Normalizer.normalize(ciudad.getString("nombre").toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (convertedString.equals("medellin")){
                            jsonString += "{id:" +ciudad.getString("id")+",";
                            jsonString += "nombre:" +"\""+ciudad.getString("nombre")+"\"},";
                        }
                    }
                    for (int i = 0, size = items.length(); i < size; i++) {
                        JSONObject ciudad = items.getJSONObject(i);
                        jsonString += "{id:" +ciudad.getString("id")+",";
                        jsonString += "nombre:" +"\""+ciudad.getString("nombre")+"\"}";
                        if (i+1 < size){
                            jsonString += ",";
                        }
                    }
                    jsonString += "]";
                    JSONArray json = new JSONArray(jsonString);
                    response.put("items",json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ciudades = loadResponse(response, spinner_ciudades, "Municipios");
                Log.w(TAG, "items:" + ciudades.toString());
            } else if (from.equals("comunas")) {
                Log.w(TAG, "respuesta:" + response.toString());
                comunas = loadResponse(response, spinner_comunas, "Comunas");
                if (comunas.size() > 0){
                    comuna_zone.setVisibility(View.VISIBLE);
                }
                Log.w(TAG, "items:" + comunas.toString());
            } else if (from.equals("tipos_solicitud")) {
                Log.w(TAG, "respuesta:" + response.toString());
                try {
                    JSONArray items = (JSONArray) response.get("items");
                    String jsonString = "[";
                    for (int i = 0, size = items.length(); i < size; i++) {
                        JSONObject tipo_solicitud = items.getJSONObject(i);
                        switch (tipo_solicitud.getString("nombre")){
                            case "Agradecimiento o Felicitacion":
                                jsonString += "{id:" +tipo_solicitud.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Agradecimiento o felicitación"+"\"}";
                                break;
                            case "Solicitud de Certificado":
                                jsonString += "{id:" +tipo_solicitud.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Solicitud de certificado"+"\"}";
                                break;
                            case "Solicitud de Informacion":
                                jsonString += "{id:" +tipo_solicitud.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Solicitud de información"+"\"}";
                                break;
                            case "Solicitud de Servicio":
                                jsonString += "{id:" +tipo_solicitud.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Solicitud de servicio"+"\"}";
                                break;
                            case "Sugerencia o Comentario":
                                jsonString += "{id:" +tipo_solicitud.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Sugerencia o comentario"+"\"}";
                                break;
                            case "Tratamiento de Datos Personales":
                                jsonString += "{id:" +tipo_solicitud.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Tratamiento de datos personales"+"\"}";
                                break;
                            default:
                                jsonString += "{id:" +tipo_solicitud.getString("id")+",";
                                jsonString += "nombre:" +"\""+tipo_solicitud.getString("nombre")+"\"}";
                                break;

                        }
                        if (i+1 < size){
                            jsonString += ",";
                        }
                    }
                    jsonString += "]";
                    JSONArray json = new JSONArray(jsonString);
                    response.put("items",json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tipos_solicitud = loadResponse(response, spinner_tipos_solicitud, "Tipos de solicitud");
            } else if (from.equals("tematicas")) {
                Log.w(TAG, "respuesta:" + response.toString());
                try {
                    JSONArray items = (JSONArray) response.get("items");
                    String jsonString = "[";
                    for (int i = 0, size = items.length(); i < size; i++) {
                        JSONObject tematica = items.getJSONObject(i);
                        switch (tematica.getString("nombre")){
                            case "Administracion Unidades Deportivas satélites":
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Administración Unidades Deportivas Satélite"+"\"}";
                                break;
                            case "Calidad del Servicio":
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Calidad del servicio"+"\"}";
                                break;
                            case "Competencia del Recurso Humano":
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Competencia del recurso humano"+"\"}";
                                break;
                            case "Administracion Escenarios deportivos barriales":
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Administración escenarios deportivos barriales"+"\"}";
                                break;
                            case "Administracion Unidad Deportiva Atanasio Girardot":
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Administración Unidad Deportiva Atanasio Girardot"+"\"}";
                                break;
                            case "Oferta deportiva, recreativa y de Actividad fisica":
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Oferta deportiva, recreativa y  de actividad física"+"\"}";
                                break;
                            case "Solicitud de informacion":
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Solicitud de información"+"\"}";
                                break;
                            case "Contrucción, adecuacion o mantenimiento de escenarios dyr":
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Construcción, adecuación o mantenimiento de escenarios dyr"+"\"}";
                                break;
                            default:
                                jsonString += "{id:" +tematica.getString("id")+",";
                                jsonString += "nombre:" +"\""+tematica.getString("nombre")+"\"}";
                                break;

                        }
                        if (i+1 < size){
                            jsonString += ",";
                        }
                    }
                    jsonString += "]";
                    JSONArray json = new JSONArray(jsonString);
                    response.put("items",json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tematicas = loadResponse(response, spinner_tematicas, "Temáticas");
            } else if (from.equals("tipos_documento")) {
                Log.w(TAG, "respuesta:" + response.toString());try {
                    JSONArray items = (JSONArray) response.get("items");
                    String jsonString = "[";
                    for (int i = 0, size = items.length(); i < size; i++) {
                        JSONObject tipo_documento = items.getJSONObject(i);
                        switch (tipo_documento.getString("nombre")){
                            case "Cedula":
                                jsonString += "{id:" +tipo_documento.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Cédula"+"\"}";
                                break;
                            case "Cedula extranjeria":
                                jsonString += "{id:" +tipo_documento.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Cédula de extranjería"+"\"}";
                                break;
                            case "Tarjeta Identidad":
                                jsonString += "{id:" +tipo_documento.getString("id")+",";
                                jsonString += "nombre:" +"\""+"Tarjeta de identidad"+"\"}";
                                break;
                            default:
                                jsonString += "{id:" +tipo_documento.getString("id")+",";
                                jsonString += "nombre:" +"\""+tipo_documento.getString("nombre")+"\"}";
                                break;

                        }
                        if (i+1 < size){
                            jsonString += ",";
                        }
                    }
                    jsonString += "]";
                    JSONArray json = new JSONArray(jsonString);
                    response.put("items",json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tipos_documento = loadResponse(response, spinner_tipos_documento, "Tipos de documento");
            }
        }

        if (from.equals("pqr_send")) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            String number_reg = "";
            if (this.isAdded()) {
                Log.w(TAG, "send:" + response.toString());
                try {
                    number_reg = response.get("numero_solicitud").toString();
                    dismissProgress();
                    ((UserAtentionFragment) getParentFragment()).showPqrConfirmationFragment(number_reg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    onfinishedFalse("");
                    dismissProgress();
                }
            }
        }

    }

    public ArrayList<PqrSpinnerClass> loadResponse(JSONObject response, Spinner spinner, String first_object) {
        InderSpinnerAdapter adapter = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
        adapter.add(first_object);
        ArrayList<PqrSpinnerClass> items_array = new ArrayList<>();
        try {
            JSONArray items = (JSONArray) response.get("items");
            Log.w(TAG, "items:" + items.toString());
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<PqrSpinnerClass>>() {
            }.getType();
            items_array = gson.fromJson(items.toString(), listType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (PqrSpinnerClass object : items_array) {
            adapter.add(object.getNombre());
        }
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);


        return items_array;
    }

    @Override
    public void onfinishedFalse(String error) {
        Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_LONG).show();
        dismissProgress();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner_ciudades) {
            if (pos > 0) {
                Log.w(TAG, "seleccion_ciudad" + ciudades.get(pos - 1));
                HashMap params = new HashMap();
                params.put("page", "1");
                params.put("limit", "200");
                params.put("municipio_id", ciudades.get(pos - 1).getId());
                String url = this.getString(R.string.url_pqr) + this.getString(R.string.pqr_communes);
                url = url.replace("{municipio_id}",params.get("municipio_id").toString());
                api.sendRequest(params, url, "get", "comunas");
            } else {
                comuna_zone.setVisibility(View.GONE);
            }
        } else if (spinner.getId() == R.id.spinner_comunas) {
            if (pos > 0) {
                ((TextView)spinner_comunas.getSelectedView()).setError(null);
            }
        } else if (spinner.getId() == R.id.spinner_tipos_documento) {
            if (pos > 0) {
                ((TextView)spinner_tipos_documento.getSelectedView()).setError(null);
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        Log.w(TAG, "pause_fragment");
        api.cancelAll();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (progressDialog != null && getRetainInstance())
            progressDialog.setDismissMessage(null);
        super.onDestroyView();
    }


    @Override
    public void onFinishedAuthentication(UserContainer userContainer, Boolean authenticated) {

    }

    @Override
    public void onFinishedRegister(Boolean success, HashMap data) {

    }

    @Override
    public void onFinishedForgetPassword() {

    }

    @Override
    public void onFinishedForgetUser() {

    }

    @Override
    public void onFinishedAddParticipant(Participant participant, Integer status) {

    }

    @Override
    public void onFinishedProfile(ApiUser.Info responseData) {

    }

    @Override
    public void onResume() {
        if (!user_info_loaded) {
            if (g.getUserIsAuthenticated()) {
                HashMap params = new HashMap();
                params.put("token", g.getToken());
                api_user.getProfile(params);
            }
        }
        super.onResume();
    }

    @Override
    public void onFinishedProfilePqr(JSONObject response) {
        Log.w(TAG, "datos:" + response.toString());
        if (this.isAdded()){
            try {
                final JSONObject info = response.getJSONObject("info");
                if (info.getString("email")!=null){
                    if(!info.getString("email").trim().equals("")){
                        email.setText(info.getString("email"));
                    }
                }
                if (info.getString("telefono1")!=null){
                    if(!info.getString("telefono1").trim().equals("")){
                        phone.setText(info.getString("telefono1"));
                    }
                }
                if (info.getString("celular")!=null){
                    if(!info.getString("celular").trim().equals("")){
                        mobile.setText(info.getString("celular"));
                    }
                }
                if (info.getString("id")!=null){
                    if(!info.getString("id").trim().equals("")){
                        id_number.setText(info.getString("id"));
                    }
                }
                String nombre="";
                if (info.getString("primer_nombre")!=null){
                    if(!info.getString("primer_nombre").trim().equals("")){
                        nombre+=info.getString("primer_nombre")+" ";
                    }
                }
                if (info.getString("segundo_nombre")!=null){
                    if(!info.getString("segundo_nombre").trim().equals("")){
                        nombre+=info.getString("segundo_nombre")+" ";
                    }
                }
                if (info.getString("primer_apellido")!=null){
                    if(!info.getString("primer_apellido").trim().equals("")){
                        nombre+=info.getString("primer_apellido");
                    }
                }
                if (!nombre.trim().equals("")){
                    names.setText(nombre);
                }
                // comparacion de tipo de identificacion con el usuario, se espera unos segundos a que cargue la lista de tipos del api
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String convertedString = null;
                        try {
                            convertedString = Normalizer.normalize(info.getJSONObject("tipo_documento").getString("nombre").toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        convertedString = Normalizer.normalize(convertedString, Normalizer.Form.NFD).replaceAll(" de ", " ");
                        InderSpinnerAdapter adapter = new InderSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        int position = 0;
                        boolean positionChange = false;
                        for (int i = 0; i < tipos_documento.size(); i++) {
                            PqrSpinnerClass object = tipos_documento.get(i);
                            String first = Normalizer.normalize( object.getNombre().toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                            String second = convertedString;
                            if (second.contains(first) || first.equals(second)) {
                                position = i;
                                positionChange = true;
                            }
                            adapter.add(object.getNombre());
                        }
                        if(positionChange){
                            spinner_tipos_documento.setAdapter(adapter);
                            spinner_tipos_documento.setSelection(position);
                        }
                    }
                }, 1500);
                user_info_loaded=true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
