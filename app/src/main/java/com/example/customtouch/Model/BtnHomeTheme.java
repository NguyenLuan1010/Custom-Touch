package com.example.customtouch.Model;

import androidx.annotation.DrawableRes;

import com.example.customtouch.R;

public enum BtnHomeTheme {

    HB_BLACK(R.drawable.hb_black),
    HB_BLACK_AROUND(R.drawable.hb_black_around),
    HB_BLACK_STROKE(R.drawable.hb_black_stroke),
    HB_BLUE(R.drawable.hb_blue),
    HB_STROKE2(R.drawable.hb_stroke2),
    HB_STROKE_GRAY(R.drawable.hb_stroke_gray);

    final int drawableRes;

    BtnHomeTheme(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public static BtnHomeTheme findByRes(@DrawableRes int drawableRes){
        for(BtnHomeTheme btnHomeTheme:values()){
            if(btnHomeTheme.drawableRes == drawableRes){
                return btnHomeTheme;
            }
        }
        return HB_BLUE;
    }
}
