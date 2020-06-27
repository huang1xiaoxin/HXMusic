package com.huangxin.hxmusic.findpager.pager.MultiType;

import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;

public class SinglePlayListData {
    private PlayListBean playListBean;

    public SinglePlayListData(PlayListBean playListBean) {
        this.playListBean = playListBean;
    }

    public PlayListBean getPlayListBean() {
        return playListBean;
    }

    public void setPlayListBean(PlayListBean playListBean) {
        this.playListBean = playListBean;
    }
}
