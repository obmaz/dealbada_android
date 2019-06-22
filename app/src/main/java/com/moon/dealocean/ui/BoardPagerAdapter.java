package com.moon.dealocean.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.moon.dealocean.base.BaseFragment;
import com.moon.dealocean.ui.board.BoardFragment;

import java.util.ArrayList;

/**
 * Created by zambo on 2016-06-30.
 */
public class BoardPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();
    private int mMaxFragmentCount = 3;
    private boolean mNeedUpdate;

    public BoardPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public boolean isNeedUpdate() {
        return mNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        mNeedUpdate = needUpdate;
    }

    public void addFragment(BaseFragment fragment) {
        if (mFragmentList.size() > getMaxFragmentCount()) {
            mFragmentList.remove(getMaxFragmentCount());
        }

        if (mFragmentList.size() > 1) {
            mFragmentList.add(1, fragment);
        } else {
            mFragmentList.add(fragment);
        }

        notifyDataSetChanged();
    }

    public int getMaxFragmentCount() {
        return mMaxFragmentCount;
    }

    public void setBoardFragment(BaseFragment fragment) {
        if (mFragmentList.size() > 0) {
            mFragmentList.remove(0);
        }

        mFragmentList.add(0, fragment);
        setNeedUpdate(true);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof BoardFragment && !isNeedUpdate()) {
            return POSITION_UNCHANGED;
        } else {
            setNeedUpdate(false);
            return POSITION_NONE;
        }
    }
}