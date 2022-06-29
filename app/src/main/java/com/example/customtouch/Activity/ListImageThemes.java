package com.example.customtouch.Activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.customtouch.Adapter.ListThemesAdapter;
import com.example.customtouch.Interface.In_ListImageThemes;
import com.example.customtouch.Model.BtnHomeTheme;
import com.example.customtouch.Presenter.ListImageThemes_Presenter;
import com.example.customtouch.R;
import com.example.customtouch.Utils.SettingSharedPref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class ListImageThemes extends AppCompatActivity implements In_ListImageThemes {

    private static final String COLOR_STRING ="#3A7D97";
    private static final String TITLE = "Themes";

    private GridView gridView;
    private BtnHomeTheme mBtnHomeTheme;

    private SettingSharedPref mSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListImageThemes_Presenter listImageThemes_presenter = new ListImageThemes_Presenter(this);
        listImageThemes_presenter.initializerView();
        listImageThemes_presenter.eventOnClickGridItem();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            mSetting.setBtnHomeTheme(mBtnHomeTheme.getDrawableRes());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void buildView() {

        setContentView(R.layout.list_themes);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(COLOR_STRING));

        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle(TITLE);

        mSetting = SettingSharedPref.getInstance(this);
        mBtnHomeTheme = BtnHomeTheme.findByRes(mSetting.getBtnHomeTheme());
        gridView = findViewById(R.id.grid_items);
        gridView.setAdapter(new ListThemesAdapter(mBtnHomeTheme));
    }

    @Override
    public void addEventClickGridItem() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBtnHomeTheme = BtnHomeTheme.values()[position];
                ((ListThemesAdapter) gridView.getAdapter()).setBtnHomeTheme(mBtnHomeTheme);
                ((ListThemesAdapter) gridView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

}
