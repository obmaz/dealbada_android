package com.moon.dealocean.ui.navigation;

import java.util.ArrayList;

/**
 * Created by zambo on 2017-08-17.
 */

public class NavExpandedMenuDataVO {
    private ArrayList<NavExpandedMenuDataVO> subMenu = new ArrayList<>();
    private boolean isOpened;
    private String title;
    private String iconImg;
    private String url;
    private String id;

    public String getId() {
        return id;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    public ArrayList<NavExpandedMenuDataVO> getSubMenu() {
        return subMenu;
    }

    public String getTitle() {
        return title;
    }

    public String getIconImg() {
        return iconImg;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "NavExpandedMenuDataVO{" +
                "subMenu=" + subMenu +
                ", isOpened=" + isOpened +
                ", title='" + title + '\'' +
                ", iconImg='" + iconImg + '\'' +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
