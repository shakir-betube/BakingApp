package com.appsys.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHandler {

    public static final String TIME_HOLDER = "SEEK_TIME_HOLDER";

    public static void setTime(Context context, long time){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(TIME_HOLDER, time);
        editor.apply();
    }

    public static long getTime(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(TIME_HOLDER, 0);
    }

    public static void reset(Context context){
        setTime(context, 0);
    }
}