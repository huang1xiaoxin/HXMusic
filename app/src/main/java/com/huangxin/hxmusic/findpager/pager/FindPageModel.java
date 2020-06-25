package com.huangxin.hxmusic.findpager.pager;

import android.util.Log;

import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;
import com.huangxin.hxmusic.retrofit.IRetrofitRequest;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindPageModel {
    private static final String baseUrl = "https://api.imjad.cn/cloudmusic/";
    private static List<String> mPlayListId;

    //获取歌曲的数据
    public static Observable<PlayListBean> getPlayListContext() {
        initPlayListId();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return Observable.fromIterable(mPlayListId).concatMap(new Function<String, ObservableSource<PlayListBean>>() {
            @Override
            public ObservableSource<PlayListBean> apply(String s) throws Exception {
                Log.e("@@@", "apply: " + s);
                return retrofit.create(IRetrofitRequest.class)
                        .getPlayListRetrofit(s);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private static void initPlayListId() {
        mPlayListId = new ArrayList<>();
        if (mPlayListId != null) {
            mPlayListId.add("3172849329");
            mPlayListId.add("4908214162");
            mPlayListId.add("4867807976");
        }
    }

    public void getPlayList() {

    }

}
