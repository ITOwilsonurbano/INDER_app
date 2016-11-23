package com.itosoftware.inderandroid.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.itosoftware.inderandroid.Adapters.AddParticipantsAdapter;
import com.itosoftware.inderandroid.Adapters.ShowParticipantsAdapter;
import com.itosoftware.inderandroid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MessageFragment extends DialogFragment  {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, container, false);

        String message = "";
        Bundle bundle = getArguments();
        if(bundle != null){
            message = bundle.getString("message");
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView textView = (TextView) rootView.findViewById(R.id.fragment_message_text);
        textView.setText(message);

        Button close = (Button) rootView.findViewById(R.id.fragment_message_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dismiss();
            }
        });

        // Do something else
        return rootView;
    }

}