package com.huangxin.hxmusic.findpager.pager;

import android.util.Log;

import com.huangxin.hxmusic.base.BasePagerFragment;
import com.huangxin.hxmusic.base.Items;
import com.huangxin.hxmusic.findpager.pager.MultiType.PlayListData;
import com.huangxin.hxmusic.findpager.pager.MultiType.SingSongItemData;
import com.huangxin.hxmusic.findpager.pager.MultiType.SinglePlayListData;
import com.huangxin.hxmusic.findpager.pager.MultiType.TitleItemData;
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
                Log.e(TAG, "accept:" + mPlayList.size() + playListBean.getPlaylist().getName());
                mPlayList.add(playListBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: 加载数据失败");
            }
        }, () -> {
            Log.e(TAG, "加载完成" + mPlayList.size());
            if (items.size() != 0) {
                items.add(0, new PlayListData(mPlayList));
                items.add(0, new TitleItemData("举荐歌单"));
            } else {
                items.add(new TitleItemData("举荐歌单"));
                items.add(new PlayListData(mPlayList));
            }

        });
    }

    public void getPageAllItems() {
        List<PlayListBean> tempList = new ArrayList<>();
        getPlayList();
        FindPageModel.getSingListContext().subscribe(playListBean -> {
            Log.e(TAG, "getPageAllItems: 加载数据成功" + playListBean.getPlaylist().getName());
            tempList.add(playListBean);
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: 加载数据失败");
            }
        }, () -> {
            //加载数据完成
            //进行转化
            transformItems(tempList);
            mBasePagerFragmentView.loadData(items);
        });
    }

    public Items transformItems(List<PlayListBean> list) {
        for (int i = 0; i < list.size(); i++) {
            items.add(new TitleItemData(list.get(i).getPlaylist().getName()));
            if (i % 2 == 0) {
                items.add(new SinglePlayListData(list.get(i)));
            } else {
                PlayListBean tempPlayListBean = list.get(i);
                List<PlayListBean.PlaylistBean.TracksBean> tracksBeans = tempPlayListBean.getPlaylist().getTracks();
                int index = 0;
                if (tracksBeans.size() > 5) {
                    index = 5;
                } else {
                    index = tracksBeans.size();
                }
                for (int j = 0; j < index; j++) {
                    items.add(new SingSongItemData(tracksBeans.get(j), tempPlayListBean));
                }
            }
        }
        return items;

    }

    public List<PlayListBean> getmPlayList() {
        return mPlayList;
    }

    public void setmPlayList(List<PlayListBean> mPlayList) {
        this.mPlayList = mPlayList;
    }
}
