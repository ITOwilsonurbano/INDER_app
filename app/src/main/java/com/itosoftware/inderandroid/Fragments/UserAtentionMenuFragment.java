package com.itosoftware.inderandroid.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itosoftware.inderandroid.R;


public class UserAtentionMenuFragment extends Fragment implements View.OnClickListener {
    View rootView;
    ImageView progress;
    private static String TAG = "UserAtentionMenuFragment";
    private View register_button;
    private View consult_button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_atention_menu, container, false);
        register_button = rootView.findViewById(R.id.register_button);
        register_button.setOnClickListener(this);
        consult_button = rootView.findViewById(R.id.consult_button);
        consult_button.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        String tag;
        tag = v.getTag().toString();
        if (tag.equals("register_button")) {
            ((UserAtentionFragment) getParentFragment()).showPqrFragment();
        }else if (tag.equals("consult_button")){
            ((UserAtentionFragment) getParentFragment()).showPqrConsultFragment();
        }
    }
}
