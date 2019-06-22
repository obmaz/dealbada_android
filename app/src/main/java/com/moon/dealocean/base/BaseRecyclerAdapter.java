package com.moon.dealocean.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by zambo on 2016-07-16.
 */
public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ITEM_MORE = 2;
    private transient Context mContext;
    private int mHeaderResId = -1;
    private int mItemResId = -1;
    private int mLoadMOreResId = -1;
    private boolean mHasMoreItem = true;
    private boolean mIsNetworkConnected = true;
    private boolean mIsLoading = false;
    private Class mHeaderViewHolderClass;
    private Class mItemViewHolderClass;
    private Class mLoadMoreViewHolderClass;
    private transient SwipeRefreshLayout mSwipeRefreshLayout;

    public Context getContext() {
        return mContext;
    }

    public abstract void loadContent();

    public abstract void loadMore();

    public abstract void refreshContent();

    public void setHeaderViewHolder(int resId, Class headerViewHolderClass) {
        mHeaderResId = resId;
        mHeaderViewHolderClass = headerViewHolderClass;
    }

    public void setItemViewHolder(int resId, Class itemViewHolderClass) {
        mItemResId = resId;
        mItemViewHolderClass = itemViewHolderClass;
    }

    public void setLoadMoreViewHolder(int resId, Class loadMoreViewHolderClass) {
        mLoadMOreResId = resId;
        mLoadMoreViewHolderClass = loadMoreViewHolderClass;
    }

    public boolean useHeader() {
        if (mHeaderResId == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void setHasMoreItem(boolean flag) {
        mHasMoreItem = flag;
    }

    public boolean hasMoreItem() {
        return mHasMoreItem;
    }

    public void setIsNetworkConnected(boolean flag) {
        mIsNetworkConnected = flag;
    }

    public boolean isNetworkConnected() {
        return mIsNetworkConnected;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean flag) {
        mIsLoading = flag;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
    }

    public void showSwipeRefreshIcon() {
        if (getSwipeRefreshLayout() != null && !getSwipeRefreshLayout().isRefreshing()) {
            getSwipeRefreshLayout().setRefreshing(true);
        }
    }

    public void hideSwipeRefreshIcon() {
        if (getSwipeRefreshLayout() != null && getSwipeRefreshLayout().isRefreshing()) {
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSwipeRefreshLayout().setRefreshing(false);
                }
            }, 1000);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        int resId = -1;
        Class clazz = null;

        if (useHeader() && viewType == TYPE_HEADER) {
            resId = mHeaderResId;
            clazz = mHeaderViewHolderClass;
        } else if (viewType == TYPE_ITEM) {
            resId = mItemResId;
            clazz = mItemViewHolderClass;
        } else if (viewType == TYPE_ITEM_MORE) {
            resId = mLoadMOreResId;
            clazz = mLoadMoreViewHolderClass;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
        Object viewHolder = null;

        try {
            viewHolder = clazz.getConstructor(View.class).newInstance(view);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return (RecyclerView.ViewHolder) viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (useHeader()) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }
        } else if (position + 1 < getItemCount()) {
            return TYPE_ITEM;
        } else if (position + 1 == getItemCount()) {
            return TYPE_ITEM_MORE;
        }
        throw new RuntimeException("Make sure your using types correctly");
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}
