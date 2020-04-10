package com.huangxin.hxmusic.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.huangxin.hxmusic.imageloader.ImageResizer;

public class Song {

    private  long id; //歌曲的id
    private String title;//歌名
    private String artist;//歌手
    private long duration;//歌曲长度
    private String url;//歌曲的地址
    private String size;//歌曲的大小
    public Song(){

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

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }
}
