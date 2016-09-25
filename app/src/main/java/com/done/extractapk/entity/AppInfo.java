package com.done.extractapk.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Done on 2016/9/24 0024.
 */

public class AppInfo {
    public String appName;
    public String packageName;
    public String versionName;
    public int versionCode;
    public int flag;
    public String path;
    public Drawable appIcon;

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", flag=" + flag +
                ", path='" + path + '\'' +
                ", appIcon=" + appIcon +
                '}';
    }
}
