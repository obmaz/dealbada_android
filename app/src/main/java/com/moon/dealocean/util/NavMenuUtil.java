package com.moon.dealocean.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moon.dealocean.R;
import com.moon.dealocean.ui.navigation.NavExpandedMenuDataVO;
import com.moon.dealocean.ui.navigation.NavExpandedMenuVO;

import java.io.InputStream;

/**
 * Created by d.moon on 2017-08-17.
 */

public class NavMenuUtil {
    private static final String MENU_JSON = "navigation_menu.json";
    private static NavExpandedMenuVO mSavedNavExpandedMenuVO;

    public static NavExpandedMenuDataVO getNavExpandedMenuChild(int headerNum, int childNum) {
        return mSavedNavExpandedMenuVO.getData().get(headerNum).getSubMenu().get(childNum);
    }

    public static NavExpandedMenuVO getNavExpandedMenuVO(Context context) {
        if (mSavedNavExpandedMenuVO == null) {
            try {
                InputStream is = context.getResources().openRawResource(R.raw.navigation_menu);
                int size = 0;
                size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                String defaultNavExpandedMenuJson = new String(buffer, "UTF-8");
                String savedNavExpandedMenuJson = Util.getString(context, MENU_JSON, defaultNavExpandedMenuJson);

                Gson gson = new GsonBuilder().create();
                mSavedNavExpandedMenuVO = gson.fromJson(savedNavExpandedMenuJson, NavExpandedMenuVO.class);
                NavExpandedMenuVO defaultNavExpandedMenuVO = gson.fromJson(defaultNavExpandedMenuJson, NavExpandedMenuVO.class);

                if (defaultNavExpandedMenuVO.getVersion() > mSavedNavExpandedMenuVO.getVersion()) {
                    setNavExpandedMenuVO(context, defaultNavExpandedMenuVO);
                    mSavedNavExpandedMenuVO = defaultNavExpandedMenuVO;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return mSavedNavExpandedMenuVO;
    }

    public static void setNavExpandedMenuVO(Context context, NavExpandedMenuVO navExpandedMenuVO) {
        mSavedNavExpandedMenuVO = navExpandedMenuVO;
        Gson gson = new GsonBuilder().create();
        String menuJson = gson.toJson(navExpandedMenuVO).toString();

        Util.putString(context, MENU_JSON, menuJson);
    }
}
