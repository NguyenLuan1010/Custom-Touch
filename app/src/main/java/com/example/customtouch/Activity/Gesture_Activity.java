package com.example.customtouch.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.customtouch.Interface.In_GestureActivity;
import com.example.customtouch.Model.Apps;
import com.example.customtouch.Presenter.GestureActivity_Presenter;
import com.example.customtouch.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Gesture_Activity extends AppCompatActivity implements In_GestureActivity, View.OnClickListener {

    private static final String PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String FILE_ROOT = "/Android/data/com.example.customtouch/files/";
    private static final String KEY_ADD_SUCCESS = "KEY_ADD";
    private static final String KEY_EDIT_SUCCESS = "KEY_EDIT_SUSSESS";
    private static final String APP_HAD_GESTURE = "This App had gesture";
    private static final String SAVE_GESTURE_FILE_SUCCESS = "Save Successfully!";
    private static final String SAVE_GESTURE_FILE_FAIL = "Save Fail !";
    private static final String WARNING_GESTURE_NULL = "Please draw something !";

    private GestureOverlayView objGestureOverLay;
    private GestureLibrary objGestureLib;

    private AppCompatButton btnAddNewGesture;
    private AppCompatButton btnDeleteGesture;

    private File mStoreFile = new File(PATH_ROOT + FILE_ROOT);
    private File gpxfile;
    private Gesture gesture;
    private GestureActivity_Presenter gestureActivity_presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureActivity_presenter = new GestureActivity_Presenter(this);
        gestureActivity_presenter.initializerView();
        gestureActivity_presenter.addEventClickListener();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private Apps getAppObject() {
        Intent intent = getIntent();
        return intent.getParcelableExtra(KEY_EDIT_SUCCESS);
    }

    @Override
    public void buildViewForGestureActivity() {
        setContentView(R.layout.activity_gesture);
        getSupportActionBar().hide();

        objGestureOverLay = findViewById(R.id.WidgetGesture);
        btnAddNewGesture = findViewById(R.id.Add_New_Gesture);
        btnDeleteGesture = findViewById(R.id.Delete_Gesture);
    }

    @Override
    public void eventOnClick() {

        btnAddNewGesture.setOnClickListener(this);
        btnDeleteGesture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Add_New_Gesture:
                onClickAddNewGesture();
                break;
            case R.id.Delete_Gesture:
                objGestureOverLay.cancelClearAnimation();
                objGestureOverLay.clear(true);
                break;
        }
    }

    private void onClickAddNewGesture() {
        checkFileExist();
        gestureActivity_presenter.addGestureToFile();
    }

    private void checkFileExist() {
        if (!mStoreFile.exists()) {
            mStoreFile.mkdirs();
        }
    }

    @Override
    public void gestureToFile() {

        gpxfile = new File(mStoreFile, getAppObject().getNameApp() + ".txt");

        if (!gpxfile.exists()) {
            whenSaveGestureToFile();
        } else {
            Toast.makeText(Gesture_Activity.this, APP_HAD_GESTURE, Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    private GestureLibrary saveGestureToFile() {
        objGestureLib = GestureLibraries.fromFile(gpxfile);
        objGestureLib.addGesture(getAppObject().getPackageName(), gesture);
        return objGestureLib;
    }

    private void whenSaveGestureToFile() {
        gesture = objGestureOverLay.getGesture();
        if (gesture == null) {
            Toast.makeText(this, WARNING_GESTURE_NULL, Toast.LENGTH_SHORT).show();
            return;
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(gpxfile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (saveGestureToFile().save()) {

            Bitmap bitMap = gesture.toBitmap(200, 200, 0, Color.YELLOW);

            Apps gestureApps = new Apps(getAppObject().getImgApp(),
                    getAppObject().getNameApp(), getAppObject().getPackageName(), bitMap);

            Toast.makeText(Gesture_Activity.this, SAVE_GESTURE_FILE_SUCCESS, Toast.LENGTH_SHORT).show();

            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            whenAddSuccess(gestureApps);
            finish();
        } else {
            Toast.makeText(Gesture_Activity.this, SAVE_GESTURE_FILE_FAIL, Toast.LENGTH_SHORT).show();
        }
    }

    private void whenAddSuccess(Apps gestureApps) {
        Intent intent = new Intent();
        intent.putExtra(KEY_ADD_SUCCESS, gestureApps);
        setResult(RESULT_OK, intent);
    }
}