package com.example.customtouch.Presenter;

import android.os.Build;

import com.example.customtouch.Interface.In_BuildUIForHomeButton;

public class BuildHomeButtonView_Presenter {
    private In_BuildUIForHomeButton inBuildUIForHomeButton;

    public BuildHomeButtonView_Presenter(In_BuildUIForHomeButton inHomeButton) {
        this.inBuildUIForHomeButton = inHomeButton;
    }

    public boolean checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return true;
        }
        return false;
    }

    public void buildViewButton(){
        inBuildUIForHomeButton.buildViewHomeButton();
    }
    public void showHomeButtonView(){
        inBuildUIForHomeButton.homeButtonView();
    }
    public void addFirstMenuTaskView(){
        inBuildUIForHomeButton.fistMenuTaskView();
    }
    public void showFirstMenuTask(){
        inBuildUIForHomeButton.addFirstMenuTaskView();
    }
    public void buildItemFirstMenuTasks(){
        inBuildUIForHomeButton.itemFirstMenuTasksView();
    }
    public void addEventOnClickFirstMenuTask(){
        inBuildUIForHomeButton.eventOnClickItemFirstMenu();
    }
    public void addSecondMenuTaskView(){
        inBuildUIForHomeButton.secondMenuTaskView();
    }
    public void showSecondMenuTask(){
        inBuildUIForHomeButton.addSecondMenuTaskView();
    }
    public void buildItemSecondMenuTasks(){
        inBuildUIForHomeButton.itemSecondMenuTasksView();
    }
    public void addEventOnClickSecondMenuTask(){
        inBuildUIForHomeButton.eventOnClickItemSecondMenu();
    }
    public void addGestureLayOut(){
        inBuildUIForHomeButton.gestureLayOutView();
    }
    public void showGestureLayOut(){
        inBuildUIForHomeButton.addGestureLayOutView();
    }
    public void actionOfGestureHomeButton(){
        inBuildUIForHomeButton.actionGestureView();
    }

    public void createNotificationChannel() {
        if (checkVersion()) {
            inBuildUIForHomeButton.notificationChannel();
        }
    }

    public void createNotificationControl() {
        inBuildUIForHomeButton.notificationControl();
    }

    public void onClickButtonInNotification(int id) {
        inBuildUIForHomeButton.handleActionNotification(id);
    }

    public void hideAllViewHomeButton(){
        inBuildUIForHomeButton.hideHomeButtonView();
    }
    public void ForAddViewIcon() {
        if (checkVersion()) {
            inBuildUIForHomeButton.createHomeButtonFromAPIO();
        } else {
            inBuildUIForHomeButton.createHomeButtonAnotherAPI();
        }
    }

    public void ForAddViewFirstMenuTask() {
        if (checkVersion()) {
            inBuildUIForHomeButton.createFirstMenuTaskFromAPIO();
        } else {

            inBuildUIForHomeButton.createFirstMenuTaskAnotherAPI();
        }
    }

    public void ForAddViewSecondMenuTask() {
        if (checkVersion()) {
            inBuildUIForHomeButton.createSecondMenuTaskFromAPIO();
        } else {

            inBuildUIForHomeButton.createSecondMenuTaskAnotherAPI();
        }
    }

    public void ForAddGestureView() {
        if (checkVersion()) {
            inBuildUIForHomeButton.createGestureViewFromAPIO();
        } else {
            inBuildUIForHomeButton.createGestureViewAnotherAPI();
        }
    }

    public void goToHomeScreen() {
        inBuildUIForHomeButton.backToHomeScreen();
    }

    public void onClickOutSideFirstMenu(){
        inBuildUIForHomeButton.clickOutSideFirstMenuTasks();
    }
    public void onClickOutSideSecondMenu(){
        inBuildUIForHomeButton.clickOutSideSecondMenuTasks();
    }
    public void onClickOutSideGestureView(){
        inBuildUIForHomeButton.clickOutSideGestureView();
    }

}
