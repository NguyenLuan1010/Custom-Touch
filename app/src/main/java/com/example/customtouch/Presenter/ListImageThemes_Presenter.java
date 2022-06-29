package com.example.customtouch.Presenter;

import com.example.customtouch.Interface.In_ListImageThemes;

public class ListImageThemes_Presenter {
    private In_ListImageThemes in_listImageThemes;

    public ListImageThemes_Presenter(In_ListImageThemes in_listImageThemes) {
        this.in_listImageThemes = in_listImageThemes;
    }
    public void initializerView(){
        in_listImageThemes.buildView();
    }
    public void eventOnClickGridItem(){
        in_listImageThemes.addEventClickGridItem();
    }
}
