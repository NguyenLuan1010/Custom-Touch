package com.example.customtouch.Adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.customtouch.Interface.In_SetGesture;
import com.example.customtouch.Model.Apps;
import com.example.customtouch.R;

import java.util.List;

public class ListGestureAppsAdapter extends RecyclerView.Adapter<ListGestureAppsAdapter.AppsViewHolder> {

    private List<Apps> listGestureApps;
    private In_SetGesture in_setGesture;

    public ListGestureAppsAdapter(List<Apps> listApps, In_SetGesture in_setGesture) {
        this.listGestureApps = listApps;
        this.in_setGesture = in_setGesture;
    }

    @NonNull
    @Override
    public AppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gesture_apps, parent, false);
        return new AppsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AppsViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        final Apps app = listGestureApps.get(position);
        if (app == null) {
            return;
        }
        holder.imgApp.setImageDrawable(app.getImgApp());
        holder.txtAppName.setText(app.getNameApp());
        holder.imgGesture.setImageBitmap(app.getImgGesture());
        holder.btnSettingChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_setGesture.OnClickItemGestureApps(app, v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listGestureApps == null ? 0 : listGestureApps.size();
    }

    class AppsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView txtAppName;
        private ImageView imgApp;
        private ImageView imgGesture;
        private ImageView btnSettingChoice;

        public AppsViewHolder(@NonNull View itemView) {
            super(itemView);
            buildView();
        }

        private void buildView() {
            linearLayout = itemView.findViewById(R.id.layout_item_gesture_apps);
            imgApp = itemView.findViewById(R.id.ImgGestureApps);
            imgGesture = itemView.findViewById(R.id.ImgGesture);
            txtAppName = itemView.findViewById(R.id.NameApps);
            btnSettingChoice = itemView.findViewById(R.id.Setting_choice);
        }
    }
}
