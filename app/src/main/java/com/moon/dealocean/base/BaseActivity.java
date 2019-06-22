package com.moon.dealocean.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.moon.dealocean.ui.settings.SettingsHelper;

/**
 * Created by zambo on 2016. 12. 26..
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SettingsHelper.getThemeResId(this));
    }

    public void startFragment(Fragment fragment, int containerViewId, String tag, boolean addToBakcStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addToBakcStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
}
