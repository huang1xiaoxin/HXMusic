package com.huangxin.hxmusic.findpager.pager;

import android.content.Context;
import android.view.View;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.base.BasePager;

/**
 * 我的音乐的页面;
 */
public class FindPager extends BasePager {
    public FindPager(Context mContext) {
        super(mContext);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.find_fragment, null);
        return view;
    }

    //初始化数据
    @Override
    public void initDate() {
        super.initDate();

    }
}
