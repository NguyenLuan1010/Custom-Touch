package com.example.customtouch.Interface;

import android.gesture.Prediction;

import java.util.List;

public interface In_BuildItemsOfTasks {

    boolean transferApp(List<Prediction> objPrediction);

    void NormalMode();

    void SilentMode();

    void VibrateMode();

    void LockScreen();

    void WifiStatusFromAPIQ();

    void WifiStatusAnotherAPI();

    void turnOnBluetooth();

    void turnOffBluetooth();

    void blueToothStatus();

    void Location();

    void locationStatus();

    void VolumeUp();

    void VolumeDown();

    void hasCamera();

    void FlashStatus();

    void FlashOn();

    void FlashOff();

    void Rotation();

    void LockRotation();
}
