package com.huangxin.hxmusic.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.huangxin.hxmusic.imageloader.ImageResizer;

import java.util.List;

public class Song {

    private  long id; //歌曲的id
    private String title;//歌名
    private String artist;//歌手
    private long duration;//歌曲长度
    private String url;//歌曲的地址
    private String size;//歌曲的大小
    private int code;
    private List<DataBean> data;

    public Song(){
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    //网络歌曲的属性
    public static class DataBean {
        /**
         * id : 28012031
         * url : https://m7.music.126.net/20200630155937/ee27a12189e1a4d9be588085f4c0dbdf/ymusic/8972/6e6e/7b86/bddf788bf92e62d7c5c9aa457dd27bf5.mp3
         * br : 128000
         * size : 3950276
         * md5 : bddf788bf92e62d7c5c9aa457dd27bf5
         * code : 200
         * expi : 1200
         * type : mp3
         * gain : 0
         * fee : 0
         * uf : null
         * payed : 0
         * flag : 256
         * canExtend : false
         * freeTrialInfo : null
         * level : standard
         * encodeType : mp3
         */

        private int id;
        private String url;
        private int br;
        private int size;
        private String md5;
        private int code;
        private int expi;
        private String type;
        private int gain;
        private int fee;
        private Object uf;
        private int payed;
        private int flag;
        private boolean canExtend;
        private Object freeTrialInfo;
        private String level;
        private String encodeType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getBr() {
            return br;
        }

        public void setBr(int br) {
            this.br = br;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getExpi() {
            return expi;
        }

        public void setExpi(int expi) {
            this.expi = expi;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getGain() {
            return gain;
        }

        public void setGain(int gain) {
            this.gain = gain;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public Object getUf() {
            return uf;
        }

        public void setUf(Object uf) {
            this.uf = uf;
        }

        public int getPayed() {
            return payed;
        }

        public void setPayed(int payed) {
            this.payed = payed;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public boolean isCanExtend() {
            return canExtend;
        }

        public void setCanExtend(boolean canExtend) {
            this.canExtend = canExtend;
        }

        public Object getFreeTrialInfo() {
            return freeTrialInfo;
        }

        public void setFreeTrialInfo(Object freeTrialInfo) {
            this.freeTrialInfo = freeTrialInfo;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getEncodeType() {
            return encodeType;
        }

        public void setEncodeType(String encodeType) {
            this.encodeType = encodeType;
        }
    }

    public Song(long id, String title, String artist, long duration, String url,String size) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.url = url;
        this.size=size;
    }
    public Bitmap loadingBitmap(String url, int reWidth, int reqHeight) {
        Bitmap bitmap=null;
        try {
            MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(url);
            byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
            bitmap=new ImageResizer().decodeSampledBitmapArrayByte(picture,40,70);

        }catch (Exception ex){

        }
        return bitmap;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

