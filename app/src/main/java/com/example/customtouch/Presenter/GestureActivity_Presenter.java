package com.example.customtouch.Presenter;

import com.example.customtouch.Interface.In_GestureActivity;

public class GestureActivity_Presenter {
    private In_GestureActivity in_gestureActivity;

    public GestureActivity_Presenter(In_GestureActivity in_gestureActivity) {
        this.in_gestureActivity = in_gestureActivity;
    }
    public void initializerView(){
        in_gestureActivity.buildViewForGestureActivity();
    }
    public void addEventClickListener(){
        in_gestureActivity.eventOnClick();
    }
    public void addGestureToFile(){
        in_gestureActivity.gestureToFile();
    }
}
