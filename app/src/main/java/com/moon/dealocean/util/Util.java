package com.moon.dealocean.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.TypedValue;

/**
 * Created by zambo on 2016-07-21.
 */
public class Util {
    public static int getPx(Context context, int dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return (int) px;
    }

    public static int getColor(Context context, int resId) {
        Resources.Theme theme = context.getTheme();
        TypedValue storedValue = new TypedValue();

        if (theme.resolveAttribute(resId, storedValue, true)) {
            return storedValue.data;
        } else {
            return ContextCompat.getColor(context, resId);
        }
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean flag) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, flag);
        editor.commit();
    }
}
