package com.itosoftware.inderandroid.graphicstest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.itosoftware.inderandroid.Adapters.InderMultiSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rasuncion on 9/12/15.
 */
public class MultiSpinner extends Spinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    private String nombreMultiSpinner;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2){
                // Quita la coma del último
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
            }
        } else {
            spinnerText = defaultText;
        }
        ArrayList <String> itemstext = new ArrayList<String>();
        itemstext.add(spinnerText);
        ArrayAdapter<String> adapter = new InderMultiSpinnerAdapter(getContext(),android.R.layout.simple_spinner_item, itemstext);

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });*/
        setAdapter(adapter);
        // Seleccionados de la opción múltiple
        listener.onItemsSelectedMultiSpinner(selected, getNombreMultiSpinner());
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items,MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = items.toString().replace("[","").replace("]","");
        this.listener = listener;

        // all selected by default
        selected = new boolean[items.size()];
        // Primera Posición seleccionada.
        selected[0] = true;
        for (int i = 1; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new InderMultiSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, items);
        setAdapter(adapter);
        // Por si desea el que aparece primero y no entra en la selección múltiple.
        listener.onItemsSelectedMultiSpinner(selected, getNombreMultiSpinner());
    }

    public void reinitItems() {
        // all selected by default
        selected = new boolean[items.size()];
        // Primera Posición seleccionada.
        selected[0] = true;
        for (int i = 1; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new InderMultiSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, items);
        setAdapter(adapter);
        // Por si desea el que aparece primero y no entra en la selección múltiple.
        listener.onItemsSelectedMultiSpinner(selected, getNombreMultiSpinner());
    }

    public interface MultiSpinnerListener {
        public void onItemsSelectedMultiSpinner(boolean[] selected, String nombreMultiSpinner);
    }

    public String getNombreMultiSpinner() {
        return nombreMultiSpinner;
    }

    public void setNombreMultiSpinner(String nombreMultiSpinner) {
        this.nombreMultiSpinner = nombreMultiSpinner;
    }
}