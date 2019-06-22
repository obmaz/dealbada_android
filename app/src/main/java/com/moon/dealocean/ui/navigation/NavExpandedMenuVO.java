package com.moon.dealocean.ui.navigation;

import java.util.ArrayList;

/**
 * Created by zambo on 2017-08-11.
 */

public class NavExpandedMenuVO {
    private int version;
    private ArrayList<NavExpandedMenuDataVO> data = new ArrayList<>();

    public int getVersion() {
        return version;
    }

    public ArrayList<NavExpandedMenuDataVO> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "NavExpandedMenuVO{" +
                "version=" + version +
                ", data=" + data +
                '}';
    }
}
