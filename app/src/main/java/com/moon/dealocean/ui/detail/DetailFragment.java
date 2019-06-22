package com.moon.dealocean.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseFragment;
import com.moon.dealocean.base.BaseRecyclerAdapter;
import com.moon.dealocean.ui.BoardPagerFragment;
import com.moon.dealocean.ui.MainActivity;
import com.moon.dealocean.ui.settings.SettingsHelper;

/**
 * Created by zambo on 16. 7. 1..
 */
public class DetailFragment extends BaseFragment implements BoardPagerFragment.PageSelectedListener {
    private BaseRecyclerAdapter mBaseRecyclerAdapter;
    private String mTitle;
    private String mDetailPageUrl;
    private boolean mIsFirstLoading = true;

    public static DetailFragment newInstance(String title, String detailPageUrl) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("detailPageUrl", detailPageUrl);
        detailFragment.setArguments(bundle);

        return detailFragment;
    }

    public void addRecyclerAdapter(BaseRecyclerAdapter baseRecyclerAdapter) {
        mBaseRecyclerAdapter = baseRecyclerAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString("title");
            mDetailPageUrl = bundle.getString("detailPageUrl");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setActionBar();

        View detailLayout = inflater.inflate(R.layout.detail_container, container, false);

        SwipeRefreshLayout detailSwipeRefreshLayout = (SwipeRefreshLayout) detailLayout.findViewById(R.id.detailSwipeRefreshLayout);
        detailSwipeRefreshLayout.setOnRefreshListener(this::refresh);

        mBaseRecyclerAdapter.setSwipeRefreshLayout(detailSwipeRefreshLayout);

        if (mIsFirstLoading) {
            mIsFirstLoading = false;
            refresh();
        }

        RecyclerView detailRecyclerView = (RecyclerView) detailLayout.findViewById(R.id.detailRecyclerView);
        detailRecyclerView.setHasFixedSize(true);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        detailRecyclerView.setAdapter(mBaseRecyclerAdapter);

        return detailLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detailRrfresh:
                refresh();
                break;
            case R.id.detailShareVia:
                runShareVia();
                break;
            case R.id.detailWebviewLock:
                unlockWebview();
                break;
            case R.id.detailBrower:
                jumpToBrower();
                break;
            case R.id.detailReplay:
                replyComment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replyComment() {
        if (!SettingsHelper.isLoginned(getContext())) {
            Toast.makeText(getContext(), "로그인이 필요 합니다", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "제작중 입니다", Toast.LENGTH_LONG).show();
        }
    }

    private void jumpToBrower() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDetailPageUrl));
        startActivity(Intent.createChooser(intent, "브라우져 선택"));
    }

    private void refresh() {
        mBaseRecyclerAdapter.refreshContent();
    }

    public void runShareVia() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, mTitle);
        intent.putExtra(Intent.EXTRA_TEXT, mDetailPageUrl);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "공유하기"));
    }

    public void unlockWebview() {
        if (mBaseRecyclerAdapter instanceof UnLockWebViewScroll) {
            ((UnLockWebViewScroll) mBaseRecyclerAdapter).unLockWebviewScroll();
        }
    }

    @Override
    public boolean onBackPressed() {
        BoardPagerFragment boardPagerFragment = ((MainActivity) getContext()).getBoardPagerFragment();
        boardPagerFragment.setCurrentItem(0);
        return true;
    }

    @Override
    public void onPageSelected() {
        setActionBar();
    }

    private void setActionBar() {
        setHasOptionsMenu(true);
    }
}
