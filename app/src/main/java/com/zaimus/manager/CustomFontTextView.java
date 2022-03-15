package com.zaimus.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by ANKIT
 */
@SuppressLint("AppCompatCustomView")
public class CustomFontTextView extends TextView {

    String customFont;

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);

    }

    private void style(Context context, AttributeSet attrs) {


        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + "Lato_Light" + ".ttf");
        setTypeface(tf);
    }
}