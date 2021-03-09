package com.example.orientation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

public class MainActivity extends AppCompatActivity {
    public static String MA = "MainActivity";
    public static int ACTION_BAR_HEIGHT = 56; // vertical, in dp units

    private float pixelDensity;
    private boolean verticalDimensionsSet;
    public static int screenHeightInVP;
    private int spacingInVP;

    private boolean horizontalDimensionsSet;
    public static int screenHeightInHP;
    private int spacingInHP;

    private Button b1, b2, b3;
    private int actionBarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpGui();
        Resources res = getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        pixelDensity = metrics.density;
        Configuration config = getResources().getConfiguration();
        checkDimensions(config);
        modifyLayout(config);
    }

    public void checkDimensions(Configuration config){
        // retrieve ActionBar height
        actionBarHeight = ( int ) (pixelDensity * ACTION_BAR_HEIGHT);
        TypedValue tv = new TypedValue();
        if(getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        Log.w(MA, "action bar height = " + actionBarHeight);

        // measure button height
        b1.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int buttonHeight = b1.getMeasuredHeight();

        // set spacing between buttons depending on orientation
        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            screenHeightInHP = (int)(config.screenHeightDp * pixelDensity);
            spacingInHP = (screenHeightInHP - actionBarHeight - 3 * buttonHeight) / 4;
            horizontalDimensionsSet = true;
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT){
            screenHeightInVP = (int)(config.screenHeightDp * pixelDensity);
            spacingInVP = (screenHeightInVP - actionBarHeight - 3 * buttonHeight) / 4;
            verticalDimensionsSet = true;
        }
    }

    public void setUpGui(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(LinearLayout.VERTICAL);

        b1 = new Button(this);
        b2 = new Button(this);
        b3 = new Button(this);

        b1.setText("GO TO VIEW 1");
        b2.setText("GO TO VIEW 2");
        b3.setText("GO TO VIEW 3");

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        b1.setLayoutParams(params);
        b2.setLayoutParams(params);
        b3.setLayoutParams(params);

        linearLayout.addView(b1);
        linearLayout.addView(b2);
        linearLayout.addView(b3);

        setContentView(linearLayout);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!verticalDimensionsSet || !horizontalDimensionsSet)
            checkDimensions(newConfig);
        modifyLayout(newConfig);
    }

    public void setLayoutMargins(int spacing){
        MarginLayoutParams params1 =
                (MarginLayoutParams) b1.getLayoutParams();
        MarginLayoutParams params2 =
                (MarginLayoutParams) b2.getLayoutParams();
        MarginLayoutParams params3 =
                (MarginLayoutParams) b3.getLayoutParams();

        params1.setMargins(0,spacing,0,0);
        params2.setMargins(0,spacing,0,0);
        params3.setMargins(0,spacing,0,0);
    }

    public void modifyLayout(Configuration newConfig){
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setLayoutMargins(spacingInHP);
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setLayoutMargins(spacingInVP);
        }
    }
}