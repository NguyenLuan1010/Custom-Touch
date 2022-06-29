package com.example.customtouch.Model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class Apps implements Parcelable {
    private Drawable imgApp;
    private String nameApp;
    private String packageName;
    private Bitmap imgGesture;

    public Apps(){

    }

    public Apps(Drawable imgApp, String nameApp, String packageName,Bitmap bitmap) {
        this.imgApp = imgApp;
        this.nameApp = nameApp;
        this.packageName = packageName;
        this.imgGesture = bitmap;
    }


    protected Apps(Parcel in) {
        nameApp = in.readString();
        packageName = in.readString();
        imgGesture = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Apps> CREATOR = new Creator<Apps>() {
        @Override
        public Apps createFromParcel(Parcel in) {
            return new Apps(in);
        }

        @Override
        public Apps[] newArray(int size) {
            return new Apps[size];
        }
    };

    public Drawable getImgApp() {
        return imgApp;
    }

    public void setImgApp(Drawable imgApp) {
        this.imgApp = imgApp;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Bitmap getImgGesture() {
        return imgGesture;
    }

    public void setImgGesture(Bitmap imgGesture) {
        this.imgGesture = imgGesture;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameApp);
        dest.writeString(packageName);
        dest.writeParcelable(imgGesture, flags);
    }
}
