package com.example.customtouch.Receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.customtouch.Activity.ControlHome;

public class LockScreen_Receiver extends DeviceAdminReceiver {

    private static final String KEY_CALLBACK_FROM_DEVICE_ADMIN = "status_callback";
    private static final String CALLBACK_RESULT_DATA = "enabled";

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);

        intent = new Intent(context, ControlHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_CALLBACK_FROM_DEVICE_ADMIN, CALLBACK_RESULT_DATA);
        context.startActivity(intent);

    }


    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {

    }

    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), LockScreen_Receiver.class);
    }
}
