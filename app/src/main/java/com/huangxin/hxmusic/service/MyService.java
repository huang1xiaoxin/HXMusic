package com.huangxin.hxmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.huangxin.hxmusic.utils.Song;

import java.io.IOException;
import java.util.List;

public class MyService extends Service {
    private final static String TAG="MyService";
    private MediaPlayer mediaPlayer;
    private MusicBinder musicBinder=new MusicBinder();
    public MyService(){
        if (mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
    }
     public class MusicBinder extends Binder{
         //歌曲播放的列表
         private List<Song> playMusicList;
         public MusicBinder(){
             if(mediaPlayer==null){
                 mediaPlayer=new MediaPlayer();
             }
         }
         //开始播放歌曲
         public void startPlayer(String path){
             //如果正在播放音乐则先暂停
             if(mediaPlayer.isPlaying()){
                 mediaPlayer.stop();

             }
             //重置
             mediaPlayer.reset();
             try {
                 mediaPlayer.setDataSource(path);
                 mediaPlayer.prepare();
                 mediaPlayer.start();
             } catch (IOException e) {
                 Log.e(TAG,"播放失败");
                 e.printStackTrace();
             }


         }
         //开始
         public void  startMusic(){
             mediaPlayer.start();
         }
         //暂停
         public void pauseMusic(){
             mediaPlayer.pause();
         }
         //是否在播放歌曲
         public boolean isPlaying(){
             return mediaPlayer.isPlaying();
         }

         public void setPlayMusicList(List<Song> playMusicList) {
             this.playMusicList = playMusicList;
         }

         public List<Song> getPlayMusicList() {
             return playMusicList;
         }
     }
    /**
     * 用于传输Binder对象
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return musicBinder;
    }
}
