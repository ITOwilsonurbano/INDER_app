package com.itosoftware.inderandroid.graphicstest;

/**
 * Created by usuario on 20/03/15.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class FontButton extends Button {

    public FontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(!isInEditMode()) {
            init();
        }
    }

    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            init();
        }
    }

    public FontButton(Context context) {
        super(context);
        if(!isInEditMode()) {
            init();
        }
    }

    public void init() {
        if(!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeueLTStd-LtCn.otf");
            setTypeface(tf);
        }
    }
}