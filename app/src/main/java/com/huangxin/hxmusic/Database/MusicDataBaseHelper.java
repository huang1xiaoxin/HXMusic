package com.huangxin.hxmusic.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MusicDataBaseHelper extends SQLiteOpenHelper {
    private   static final String CREATE_LIKE_MUSIC_TABLE="create table likeMusicTable(" +
            "id text primary key not null,title text,artist test,duration text,size text,uri text)";
    private   static final String CREATE_HISTORY_MUSIC_TABLE="create table historyMusicTable(" +
            "id text primary key not null,title text,artist test,duration text,size text,uri text)";;
    public MusicDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(CREATE_LIKE_MUSIC_TABLE);
         db.execSQL(CREATE_HISTORY_MUSIC_TABLE);
        Log.e("数据库","已创建");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists likeMusicTable");
        db.execSQL("drop table if exists historyMusicTable");
        onCreate(db);
    }
}
