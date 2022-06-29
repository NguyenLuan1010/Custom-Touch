package com.example.customtouch.Presenter;

import android.view.View;

import com.example.customtouch.Interface.In_SetGesture;
import com.example.customtouch.Model.Apps;

public class SetGesture_Presenter {
    private In_SetGesture in_setGesture;

    public SetGesture_Presenter(In_SetGesture in_setGesture) {
        this.in_setGesture = in_setGesture;
    }
    public void initializerView(){
        in_setGesture.buildView();
    }
    public void setDataForListGestureApps(){
        in_setGesture.dataForListGestureApps();
    }
    public void setDataForRecycleView(){
        in_setGesture.dataForRecycleView();
    }
    public void showPopUp(Apps gesture, View v, int position){
        in_setGesture.popUpView(gesture,v,position);
    }
    public void deleteGesture(Apps gestureApps){
        in_setGesture.deleteGestureApp(gestureApps);
    }
    public void dialogViewToDelete(Apps gestureApps){
        in_setGesture.confirmToDelete(gestureApps);
    }
    public void editGestureApp(Apps gestureApps, int position){
        in_setGesture.editGestureApp(gestureApps,position);
    }
    public void dialogViewToEdit(Apps gestureApps, int position){
       in_setGesture.dialogForEdit( gestureApps,  position);
    }
}
