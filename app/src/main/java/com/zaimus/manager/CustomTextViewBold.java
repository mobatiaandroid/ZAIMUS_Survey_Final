package com.zaimus.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextViewBold extends androidx.appcompat.widget.AppCompatTextView {

    String customFont;

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {


        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + "Lato_Bold" + ".ttf");
        setTypeface(tf);
    }
}