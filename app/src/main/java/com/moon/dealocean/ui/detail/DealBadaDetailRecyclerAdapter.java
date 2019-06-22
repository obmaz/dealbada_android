package com.moon.dealocean.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moon.dealocean.R;
import com.moon.dealocean.base.BaseRecyclerAdapter;
import com.moon.dealocean.base.LoadMoreHolder;
import com.moon.dealocean.base.URLImageGetter;
import com.moon.dealocean.network.ResponseCallback;
import com.moon.dealocean.network.dealbada.DealbadaClient;
import com.moon.dealocean.network.vo.DetailItemVO;
import com.moon.dealocean.network.vo.LinkInfoVO;
import com.moon.dealocean.parser.dealbada.DealBadaDetailParser;
import com.moon.dealocean.ui.Constant;
import com.moon.dealocean.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import net.htmlparser.jericho.Source;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by zambo on 2016-07-04.
 */
public class DealBadaDetailRecyclerAdapter extends BaseRecyclerAdapter implements UnLockWebViewScroll {
    private ArrayList<DetailItemVO> mDetailItemList = new ArrayList<>();
    private int mCommentPageNum = 1;
    private boolean mIsFirstLoad = true;
    private transient WebView mContentWebView;
    private transient Context mContext;
    private String mOwnerNickName;
    private String mUrl;

    public DealBadaDetailRecyclerAdapter(String url) {
        mUrl = url;
        setHeaderViewHolder(R.layout.detail_header, DetailHeaderHolder.class);
        setItemViewHolder(R.layout.detail_comment, DetailCommentHolder.class);
        setLoadMoreViewHolder(R.layout.load_more, LoadMoreHolder.class);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DetailHeaderHolder) {
            onBindHeaderViewHolder((DetailHeaderHolder) holder);
        } else if (holder instanceof DetailCommentHolder) {
            onBindCommentViewHolder((DetailCommentHolder) holder, position);
        } else if (holder instanceof LoadMoreHolder) {
            onBindLoadMoreViewHolder((LoadMoreHolder) holder, position);
        }
    }

    private void onBindHeaderViewHolder(final DetailHeaderHolder detailHeaderHolder) {
        Uri uri = Uri.parse(getHeader().thumbNailImgUrl);
        mContext = detailHeaderHolder.thumbnail.getContext();
        Glide.with(mContext).load(uri).into(detailHeaderHolder.thumbnail);

        detailHeaderHolder.title.setText(getHeader().title);
        detailHeaderHolder.nickName.setText(getHeader().nickName);
        detailHeaderHolder.date.setText(getHeader().date);

        detailHeaderHolder.viewCount.setText("" + getHeader().viewCount);

        if (getHeader().viewCount > Constant.VIEW_ACCENT_COUNT) {
            detailHeaderHolder.indicator.setBackgroundColor(Util.getColor(getContext(), R.attr.colorAccent));
        } else {
            detailHeaderHolder.indicator.setBackgroundColor(Util.getColor(getContext(), R.attr.colorPrimary));
        }

        Drawable commentCountShape = ContextCompat.getDrawable(getContext(), R.drawable.comment_count_shape);

        if (getHeader().commentCount > Constant.COMMENT_ACCENT_COUNT) {
            commentCountShape.setColorFilter(Util.getColor(getContext(), R.attr.colorAccent), PorterDuff.Mode.SRC);
        } else {
            commentCountShape.setColorFilter(Util.getColor(getContext(), R.attr.colorPrimary), PorterDuff.Mode.SRC);
        }

        detailHeaderHolder.commentCount.setText("" + getHeader().commentCount);
        detailHeaderHolder.commentCount.setBackground(commentCountShape);
        detailHeaderHolder.dividerCommentCount.setText("" + getHeader().commentCount);
        detailHeaderHolder.dividerCommentCount.setBackground(commentCountShape);

        detailHeaderHolder.linkLayout.removeAllViews();
        for (final LinkInfoVO linkInfo : getHeader().linkInfoList) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            LinearLayout headerLink = (LinearLayout) inflater.inflate(R.layout.detail_header_link, null);

            TextView linkCount = (TextView) headerLink.findViewById(R.id.detailHeaderLinkCount);
            TextView linkUrlText = (TextView) headerLink.findViewById(R.id.detailHeaderLinkUrlText);
            linkCount.setText(linkInfo.linkCount);
            linkUrlText.setText(linkInfo.linkUrlText);
            linkUrlText.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkInfo.linkUrl));
                getContext().startActivity(intent);
            });

            detailHeaderHolder.linkLayout.addView(headerLink);
        }

        String imageToFitStyle = "<style>img{display: inline height: auto; max-width: 100%;}</style>";
        mContentWebView = detailHeaderHolder.content;
        setContentWebviewSettings();

        if (!getHeader().content.equalsIgnoreCase("")) {
            mContentWebView.loadDataWithBaseURL(DealbadaClient.BASE_URL, imageToFitStyle + getHeader().content, "text/html; charset=UTF-8", "utf-8", "");
        } else {
            if (mIsFirstLoad) {
                mIsFirstLoad = false;
                mContentWebView.loadUrl(mUrl);
            }
        }
    }

    private void onBindCommentViewHolder(DetailCommentHolder detailCommentHolder, int position) {
        if (getCommentFromSource(position).nickName.equals(mOwnerNickName)) {
            detailCommentHolder.cardView.setCardBackgroundColor(Util.getColor(getContext(), R.color.backgroundDark));
        } else {
            detailCommentHolder.cardView.setCardBackgroundColor(Util.getColor(getContext(), R.color.background));
        }

        int paddingLeft = getCommentFromSource(position).titleMaginLeft;
        int paddingTop = detailCommentHolder.cardView.getContentPaddingTop();
        int paddingRight = detailCommentHolder.cardView.getContentPaddingRight();
        int paddingBottom = detailCommentHolder.cardView.getContentPaddingBottom();
        detailCommentHolder.cardView.setContentPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        detailCommentHolder.nickName.setText(getCommentFromSource(position).nickName);
        Uri uri = Uri.parse(getCommentFromSource(position).thumbNailImgUrl);
        Context context = detailCommentHolder.thumbnail.getContext();
        Glide.with(context).load(uri).into(detailCommentHolder.thumbnail);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        //Application에서 한번만 init 하면 됨
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(20 * 1024 * 1024); // 20 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
//        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(config.build());

        // From https://github.com/babylikebird/htmlImage
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(getContext());
        ImageLoader.getInstance().init(config);

        // 기본 제공하는 Linkify.WEB의 경우 .이 들어간 문장은 전부 링크로 만듦. 그래서 커스텀 하게 구현
        detailCommentHolder.title.setText(Html.fromHtml(getCommentFromSource(position).title, new URLImageGetter(detailCommentHolder.title, options), null));
        detailCommentHolder.title.setLinksClickable(true);
        Pattern linkPattern = Pattern.compile("\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Linkify.addLinks(detailCommentHolder.title, linkPattern, "");

        detailCommentHolder.date.setText(getCommentFromSource(position).date);
        detailCommentHolder.likeCount.setText("" + getCommentFromSource(position).likeCount);
    }

    private void onBindLoadMoreViewHolder(final LoadMoreHolder loadMoreHolder, int position) {
        loadMoreHolder.reoladText.setVisibility(View.GONE);
        loadMoreHolder.loadMoreIng.setVisibility(View.GONE);
        loadMoreHolder.endText.setVisibility(View.GONE);

        if (!isNetworkConnected()) {
            loadMoreHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    setIsNetworkConnected(true);
                    notifyItemChanged(getItemCount() - 1);
                }
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

        DealbadaClient.getInstance().getDetail(mUrl, mCommentPageNum, new ResponseCallback() {
                    @Override
                    public void opSuccess(Source source) {
                        if (mCommentPageNum == 1) {
                            mDetailItemList.clear();
                            mDetailItemList.addAll(getDataFromSource(source));
                        } else {
                            ArrayList<DetailItemVO> detailCommentList = getCommentFromSource(source);
                            if (detailCommentList == null || detailCommentList.size() == 0) {
                                setHasMoreItem(false);
                            } else {
                                if (mDetailItemList.get(0).commentCount > 50) {
                                    mDetailItemList.addAll(detailCommentList);
                                } else {
                                    setHasMoreItem(false);
                                }
                            }
                        }
                        notifyDataSetChanged();
                        hideSwipeRefreshIcon();
                        setIsLoading(false);
                    }

                    @Override
                    public void onFail() {
                        setIsNetworkConnected(false);
                        notifyItemChanged(getItemCount() - 1);
                        hideSwipeRefreshIcon();
                        setIsLoading(false);
                    }
                }
        );
    }

    private ArrayList<DetailItemVO> getDataFromSource(Source source) {
        final ArrayList<DetailItemVO> detailItemList = new ArrayList<>();
        DetailItemVO detailItem = DealBadaDetailParser.getInstance().getDetailMain(source);

        if (detailItem != null) {
            detailItemList.add(detailItem);
            mOwnerNickName = detailItemList.get(0).nickName;
            detailItemList.addAll(DealBadaDetailParser.getInstance().getDetailCommentList(source));
        } else {
            setHasMoreItem(false);
            // 더미 아이템을 추가함, 더미가 없으면 onBind 류가 호출이 안됨
            // 더미를 없애고 onBind 콜을 하지 않고 여기에서 팝업을 띠우는 방식으로 해도 됨
            detailItem = new DetailItemVO();
            detailItemList.add(detailItem);
        }
        return detailItemList;
    }

    private ArrayList<DetailItemVO> getCommentFromSource(Source source) {
        ArrayList<DetailItemVO> detailCommentList = DealBadaDetailParser.getInstance().getDetailCommentList(source);
        return detailCommentList;
    }

    @Override
    public void refreshContent() {
        Log.d("moon", "moon a refreshContent : " + mUrl);
        mCommentPageNum = 1;
        mIsFirstLoad = true;

        showSwipeRefreshIcon();
        loadContent();
    }

    @Override
    public void loadMore() {
        Log.d("moon", "moon a loadMore : " + mUrl);

        if (!isLoading()) {
            if (isNetworkConnected()) {
                mCommentPageNum++;
            }
            loadContent();
        }
    }

    private DetailItemVO getCommentFromSource(int position) {
        if (position == getItemCount()) {
            return mDetailItemList.get(position - 1);
        } else if (position > 0) {
            return mDetailItemList.get(position);
        } else {
            return null;
        }
    }

    private DetailItemVO getHeader() {
        return mDetailItemList.get(0);
    }

    @Override
    public int getItemCount() {
        if (mDetailItemList.size() == 0) {
            return 0;
        } else {
            return mDetailItemList.size() + 1;
        }
    }

    private void setContentWebviewSettings() {
        mContentWebView.setVerticalScrollBarEnabled(false);
        mContentWebView.setHorizontalScrollBarEnabled(false);
        mContentWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return (motionEvent.getAction()) == MotionEvent.ACTION_MOVE;
            }
        });

        mContentWebView.getSettings().setJavaScriptEnabled(true);
        mContentWebView.getSettings().setTextZoom(100);
        mContentWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("moon", "moon a message : " + message);
                mContentWebView.loadData(message, "text/html; charset=UTF-8", "utf-8");
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

    @Override
    public void unLockWebviewScroll() {
        mContentWebView.setOnTouchListener(null);
        Toast.makeText(mContext, R.string.unlock_content_scroll, Toast.LENGTH_SHORT).show();
    }
}
