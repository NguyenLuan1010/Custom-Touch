package com.example.customtouch.Presenter;

import com.example.customtouch.Interface.In_ControlHome;

public class ControlHome_Presenter {
    private In_ControlHome in_controlHome;

    public ControlHome_Presenter(In_ControlHome in_controlHome) {
        this.in_controlHome = in_controlHome;
    }
    public void createPackageApp(){
        in_controlHome.packageApp();
    }
    public void initializerView(){
        in_controlHome.buildViewForControlHome();
    }
    public void setStatusButtonControl(){
        in_controlHome.statusButtonControl();
    }
    public void addEventOnClick(){
        in_controlHome.addEvent();
    }
    public void show_Dialog_To_Confirm_Disable_DeviceAdmin(){
        in_controlHome.confirmToDisableDeviceAdmin();
    }
}
