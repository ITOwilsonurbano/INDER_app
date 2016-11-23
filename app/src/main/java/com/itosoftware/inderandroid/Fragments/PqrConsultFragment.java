package com.itosoftware.inderandroid.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;;
import com.itosoftware.inderandroid.Api.Pqr.ApiPqr;
import com.itosoftware.inderandroid.Interface.OnLoadMoreListener;
import com.itosoftware.inderandroid.Interface.PqrListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Api.Pqr.Pqr;
import com.itosoftware.inderandroid.Utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PqrConsultFragment extends Fragment implements View.OnClickListener, PqrListener {
    View rootView;
    ImageView progress;
    View back;
    View container_view;
    View search;
    private RecyclerView recycler;
    private PqrAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private static String TAG = "PqrUserInfoFragment";
    private List items;
    private SwipeRefreshLayout refreshLayout;
    ApiPqr api;
    int page = 1;
    String numero_solicitud_value = null;
    String identificacion_value = null;
    String fecha_desde_txt_value = null;
    String fecha_hasta2_txt_value = null;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pqr_consult, container, false);
        container_view = rootView.findViewById(R.id.container);
        back = rootView.findViewById(R.id.back);
        back.setOnClickListener(this);
        search = rootView.findViewById(R.id.search);
        search.setOnClickListener(this);
        search.performClick();
        items = new ArrayList();

        recycler = (RecyclerView) rootView.findViewById(R.id.rv);

        lManager = new CustomGridLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        adapter = new PqrAdapter();
        recycler.setAdapter(adapter);
        api = new ApiPqr(this);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (items.size() > 0 && items.get(items.size()-1) != null){
                    items.add(null);
                    adapter.notifyItemInserted(items.size() - 1);
                    adapter.notifyDataSetChanged();
                    adapter.setLoaded();
                    refreshLayout.setRefreshing(false);
                }
                //Load data
                page++;
                sendPetition(page);
                //Load more data for reyclerview
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Remove loading item
                        if (items.size() > 0 && (items.get(items.size()-1) == null || items.get(0) == null)){
                            if(items.get(items.size()-1) == null){
                                items.remove(items.size() - 1);
                                adapter.notifyItemRemoved(items.size() -1 );
                            }
                            if(items.get(0) == null){
                                items.remove(0);
                                adapter.notifyItemRemoved(0);
                            }
                        }
                    }
                }, 5000);

            }
        });

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ((CustomGridLayoutManager)lManager).setScrollEnabled(true);
                        items.clear();
                        //Load data
                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();
                        //Load more data for reyclerview
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Remove loading item
                                if (items.size() > 0 && (items.get(items.size()-1) == null || items.get(0) == null)){
                                    if(items.get(items.size()-1) == null){
                                        items.remove(items.size() - 1);
                                        adapter.notifyItemRemoved(items.size() -1 );
                                    }
                                    if(items.get(0) == null){
                                        items.remove(0);
                                        adapter.notifyItemRemoved(0);
                                    }
                                }
                            }
                        }, 5000);
                        //Load data
                        page = 1;
                        sendPetition(page);
                    }
                }
        );
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {

                    @Override public void onItemClick(View view, int position) {
                        try{
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            String object = "null";
                            if(position == -1){
                                object = gson.toJson(items.get(position+1));
                            }else{
                                object = gson.toJson(items.get(position));
                            }
                            if(!object.equals("null")){
                                bundle.putString("object", object);
                                DialogFragmentPqr dialog = new DialogFragmentPqr();
                                dialog.setArguments(bundle);
                                dialog.show(getChildFragmentManager(), "Diag");
                            }
                        }catch (Exception e){
                            Log.e("click Item",e.getMessage());
                        }
                    }
                })
        );
        return rootView;
    }

    public void sendPetition(int page){
        if (this.isAdded()) {
            HashMap params = new HashMap();
            String complemento = "?pagina="+page+"&elementos_por_pagina=10";
            if (numero_solicitud_value != null){
                complemento +="&numero_solicitud="+numero_solicitud_value;
            }else{
                complemento +="&numero_solicitud=";
            }
            if (identificacion_value != null){
                complemento +="&identificacion="+identificacion_value;
            }else{
                complemento +="&identificacion=";
            }
            if (fecha_desde_txt_value != null){
                complemento +="&fecha_desde="+fecha_desde_txt_value;
            }else{
                complemento +="&fecha_desde=";
            }
            if (fecha_hasta2_txt_value != null){
                complemento +="&fecha_hasta="+fecha_hasta2_txt_value;
            }else{
                complemento +="&fecha_hasta=";
            }
            String url = this.getString(R.string.url_pqr) + this.getString(R.string.pqr_send)+complemento;
            api.sendRequest(params, url, "get", "pqrs");
        }
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();
        if (tag.equals("back")) {
            Log.w(TAG, "back");
            hydeSoftKeyboard();
            ((UserAtentionFragment) getParentFragment()).showMenuFragment(3);
        }else if (tag.equals("search")) {
            Log.w(TAG, "search");
            hydeSoftKeyboard();
            DialogFragmentPqrFilter dialog = new DialogFragmentPqrFilter();
            dialog.setTargetFragment(this, 1);
            dialog.show(getFragmentManager(), "Diag");
        }
    }


    public void hydeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    public void onfinishedTrue(JSONObject response, String from) {
        if (this.isAdded()) {
            if (from.equals("pqrs")) {
                try{
                    JSONArray datos = response.getJSONArray("items");
                    for (int i = 0, size = datos.length(); i < size; i++) {
                        JSONObject pqr = datos.getJSONObject(i);
                        Pqr p = new Pqr(
                                pqr.getString("numero_proceso"),
                                pqr.getString("numero_proceso"),
                                pqr.getString("numero_solicitud"),
                                pqr.getString("identificacion"),
                                pqr.getString("estado"),
                                pqr.getString("funcionario_responsable"),
                                pqr.getString("fecha_registro"),
                                pqr.getString("fecha_estimada_respuesta"),
                                pqr.getString("fecha_respuesta"),
                                pqr.getString("documento")
                        );
                        items.add(p);
                    }
                    adapter.notifyDataSetChanged();
                    adapter.setLoaded();
                    refreshLayout.setRefreshing(false);
                    ((CustomGridLayoutManager)lManager).setScrollEnabled(true);
                    if (items.size() == 0){
                        Toast.makeText(getActivity(), "No hay resultados, intenta con otros términos de búsqueda.", Toast.LENGTH_LONG).show();
                    }else if (datos.length() == 0){
                        Toast.makeText(getActivity(), "No hay más entradas para mostrar", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Log.e("pqrs",e.getMessage());
                }
            }
        }
    }

    @Override
    public void onfinishedFalse(String error) {
        Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_LONG).show();
    }


    static class PqrViewHolder extends RecyclerView.ViewHolder {
        public TextView numero_proceso;
        public TextView numero_solicitud;
        public TextView identificacion;
        public TextView fecha_registro;
        public PqrViewHolder(View v) {
            super(v);
            numero_proceso = (TextView) v.findViewById(R.id.numero_proceso);
            numero_solicitud = (TextView) v.findViewById(R.id.numero_solicitud);
            identificacion  = (TextView) v.findViewById(R.id.identificacion);
            fecha_registro = (TextView) v.findViewById(R.id.fecha_registro);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }


    class PqrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private OnLoadMoreListener mOnLoadMoreListener;

        private boolean isLoading;
        private int visibleThreshold = 2;
        private int lastVisibleItem, totalItemCount;

        public PqrAdapter() {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recycler.getLayoutManager();
            recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.pqr_section_list, parent, false);
                return new PqrViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try{
                if (holder instanceof PqrViewHolder) {
                    Pqr pqr = (Pqr) items.get(position);
                    PqrViewHolder pqrViewHolder = (PqrViewHolder) holder;
                    pqrViewHolder.numero_proceso.setText(pqr.getNumero_proceso());
                    pqrViewHolder.numero_solicitud.setText(pqr.getNumero_solicitud());
                    pqrViewHolder.identificacion.setText(pqr.getIdentificacion());
                    pqrViewHolder.fecha_registro.setText(pqr.getFecha_registro());
                } else if (holder instanceof LoadingViewHolder) {
                    LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setIndeterminate(true);
                }
            }catch(Exception e){
                Log.e("Reload",e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        public void setLoaded() {
            isLoading = false;
        }
    }
    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {

            return isScrollEnabled && super.canScrollVertically();
        }
    }

    public List getItems() {
        return items;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1){
            numero_solicitud_value = data.getStringExtra("numero_solicitud");
            identificacion_value = data.getStringExtra("identificacion");
            fecha_desde_txt_value = data.getStringExtra("fecha_desde_txt");
            fecha_hasta2_txt_value = data.getStringExtra("fecha_hasta2_txt");
            items.clear();
            //Load data
            items.add(null);
            adapter.notifyItemInserted(items.size() - 1);
            adapter.notifyDataSetChanged();
            adapter.setLoaded();
            //Load data
            page = 1;
            sendPetition(page);
            //Load more data for reyclerview
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Remove loading item
                    if (items.size() > 0 && (items.get(items.size()-1) == null || items.get(0) == null)){
                        if(items.get(items.size()-1) == null){
                            items.remove(items.size() - 1);
                            adapter.notifyItemRemoved(items.size() -1 );
                        }
                        if(items.get(0) == null){
                            items.remove(0);
                            adapter.notifyItemRemoved(0);
                        }
                    }
                }
            }, 5000);
        }
    }
}


