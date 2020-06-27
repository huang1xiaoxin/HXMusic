package com.huangxin.hxmusic.findpager.pager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.MultiTypeAdapter;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.base.BasePagerFragment;
import com.huangxin.hxmusic.base.Items;
import com.huangxin.hxmusic.findpager.pager.MultiType.PlayListData;
import com.huangxin.hxmusic.findpager.pager.MultiType.PlayListItemView;
import com.huangxin.hxmusic.findpager.pager.MultiType.SingPlayListItemView;
import com.huangxin.hxmusic.findpager.pager.MultiType.SingSongItemData;
import com.huangxin.hxmusic.findpager.pager.MultiType.SingSongItemView;
import com.huangxin.hxmusic.findpager.pager.MultiType.SinglePlayListData;
import com.huangxin.hxmusic.findpager.pager.MultiType.TitleItemData;
import com.huangxin.hxmusic.findpager.pager.MultiType.TitleItemView;

import io.reactivex.Observable;

/**
 * 我的音乐的页面;
 */
public class FindPagerFragment extends BasePagerFragment {

    private final static String TAG = "FindPagerFragment";
    private RecyclerView mRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private Items items;
    private FinPagerPresenter pagerPresenter;
    public Observable getDataObservable;


    public FindPagerFragment() {
        pagerPresenter = new FinPagerPresenter(this);

    }

    @Override
    protected void fragmentFirstLoadingData() {
        //加载数据
        pagerPresenter.getPageAllItems();

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "initView: 初始化界面");
        View view = inflater.inflate(R.layout.find_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.recycle_view);
        multiTypeAdapter = new MultiTypeAdapter();
        registerDataItems();
        //一定要设置这一句
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(multiTypeAdapter);
        return view;
    }

    public void loadData(Items items) {
        this.items = items;
        multiTypeAdapter.setItems(items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void registerDataItems() {
        super.registerDataItems();
        multiTypeAdapter.register(PlayListData.class, new PlayListItemView(context));
        multiTypeAdapter.register(SinglePlayListData.class, new SingPlayListItemView(context));
        multiTypeAdapter.register(TitleItemData.class, new TitleItemView(context));
        multiTypeAdapter.register(SingSongItemData.class, new SingSongItemView(context));
    }

}
