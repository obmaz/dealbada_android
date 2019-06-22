package com.moon.dealocean.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseActivity;

/**
 * Created by zambo on 2016. 12. 22..
 */

public class GeneralActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        Toolbar toolbar = (Toolbar) findViewById(R.id.generalToolBar);
        setSupportActionBar(toolbar);
    }
}
