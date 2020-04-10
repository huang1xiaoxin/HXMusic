package com.huangxin.hxmusic.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BasePager  {
    public Context context;
    public View rootView;
    public Activity activity;
    public BasePager(Context mContext){
        context=mContext;
        rootView=initView();
    }
    //强迫子类去实现，初始化界面的方法
    public abstract View initView();

    //初始化数据
    public void initDate(){}

}
