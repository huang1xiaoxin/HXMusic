package com.huangxin.hxmusic.findpager.pager;

import android.util.Log;

import com.huangxin.hxmusic.base.BasePagerFragment;
import com.huangxin.hxmusic.base.Items;
import com.huangxin.hxmusic.findpager.pager.MultiType.PlayListData;
import com.huangxin.hxmusic.findpager.pager.MultiType.SingSongItemData;
import com.huangxin.hxmusic.findpager.pager.MultiType.SinglePlayListData;
import com.huangxin.hxmusic.findpager.pager.MultiType.TitleItemData;
import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;
import com.huangxin.hxmusic.utils.Song;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
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
                mBasePagerFragmentView.setmLoadingDataSuccess(false);
            }
        }, () -> {
            Log.e(TAG, "加载完成" + mPlayList.size());
            if (items.size() != 0) {
                items.add(0, new PlayListData(mPlayList));
                items.add(0, new TitleItemData("举荐"));
            } else {
                items.add(new TitleItemData("举荐"));
                items.add(new PlayListData(mPlayList));
            }

        });
    }

    public void getPageAllItems() {
        if (items.size() > 0) {
            items.clear();
        }
        List<PlayListBean> tempList = new ArrayList<>();
        getPlayList();
        FindPageModel.getSingListContext().subscribe(playListBean -> {
            Log.e(TAG, "getPageAllItems: 加载数据成功" + playListBean.getPlaylist().getName());
            tempList.add(playListBean);
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: 加载数据失败");
                mBasePagerFragmentView.setmLoadingDataSuccess(false);
            }
        }, () -> {
            //加载数据完成
            //进行转化
            transformItems(tempList);
            mBasePagerFragmentView.hideLoading();
            mBasePagerFragmentView.loadData(items);
        });
    }

    public Items transformItems(List<PlayListBean> list) {
        for (int i = 0; i < list.size(); i++) {
            items.add(new TitleItemData(list.get(i).getPlaylist().getName()));
            if (i % 2 == 0 || i == 1) {
                items.add(new SinglePlayListData(list.get(i)));
            } else {
                PlayListBean tempPlayListBean = list.get(i);
                List<PlayListBean.PlaylistBean.TracksBean> tracksBeans = tempPlayListBean.getPlaylist().getTracks();
                int index = 0;
                if (tracksBeans.size() > 5) {
                    if (i == list.size() - 1) {
                        index = tracksBeans.size();
                    } else {
                        index = 5;
                    }
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

    //获取单首歌曲
    public void getSong(List<PlayListBean.PlaylistBean.TracksBean> tracksBeans) {
        List<Song> songs = new ArrayList<>();
        FindPageModel.getSongContext(tracksBeans).subscribe((song) -> {
            songs.add(song);
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //接受数据失败
                Log.e(TAG, "accept: 歌曲加载失败");

            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //接受数据成功
                Log.e(TAG, "run: 歌曲加载成功");

            }
        });
    }

    public List<PlayListBean> getmPlayList() {
        return mPlayList;
    }

    public void setmPlayList(List<PlayListBean> mPlayList) {
        this.mPlayList = mPlayList;
    }
}
