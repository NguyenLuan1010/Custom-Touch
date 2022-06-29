package com.example.customtouch.Interface;

import android.view.View;

import com.example.customtouch.Model.Apps;

import java.io.File;

public interface In_SetGesture {
    void buildView();
    void dataForListGestureApps();
    void dataForRecycleView();
    void popUpView(Apps gesture, View v, int position);
    void deleteGestureApp(Apps gestureApps);
    void confirmToDelete(Apps gestureApps);
    void editGestureApp(Apps gestureApps, int position);
    void dialogForEdit(Apps gestureApps, int position);
    void OnClickItemGestureApps(Apps apps, View v, int position);
}
