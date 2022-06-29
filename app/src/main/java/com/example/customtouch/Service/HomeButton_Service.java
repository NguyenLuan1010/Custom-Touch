package com.example.customtouch.Service;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;


import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;


import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.PixelFormat;


import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.customtouch.Activity.ControlHome;
import com.example.customtouch.Activity.IntentForSettingManager;
import com.example.customtouch.Interface.In_BuildItemsOfTasks;
import com.example.customtouch.Interface.In_BuildUIForHomeButton;
import com.example.customtouch.Model.Apps;
import com.example.customtouch.Presenter.BuildHomeButtonView_Presenter;
import com.example.customtouch.Presenter.BuildItemsTasks_Presenter;
import com.example.customtouch.R;
import com.example.customtouch.Receiver.LockScreen_Receiver;
import com.example.customtouch.Receiver.Notification_Receiver;
import com.example.customtouch.Utils.SettingSharedPref;

import java.io.File;
import java.util.List;

public class HomeButton_Service extends Service implements In_BuildUIForHomeButton, In_BuildItemsOfTasks,
        GestureOverlayView.OnGesturePerformedListener {

    private static final String CHANNEL_ID = "com.example.customtouch";
    private static final String APP_NAME = "Custom Touch";
    private static final String KEY_FOR_SETTING = "setting_manager";
    private static final String KEY_TRANSMISSION = "transmission_actionClick";
    private static final String KEY_ACTION_CLICK = "action_click";
    private static final String DEVICE_NO_FLASH = "This device has no flash";
    private static final int VALUE_DEVICE_ADMIN = 1;
    private static final int VALUE_LOCK_ROTATION = 2;
    private static final int VALUE_OPEN_ROTATION = 3;
    private static final int VALUE_MANAGER_WRITE_SETTING = 4;


    private BuildHomeButtonView_Presenter buildHomeButton_View_presenter;
    private BuildItemsTasks_Presenter buildItemsTasks_presenter;

    private View homeIcon;
    private View firstMenuTask;
    private View drawGesture;
    private View secondMenuTask;
    private int LAYOUT_FLAG;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams paramsIcon;
    private WindowManager.LayoutParams paramsFirstMenuTask;
    private WindowManager.LayoutParams paramsSecondMenuTask;
    private WindowManager.LayoutParams paramsDrawGesture;

    private int state;
    private final int TYPE_ICON = 0;
    private final int TYPE_FIRST_MENU_TASK = 1;
    private final int TYPE_GESTURE_VIEW = 2;
    private final int TYPE_SECOND_MENU_TASK = 3;
    private boolean isFlashActive = false;

    private LinearLayout btnLock;
    private LinearLayout btnGestureView;
    private LinearLayout btnRingNormal;
    private LinearLayout btnRingSilent;
    private LinearLayout btnRingRang;
    private LinearLayout btnSetting;
    private LinearLayout btnHome;

    //Items of SecondMenuTasks
    private LinearLayout btnWifi;
    private LinearLayout btnTurnOnBluetooth;
    private LinearLayout btnTurnOffBluetooth;
    private LinearLayout btnVolumeUp;
    private LinearLayout btnVolumeDown;
    private LinearLayout btnFlashOn;
    private LinearLayout btnFlashOff;
    private LinearLayout btnTurnOnLocation;
    private LinearLayout btnTurnOffLocation;
    private LinearLayout btnLockRotation;
    private LinearLayout btnRotation;
    private LinearLayout btnBackToFistMenu;
    private LinearLayout btnFlashEnable;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    private DevicePolicyManager devicePolicyManager;
    private WifiManager wifiManager;

    private BluetoothAdapter bluetoothAdapter;
    private Intent blueToothIntent;

    private AudioManager audioManager;
    private CameraManager cameraManager;

    private String cameraID;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        buildHomeButton_View_presenter = new BuildHomeButtonView_Presenter(this);
        buildItemsTasks_presenter = new BuildItemsTasks_Presenter(this);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        buildHomeButton_View_presenter.buildViewButton();
        buildHomeButton_View_presenter.showHomeButtonView();
        buildHomeButton_View_presenter.addFirstMenuTaskView();
        buildHomeButton_View_presenter.addSecondMenuTaskView();
        buildHomeButton_View_presenter.addGestureLayOut();

        buildItemsTasks_presenter.checkDeviceHasCamera();
        buildHomeButton_View_presenter.actionOfGestureHomeButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void createHomeButtonFromAPIO() {

        LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        paramsIcon = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,

                PixelFormat.TRANSLUCENT);
    }

    @Override
    public void createHomeButtonAnotherAPI() {
        LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        paramsIcon = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    @SuppressLint({"InflateParams", "RtlHardcoded"})
    @Override
    public void buildViewHomeButton() {
        homeIcon = LayoutInflater.from(this).inflate(R.layout.home_button, null);

        buildHomeButton_View_presenter.ForAddViewIcon();

        paramsIcon.gravity = Gravity.TOP | Gravity.LEFT;
        paramsIcon.x = 0;
        paramsIcon.y = 100;
    }

    @Override
    public void homeButtonView() {

        checkCurrentStateForHomeButton();

        setLayOutForHomeButton();
    }


    private void checkCurrentStateForHomeButton() {
        if (state == TYPE_FIRST_MENU_TASK) {
            mWindowManager.removeView(firstMenuTask);
        } else if (state == TYPE_GESTURE_VIEW) {
            mWindowManager.removeView(drawGesture);
        } else if (state == TYPE_SECOND_MENU_TASK) {
            mWindowManager.removeView(secondMenuTask);
        }
    }

    private void setLayOutForHomeButton() {
        if (!homeIcon.isShown()) {
            SettingSharedPref mSetting = SettingSharedPref.getInstance(this);
            //Items of FirstMenuTasks
            ImageView homeButton = homeIcon.findViewById(R.id.home_button);
            homeButton.setImageResource(mSetting.getBtnHomeTheme());
            mSetting.setOnBtnHomeChangeListener(homeButton::setImageResource);
            mWindowManager.addView(homeIcon, paramsIcon);
            state = TYPE_ICON;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void createFirstMenuTaskFromAPIO() {
        LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        paramsFirstMenuTask = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
    }

    @Override
    public void createFirstMenuTaskAnotherAPI() {
        LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        paramsFirstMenuTask = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    @SuppressLint("InflateParams")
    @Override
    public void fistMenuTaskView() {
        firstMenuTask = LayoutInflater.from(this).inflate(R.layout.first_menu_items, null);
        buildHomeButton_View_presenter.buildItemFirstMenuTasks();
        buildHomeButton_View_presenter.ForAddViewFirstMenuTask();
    }

    @Override
    public void addFirstMenuTaskView() {
        if (state == TYPE_ICON) {
            mWindowManager.removeView(homeIcon);
        } else if (state == TYPE_GESTURE_VIEW) {
            mWindowManager.removeView(drawGesture);
        } else if (state == TYPE_SECOND_MENU_TASK) {
            mWindowManager.removeView(secondMenuTask);
        }
        if (!firstMenuTask.isShown()) {
            mWindowManager.addView(firstMenuTask, paramsFirstMenuTask);
            state = TYPE_FIRST_MENU_TASK;
        }
        getBlueToothStatus();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void clickOutSideFirstMenuTasks() {
        firstMenuTask.findViewById(R.id.first_menu_task).setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    return false;
                case MotionEvent.ACTION_OUTSIDE:

                    getStatusMode();
                    buildItemsTasks_presenter.CheckLocationStatus();
                    buildHomeButton_View_presenter.showHomeButtonView();

                    break;
            }
            return false;
        });
    }

    @Override
    public void itemFirstMenuTasksView() {
        btnHome = firstMenuTask.findViewById(R.id.Home_Button);
        btnGestureView = firstMenuTask.findViewById(R.id.Gesture_View);
        btnLock = firstMenuTask.findViewById(R.id.Lock_Screen);
        btnRingRang = firstMenuTask.findViewById(R.id.Ring_Rang);
        btnRingNormal = firstMenuTask.findViewById(R.id.Ring_Normal);
        btnRingSilent = firstMenuTask.findViewById(R.id.Ring_Silent);
        btnSetting = firstMenuTask.findViewById(R.id.Setting);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        getStatusMode();

        buildHomeButton_View_presenter.addEventOnClickFirstMenuTask();
    }

    @Override
    public void eventOnClickItemFirstMenu() {

        btnHome.setOnClickListener(onClickOnFirstMenu);
        btnGestureView.setOnClickListener(onClickOnFirstMenu);
        btnLock.setOnClickListener(onClickOnFirstMenu);
        btnRingRang.setOnClickListener(onClickOnFirstMenu);
        btnRingNormal.setOnClickListener(onClickOnFirstMenu);
        btnRingSilent.setOnClickListener(onClickOnFirstMenu);
        btnSetting.setOnClickListener(onClickOnFirstMenu);

    }

    View.OnClickListener onClickOnFirstMenu = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Home_Button:
                    buildHomeButton_View_presenter.goToHomeScreen();
                    buildHomeButton_View_presenter.showHomeButtonView();
                    break;
                case R.id.Gesture_View:
                    buildHomeButton_View_presenter.showGestureLayOut();
                    buildHomeButton_View_presenter.onClickOutSideGestureView();
                    gestureView();
                    break;
                case R.id.Lock_Screen:
                    buildItemsTasks_presenter.lockScreen();
                    break;
                case R.id.Ring_Rang:
                    if (!changeModePermission()) {
                        giveModePermission();
                        return;
                    }
                    buildItemsTasks_presenter.normalMode();
                    break;
                case R.id.Ring_Normal:
                    if (!changeModePermission()) {
                        giveModePermission();
                        return;
                    }
                    buildItemsTasks_presenter.silentMode();
                    break;
                case R.id.Ring_Silent:
                    if (!changeModePermission()) {
                        giveModePermission();
                        return;
                    }
                    buildItemsTasks_presenter.vibrateMode();
                    break;
                case R.id.Setting:
                    buildHomeButton_View_presenter.showSecondMenuTask();
                    buildHomeButton_View_presenter.onClickOutSideSecondMenu();
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void createSecondMenuTaskFromAPIO() {
        LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        paramsSecondMenuTask = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
    }

    @Override
    public void createSecondMenuTaskAnotherAPI() {
        LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        paramsSecondMenuTask = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    @SuppressLint("InflateParams")
    @Override
    public void secondMenuTaskView() {
        secondMenuTask = LayoutInflater.from(this).inflate(R.layout.second_menu_items, null);
        buildHomeButton_View_presenter.buildItemSecondMenuTasks();
        buildHomeButton_View_presenter.ForAddViewSecondMenuTask();
    }

    @Override
    public void addSecondMenuTaskView() {
        if (state == TYPE_FIRST_MENU_TASK) {
            mWindowManager.removeView(firstMenuTask);
        } else if (state == TYPE_GESTURE_VIEW) {
            mWindowManager.removeView(drawGesture);
        } else if (state == TYPE_ICON) {
            mWindowManager.removeView(homeIcon);
        }
        if (!secondMenuTask.isShown()) {
            mWindowManager.addView(secondMenuTask, paramsSecondMenuTask);
            state = TYPE_SECOND_MENU_TASK;
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void clickOutSideSecondMenuTasks() {
        secondMenuTask.findViewById(R.id.second_menu_tasks).setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    return false;
                case MotionEvent.ACTION_OUTSIDE:

                    buildHomeButton_View_presenter.showHomeButtonView();

                    break;
            }
            return false;
        });

    }

    @Override
    public void itemSecondMenuTasksView() {

        btnWifi = secondMenuTask.findViewById(R.id.Wifi);
        btnBackToFistMenu = secondMenuTask.findViewById(R.id.BackToFirstMenu);
        btnTurnOnBluetooth = secondMenuTask.findViewById(R.id.TurnOnBluetooth);
        btnTurnOffBluetooth = secondMenuTask.findViewById(R.id.TurnOffBluetooth);
        btnVolumeDown = secondMenuTask.findViewById(R.id.Volume_Down);
        btnVolumeUp = secondMenuTask.findViewById(R.id.Volume_Up);
        btnFlashOff = secondMenuTask.findViewById(R.id.FlashLightOff);
        btnFlashOn = secondMenuTask.findViewById(R.id.FlashLightOn);
        btnFlashEnable = secondMenuTask.findViewById(R.id.FlashLightEnabled);
        btnTurnOnLocation = secondMenuTask.findViewById(R.id.TurnOnLocation);
        btnTurnOffLocation = secondMenuTask.findViewById(R.id.TurnOffLocation);
        btnRotation = secondMenuTask.findViewById(R.id.Rotation);
        btnLockRotation = secondMenuTask.findViewById(R.id.Lock_Rotation);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        buildItemsTasks_presenter.CheckLocationStatus();

        getBlueToothStatus();
        setItemsManager();
        getCameraId();

        buildHomeButton_View_presenter.addEventOnClickSecondMenuTask();

    }

    private void getBlueToothStatus() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                btnTurnOffBluetooth.setVisibility(View.GONE);
                btnTurnOnBluetooth.setVisibility(View.VISIBLE);
            } else {
                btnTurnOffBluetooth.setVisibility(View.VISIBLE);
                btnTurnOnBluetooth.setVisibility(View.GONE);
            }
        }
    }

    private void setItemsManager() {
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

    }

    private void getCameraId() {
        try {
            cameraID = cameraManager.getCameraIdList()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void eventOnClickItemSecondMenu() {
        buildItemsTasks_presenter.getBlueToothStatus();
        buildItemsTasks_presenter.getStatusFlash();

        btnWifi.setOnClickListener(onClickOnSecondMenu);
        btnBackToFistMenu.setOnClickListener(onClickOnSecondMenu);
        btnTurnOffBluetooth.setOnClickListener(onClickOnSecondMenu);
        btnTurnOnBluetooth.setOnClickListener(onClickOnSecondMenu);
        btnVolumeDown.setOnClickListener(onClickOnSecondMenu);
        btnVolumeUp.setOnClickListener(onClickOnSecondMenu);
        btnFlashEnable.setOnClickListener(onClickOnSecondMenu);
        btnFlashOff.setOnClickListener(onClickOnSecondMenu);
        btnFlashOn.setOnClickListener(onClickOnSecondMenu);
        btnTurnOnLocation.setOnClickListener(onClickOnSecondMenu);
        btnTurnOffLocation.setOnClickListener(onClickOnSecondMenu);
        btnLockRotation.setOnClickListener(onClickOnSecondMenu);
        btnRotation.setOnClickListener(onClickOnSecondMenu);
    }

    private final View.OnClickListener onClickOnSecondMenu = new View.OnClickListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.Wifi:
                    buildHomeButton_View_presenter.showHomeButtonView();
                    buildItemsTasks_presenter.changeWifiStatus();
                    break;
                case R.id.BackToFirstMenu:
                    buildHomeButton_View_presenter.showFirstMenuTask();
                    break;
                case R.id.TurnOnBluetooth:

                    buildItemsTasks_presenter.disableBluetooth();
                    buildHomeButton_View_presenter.showHomeButtonView();


                    break;
                case R.id.TurnOffBluetooth:

                    buildItemsTasks_presenter.enableBluetooth();
                    buildHomeButton_View_presenter.showHomeButtonView();

                    break;
                case R.id.Volume_Down:
                    buildItemsTasks_presenter.VolumeUp();
                    break;
                case R.id.Volume_Up:
                    buildItemsTasks_presenter.VolumeDown();
                    break;
                case R.id.FlashLightOff:
                case R.id.FlashLightOn:
                    controlFlashLight();

                    break;
                case R.id.FlashLightEnabled:
                    Toast.makeText(HomeButton_Service.this, DEVICE_NO_FLASH, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.TurnOnLocation:
                case R.id.TurnOffLocation:

                    buildItemsTasks_presenter.IntentLocation();
                    buildHomeButton_View_presenter.showHomeButtonView();
                    break;

                case R.id.Rotation:
                    buildItemsTasks_presenter.OpenRotation();
                    break;
                case R.id.Lock_Rotation:
                    buildItemsTasks_presenter.LockRotation();
                    break;
            }
        }
    };

    private void controlFlashLight() {
        if (isFlashActive) {
            buildItemsTasks_presenter.TurnOffFlash();
            isFlashActive = false;
        } else {

            buildItemsTasks_presenter.TurnOnFlash();
            isFlashActive = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void createGestureViewFromAPIO() {
        LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        paramsDrawGesture = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    @Override
    public void createGestureViewAnotherAPI() {
        LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        paramsDrawGesture = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
    }

    @SuppressLint("InflateParams")
    @Override
    public void gestureLayOutView() {
        drawGesture = LayoutInflater.from(this).inflate(R.layout.gesture_view, null);
        buildHomeButton_View_presenter.ForAddGestureView();
    }

    @Override
    public void addGestureLayOutView() {
        if (state == TYPE_FIRST_MENU_TASK) {
            mWindowManager.removeView(firstMenuTask);
        } else if (state == TYPE_ICON) {
            mWindowManager.removeView(homeIcon);
        } else if (state == TYPE_SECOND_MENU_TASK) {
            mWindowManager.removeView(secondMenuTask);
        }

        if (!drawGesture.isShown()) {
            mWindowManager.addView(drawGesture, paramsDrawGesture);
            state = TYPE_GESTURE_VIEW;
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void clickOutSideGestureView() {
        drawGesture.findViewById(R.id.root_draw_gesture).setOnTouchListener((View v, MotionEvent event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    return false;
                case MotionEvent.ACTION_OUTSIDE:

                    buildHomeButton_View_presenter.showHomeButtonView();

                    break;
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void actionGestureView() {
        homeIcon.findViewById(R.id.root_container).setOnTouchListener((v, event) -> {
            getStatusMode();
            buildItemsTasks_presenter.CheckLocationStatus();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    actionDown(event);

                    return true;
                case MotionEvent.ACTION_UP:

                    actionUp(event);

                    return true;
                case MotionEvent.ACTION_MOVE:

                    moveAction(event);

                    return true;

            }
            return false;
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        buildHomeButton_View_presenter = new BuildHomeButtonView_Presenter(this);

        buildHomeButton_View_presenter.createNotificationChannel();
        buildHomeButton_View_presenter.createNotificationControl();

        getTransmissionId(intent);

        return START_STICKY;

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void notificationChannel() {

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, APP_NAME, importance);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void notificationControl() {

        Intent intent = new Intent(HomeButton_Service.this, ControlHome.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(HomeButton_Service.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification);

        setButtonClickViewForNotification(notificationLayout);

        setNotificationAttribute(pendingIntent, notificationLayout);

    }

    private void setButtonClickViewForNotification(RemoteViews notificationLayout) {

        notificationLayout.setOnClickPendingIntent(R.id.Hide_Home_Button,
                onButtonNotificationClick(R.id.Hide_Home_Button));

        notificationLayout.setOnClickPendingIntent(R.id.Show_Home_Button,
                onButtonNotificationClick(R.id.Show_Home_Button));

    }

    private PendingIntent onButtonNotificationClick(@IdRes int id) {

        Intent intent = new Intent(HomeButton_Service.this, Notification_Receiver.class);

        intent.putExtra(KEY_ACTION_CLICK, id);

        return PendingIntent.getBroadcast(HomeButton_Service.this, id, intent, PendingIntent.FLAG_IMMUTABLE);

    }

    private void setNotificationAttribute(PendingIntent pendingIntent, RemoteViews notificationLayout) {

        Notification
                notification = new NotificationCompat.Builder(HomeButton_Service.this, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.icon_app)
                .setContentIntent(pendingIntent)
                .setCustomContentView(notificationLayout)
                .build();

        notification.flags = Notification.FLAG_NO_CLEAR;
        startForeground(1, notification);

        NotificationManager notificationManager =
                (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }

    private void getTransmissionId(Intent intent) {
        try {

            int id = intent.getIntExtra(KEY_TRANSMISSION, 0);

            buildHomeButton_View_presenter.onClickButtonInNotification(id);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void handleActionNotification(int id) {
        switch (id) {
            case R.id.Hide_Home_Button:

                buildHomeButton_View_presenter.hideAllViewHomeButton();

                break;
            case R.id.Show_Home_Button:

                buildHomeButton_View_presenter.hideAllViewHomeButton();

                if (!homeIcon.isShown()) {
                    mWindowManager.addView(homeIcon, paramsIcon);
                    state = TYPE_ICON;
                }

                break;
        }
    }

    @Override
    public void hideHomeButtonView() {
        if (homeIcon.isShown()) {

            mWindowManager.removeView(homeIcon);

        }
        if (firstMenuTask.isShown()) {

            mWindowManager.removeView(firstMenuTask);

        }
        if (secondMenuTask.isShown()) {

            mWindowManager.removeView(secondMenuTask);

        }
        if (drawGesture.isShown()) {

            mWindowManager.removeView(drawGesture);

        }
    }


    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(HomeButton_Service.this, IntentForSettingManager.class);
        intent.putExtra(KEY_FOR_SETTING, VALUE_MANAGER_WRITE_SETTING);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @SuppressLint("WrongConstant")
    private void getStatusMode() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {

            btnRingNormal.setVisibility(View.GONE);
            btnRingRang.setVisibility(View.GONE);
            btnRingSilent.setVisibility(View.VISIBLE);

        } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {

            btnRingNormal.setVisibility(View.GONE);
            btnRingRang.setVisibility(View.VISIBLE);
            btnRingSilent.setVisibility(View.GONE);

        } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {

            btnRingNormal.setVisibility(View.VISIBLE);
            btnRingRang.setVisibility(View.GONE);
            btnRingSilent.setVisibility(View.GONE);

        }
    }


    private boolean changeModePermission() {
        NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && n.isNotificationPolicyAccessGranted();
    }

    private void giveModePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(intent);
            } catch (ActivityNotFoundException exception) {
                exception.printStackTrace();
            }

        }
    }

    private void actionDown(MotionEvent event) {
        //remember the initial position.
        initialX = paramsIcon.x;
        initialY = paramsIcon.y;

        //get the touch location
        initialTouchX = event.getRawX();
        initialTouchY = event.getRawY();
    }

    private void actionUp(MotionEvent event) {
        int Xdiff = (int) (event.getRawX() - initialTouchX);
        int Ydiff = (int) (event.getRawY() - initialTouchY);


        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
        //So that is click event.
        if (Xdiff < 10 && Ydiff < 10) {
            buildHomeButton_View_presenter.showFirstMenuTask();
            buildHomeButton_View_presenter.onClickOutSideFirstMenu();
        }

    }

    private void moveAction(MotionEvent event) {
        //Calculate the X and Y coordinates of the view.
        paramsIcon.x = initialX + (int) (event.getRawX() - initialTouchX);
        paramsIcon.y = initialY + (int) (event.getRawY() - initialTouchY);

        //Update the layout with new X & Y coordinate
        mWindowManager.updateViewLayout(homeIcon, paramsIcon);
    }

    private void gestureView() {
        GestureOverlayView objGestureOverLay = drawGesture.findViewById(R.id.Draw_Gesture);
        objGestureOverLay.addOnGesturePerformedListener(this);
    }

    public File[] getAllFile() {
        File mStoreFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.customtouch/files");
        if (!mStoreFile.exists()) {
            mStoreFile.mkdirs();
        }
        return mStoreFile.listFiles();
    }


    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        if (getAllFile() != null) {
            for (int i = 0; i < getAllFile().length; i++) {

                File f = getAllFile()[i];

                if (f.isFile() && f.getName().contains(".txt")) {

                    GestureLibrary objGestureLib = GestureLibraries.fromFile(f.getAbsolutePath());
                    objGestureLib.load();

                    List<Prediction> objPrediction = objGestureLib.recognize(gesture);
                    if (objPrediction.size() > 0 && objPrediction.get(0).score > 4) {

                        if (buildItemsTasks_presenter.intentTransferApp(objPrediction)) {
                            break;
                        }

                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        buildHomeButton_View_presenter.hideAllViewHomeButton();

        SettingSharedPref.getInstance(this).removeOnBtnHomeChangeListener();
    }

    @Override
    public void backToHomeScreen() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    @Override
    public boolean transferApp(List<Prediction> objPrediction) {
        String gestureName = objPrediction.get(0).name;

        buildHomeButton_View_presenter.showHomeButtonView();

        Intent intentLauncher = getPackageManager().getLaunchIntentForPackage(gestureName);
        if (intentLauncher != null) {

            startActivity(intentLauncher);

            return true;
        }
        return false;
    }

    @Override
    public void NormalMode() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        btnRingNormal.setVisibility(View.VISIBLE);
        btnRingRang.setVisibility(View.GONE);
        btnRingSilent.setVisibility(View.GONE);
    }

    @Override
    public void SilentMode() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        btnRingNormal.setVisibility(View.GONE);
        btnRingRang.setVisibility(View.GONE);
        btnRingSilent.setVisibility(View.VISIBLE);
    }

    @Override
    public void VibrateMode() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        btnRingNormal.setVisibility(View.GONE);
        btnRingRang.setVisibility(View.VISIBLE);
        btnRingSilent.setVisibility(View.GONE);

    }

    @Override
    public void LockScreen() {
        boolean active = devicePolicyManager.isAdminActive(LockScreen_Receiver.getComponentName(HomeButton_Service.this));

        if (active) {
            devicePolicyManager.lockNow();
        } else {
            Intent intent = new Intent(this, IntentForSettingManager.class);
            intent.putExtra(KEY_FOR_SETTING, VALUE_DEVICE_ADMIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void WifiStatusFromAPIQ() {
        Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
        panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(panelIntent);
    }

    @Override
    public void WifiStatusAnotherAPI() {

        wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
    }

    @Override
    public void turnOnBluetooth() {

        blueToothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        blueToothIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(blueToothIntent);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void turnOffBluetooth() {

        if (!bluetoothAdapter.isDiscovering()) {
            blueToothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            blueToothIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(blueToothIntent);
        }
        if (bluetoothAdapter.disable()) {

        }

    }

    @Override
    public void blueToothStatus() {
        if (bluetoothAdapter == null) {
            btnTurnOffBluetooth.setVisibility(View.VISIBLE);
            btnTurnOnBluetooth.setVisibility(View.GONE);
        } else if (bluetoothAdapter.isEnabled()) {
            btnTurnOffBluetooth.setVisibility(View.GONE);
            btnTurnOnBluetooth.setVisibility(View.VISIBLE);

        } else {
            btnTurnOffBluetooth.setVisibility(View.VISIBLE);
            btnTurnOnBluetooth.setVisibility(View.GONE);

        }
    }

    @Override
    public void Location() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void locationStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;

        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            btnTurnOnLocation.setVisibility(View.VISIBLE);
            btnTurnOffLocation.setVisibility(View.GONE);
        } else {
            btnTurnOnLocation.setVisibility(View.GONE);
            btnTurnOffLocation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void VolumeUp() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public void VolumeDown() {

        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public void hasCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                btnFlashEnable.setVisibility(View.GONE);
            } else {
                btnFlashEnable.setVisibility(View.VISIBLE);
                btnFlashOn.setVisibility(View.GONE);
                btnFlashOff.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void FlashStatus() {
        if (isFlashActive) {
            btnFlashOff.setVisibility(View.VISIBLE);
            btnFlashOn.setVisibility(View.GONE);

        } else {
            btnFlashOff.setVisibility(View.GONE);
            btnFlashOn.setVisibility(View.VISIBLE);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void FlashOn() {
        try {
            cameraManager.setTorchMode(cameraID, true);

            btnFlashOff.setVisibility(View.VISIBLE);
            btnFlashOn.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void FlashOff() {
        try {
            cameraManager.setTorchMode(cameraID, false);
            btnFlashOff.setVisibility(View.GONE);
            btnFlashOn.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Rotation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getApplicationContext())) {
                btnRotation.setVisibility(View.GONE);
                btnLockRotation.setVisibility(View.VISIBLE);
                Intent intent_Rotation = new Intent(HomeButton_Service.this, IntentForSettingManager.class);
                intent_Rotation.putExtra(KEY_FOR_SETTING, VALUE_OPEN_ROTATION);
                intent_Rotation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_Rotation);
            } else {
                openAndroidPermissionsMenu();
            }
        }
    }

    @Override
    public void LockRotation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getApplicationContext())) {
                btnRotation.setVisibility(View.VISIBLE);
                btnLockRotation.setVisibility(View.GONE);
                Intent intentLockRotation = new Intent(HomeButton_Service.this, IntentForSettingManager.class);
                intentLockRotation.putExtra(KEY_FOR_SETTING, VALUE_LOCK_ROTATION);
                intentLockRotation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentLockRotation);
            } else {
                openAndroidPermissionsMenu();
            }
        }
    }
}
