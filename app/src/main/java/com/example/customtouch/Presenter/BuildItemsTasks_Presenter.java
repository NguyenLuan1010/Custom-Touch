package com.example.customtouch.Presenter;

import android.gesture.Prediction;
import android.os.Build;

import com.example.customtouch.Interface.In_BuildItemsOfTasks;

import java.util.List;

public class BuildItemsTasks_Presenter {
    private In_BuildItemsOfTasks in_buildItemsOfTasks;

    public BuildItemsTasks_Presenter(In_BuildItemsOfTasks in_buildItemsOfTasks) {
        this.in_buildItemsOfTasks = in_buildItemsOfTasks;
    }

    public boolean intentTransferApp(List<Prediction> objPrediction){
       return in_buildItemsOfTasks.transferApp(objPrediction);
    }
    public void silentMode() {

        in_buildItemsOfTasks.SilentMode();

    }

    public void vibrateMode() {

        in_buildItemsOfTasks.VibrateMode();

    }

    public void normalMode() {
        in_buildItemsOfTasks.NormalMode();
    }

    public void lockScreen() {
        in_buildItemsOfTasks.LockScreen();
    }

    public void changeWifiStatus() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            in_buildItemsOfTasks.WifiStatusAnotherAPI();
        } else {
            in_buildItemsOfTasks.WifiStatusFromAPIQ();
        }
    }

    public void getBlueToothStatus(){
        in_buildItemsOfTasks.blueToothStatus();
    }
    public void enableBluetooth() {
        in_buildItemsOfTasks.turnOnBluetooth();
    }

    public void disableBluetooth() {
        in_buildItemsOfTasks.turnOffBluetooth();
    }

    public void VolumeUp() {
        in_buildItemsOfTasks.VolumeUp();
    }

    public void VolumeDown() {
        in_buildItemsOfTasks.VolumeDown();
    }

    public void getStatusFlash(){
        in_buildItemsOfTasks.FlashStatus();
    }

    public void TurnOnFlash() {
        in_buildItemsOfTasks.FlashOn();
    }

    public void TurnOffFlash() {
        in_buildItemsOfTasks.FlashOff();
    }

    public void checkDeviceHasCamera(){
        in_buildItemsOfTasks.hasCamera();
    }

    public void IntentLocation(){
        in_buildItemsOfTasks.Location();
    }
    public void CheckLocationStatus(){
        in_buildItemsOfTasks.locationStatus();
    }
    public void OpenRotation(){
        in_buildItemsOfTasks.Rotation();
    }
    public void LockRotation(){
        in_buildItemsOfTasks.LockRotation();
    }
}
