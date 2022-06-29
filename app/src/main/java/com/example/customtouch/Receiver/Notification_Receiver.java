package com.example.customtouch.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.customtouch.Activity.ControlHome;
import com.example.customtouch.Service.HomeButton_Service;

public class Notification_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int actionClick = intent.getIntExtra("action_click", 0);

        Intent intentClick = new Intent(context, HomeButton_Service.class);
        intentClick.putExtra("transmission_actionClick", actionClick);
        //context.stopService(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentClick);
        } else {
            context.startService(intentClick);
        }
    }
}
