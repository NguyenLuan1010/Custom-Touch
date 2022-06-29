package com.example.customtouch.Activity;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.ActivityManager;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customtouch.Interface.In_ControlHome;
import com.example.customtouch.Model.Apps;
import com.example.customtouch.Presenter.ControlHome_Presenter;
import com.example.customtouch.R;
import com.example.customtouch.Receiver.LockScreen_Receiver;
import com.example.customtouch.Service.HomeButton_Service;

import java.io.File;
import java.util.Objects;

public class ControlHome extends AppCompatActivity implements In_ControlHome, View.OnClickListener {

    private static final String KEY_FOR_SETTING = "setting_manager";
    private static final int VALUE_DEVICE_ADMIN = 1;
    private static final String KEY_CALLBACK_FROM_DEVICE_ADMIN = "status_callback";
    private static final String CALLBACK_RESULT_DATA = "enabled";
    private static final String MESSAGE_TO_DISABLE_CONFIRM = "Are you sure to disable Device Admin?";

    private ImageView imageViewStart;
    private ImageView imageViewStop;


    private LinearLayout btnSetThemesForButton;
    private LinearLayout btnEnabledDeviceAdmin;
    private LinearLayout btnDisabledDeviceAdmin;
    private LinearLayout btnSetGestureForApp;

    private TextView txtStatus;

    private ControlHome_Presenter controlHome_presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controlHome_presenter = new ControlHome_Presenter(this);

        controlHome_presenter.createPackageApp();

        controlHome_presenter.initializerView();

        controlHome_presenter.setStatusButtonControl();

        controlHome_presenter.addEventOnClick();

        setThemesStatusDeviceAdmin();
    }

    private void setThemesStatusDeviceAdmin() {
        if (isDeviceAdminActive()) {
            btnDisabledDeviceAdmin.setVisibility(View.GONE);
            btnEnabledDeviceAdmin.setVisibility(View.VISIBLE);
        } else {
            btnDisabledDeviceAdmin.setVisibility(View.VISIBLE);
            btnEnabledDeviceAdmin.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void packageApp() {
        File directoryToStore;
        directoryToStore = getBaseContext().getExternalFilesDir("");
        if (!directoryToStore.exists())
            directoryToStore.mkdir();
    }

    @Override
    public void buildViewForControlHome() {
        setContentView(R.layout.activity_control_home);
        Objects.requireNonNull(getSupportActionBar()).hide();

        imageViewStart = findViewById(R.id.Start);
        imageViewStart.setOnClickListener(this);
        imageViewStop = findViewById(R.id.Stop);
        imageViewStop.setOnClickListener(this);


        btnSetThemesForButton = findViewById(R.id.HomeButton_Themes);
        btnSetThemesForButton.setOnClickListener(this);
        btnEnabledDeviceAdmin = findViewById(R.id.Enable_Device_Admin);
        btnEnabledDeviceAdmin.setOnClickListener(this);
        btnDisabledDeviceAdmin = findViewById(R.id.Disable_Device_Admin);
        btnDisabledDeviceAdmin.setOnClickListener(this);

        btnSetGestureForApp = findViewById(R.id.Set_Gesture_Button);
        btnSetGestureForApp.setOnClickListener(this);

        txtStatus = findViewById(R.id.Status);

    }

    @Override
    public void statusButtonControl() {
        HomeButton_Service homeButton_service = new HomeButton_Service();
        if (isMyServiceRunning(homeButton_service.getClass())) {
            imageViewStart.setVisibility(View.VISIBLE);
            imageViewStop.setVisibility(View.GONE);
        } else {
            imageViewStart.setVisibility(View.GONE);
            imageViewStop.setVisibility(View.VISIBLE);
        }
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;
            }
        }
        return false;
    }

    @Override
    public void addEvent() {
        imageViewStart.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);
        btnSetGestureForApp.setOnClickListener(this);
        btnSetThemesForButton.setOnClickListener(this);
    }

    @Override
    public void confirmToDisableDeviceAdmin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ControlHome.this);
        builder.setMessage(MESSAGE_TO_DISABLE_CONFIRM);
        builder.setCancelable(true);

        builder.setPositiveButton("Yes",
                (dialog, id) -> {
                    deactivateDeviceAdmin();
                    btnDisabledDeviceAdmin.setVisibility(View.VISIBLE);
                    btnEnabledDeviceAdmin.setVisibility(View.GONE);
                    dialog.cancel();
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Start:
                onClickStartButton();
                break;
            case R.id.Stop:
                onClickStopButton();
                break;
            case R.id.Set_Gesture_Button:
                startActivity(new Intent(ControlHome.this, SetGesture.class));
                break;
            case R.id.HomeButton_Themes:
                startActivity(new Intent(ControlHome.this, ListImageThemes.class));
                break;
            case R.id.Enable_Device_Admin:
                controlHome_presenter.show_Dialog_To_Confirm_Disable_DeviceAdmin();
                break;
            case R.id.Disable_Device_Admin:
                activeDeviceAdmin();
                break;
        }
    }

    private boolean isDeviceAdminActive() {
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dpm.isAdminActive(LockScreen_Receiver.getComponentName(this));
    }

    private void deactivateDeviceAdmin() {
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        dpm.removeActiveAdmin(LockScreen_Receiver.getComponentName(this));
    }

    ActivityResultLauncher<Intent> resultLauncherForDeviceAdminStatus =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                Intent intent = result.getData();
                if (intent != null) {
                    String status = intent.getStringExtra(KEY_CALLBACK_FROM_DEVICE_ADMIN);

                    if (status.equals(CALLBACK_RESULT_DATA)) {
                        btnDisabledDeviceAdmin.setVisibility(View.GONE);
                        btnEnabledDeviceAdmin.setVisibility(View.VISIBLE);
                    }
                }

            });

    private void activeDeviceAdmin() {
        Intent intent = new Intent(this, IntentForSettingManager.class);
        intent.putExtra(KEY_FOR_SETTING, VALUE_DEVICE_ADMIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultLauncherForDeviceAdminStatus.launch(intent);
    }


    private void onClickStartButton() {

        imageViewStart.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        txtStatus.setText("Stop");

        Intent intent = new Intent(ControlHome.this, HomeButton_Service.class);
        stopService(intent);

    }


    private void onClickStopButton() {

        imageViewStart.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        txtStatus.setText("Start");

        checkHadOverlayPermission();

        startForegroundForOtherVersion();
    }

    private void startForegroundForOtherVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(ControlHome.this, HomeButton_Service.class));
        } else {
            startService(new Intent(ControlHome.this, HomeButton_Service.class));
        }
    }

    private boolean checkHadOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(ControlHome.this)) {

            giveOverlayPermission();
            return true;

        }
        return false;
    }



    private void giveOverlayPermission() {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

        imageViewStop.setVisibility(View.VISIBLE);
        imageViewStart.setVisibility(View.GONE);
        txtStatus.setText("Stop");
    }


    //    private void checkPermissionToWriteFile() {
//        if (ContextCompat.checkSelfPermission(ControlHome.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            createGestureFile();
//        }else{
//            askPermission();
//        }
//    }

    //    private void askPermission(){
//        ActivityCompat.requestPermissions(ControlHome.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//        PERMISSION_REQUEST_CODE);
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == PERMISSION_REQUEST_CODE){
//            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                createGestureFile();
//            }else{
//                Toast.makeText(ControlHome.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

}