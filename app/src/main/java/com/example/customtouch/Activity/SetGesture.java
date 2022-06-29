package com.example.customtouch.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.customtouch.Adapter.ListGestureAppsAdapter;
import com.example.customtouch.Interface.In_SetGesture;
import com.example.customtouch.Model.Apps;
import com.example.customtouch.Presenter.SetGesture_Presenter;
import com.example.customtouch.R;
import com.example.customtouch.Service.HomeButton_Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetGesture extends AppCompatActivity implements In_SetGesture {

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String ROOT_FILE = "/Android/data/com.example.customtouch/files";
    public static final String KEY_ADD_SUCCESS = "KEY_ADD";
    private static final String COLOR_STRING = "#3A7D97";
    private static final String TITLE = "Gesture Apps";
    private static final String DELETE_SUCCESS = "Delete Success";
    private static final String DELETE_FAIL = "Delete Fail";
    private static final String EDIT_SUCCESS = "Edit success";
    private static final String EDIT_FAIL = "Edit fail";
    private static final String MESSAGE_TO_DELETE_CONFIRM = "Are you sure delete it?";

    private final List<Apps> listGestureApps = new ArrayList<>();
    private ListGestureAppsAdapter listGestureAppAdaptor;
    private final HomeButton_Service homeButton_service = new HomeButton_Service();

    private RecyclerView listViewAllApps;
    private GestureOverlayView objGestureOverLay;
    private GestureLibrary objGestureLib;
    private SetGesture_Presenter setGesture_presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setGesture_presenter = new SetGesture_Presenter(this);

        setGesture_presenter.initializerView();
        setGesture_presenter.setDataForListGestureApps();
        setGesture_presenter.setDataForRecycleView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_gesture, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_new) {
            resultLauncherForAdd.launch(new Intent(SetGesture.this, List_Apps_Activity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> resultLauncherForAdd =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Apps gestureApps = intent.getParcelableExtra(KEY_ADD_SUCCESS);
                        try {
                            addToLists(gestureApps);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void addToLists(Apps gestureApps) throws PackageManager.NameNotFoundException {
        Drawable iconApp = getApplicationContext().getPackageManager().getApplicationIcon(gestureApps.getPackageName());
        gestureApps.setImgApp(iconApp);
        listGestureApps.add(gestureApps);
        listGestureAppAdaptor.notifyItemChanged(listGestureApps.size() - 1);
        listViewAllApps.scrollToPosition(listGestureApps.size() - 1);
    }


    @Override
    public void buildView() {
        setContentView(R.layout.activity_set_gesture);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor(COLOR_STRING));

        // Set BackgroundDrawable
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle(TITLE);
        listViewAllApps = findViewById(R.id.ListGestureApps);
    }

    @Override
    public void dataForListGestureApps() {
        if (homeButton_service.getAllFile() != null) {

            for (int i = 0; i < homeButton_service.getAllFile().length; i++) {

                File f = homeButton_service.getAllFile()[i];

                if (f.isFile() && f.getName().contains(".txt")) {

                    GestureLibrary gestureLib = GestureLibraries.fromFile(f.getAbsolutePath());
                    if (gestureLib != null && gestureLib.load()) {

                        for (String pkName : gestureLib.getGestureEntries()) {
                            for (Gesture gesture : gestureLib.getGestures(pkName)) {

                                addToListGestureApps(gesture, pkName, f);

                            }
                        }
                    }
                }
            }
        }
    }

    private void addToListGestureApps(Gesture gesture, String pkName, File f) {
        try {
            Bitmap bitmap = gesture.toBitmap(200, 200, 0, Color.YELLOW);

            Drawable iconApp = getApplicationContext().getPackageManager().getApplicationIcon(pkName);

            Apps gestureApps = new Apps(iconApp, getAppName(f.getName()), pkName, bitmap);
            listGestureApps.add(gestureApps);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getAppName(String fileName) {
        String[] app = fileName.split("\\.");
        return app[0];
    }

    @Override
    public void dataForRecycleView() {
        listGestureAppAdaptor = new ListGestureAppsAdapter(listGestureApps, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listViewAllApps.setLayoutManager(linearLayoutManager);
        listViewAllApps.setAdapter(listGestureAppAdaptor);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void popUpView(Apps gesture, View v, int position) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    setGesture_presenter.dialogViewToEdit(gesture, position);
                    return true;
                case R.id.delete:
                    setGesture_presenter.dialogViewToDelete(gesture);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.inflate(R.menu.sub_menu_choice);
        popupMenu.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void deleteGestureApp(Apps gestureApps) {
        File dir = new File(ROOT_PATH + ROOT_FILE);

        String appName = gestureApps.getNameApp() + ".txt";
        File file = new File(dir, appName);

        if (file.delete()) {
            listGestureApps.remove(gestureApps);
            listGestureAppAdaptor.notifyDataSetChanged();
            Toast.makeText(SetGesture.this, DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SetGesture.this, DELETE_FAIL, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void confirmToDelete(Apps gestureApps) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetGesture.this);
        builder.setMessage(MESSAGE_TO_DELETE_CONFIRM);
        builder.setCancelable(true);

        builder.setPositiveButton("Yes",
                (dialog, id) -> {
                    setGesture_presenter.deleteGesture(gestureApps);
                    dialog.cancel();
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void editGestureApp(Apps gestureApps, int position) {
        File file = new File(ROOT_PATH + ROOT_FILE, gestureApps.getNameApp() + ".txt");
        if (clearTheFile(file)) {
            Gesture gesture = objGestureOverLay.getGesture();
            if (gesture == null) {
                Toast.makeText(this, "Please draw something!", Toast.LENGTH_SHORT).show();
                return;
            }
            objGestureLib = GestureLibraries.fromFile(file);
            objGestureLib.addGesture(gestureApps.getPackageName(), gesture);
            Bitmap newBitMap = gesture.toBitmap(200, 200, 0, Color.YELLOW);

            if (objGestureLib.save()) {

                setListAgainAfterEdit(gestureApps,newBitMap,position);

                Toast.makeText(SetGesture.this, EDIT_SUCCESS, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SetGesture.this, EDIT_FAIL, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void setListAgainAfterEdit(Apps gestureApps,Bitmap newBitMap, int position){
        Apps newGestureApps = new Apps(gestureApps.getImgApp(),
                gestureApps.getNameApp(), gestureApps.getPackageName(), newBitMap);

        listGestureApps.set(position, newGestureApps);
        listGestureAppAdaptor.notifyItemChanged(position);
    }
    public boolean clearTheFile(File file) {
        try {
            FileWriter fwOb = new FileWriter(file, false);
            PrintWriter pwOb = new PrintWriter(fwOb, false);
            pwOb.flush();
            pwOb.close();
            fwOb.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void dialogForEdit(Apps gestureApps, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetGesture.this);

        LayoutInflater inflater = SetGesture.this.getLayoutInflater();

        View view = inflater.inflate(R.layout.gesture_view_dialog, null);

        initializerGestureLib(view);// Initializer Gesture Library

        builder.setView(view);

        builder.setCancelable(true);
        builder.setPositiveButton("Yes",
                (dialog, id) -> {

                    setGesture_presenter.editGestureApp(gestureApps, position);
                    dialog.cancel();
                });

        builder.setNegativeButton("No",
                (dialog, id) -> dialog.dismiss());

        customAlertForEditDialog(builder);//Custom button for dialog
    }

    private void initializerGestureLib(View view) {
        objGestureOverLay = view.findViewById(R.id.GestureDialog);
        objGestureLib = GestureLibraries.fromFile(ROOT_PATH + ROOT_FILE);
        objGestureLib.load();
    }

    private void customAlertForEditDialog(AlertDialog.Builder builder){
        AlertDialog alert = builder.create();
        alert.create();
        alert.setOnShowListener(arg0 -> setColorForButtonEditDialog(alert));
        alert.show();
    }
    private void setColorForButtonEditDialog(AlertDialog alert) {
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(62, 151, 186));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(62, 151, 186));
    }

    @Override
    public void OnClickItemGestureApps(Apps apps, View v, int position) {
        setGesture_presenter.showPopUp(apps, v, position);
    }

}