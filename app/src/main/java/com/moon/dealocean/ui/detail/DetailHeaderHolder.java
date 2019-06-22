package com.moon.dealocean.ui.detail;

import androidx.cardview.widget.CardView;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseViewHolder;

/**
 * Created by zambo on 16. 7. 4..
 */
public class DetailHeaderHolder extends BaseViewHolder {
    public CardView cardView;
    public View indicator;
    public RoundedImageView thumbnail;
    public TextView title;
    public TextView nickName;
    public TextView date;
    public TextView viewCount;
    public TextView commentCount;
    public LinearLayout linkLayout;
    public WebView content;
    public TextView dividerCommentCount;

    public DetailHeaderHolder(View view) {
        super(view);
        cardView = (CardView) view.findViewById(R.id.detailHeaderCardView);
        indicator = (View) view.findViewById(R.id.detailHeaderIndicator);
        thumbnail = (RoundedImageView) view.findViewById(R.id.detailHeaderThumbnail);
        title = (TextView) view.findViewById(R.id.detailHeaderTitle);
        nickName = (TextView) view.findViewById(R.id.detailHeaderNickName);
        date = (TextView) view.findViewById(R.id.detailHeaderDate);
        viewCount = (TextView) view.findViewById(R.id.detailHeaderViewCount);
        commentCount = (TextView) view.findViewById(R.id.detailHeaderCommentCount);
        linkLayout = (LinearLayout) view.findViewById(R.id.detailHeaderLink);
        content = (WebView) view.findViewById(R.id.detailHeaderContent);
        dividerCommentCount = (TextView) view.findViewById(R.id.detailHeaderDividerCommentCount);
    }
}

