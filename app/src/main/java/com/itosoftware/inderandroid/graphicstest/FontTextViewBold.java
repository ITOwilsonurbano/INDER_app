package com.itosoftware.inderandroid.graphicstest;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextViewBold extends TextView {

    public FontTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(!isInEditMode()) {
            init();
        }
    }

    public FontTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            init();
        }
    }

    public FontTextViewBold(Context context) {
        super(context);
            if(!isInEditMode()) {
                init();
            }
    }

    public void init() {
        if(!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeueLTStd-BdCn.otf");
            setTypeface(tf);
        }
    }
}