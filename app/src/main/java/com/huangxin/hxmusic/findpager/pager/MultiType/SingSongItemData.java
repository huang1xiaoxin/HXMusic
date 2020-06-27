package com.huangxin.hxmusic.findpager.pager.MultiType;

import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;

public class SingSongItemData {
    public PlayListBean.PlaylistBean.TracksBean mTracksBean;
    public PlayListBean playListBean;

    public SingSongItemData(PlayListBean.PlaylistBean.TracksBean mTracksBean, PlayListBean playListBean) {
        this.mTracksBean = mTracksBean;
        this.playListBean = playListBean;
    }

    public PlayListBean.PlaylistBean.TracksBean getmTracksBean() {
        return mTracksBean;
    }

    public void setmTracksBean(PlayListBean.PlaylistBean.TracksBean mTracksBean) {
        this.mTracksBean = mTracksBean;
    }

    public PlayListBean getPlayListBean() {
        return playListBean;
    }

    public void setPlayListBean(PlayListBean playListBean) {
        this.playListBean = playListBean;
    }
}
