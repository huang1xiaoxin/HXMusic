package com.huangxin.hxmusic.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.huangxin.hxmusic.R;

public class GlideUtill {
    private final static int load = R.drawable.load;

    public static void LoadingRoundRect(Context context, String url, ImageView view, int mRoundRadius) {
        Glide.with(context).load(url).placeholder(load).transform(new RoundedCorners(20)).into(
                view
        );
    }

    public static void LoadingCircle(Context context, String url, ImageView view) {
        Glide.with(context).load(url).placeholder(load).transform(new CircleCrop()).into(
                view
        );
    }
}
