package com.example.thomasburch.localchat;


import android.content.Context;
/**
 * Created by thomasburch on 2/12/16.
 */
public class AppInfo {
    private static AppInfo instance = null;

    protected AppInfo() {
        // Exists only to defeat instantiation.
    }
    // Here are some values we want to keep global.
    public String my_user_id;
    public String my_nickname;
    public String my_lat;
    public String my_lng;

    public static AppInfo getInstance(Context context) {
        if(instance == null) {
            instance = new AppInfo();
            instance.my_user_id = null;
            instance.my_nickname = null;
            instance.my_lat = null;
            instance.my_lng = null;
        }
        return instance;
    }
}