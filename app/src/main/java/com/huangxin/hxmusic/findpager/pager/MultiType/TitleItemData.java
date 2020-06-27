package com.huangxin.hxmusic.findpager.pager.MultiType;

public class TitleItemData {
    private String mTitle;
    private String moreText;

    public TitleItemData(String mTitle, String moreText) {
        this.mTitle = mTitle;
        this.moreText = moreText;
    }

    public TitleItemData(String mTitle) {
        this(mTitle, "更多");
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMoreText() {
        return moreText;
    }

    public void setMoreText(String moreText) {
        this.moreText = moreText;
    }
}
