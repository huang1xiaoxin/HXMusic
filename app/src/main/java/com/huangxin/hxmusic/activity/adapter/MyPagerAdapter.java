package com.huangxin.hxmusic.activity.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.huangxin.hxmusic.base.BasePager;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<BasePager> pagerList;
    public MyPagerAdapter(ArrayList<BasePager> pagerList){
        this.pagerList=pagerList;

    }
    @Override
    public int getCount() {
        return pagerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        BasePager pager=pagerList.get(position);
        //一定要初始化数据
        pager.initDate();
        Log.e("ViewPage","已加载");
        View view=pager.rootView;
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
