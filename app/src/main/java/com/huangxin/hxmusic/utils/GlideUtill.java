package com.huangxin.hxmusic.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.huangxin.hxmusic.R;

public class GlideUtill {
    private final static int load = R.drawable.load;
    private final static int error = R.drawable.loding_error;

    public static void LoadingRoundRect(Context context, String url, ImageView view, int mRoundRadius) {
        Glide.with(context).load(url).placeholder(load).error(error).transform(new RoundedCorners(20)).into(
                view
        );
    }

    public static void LoadingCircle(Context context, String url, ImageView view) {
        Glide.with(context).load(url).placeholder(load).error(error).transform(new CircleCrop()).into(
                view
        );
    }

    //设置自定义的View
    public static void LoadingCircleImageView(Context context, String url, ImageView view) {
        Glide.with(context).load(url).placeholder(load).error(error).into(
                new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setImageDrawable(resource);
                    }
                }
        );
    }


}
