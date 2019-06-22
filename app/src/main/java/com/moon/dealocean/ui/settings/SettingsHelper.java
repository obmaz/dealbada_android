package com.moon.dealocean.ui.settings;

import android.content.Context;
import android.webkit.CookieManager;

import com.moon.dealocean.R;
import com.moon.dealocean.util.Util;

/**
 * Created by zambo on 2016-12-27.
 */

public class SettingsHelper {
    private static final String THEME_INDIGO = "1";
    private static final String THEME_TEAL = "2";
    private static final String THEME_PURPLE = "3";
    private static final String DEFAULT_DRAWER_EDGE_RANGE = "10";

    public static int getThemeResId(Context context) {
        String theme = Util.getString(context, "settingsTheme", THEME_INDIGO);
        int themeResId = -1;

        switch (theme) {
            case THEME_INDIGO:
                themeResId = R.style.AppTheme_NoActionBar_Indigo;
                break;
            case THEME_TEAL:
                themeResId = R.style.AppTheme_NoActionBar_Teal;
                break;
            case THEME_PURPLE:
                themeResId = R.style.AppTheme_NoActionBar_Purple;
                break;
        }

        return themeResId;
    }

    public static int getDrawerEdgeRange(Context context) {
        String drawerEdgeRange = Util.getString(context, "settingsDrawerEdgeRange", DEFAULT_DRAWER_EDGE_RANGE);
        return Integer.parseInt(drawerEdgeRange);
    }

    public static boolean isLoginned(Context context) {
        return Util.getBoolean(context, "isLoggined", false);
    }

    public static void setLoginned(Context context, boolean loginStatus) {
        Util.putBoolean(context, "isLoggined", loginStatus);
    }

    public static void deleteLoginTokken(Context context) {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().removeSessionCookies(null);
        CookieManager.getInstance().flush();
        setLoginned(context, false);
    }
}
