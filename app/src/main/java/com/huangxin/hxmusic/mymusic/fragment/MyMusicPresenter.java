package com.huangxin.hxmusic.mymusic.fragment;

import com.huangxin.hxmusic.Database.DataDo;
import com.huangxin.hxmusic.base.BasePagerFragment;
import com.huangxin.hxmusic.utils.ConstInterface;
import com.huangxin.hxmusic.utils.Song;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MyMusicPresenter {
    private BasePagerFragment mBasePagerFragment;

    public MyMusicPresenter(BasePagerFragment basePagerFragment) {
        mBasePagerFragment = basePagerFragment;
    }

    public void getSongListFromData() {
        MyMusicModel.getSongListFromData().subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                List<Song> likeSongList = DataDo.getInstance(mBasePagerFragment.getContext()).queryThreeSong(ConstInterface.LIKE_MUSIC_TABLE);
                List<Song> historySongList = DataDo.getInstance(mBasePagerFragment.getContext()).queryThreeSong(ConstInterface.HISTORY_MUSIC_TABLE);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
