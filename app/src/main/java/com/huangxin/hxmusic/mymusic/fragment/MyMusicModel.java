package com.huangxin.hxmusic.mymusic.fragment;

import com.huangxin.hxmusic.utils.ConstInterface;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class MyMusicModel {
    public static Observable getSongListFromData() {
        return Observable.just(ConstInterface.LIKE_MUSIC_TABLE, ConstInterface.HISTORY_MUSIC_TABLE).flatMap(new Function<Integer, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(integer);
                    }
                });
            }
        });
    }
}

