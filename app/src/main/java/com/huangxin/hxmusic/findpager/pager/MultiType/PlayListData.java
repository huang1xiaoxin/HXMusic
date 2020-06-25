package com.huangxin.hxmusic.findpager.pager.MultiType;

import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;

import java.util.List;

public class PlayListData {
    public List<PlayListBean> listBeanList;
    public String s;

    public PlayListData(List<PlayListBean> listDataList) {
        this.listBeanList = listDataList;
    }

}
