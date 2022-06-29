package com.example.customtouch.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customtouch.Interface.In_ListAppsActivity;
import com.example.customtouch.Model.Apps;
import com.example.customtouch.R;

import java.util.ArrayList;
import java.util.List;

public class ListAppAdapter extends RecyclerView.Adapter<ListAppAdapter.AppsViewHolder> implements Filterable {

    private List<Apps> listApps;
    private List<Apps> listAppsOld;
    private In_ListAppsActivity in_listAppsActivity;
    public ListAppAdapter(List<Apps> listApps, In_ListAppsActivity in_listAppsActivity) {
        this.listApps = listApps;
        this.listAppsOld = listApps;
        this.in_listAppsActivity = in_listAppsActivity;
    }

    @NonNull
    @Override
    public AppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_app,parent,false);
        return new AppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsViewHolder holder, int position) {
       final Apps app =  listApps.get(position);
        if(app == null){
            return;
        }
        holder.imgApp.setImageDrawable(app.getImgApp());
        holder.nameApp.setText(app.getNameApp());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_listAppsActivity.OnClickItemApps(app);
            }
        });
    }

    @Override
    public int getItemCount() {

        return listApps == null ? 0 : listApps.size();
    }

    class AppsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private ImageView imgApp;
        private TextView nameApp;


        public AppsViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout_item_apps);
            imgApp = itemView.findViewById(R.id.ImgApps);
            nameApp = itemView.findViewById(R.id.NameApps);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty()){
                    listApps = listAppsOld;
                }else{
                    List<Apps> lists = new ArrayList<>();
                    for(Apps app:listAppsOld){
                        if(app.getNameApp().toLowerCase().contains(search.toLowerCase())){
                            lists.add(app);
                        }
                    }
                    listApps = lists;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listApps;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listApps = (List<Apps>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
