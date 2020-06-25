package com.huangxin.hxmusic.mvpager.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.base.BasePagerFragment;

/**
 * 我的音乐的页面;
 */
public class MVPagerFragment extends BasePagerFragment {
    private final static String TAG = "MVPagerFragment";

    @Override
    protected void fragmentFirstLoadingData() {

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mv_fragment, container, false);

    }

}
