package com.moon.dealocean.base;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.NonViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Administrator on 2015/8/17.
 */
public class URLImageGetter implements Html.ImageGetter {
    private TextView mTextView;
    private DisplayImageOptions mOptions;

    public URLImageGetter(TextView textView, DisplayImageOptions options) {
        mTextView = textView;
        mOptions = options;
    }

    @Override
    public Drawable getDrawable(String source) {
        final URLDrawable urlDrawable = new URLDrawable();
        ImageSize imageSize = new ImageSize(1280, 720);
        NonViewAware nonViewAware = new NonViewAware(imageSize, ViewScaleType.FIT_INSIDE);
        ImageLoader.getInstance().displayImage(source, nonViewAware, mOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                urlDrawable.bitmap = loadedImage;
                urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                mTextView.invalidate();
                mTextView.setText(mTextView.getText()); // 解决图文重叠
            }
        });
        /*ImageLoader.getInstance().loadImage(source,options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }
        });*/
        return urlDrawable;
    }
}