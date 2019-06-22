package com.moon.dealocean;

import android.app.Application;

import androidx.preference.PreferenceManager;

/**
 * Created by zambo on 2016-12-27.
 */

public class DealoceanApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
//        DealbadaClient.newInstance(getApplicationContext());
    }
}
