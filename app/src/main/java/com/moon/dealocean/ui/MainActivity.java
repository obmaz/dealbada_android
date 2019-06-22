package com.moon.dealocean.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.customview.widget.ViewDragHelper;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lapism.searchview.SearchView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseActivity;
import com.moon.dealocean.base.BaseFragment;
import com.moon.dealocean.network.ResponseCallback;
import com.moon.dealocean.network.dealbada.DealbadaClient;
import com.moon.dealocean.network.vo.UserVO;
import com.moon.dealocean.parser.dealbada.DealBadaUserParser;
import com.moon.dealocean.ui.navigation.NavExpandableListAdapter;
import com.moon.dealocean.ui.navigation.NavExpandedMenuDataVO;
import com.moon.dealocean.ui.navigation.NavExpandedMenuVO;
import com.moon.dealocean.ui.settings.SettingsHelper;
import com.moon.dealocean.util.NavMenuUtil;
import com.moon.dealocean.util.Util;

import net.htmlparser.jericho.Source;

import java.lang.reflect.Field;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    public SearchView mSearchView;
    public DrawerLayout mDrawer;
    private TextView mNavHeaderProfileNickname;
    private TextView mNavHeaderProfileInfo;
    private NavExpandedMenuVO mNavExpandedMenuVO;
    private BoardPagerFragment mBoardPagerFragment;
    private int mDefaultEdgeRange;
    private Field mEdgeSize = null;
    private ViewDragHelper mDraggerObj = null;

    private ExpandableListView mNavExpandableList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDrawerEdgeRange();
        initNavigation();
        updateUserInfo();

        String defaultBoard = Util.getString(this, "settingsDefaultBoard", "0"); // ListPreference cannot be integer
        NavExpandedMenuDataVO navExpandedMenuDataVO = NavMenuUtil.getNavExpandedMenuChild(0, Integer.parseInt(defaultBoard));
        startFromExpandedMenu(navExpandedMenuDataVO);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String url = intent.getDataString();
        if (url != null) {
            mBoardPagerFragment.startDetailFragment("", url);
        }
    }

    public SearchView getSearchView() {
        return mSearchView;
    }

    private void initView() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolBar);
        mSearchView = (SearchView) findViewById(R.id.mainSearchView);
        mDrawer = (DrawerLayout) findViewById(R.id.mainDrawerLayout);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        // Init SearchView
        mSearchView.setVersion(SearchView.VERSION_MENU_ITEM);
        mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);

        // Init NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        RoundedImageView navHeaderProfileImage = (RoundedImageView) navHeader.findViewById(R.id.navHeaderProfileImage);
        navHeaderProfileImage.setBackgroundColor(Color.parseColor("#FFFFFF")); // XML 에서 설정이 안되므로 코드로 필요

        mNavHeaderProfileNickname = (TextView) navHeader.findViewById(R.id.navHeaderProfileNickname);
        mNavHeaderProfileNickname.setText("Nick Name - Now Loading");

        mNavHeaderProfileInfo = (TextView) navHeader.findViewById(R.id.navHeaderProfileInfo);
        mNavHeaderProfileInfo.setText("Profile Info - Now Loading");
        mNavExpandableList = (ExpandableListView) findViewById(R.id.main_nav_list);
    }

    private void initNavigation() {
        // To set Navigation Menu Adapter
        mNavExpandedMenuVO = NavMenuUtil.getNavExpandedMenuVO(this);
        NavExpandableListAdapter navMenuAdapter = new NavExpandableListAdapter(this, mNavExpandedMenuVO);
        mNavExpandableList.setAdapter(navMenuAdapter);


        // To recover Navigation Menu status
        int i = 0;
        for (NavExpandedMenuDataVO navExpandedMenuDataVO : mNavExpandedMenuVO.getData()) {
            if (navExpandedMenuDataVO.isOpened()) {
                mNavExpandableList.expandGroup(i);
            }
            i++;
        }

        mNavExpandableList.setOnChildClickListener((expandableListView, view, headerNum, childNum, l) -> {
            NavExpandedMenuDataVO navExpandedMenuDataVO = NavMenuUtil.getNavExpandedMenuChild(headerNum, childNum);
            startFromExpandedMenu(navExpandedMenuDataVO);
            mDrawer.closeDrawer(GravityCompat.START);
            return false;
        });

        mNavExpandableList.setOnGroupClickListener((expandableListView, view, headerNum, l) -> {
            return false;
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    private void updateUserInfo() {
        if (SettingsHelper.isLoginned(this)) {
            DealbadaClient.getInstance().getUserInfo(new ResponseCallback() {
                @Override
                public void opSuccess(Source source) {
                    Log.d("moon", "moon aaaa updateUserInfo opSuccess");
                    setUserInfoFromSource(source);
                }

                @Override
                public void onFail() {
                    Log.d("moon", "moon aaaa updateUserInfo onFail ");
                }
            });
        } else {
            Log.d("moon", "moon aaaa updateUserInfo isLoginned false");

            mNavHeaderProfileNickname.setText("Need Login");
            mNavHeaderProfileInfo.setText("Need Login");
        }
    }

    private void setUserInfoFromSource(Source source) {
        UserVO user = DealBadaUserParser.getInstance().getUser(source);

        Log.d("moon", "moon aaaa setUserInfoFromSource");

        // Loginned
        if (user != null) {
            Log.d("moon", "moon aaaa setUserInfoFromSource is not null");
            mNavHeaderProfileNickname.setText(user.nickName);
            mNavHeaderProfileInfo.setText("Message : " + user.message + " / Point : " + user.point);
        } else {
            // 로그인 토큰 정보가 있어서 시도 했지만, 로그인이 안된 경우 로그인 토큰 삭제함, 잘 작동 안됨, 로직 확인 필요
//            SettingsHelper.deleteLoginTokken(this);
            Log.d("moon", "moon aaaa setUserInfoFromSource is null");
        }
    }

    private void initDrawerEdgeRange() {
        try {
            Field dragger = mDrawer.getClass().getDeclaredField("mLeftDragger");
            dragger.setAccessible(true);
            mDraggerObj = (ViewDragHelper) dragger.get(mDrawer);
            mEdgeSize = mDraggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            mDefaultEdgeRange = mEdgeSize.getInt(mDraggerObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDrawerEdgeRange(boolean flag) {
        try {
            mEdgeSize.setInt(mDraggerObj, mDefaultEdgeRange * 1);

            if (flag) {
                mEdgeSize.setInt(mDraggerObj, mDefaultEdgeRange * SettingsHelper.getDrawerEdgeRange(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.mainContainer);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (!getCurrentFragment().onBackPressed()) {
                super.onBackPressed();
            }
        }
    }

    public BoardPagerFragment getBoardPagerFragment() {
        return mBoardPagerFragment;
    }

    private void startFromExpandedMenu(NavExpandedMenuDataVO navExpandedMenuDataVO) {
        String title = navExpandedMenuDataVO.getTitle();
        String url = navExpandedMenuDataVO.getUrl();

        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme().toLowerCase();

        switch (scheme) {
            case "http":
            case "https":
                mBoardPagerFragment = BoardPagerFragment.newInstance(title, url);
                startFragment(mBoardPagerFragment, R.id.mainContainer, "BoardPagerFragment", false);
                break;
            case "activity":
                try {
                    Class clazz = Class.forName(uri.getHost());
                    startActivity(new Intent(this, clazz));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
