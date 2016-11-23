package com.itosoftware.inderandroid.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.Pqr.Pqr;
import com.itosoftware.inderandroid.Interface.OnLoadMoreListener;
import com.itosoftware.inderandroid.R;
import com.itosoftware.inderandroid.Utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class PqrConfirmationFragment extends Fragment implements View.OnClickListener {
    View rootView;
    ImageView progress;
    View back;
    TextView numero_reg;
    private static String TAG = "PqrConfirmationFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pqr_confirmation, container, false);
        back = rootView.findViewById(R.id.back);
        back.setOnClickListener(this);
        numero_reg = (TextView) rootView.findViewById(R.id.numero_reg);
        numero_reg.setText(getArguments().getString("numero_reg"));
        return rootView;
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();
        if (tag.equals("back")) {
            Log.w(TAG, "back");
            ((UserAtentionFragment) getParentFragment()).showMenuFragment(4);
        }
    }



}


