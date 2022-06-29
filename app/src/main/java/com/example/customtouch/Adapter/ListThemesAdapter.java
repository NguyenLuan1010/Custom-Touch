package com.example.customtouch.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customtouch.Model.BtnHomeTheme;
import com.example.customtouch.R;

import java.io.File;

public class ListThemesAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private BtnHomeTheme btnHomeTheme;

    public ListThemesAdapter(BtnHomeTheme btnHomeTheme) {
        this.btnHomeTheme = btnHomeTheme;
    }

    public void setBtnHomeTheme(BtnHomeTheme btnHomeTheme) {
        this.btnHomeTheme = btnHomeTheme;
    }

    @Override
    public int getCount() {
        return BtnHomeTheme.values().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null){
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.image_themes,null);
        }
        BtnHomeTheme theme = BtnHomeTheme.values()[position];

        View mView = convertView.findViewById(R.id.image_icon);
        if (theme.equals(btnHomeTheme)) {
            mView.setBackgroundResource(R.drawable.stroke_layout);
        }else{
            mView.setBackgroundResource(0);
        }
        ImageView imageView = convertView.findViewById(R.id.image_items);
        imageView.setImageResource(theme.getDrawableRes());
        return convertView;
    }
}
