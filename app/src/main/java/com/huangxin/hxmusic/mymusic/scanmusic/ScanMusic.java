package com.huangxin.hxmusic.mymusic.scanmusic;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.huangxin.hxmusic.utils.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanMusic {

    public List<Song> ScanMusicList(ContentResolver contentResolver){
        List<Song> songList=new ArrayList<>();
        Cursor cursor=contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for (int i=0;i<cursor.getCount();i++){
            Song song=new Song();
            cursor.moveToNext();
            //音乐id
            long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            //歌名
            String title=cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            //歌手
            String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            //时间长度
            long duration=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            //歌曲地址
            String url=cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            //判断是否是音乐
            int isMusic=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            //歌曲的大小
            float size=cursor.getFloat(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            if ((isMusic!=0&&duration/(1000*60)>=1)){
                song.setId(id);
                song.setTitle(title);
                song.setArtist(artist);
                //计算出M为单位的大小
                float sizeM=Math.round((size/1024/1024*1000)/1000);
                song.setSize(sizeM+"M");
                song.setUrl(url);
                song.setDuration(duration);
                songList.add(song);

            }
        }
        return  songList;

    }
}
