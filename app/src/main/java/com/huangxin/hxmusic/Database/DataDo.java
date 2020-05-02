package com.huangxin.hxmusic.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.huangxin.hxmusic.utils.ConstInterface;
import com.huangxin.hxmusic.utils.Song;

import java.util.ArrayList;
import java.util.List;

public class DataDo {
    public static DataDo instance;
    ;
    private final String name = "MusicData.db";
    private final String likeMusicTable = "likeMusicTable";
    private final String historyMusicTable = "historyMusicTable";
    private SQLiteDatabase sqLiteDatabase;
    private MusicDataBaseHelper musicDataBaseHelper;
    private String LogTag = "数据库：";


    private DataDo(Context context) {
        musicDataBaseHelper = new MusicDataBaseHelper(context, name, null, 1);
        sqLiteDatabase = musicDataBaseHelper.getWritableDatabase();
    }

    //通过单例模式提供调用
    public static DataDo getInstance(Context context) {
        if (instance == null) {
            synchronized (DataDo.class) {
                if (instance == null) {
                    instance = new DataDo(context);
                }
            }
        }
        return instance;
    }

    /**
     * 添加音乐
     *
     * @param TAG
     * @param song
     */
    public void addMusicData(int TAG, Song song) {
        String tableName = "";
        if (TAG == ConstInterface.HISTORY_MUSIC_TABLE) {
            tableName = historyMusicTable;
        } else if (TAG == ConstInterface.LIKE_MUSIC_TABLE) {
            tableName = likeMusicTable;
        }
        if (song == null) {
            return;
        }
        //判断是否已经存在在数据库中 如果存在则直接返回
        if (isExitsTable(TAG, song)) {
            return;
        }
        String title = song.getTitle();
        String artist = song.getArtist();
        String size = song.getSize();
        String duration = song.getDuration() + "";
        String uri = song.getUrl();
        String id = song.getId() + "";
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("id", id);
            contentValues.put("title", title);
            contentValues.put("size", size);
            contentValues.put("duration", duration);
            contentValues.put("uri", uri);
            contentValues.put("artist", artist);
            sqLiteDatabase.insert(tableName, null, contentValues);
            Log.e(LogTag, "添加音乐：" + title);
        } catch (Exception ex) {
            Log.e(LogTag, "已经存在该音乐" + title);
        }
        contentValues.clear();
    }

    /**
     * 获取所有的歌曲
     *
     * @param TAG
     * @return
     */
    public List<Song> getAllMusicFromTable(int TAG) {
        String tableName = "";
        if (TAG == ConstInterface.HISTORY_MUSIC_TABLE) {
            tableName = historyMusicTable;
        } else if (TAG == ConstInterface.LIKE_MUSIC_TABLE) {
            tableName = likeMusicTable;
        }
        List<Song> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                long id = Long.parseLong(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String artist = cursor.getString(cursor.getColumnIndex("artist"));
                long duration = Long.parseLong(cursor.getString(cursor.getColumnIndex("duration")));
                String size = cursor.getString(cursor.getColumnIndex("size"));
                String uri = cursor.getString(cursor.getColumnIndex("uri"));
                song.setDuration(duration);
                song.setUrl(uri);
                song.setSize(size);
                song.setArtist(artist);
                song.setTitle(title);
                song.setId(id);
                song.setUrl(uri);
                list.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    //判断是否存在数据库
    public boolean isExitsTable(int TAG, Song song) {
        List<Song> list = getAllMusicFromTable(TAG);
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).getId() == song.getId()) && list.get(i).getUrl().equals(song.getUrl())) {
                return true;
            }
        }
        return false;
    }

    //删除单条数据
    public void deleteSong(int TAG, Song song) {
        String tableName = "";
        if (TAG == ConstInterface.HISTORY_MUSIC_TABLE) {
            tableName = historyMusicTable;
        } else if (TAG == ConstInterface.LIKE_MUSIC_TABLE) {
            tableName = likeMusicTable;
        }
        sqLiteDatabase.delete(tableName, "id=? and uri=?", new String[]{String.valueOf(song.getId()), song.getUrl()});
    }
}
