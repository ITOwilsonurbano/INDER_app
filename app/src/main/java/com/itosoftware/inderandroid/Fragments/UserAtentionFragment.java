package com.itosoftware.inderandroid.Fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.itosoftware.inderandroid.Api.Recommended.Recommended;
import com.itosoftware.inderandroid.App.Globals;
import com.itosoftware.inderandroid.R;


public class UserAtentionFragment extends Fragment {
    View rootView;
    UserAtentionMenuFragment firstFragment;
    PqrFragment secondFragment;
    PqrConsultFragment thirdFragment;
    PqrConfirmationFragment forthFragment;
    ImageView progress;
    Globals g;
    int act_fragment;
    private static String TAG = "UserAtentionFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        rootView = inflater.inflate(R.layout.fragment_user_atention, container, false);
        g = (Globals) getActivity().getApplication();

        progress = (ImageView) rootView.findViewById(R.id.fragment_user_atention_loading_panel);
        progress.setVisibility(View.GONE);
        if (savedInstanceState == null) {
            if (rootView.findViewById(R.id.frame_layout) != null) {

                // Create a new Fragment to be placed in the activity layout
                firstFragment = new UserAtentionMenuFragment();
                secondFragment = new PqrFragment();
                thirdFragment = new PqrConsultFragment();
                forthFragment = new PqrConfirmationFragment();
                // Add the fragment to the 'fragment_container' FrameLayout
                getChildFragmentManager().beginTransaction()
                        .add(R.id.frame_layout, firstFragment, "first").commit();
                act_fragment = 1;

            }
        } else {
            if (getChildFragmentManager().findFragmentByTag("first") != null) {
                firstFragment = (UserAtentionMenuFragment) getChildFragmentManager().findFragmentByTag("first");
            } else {
                firstFragment = new UserAtentionMenuFragment();
            }
            if (getChildFragmentManager().findFragmentByTag("second") != null) {
                secondFragment = (PqrFragment) getChildFragmentManager().findFragmentByTag("second");
            } else {
                secondFragment = new PqrFragment();
            }
            if (getChildFragmentManager().findFragmentByTag("third") != null) {
                thirdFragment = (PqrConsultFragment) getChildFragmentManager().findFragmentByTag("third");
            } else {
                thirdFragment = new PqrConsultFragment();
            }
            if (getChildFragmentManager().findFragmentByTag("forth") != null) {
                forthFragment = (PqrConfirmationFragment) getChildFragmentManager().findFragmentByTag("forth");
            } else {
                forthFragment = new PqrConfirmationFragment();
            }
        }
        return rootView;

    }

    public void showPqrFragment() {
        getChildFragmentManager().beginTransaction()
                .remove(getChildFragmentManager().findFragmentByTag("first")).commit();
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame_layout, secondFragment, "second").commit();
    }

    public void showPqrConsultFragment() {
        getChildFragmentManager().beginTransaction()
                .remove(getChildFragmentManager().findFragmentByTag("first")).commit();
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame_layout, thirdFragment, "third").commit();
    }

    public void showMenuFragment(int fragment_number) {
        if (fragment_number == 2) {
            getChildFragmentManager().beginTransaction()
                    .remove(getChildFragmentManager().findFragmentByTag("second")).commit();
        } else if (fragment_number == 3) {
            getChildFragmentManager().beginTransaction()
                    .remove(getChildFragmentManager().findFragmentByTag("third")).commit();
        } else if (fragment_number == 4) {
            forthFragment = new PqrConfirmationFragment();
            getChildFragmentManager().beginTransaction()
                    .remove(getChildFragmentManager().findFragmentByTag("forth")).commit();
        }
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame_layout, firstFragment, "first").commit();
    }

    public void showPqrConfirmationFragment(String numero_reg) {
        getChildFragmentManager().beginTransaction()
                .remove(getChildFragmentManager().findFragmentByTag("second")).commit();
        secondFragment = new PqrFragment();
        Bundle data = new Bundle();
        data.putString("numero_reg", numero_reg);
        forthFragment.setArguments(data);
        getChildFragmentManager().beginTransaction()
                .add(R.id.frame_layout, forthFragment, "forth").commit();
    }

}
