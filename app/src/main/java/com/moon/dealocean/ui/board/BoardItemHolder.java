package com.moon.dealocean.ui.board;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseViewHolder;

/**
 * Created by zambo on 16. 7. 4..
 */
public class BoardItemHolder extends BaseViewHolder {
    public CardView cardView;
    public View indicator;
    public RoundedImageView thumbnail;
    public TextView title;
    public TextView notice;
    public TextView nickname;
    public TextView date;
    public TextView commentCount;
    public TextView viewCount;

    public BoardItemHolder(View view) {
        super(view);
        cardView = (CardView) view.findViewById(R.id.boardItemCardView);
        indicator = (View) view.findViewById(R.id.boardItemIndicator);
        thumbnail = (RoundedImageView) view.findViewById(R.id.boardItemThumbnail);
        title = (TextView) view.findViewById(R.id.boardItemTitle);
        notice = (TextView) view.findViewById(R.id.boardItemNotice);
        nickname = (TextView) view.findViewById(R.id.boardItemNickName);
        date = (TextView) view.findViewById(R.id.boardItemDate);
        commentCount = (TextView) view.findViewById(R.id.boardItemCommentCount);
        viewCount = (TextView) view.findViewById(R.id.boardItemViewCount);
    }
}
