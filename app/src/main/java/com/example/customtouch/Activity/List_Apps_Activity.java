package com.example.customtouch.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.customtouch.Adapter.ListAppAdapter;
import com.example.customtouch.Interface.In_ListAppsActivity;
import com.example.customtouch.Model.Apps;
import com.example.customtouch.Presenter.ListAppsActivity_Presenter;
import com.example.customtouch.R;

import java.util.ArrayList;
import java.util.List;

public class List_Apps_Activity extends AppCompatActivity implements In_ListAppsActivity {

    private static final String COLOR_STRING = "#3A7D97";
    private static final String KEY_EDIT_SUCCESS = "KEY_EDIT_SUSSESS";

    private ProgressBar mProgressBar;
    private RecyclerView listViewAllApps;

    private List<Apps> listApps = new ArrayList<>();
    private ListAppAdapter listAppAdaptor;
    private SearchView searchView;
    private ListAppsActivity_Presenter listAppsActivity_presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listAppsActivity_presenter = new ListAppsActivity_Presenter(this);
        listAppsActivity_presenter.initializerView();
        listAppsActivity_presenter.loadAllApps();

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

        buildSearchView(menu);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listAppAdaptor.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAppAdaptor.getFilter().filter(newText);
                return false;
            }
        });
        return true;

    }

    private void buildSearchView(Menu menu) {
        getMenuInflater().inflate(R.menu.main_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void buildViewForListActivity() {
        setContentView(R.layout.activity_list_apps);
        getSupportActionBar().setTitle("");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor(COLOR_STRING));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        mProgressBar = findViewById(R.id.progress_bar);
        listViewAllApps = findViewById(R.id.ListApps);
    }

    @Override
    public void loadApp() {
        showLoading(true);
        exec(() -> {
            PackageManager pm = getPackageManager();
            listApps = new ArrayList<>();
            listAppsActivity_presenter.getApps(pm);
            runOnUiThread(() -> {
                showLoading(false);
                listAppsActivity_presenter.setDataForRecycleView();
            });
        });
    }

    private void exec(Runnable runnable) {
        new Thread(runnable).start();
    }

    private void showLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        listViewAllApps.setVisibility(!isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void getAppFromAPI_R(@NonNull PackageManager pm) {
        List<ApplicationInfo> applicationInfoList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            Intent intent = pm.getLaunchIntentForPackage(applicationInfo.packageName);
            if (intent != null && Intent.ACTION_MAIN.equals(intent.getAction())) {
                listApps.add(loadAppFromPackage(pm, applicationInfo));
            }
        }
    }

    @Override
    public void getAppOtherAPI(@NonNull PackageManager pm) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(mainIntent, PackageManager.GET_META_DATA);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (resolveInfo.activityInfo != null && resolveInfo.activityInfo.packageName != null) {
                listApps.add(loadAppFromPackage(pm, resolveInfo.activityInfo));
            }
        }
    }

    @NonNull
    private Apps loadAppFromPackage(@NonNull PackageManager pm, @NonNull PackageItemInfo packageItemInfo) {
        String appName = packageItemInfo.loadLabel(pm).toString();
        Drawable imgApp = packageItemInfo.loadIcon(pm);
        return new Apps(imgApp, appName, packageItemInfo.packageName, null);
    }

    @Override
    public void dataRecycleView() {
        listAppAdaptor = new ListAppAdapter(listApps, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listViewAllApps.setLayoutManager(linearLayoutManager);
        listViewAllApps.setAdapter(listAppAdaptor);
    }

    @Override
    public void OnClickItemApps(Apps apps) {
        onClickGoToDetail(apps);
    }

    //Result Launcher from GestureActivity
    ActivityResultLauncher<Intent> resultLauncherForAdd =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });

    private void onClickGoToDetail(Apps app) {
        Intent intent = new Intent(List_Apps_Activity.this, Gesture_Activity.class);
        intent.putExtra(KEY_EDIT_SUCCESS, app);
        resultLauncherForAdd.launch(intent);
    }
}
