package com.moon.dealocean.ui.board;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseRecyclerAdapter;
import com.moon.dealocean.base.LoadMoreHolder;
import com.moon.dealocean.network.ResponseCallback;
import com.moon.dealocean.network.dealbada.DealbadaClient;
import com.moon.dealocean.network.vo.BoardItemVO;
import com.moon.dealocean.parser.dealbada.DealBadaBoardParser;
import com.moon.dealocean.ui.BoardPagerFragment;
import com.moon.dealocean.ui.Constant;
import com.moon.dealocean.ui.MainActivity;
import com.moon.dealocean.util.Util;

import net.htmlparser.jericho.Source;

import java.util.ArrayList;

/**
 * Created by zambo on 2016-06-24.
 */
public class DealBadaBoardRecyclerAdapter extends BaseRecyclerAdapter {
    private ArrayList<BoardItemVO> mBoardItemVOList = new ArrayList<>();
    private int mPage = 1;
    private String mUrl;

    public DealBadaBoardRecyclerAdapter(String url) {
        mUrl = url;
        setItemViewHolder(R.layout.board_item, BoardItemHolder.class);
        setLoadMoreViewHolder(R.layout.load_more, LoadMoreHolder.class);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BoardItemHolder) {
            onBindBoardViewHolder((BoardItemHolder) holder, position);
        } else if (holder instanceof LoadMoreHolder) {
            onBindLoadMoreViewHolder((LoadMoreHolder) holder, position);
        }
    }

    private void onBindBoardViewHolder(BoardItemHolder boardHolder, int position) {
        // 공지사항 체크
        boardHolder.notice.setVisibility(View.GONE);
        boardHolder.thumbnail.setVisibility(View.GONE);

        if (mBoardItemVOList.get(position).category.equalsIgnoreCase("공지")) {
            boardHolder.notice.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.getPx(getContext(), 17));
            boardHolder.title.setLayoutParams(params);
        } else if (!TextUtils.isEmpty(mBoardItemVOList.get(position).thumbNailImgUrl)) {
            boardHolder.thumbnail.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.getPx(getContext(), 51));
            boardHolder.title.setLayoutParams(params);
            Uri uri = Uri.parse(mBoardItemVOList.get(position).thumbNailImgUrl);
            Context context = boardHolder.thumbnail.getContext();
            Glide.with(context).load(uri).into(boardHolder.thumbnail);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.getPx(getContext(), 17));
            boardHolder.title.setLayoutParams(params);
        }

        String viewCount = mBoardItemVOList.get(position).viewCount;
        boardHolder.viewCount.setText(viewCount);

        if (!viewCount.equalsIgnoreCase("") && Integer.parseInt(viewCount) > Constant.VIEW_ACCENT_COUNT) {
            boardHolder.indicator.setBackgroundColor(Util.getColor(getContext(), R.attr.colorAccent));
        } else {
            boardHolder.indicator.setBackgroundColor(Util.getColor(getContext(), R.attr.colorPrimary));
        }

        String commentCount = mBoardItemVOList.get(position).commentCount;
        Drawable commentCountShape = ContextCompat.getDrawable(getContext(), R.drawable.comment_count_shape);

        if (!commentCount.equalsIgnoreCase("") && Integer.parseInt(commentCount) > Constant.COMMENT_ACCENT_COUNT) {
            commentCountShape.setColorFilter(Util.getColor(getContext(), R.attr.colorAccent), PorterDuff.Mode.SRC);
        } else {
            commentCountShape.setColorFilter(Util.getColor(getContext(), R.attr.colorPrimary), PorterDuff.Mode.SRC);
        }

        boardHolder.commentCount.setText(commentCount);
        boardHolder.commentCount.setBackground(commentCountShape);
        boardHolder.title.setText(mBoardItemVOList.get(position).title);
        boardHolder.nickname.setText(mBoardItemVOList.get(position).nickName);
        boardHolder.date.setText(mBoardItemVOList.get(position).date);
        boardHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                BoardPagerFragment boardPagerFragment = ((MainActivity) getContext()).getBoardPagerFragment();
                boardPagerFragment.startDetailFragment(mBoardItemVOList.get(position).title, mBoardItemVOList.get(position).detailPageUrl);
            }
        });

        // 딜 종료 여부 체크
        if (mBoardItemVOList.get(position).isDealFinish) {
            boardHolder.title.setPaintFlags(boardHolder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            boardHolder.title.setAlpha(0.3f);
            boardHolder.thumbnail.setAlpha(0.3f);
        } else {
            boardHolder.title.setPaintFlags(boardHolder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            boardHolder.title.setAlpha(1f);
            boardHolder.thumbnail.setAlpha(1f);
        }
    }

    private void onBindLoadMoreViewHolder(LoadMoreHolder loadMoreHolder, int position) {
        loadMoreHolder.reoladText.setVisibility(View.GONE);
        loadMoreHolder.loadMoreIng.setVisibility(View.GONE);
        loadMoreHolder.endText.setVisibility(View.GONE);

        if (!isNetworkConnected()) {
            loadMoreHolder.setClickListener((view, position1, isLongClick) -> {
                setIsNetworkConnected(true);
                notifyItemChanged(getItemCount() - 1);
            });

            loadMoreHolder.reoladText.setVisibility(View.VISIBLE);
        } else if (hasMoreItem()) {
            loadMoreHolder.loadMoreIng.setVisibility(View.VISIBLE);
            loadMore();
        } else {
            loadMoreHolder.endText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadContent() {
        setIsLoading(true);

        DealbadaClient.getInstance().getDealList(mUrl, mPage, new ResponseCallback() {
                    @Override
                    public void opSuccess(Source source) {
                        ArrayList<BoardItemVO> boardItemList = getData(source);

                        if (mPage == 1) {
                            mBoardItemVOList.clear();
                            mBoardItemVOList.addAll(boardItemList);
                        } else {
                            if (boardItemList.size() == 0) {
                                setHasMoreItem(false);
                            } else {
                                mBoardItemVOList.addAll(boardItemList);
                            }
                        }
                        notifyDataSetChanged();
                        hideSwipeRefreshIcon();
                        setIsLoading(false);
                    }

                    @Override
                    public void onFail() {
                        Log.d("moon", "onFail");
                        setIsNetworkConnected(false);
                        notifyItemChanged(getItemCount() - 1);
                        hideSwipeRefreshIcon();
                        setIsLoading(false);
                    }
                }
        );
    }

    private ArrayList<BoardItemVO> getData(Source source) {
        return DealBadaBoardParser.getInstance().getBoardItemList(source);
    }

    @Override
    public void refreshContent() {
        mPage = 1;
        showSwipeRefreshIcon();
        loadContent();
    }

    @Override
    public void loadMore() {
        if (!isLoading()) {
            setIsLoading(true);
            if (isNetworkConnected()) {
                mPage++;
            }
            loadContent();
        }
    }

    @Override
    public int getItemCount() {
        if (mBoardItemVOList.size() == 0) {
            return 0;
        } else {
            return mBoardItemVOList.size() + 1;
        }
    }
}