package com.moon.dealocean.ui.settings;

import android.os.Bundle;

import androidx.core.app.NavUtils;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseActivity;

/**
 * Created by zambo on 2016. 12. 5..
 */

public class SettingsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settingToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startFragment(SettingsFragment.newInstance(), R.id.pref_container, "SettingsFragment", true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
