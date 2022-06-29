package com.example.customtouch.Presenter;

import android.content.pm.PackageManager;
import android.os.Build;

import com.example.customtouch.Interface.In_ListAppsActivity;

public class ListAppsActivity_Presenter {
   private In_ListAppsActivity in_listAppsActivity;

   public ListAppsActivity_Presenter(In_ListAppsActivity in_listAppsActivity) {
      this.in_listAppsActivity = in_listAppsActivity;
   }
   public void initializerView(){
      in_listAppsActivity.buildViewForListActivity();
   }
   public void loadAllApps(){
      in_listAppsActivity.loadApp();
   }
   public void getApps(PackageManager packageManager){
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
         in_listAppsActivity.getAppFromAPI_R(packageManager);
      }else{
         in_listAppsActivity.getAppOtherAPI(packageManager);
      }
   }
   public void setDataForRecycleView(){
      in_listAppsActivity.dataRecycleView();
   }
}
