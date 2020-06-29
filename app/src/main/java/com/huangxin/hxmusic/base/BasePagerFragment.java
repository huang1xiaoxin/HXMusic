package com.huangxin.hxmusic.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huangxin.hxmusic.R;

public abstract class BasePagerFragment extends Fragment {
    public Context context;
    //获取一个视图
    public View rootView;
    //是否已经初始化数据
    public boolean isFirstLoadData;
    public boolean isReuseView;
    private LinearLayout mLoadingLinearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        initValue();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container, savedInstanceState);
        initLoadingView(container);
        return view;
    }

    /**
     * onViewCreated在onCreateView执行完后立即执行。
     * onCreateView返回的就是fragment要显示的view。
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = view;
            Log.e("页面", "onViewCreated: 设置View");
        }
        super.onViewCreated(isReuseView ? rootView : view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoadData) {
            fragmentFirstLoadingData();
            isFirstLoadData = false;
        }
        //提供给当需要每次显示都要更新数据的功能
        fragmentEveryLoadingData();
    }

    /**
     * 支持每次显示都刷新数据
     */

    private void fragmentEveryLoadingData() {
    }

    /**
     * 第一次Fragment显示时加载数据
     */
    protected abstract void fragmentFirstLoadingData();

    //强迫子类去实现，初始化界面的方法
    public abstract View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public void initValue() {
        isFirstLoadData = true;
        isReuseView = true;
        rootView = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirstLoadData = true;
        isReuseView = true;
    }

    public boolean isReuseView() {
        return isReuseView;
    }

    /**
     * 设置是否复用View，默认为复用,true
     * view 的复用是指，ViewPager 在销毁和重建 Fragment 时会不断调用 onCreateView() -> onDestroyView
     * 之间的生命函数，这样可能会出现重复创建 view 的情况，导致界面上显示多个相同的 Fragment
     * view 的复用其实就是指保存第一次创建的 view，后面再 onCreateView() 时直接返回第一次创建的 view
     * 如果选择false则需要每次显示都要加载数据，则要重写fragmentEveryLoadingData()
     * <p>
     * 当ViewPage切换时会调用destroyItem()的方法，其父类然后会调用onDestroyView(),因此也可以在onDestroyView中做处理
     *
     * @param reuseView
     */

    public void setReuseView(boolean reuseView) {
        isReuseView = reuseView;
    }

    //注册Items
    public void registerDataItems() {
    }

    public void loadData(Items items) {

    }

    private void initLoadingView(View container) {
        //布局的属性
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mLoadingLinearLayout = new LinearLayout(getActivity());
        mLoadingLinearLayout.setOrientation(LinearLayout.VERTICAL);
        //默认是隐藏的
        mLoadingLinearLayout.setVisibility(View.GONE);

        //加载按钮的属性
        FrameLayout.LayoutParams proLayoutParams = new FrameLayout.LayoutParams(
                dip2dx(60), dip2dx(60)
        );
        ProgressBar progressBar = new ProgressBar(getActivity());
        Drawable drawable = getActivity().getDrawable(R.drawable.progressbar);
        progressBar.setIndeterminateDrawable(drawable);

        //加载TextView的属性
        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("加载中...");
        textView.setLayoutParams(textParams);
        container.getContext().getApplicationContext();
        //添加到View
        getActivity().addContentView(mLoadingLinearLayout, layoutParams);

        mLoadingLinearLayout.addView(progressBar, proLayoutParams);
        mLoadingLinearLayout.addView(textView, textParams);

    }

    /**
     * 隐藏正在加载
     */
    public void hideLoading() {
        if (mLoadingLinearLayout != null && mLoadingLinearLayout.getVisibility() == View.VISIBLE) {
            mLoadingLinearLayout.setVisibility(View.GONE);
        }

    }

    /**
     * 显示正在加载
     */
    public void showLoading() {
        if (mLoadingLinearLayout != null && mLoadingLinearLayout.getVisibility() == View.GONE) {
            mLoadingLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private int dip2dx(int dp) {
        float scan = context.getResources().getDisplayMetrics().density;
        return (int) (scan * dp + 0.5f);
    }

    @Override
    public void onPause() {
        super.onPause();
        //隐藏加载框
        hideLoading();
    }
}
