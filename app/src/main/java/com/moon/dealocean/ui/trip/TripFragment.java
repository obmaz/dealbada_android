package com.moon.dealocean.ui.trip;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseFragment;
import com.moon.dealocean.base.BaseRecyclerAdapter;
import com.moon.dealocean.ui.BoardPagerFragment;
import com.moon.dealocean.ui.MainActivity;
import com.moon.dealocean.ui.board.DealBadaBoardRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zambo on 16. 7. 1..
 */
public class TripFragment extends BaseFragment implements BoardPagerFragment.PageSelectedListener {
    private BaseRecyclerAdapter mBaseRecyclerAdapter;
    private SearchView mSearchView;
    private String mTitle;
    private String mUrl;
    private boolean mIsFirstLoading = true;

    public static TripFragment newInstance(String title, String boardPath) {
        Log.d("moon", "moon TripFragment newInstance");
        TripFragment tripFragment = new TripFragment();
        Bundle bundle = new Bundle();
        bundle.putString("boardPath", boardPath);
        bundle.putString("title", title);
        tripFragment.setArguments(bundle);
        return tripFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString("title", "");
        mUrl = getArguments().getString("boardPath", "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("moon", "moon BoardFragment onCreateView");
        View boardLayout = inflater.inflate(R.layout.board_container, container, false);

        SwipeRefreshLayout boardSwipeRefreshLayout = (SwipeRefreshLayout) boardLayout.findViewById(R.id.boardSwipeRefreshLayout);
        boardSwipeRefreshLayout.setOnRefreshListener(this::refresh);

        if (mBaseRecyclerAdapter == null) {
            mBaseRecyclerAdapter = new DealBadaBoardRecyclerAdapter(mUrl);
        }

        mBaseRecyclerAdapter.setSwipeRefreshLayout(boardSwipeRefreshLayout);

        setActionBar();

        if (mIsFirstLoading) {
            mIsFirstLoading = false;
            refresh();
        }

        RecyclerView boardRecyclerView = (RecyclerView) boardLayout.findViewById(R.id.boardRecyclerView);
        boardRecyclerView.setHasFixedSize(true);
        boardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        boardRecyclerView.setAdapter(mBaseRecyclerAdapter);

        mSearchView = ((MainActivity) getActivity()).getSearchView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        List<SearchItem> suggestionsList = new ArrayList<>();
        suggestionsList.add(new SearchItem("search1"));
        suggestionsList.add(new SearchItem("search2"));
        suggestionsList.add(new SearchItem("search3"));

        SearchAdapter searchAdapter = new SearchAdapter(getContext(), suggestionsList);
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String query = textView.getText().toString();
//                getData(query, position);
                // mSearchView.close(false);
            }
        });
        mSearchView.setAdapter(searchAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return boardLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.fragment_board, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.boardSearch:
                mSearchView.open(true);
                return true;
            case R.id.boardRefresh:
                refresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        mBaseRecyclerAdapter.refreshContent();
    }

    @Override
    public void onPageSelected() {
        setActionBar();
    }

    private void setActionBar() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle(mTitle);
        }
        setHasOptionsMenu(true);
    }
}