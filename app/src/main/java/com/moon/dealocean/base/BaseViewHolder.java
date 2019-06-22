package com.moon.dealocean.base;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.moon.dealocean.ui.board.DealBadaBoardRecyclerAdapter;

/**
 * Created by zambo on 2016-07-17.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private BaseRecyclerAdapter.ItemClickListener mItemClickListener;

    public BaseViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
    }

    public void setClickListener(DealBadaBoardRecyclerAdapter.ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (mItemClickListener != null) {
            mItemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
