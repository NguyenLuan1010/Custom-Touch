package com.example.customtouch.Interface;

import android.content.pm.PackageManager;

import com.example.customtouch.Model.Apps;

public interface In_ListAppsActivity {
    void buildViewForListActivity();
    void loadApp();
    void getAppFromAPI_R(PackageManager packageManager);
    void getAppOtherAPI(PackageManager packageManager);
    void dataRecycleView();
    void OnClickItemApps(Apps apps);
}
