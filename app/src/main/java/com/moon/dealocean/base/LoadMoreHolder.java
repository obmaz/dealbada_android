package com.moon.dealocean.base;

import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moon.dealocean.R;

/**
 * Created by zambo on 16. 7. 4..
 */
public class LoadMoreHolder extends BaseViewHolder {
    public CardView cardView;
    public ProgressBar loadMoreIng;
    public TextView endText;
    public TextView reoladText;

    public LoadMoreHolder(View view) {
        super(view);
        cardView = (CardView) view.findViewById(R.id.loadMoreCardView);
        loadMoreIng = (ProgressBar) view.findViewById(R.id.loadMoreIng);
        endText = (TextView) view.findViewById(R.id.loadMoreEndText);
        reoladText = (TextView) view.findViewById(R.id.loadMoreReloadText);
    }
}
