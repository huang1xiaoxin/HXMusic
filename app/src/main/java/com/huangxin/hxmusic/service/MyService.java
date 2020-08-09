package com.huangxin.hxmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.huangxin.hxmusic.Database.DataDo;
import com.huangxin.hxmusic.utils.ConstInterface;
import com.huangxin.hxmusic.utils.Song;
import com.huangxin.hxmusic.utils.ViewControlObserver;

import java.io.IOException;
import java.util.List;

public class MyService extends Service {
    private final static String TAG = "MyService";
    private MediaPlayer mediaPlayer;
    private ViewControlObserver viewControlObserver;

    private MusicBinder musicBinder = new MusicBinder();

    public MyService() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (viewControlObserver == null) {
            viewControlObserver = new ViewControlObserver();
        }
    }

    /**
     * 用于传输Binder对象
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    @Override
    public void onDestroy() {
        musicBinder.onDestroy();
        super.onDestroy();
    }

    //定义一个播放完变成下一曲的更新信息的接口
    public interface UpdateInfoListener {
        void updateInfo(int index);
    }

    //定义一个播放完成后下一曲的更新的接口
    public interface UpdateInfoInLocalActivity {
        void updateInfo(int index);
    }

    //定义一个播放完成后下一曲的更新的接口
    public interface UpdateInfoInMainActivity {
        void updateInfo(int index);
    }

    public class MusicBinder extends Binder {
        private int CurrentIndex = -1;
        //歌曲播放的列表
        private int modelTag = 3;
        private List<Song> playMusicList;
        private UpdateInfoListener updateInfoListener;
        private UpdateInfoInLocalActivity updateInfoInLocalActivity;
        private boolean localActivityShow = false;
        private boolean mainActivityShow = false;
        private UpdateInfoInMainActivity updateInfoInMainActivity;


        public MusicBinder() {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            if (viewControlObserver == null) {
                viewControlObserver = new ViewControlObserver();
            }

        }

        //开始播放歌曲
        public void startPlayer(final int index) {
            CurrentIndex = index;
            //如果正在播放音乐则先暂停
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            //重置
            mediaPlayer.reset();
            try {
                String url = playMusicList.get(index).getUrl();
                //url="https://m7.music.126.net/20200630155937/ee27a12189e1a4d9be588085f4c0dbdf/ymusic/8972/6e6e/7b86/bddf788bf92e62d7c5c9aa457dd27bf5.mp3";
                mediaPlayer.setDataSource(url);
                //因为service中也是在主线程的,音乐在准备的时候需要时间，故放在异步
                mediaPlayer.prepareAsync();
                viewControlObserver.updateButtonState(ConstInterface.STARE_PLAYING);
            } catch (IOException e) {
                Log.e(TAG, "播放失败");
                e.printStackTrace();
            }
            //播放完一首歌之后切换下一首
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放完成
                    if (modelTag == ConstInterface.REPEAT_PLAy) {
                        startPlayer(getCurrentIndex());
                    } else {
                        nextSong();
                    }
                    if (updateInfoListener != null) {
                        updateInfoListener.updateInfo(CurrentIndex);
                    }

                    if (localActivityShow) {
                        updateInfoInLocalActivity.updateInfo(CurrentIndex);
                    }
                    if (mainActivityShow) {
                        updateInfoInMainActivity.updateInfo(CurrentIndex);
                    }

                }
            });
            //这里要返回true，才可以让回调是否播放的当前歌曲的接口有限
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return true;
                }
            });
            //设置准备完歌曲后的实现
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    //添加播放历史的音乐
                    DataDo.getInstance(getApplicationContext()).addMusicData(ConstInterface.HISTORY_MUSIC_TABLE,
                            playMusicList.get(index));
                    if (updateInfoListener != null) {
                        //更改UI信息
                        updateInfoListener.updateInfo(index);
                    }
                }
            });

        }

        //下一曲
        public void nextSong() {
            if (playMusicList != null) {
                //重置资源
                mediaPlayer.reset();
                if (modelTag == ConstInterface.LIST_PLAY || modelTag == ConstInterface.REPEAT_PLAy) {
                    CurrentIndex++;
                    if (CurrentIndex >= playMusicList.size()) {
                        CurrentIndex = 0;
                    }
                } else if (modelTag == ConstInterface.RANDOM_PLAY) {
                    CurrentIndex = (int) (Math.random() * playMusicList.size());
                    if (CurrentIndex > playMusicList.size() - 1) {
                        CurrentIndex = playMusicList.size() - 1;
                    }
                }
                startPlayer(CurrentIndex);
            } else {
                Toast.makeText(MyService.this, "请选择需要播放的歌曲", Toast.LENGTH_SHORT).show();
            }

        }

        //上一曲
        public void lastSong() {
            if (mediaPlayer != null) {
                //重置资源
                mediaPlayer.reset();
                if (modelTag == ConstInterface.LIST_PLAY || modelTag == ConstInterface.REPEAT_PLAy) {
                    CurrentIndex--;
                    if (CurrentIndex < 0) {
                        CurrentIndex = playMusicList.size() - 1;
                    }
                } else if (modelTag == ConstInterface.RANDOM_PLAY) {
                    CurrentIndex = (int) (Math.random() * playMusicList.size());
                    if (CurrentIndex > playMusicList.size() - 1) {
                        CurrentIndex = playMusicList.size() - 1;
                    }
                }
                startPlayer(CurrentIndex);
            } else {
                Toast.makeText(MyService.this, "请选择需要播放的歌曲", Toast.LENGTH_SHORT).show();
            }

        }

        //开始
        public void startMusic() {
            viewControlObserver.updateButtonState(ConstInterface.STARE_PLAYING);
            mediaPlayer.start();
        }

        //暂停
        public void pauseMusic() {
            viewControlObserver.updateButtonState(ConstInterface.STARE_PAUSE);
            mediaPlayer.pause();
        }

        //是否在播放歌曲
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public int getCurrentIndex() {
            return CurrentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            CurrentIndex = currentIndex;
        }

        public List<Song> getPlayMusicList() {
            return playMusicList;
        }

        public void setPlayMusicList(List<Song> playMusicList) {
            this.playMusicList = playMusicList;
        }

        public int getModelTag() {
            return modelTag;
        }

        public void setModelTag(int modelTag) {
            this.modelTag = modelTag;
        }

        public int getCurrentProgress() {
            return mediaPlayer.getCurrentPosition();
        }

        //滑动播放
        public void setStartMusicSeekTo(int progress) {
            mediaPlayer.seekTo(progress);
            mediaPlayer.start();
        }

        public void setUpdateInfoListener(UpdateInfoListener updateInfoListener) {
            this.updateInfoListener = updateInfoListener;
        }

        public void setUpdateInfoInLocalActivity(UpdateInfoInLocalActivity updateInfoInLocalActivity) {
            this.updateInfoInLocalActivity = updateInfoInLocalActivity;
        }

        public void setLocalActivityShow(boolean localActivityShow) {
            this.localActivityShow = localActivityShow;
        }

        public void setUpdateInfoInMainActivity(UpdateInfoInMainActivity updateInfoInMainActivity) {
            this.updateInfoInMainActivity = updateInfoInMainActivity;
        }

        public void setMainActivityShow(boolean mainActivityShow) {
            this.mainActivityShow = mainActivityShow;
        }

        public void onDestroy() {
            // viewControlObserver.clean();
        }

        public ViewControlObserver getViewControlObserver() {
            return viewControlObserver;
        }
    }

}
