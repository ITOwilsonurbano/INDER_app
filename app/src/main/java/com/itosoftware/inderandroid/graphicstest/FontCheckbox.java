package com.itosoftware.inderandroid.graphicstest;

/**
 * Created by usuario on 26/03/15.
 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class FontCheckbox extends CheckBox {

    public FontCheckbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontCheckbox(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeueLTStd-LtCn.otf");
        setTypeface(tf);
    }
}