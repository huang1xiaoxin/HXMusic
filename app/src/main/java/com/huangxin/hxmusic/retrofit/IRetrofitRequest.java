package com.huangxin.hxmusic.retrofit;

import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRetrofitRequest {
    @GET("?type=playlist")
    Observable<PlayListBean> getPlayListRetrofit(@Query("id") String id);

    @GET("?type=playlist")
    Observable<PlayListBean> getSingleListRetrofit(@Query("id") String id);
}
