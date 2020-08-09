package com.huangxin.hxmusic.findpager.pager;

import android.annotation.SuppressLint;
import android.util.Log;

import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;
import com.huangxin.hxmusic.retrofit.IRetrofitRequest;
import com.huangxin.hxmusic.retrofit.RetrofitRequest;
import com.huangxin.hxmusic.utils.Song;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FindPageModel {
    private static List<String> mTopItemPlayListId;
    private static List<String> mNormalItemPlayListId;
    private static final String TAG = "FindPageModel";

    //获取歌曲的数据
    public static Observable<PlayListBean> getPlayListContext() {
        initPlayListId();

        return Observable.fromIterable(mTopItemPlayListId).concatMap(new Function<String, ObservableSource<PlayListBean>>() {
            @Override
            public ObservableSource<PlayListBean> apply(String s) throws Exception {
                Log.e("@@@", "apply: " + s);
                return RetrofitRequest.getBaseRetrofit().create(IRetrofitRequest.class)
                        .getPlayListRetrofit(s);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private static void initPlayListId() {
        mTopItemPlayListId = new ArrayList<>();
        mTopItemPlayListId.add("4908214162");
        mTopItemPlayListId.add("3172849329");
        mTopItemPlayListId.add("4876758556");
    }

    private static void initNormalPlayListId() {
        mNormalItemPlayListId = new ArrayList<>();
        mNormalItemPlayListId.add("4983945186");
        mNormalItemPlayListId.add("4970544899");
        mNormalItemPlayListId.add("5078834278");
        mNormalItemPlayListId.add("5041838938");
//        mNormalItemPlayListId.add("4906766923");
//        mNormalItemPlayListId.add("2960248142");
        mNormalItemPlayListId.add("4879799554");
        mNormalItemPlayListId.add("2951338871");

    }

    public static Observable<PlayListBean> getSingListContext() {
        initNormalPlayListId();
        return Observable.fromIterable(mNormalItemPlayListId).concatMap(new Function<String, ObservableSource<PlayListBean>>() {
            @Override
            public ObservableSource<PlayListBean> apply(String s) throws Exception {
                Log.e(TAG, "apply: " + s);
                return RetrofitRequest.getBaseRetrofit().create(IRetrofitRequest.class)
                        .getSingleListRetrofit(s);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //更据歌曲的Id获取歌曲
    private static Observable<Song> getSongContext(String id) {
        return RetrofitRequest.getBaseRetrofit().
                create(IRetrofitRequest.class).getSongRetrofit(id);
    }

    //更据歌单来获取，实质是通过id
    @SuppressLint("CheckResult")
    public static Observable<Song> getSongContext(List<PlayListBean.PlaylistBean.TracksBean> tracksBeans) {
        return Observable.fromIterable(tracksBeans).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .concatMap(new Function<PlayListBean.PlaylistBean.TracksBean, ObservableSource<Song>>() {
                    @Override
                    public ObservableSource<Song> apply(PlayListBean.PlaylistBean.TracksBean tracksBean) throws Exception {
                        return getSongContext(tracksBean.getId() + "");
                    }
                });
    }
}
