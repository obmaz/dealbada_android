package com.moon.dealocean.ui.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseFragment;
import com.moon.dealocean.util.Util;

/**
 * Created by zambo on 2016. 12. 23..
 */

public class LoginFragment extends BaseFragment {
    private static final String LOGIN_URL = "https://nid.naver.com/oauth2.0/authorize?client_id=Lwj2yX91T75T3qxEfEjn&response_type=code&redirect_uri=http://www.dealbada.com&state=65c33dfdcc6c82f45bcb04a549334150";
    private Activity mActivity;

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginLayout = inflater.inflate(R.layout.login, container, false);
        WebView loginWebView = (WebView) loginLayout.findViewById(R.id.loginWebView);
        loginWebView.setWebViewClient(new LoginWebViewClient());
        loginWebView.getSettings().setJavaScriptEnabled(true);
        loginWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        loginWebView.loadUrl(LOGIN_URL);

        mActivity = getActivity();
        return loginLayout;
    }

    @Override
    public boolean onBackPressed() {
        if (mActivity != null) {
            mActivity.onBackPressed();
        }
        return true;
    }

    private class LoginWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("moon", "onPageFinished url : " + url);
            if (url.contains("oauth_token") && url.contains("inapp_view=")) {
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.flush();

                String cookie = cookieManager.getCookie(url);
                Util.putString(getContext(), "cookie", cookie);
                SettingsHelper.setLoginned(getContext(), true);

                onBackPressed();
            }
        }
    }
}
