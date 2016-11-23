package com.itosoftware.inderandroid.graphicstest;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by usuario on 19/03/15.
 */
public class FontRadio extends RadioButton {

    public FontRadio(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontRadio(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontRadio(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Raleway_Regular.ttf");
        setTypeface(tf);
    }
}