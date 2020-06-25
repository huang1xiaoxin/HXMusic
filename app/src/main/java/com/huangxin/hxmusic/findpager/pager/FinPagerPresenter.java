package com.huangxin.hxmusic.findpager.pager;

import android.util.Log;

import com.huangxin.hxmusic.base.BasePagerFragment;
import com.huangxin.hxmusic.base.Items;
import com.huangxin.hxmusic.findpager.pager.MultiType.PlayListData;
import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class FinPagerPresenter {
    private static final String TAG = "FinPagerPresenter";
    private BasePagerFragment mBasePagerFragmentView;
    private List<PlayListBean> mPlayList;
    private Items items = new Items();

    public FinPagerPresenter(BasePagerFragment basePagerFragmentView) {
        mBasePagerFragmentView = basePagerFragmentView;
        mPlayList = new ArrayList<>();
    }

    public void getPlayList() {
        FindPageModel.getPlayListContext().subscribe((playListBean) -> {
            if (!mPlayList.contains(playListBean)) {
                Log.e("@@", "accept:" + mPlayList.size() + playListBean.getPlaylist().getName());
                mPlayList.add(playListBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: 加载数据失败");
                Log.e("@@", "accept: 加载数据失败");
            }
        }, () -> {
            Log.e("@@", "加载完成" + mPlayList.size());
            items.add(new PlayListData(mPlayList));
            mBasePagerFragmentView.loadData(items);
        });
    }

    public List<PlayListBean> getmPlayList() {
        return mPlayList;
    }

    public void setmPlayList(List<PlayListBean> mPlayList) {
        this.mPlayList = mPlayList;
    }
}
