package com.example.customtouch.Activity;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customtouch.Receiver.LockScreen_Receiver;

public class IntentForSettingManager extends AppCompatActivity {

    private static final String KEY_FOR_SETTING = "setting_manager";
    private static final int VALUE_DEVICE_ADMIN = 1;
    private static final int VALUE_LOCK_ROTATION=2;
    private static final int VALUE_OPEN_ROTATION = 3;
    private static final int VALUE_MANAGER_WRITE_SETTING = 4;
    private static final int DEFAULT_VALUE =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startIntent();
        finish();

    }

    private void startIntent() {
       switch (getValueKey()){
           case VALUE_DEVICE_ADMIN:
               intentToDeviceAdmin();
               break;
           case VALUE_LOCK_ROTATION:
               setRotationScreenFromSettings(IntentForSettingManager.this, false);
               break;
           case VALUE_OPEN_ROTATION:
               setRotationScreenFromSettings(IntentForSettingManager.this,true);
               break;
           case VALUE_MANAGER_WRITE_SETTING:
               intentToManagerWriteSetting();
               break;
       }
    }

    private int getValueKey(){
        Intent intent = getIntent();
        return intent.getIntExtra(KEY_FOR_SETTING,DEFAULT_VALUE);
    }

    private void intentToDeviceAdmin(){
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, LockScreen_Receiver.getComponentName(this));
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable app!");
        startActivity(intent);
    }

    private void intentToManagerWriteSetting(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getApplication().getPackageName()));
        startActivity(intent);
    }

    private void setRotationScreenFromSettings(Context context, boolean enabled) {

        try {
            if (Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 1) {
                Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                Settings.System.putInt(context.getContentResolver(), Settings.System.USER_ROTATION, defaultDisplay.getRotation());
                Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
            } else {
                Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
            }

            Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}
