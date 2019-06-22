package com.moon.dealocean.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseFragment;
import com.moon.dealocean.ui.board.BoardFragment;
import com.moon.dealocean.ui.detail.DealBadaDetailRecyclerAdapter;
import com.moon.dealocean.ui.detail.DetailFragment;

/**
 * Created by zambo on 2016-08-15.
 */
public class BoardPagerFragment extends BaseFragment {
    public ViewPager mBoardPager;
    private BoardPagerAdapter mBoardPagerAdapter;

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                ((MainActivity) getActivity()).setDrawerEdgeRange(true);
            } else {
                ((MainActivity) getActivity()).setDrawerEdgeRange(false);
            }

            BaseFragment currentFragment = getCurrentFragment();

            if (currentFragment != null) {
                ((BoardPagerFragment.PageSelectedListener) currentFragment).onPageSelected();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static BoardPagerFragment newInstance(String title, String url) {
        BoardPagerFragment boardPagerFragment = new BoardPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        boardPagerFragment.setArguments(bundle);
        return boardPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getArguments().getString("title", "");
        String url = getArguments().getString("url", "");
        BoardFragment boardFragment = BoardFragment.newInstance(title, url);

        mBoardPagerAdapter = new BoardPagerAdapter(getActivity().getSupportFragmentManager());
        mBoardPagerAdapter.setBoardFragment(boardFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_view_pager, container, false);
        mBoardPager = (ViewPager) view.findViewById(R.id.mainViewPager);
        mBoardPager.setAdapter(mBoardPagerAdapter);
        mBoardPager.addOnPageChangeListener(pageChangeListener);
        mBoardPager.post(() -> pageChangeListener.onPageSelected(mBoardPager.getCurrentItem()));
        return view;
    }

    public int getCurrentItem() {
        return mBoardPager.getCurrentItem();
    }

    public void setCurrentItem(int index) {
        if (mBoardPager != null) {
            mBoardPager.setCurrentItem(index);
        }
    }

    public BaseFragment getCurrentFragment() {
        return (BaseFragment) mBoardPagerAdapter.getItem(getCurrentItem());
    }

    public void addFragment(BaseFragment fragment) {
        mBoardPagerAdapter.addFragment(fragment);
    }

    @Override
    public boolean onBackPressed() {
        return getCurrentFragment().onBackPressed();
    }

    public void startDetailFragment(String title, String url) {
        DealBadaDetailRecyclerAdapter dealBadaDetailRecyclerAdapter = new DealBadaDetailRecyclerAdapter(url);
        DetailFragment detailFragment = DetailFragment.newInstance(title, url);
        detailFragment.addRecyclerAdapter(dealBadaDetailRecyclerAdapter);

        addFragment(detailFragment);
        setCurrentItem(1);
    }

    public interface PageSelectedListener {
        public void onPageSelected();
    }
}
