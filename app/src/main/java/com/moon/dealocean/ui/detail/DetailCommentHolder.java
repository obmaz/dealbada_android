package com.moon.dealocean.ui.detail;

import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseViewHolder;

/**
 * Created by zambo on 16. 7. 4..
 */
public class DetailCommentHolder extends BaseViewHolder {
    public CardView cardView;
    public LinearLayout commentLayout;
    public RoundedImageView thumbnail;
    public TextView title;
    public TextView nickName;
    public TextView date;
    public TextView likeCount;

    public DetailCommentHolder(View view) {
        super(view);
        cardView = (CardView) view.findViewById(R.id.detailCommentCardView);
        commentLayout = (LinearLayout) view.findViewById(R.id.detailComment);
        thumbnail = (RoundedImageView) view.findViewById(R.id.detailCommentThumbnail);
        title = (TextView) view.findViewById(R.id.detailCommentTitle);
        nickName = (TextView) view.findViewById(R.id.detailCommentNickName);
        date = (TextView) view.findViewById(R.id.detailCommentDate);
        likeCount = (TextView) view.findViewById(R.id.detailCommentLikeCount);
    }
}
