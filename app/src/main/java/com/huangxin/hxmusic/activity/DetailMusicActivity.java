package com.huangxin.hxmusic.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.huangxin.hxmusic.Database.DataDo;
import com.huangxin.hxmusic.PopupWindow.ShowPopupWindow;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.ButtonStateSubject;
import com.huangxin.hxmusic.utils.ConstInterface;
import com.huangxin.hxmusic.utils.Song;
import com.huangxin.hxmusic.utils.UpdateDataInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DetailMusicActivity extends AppCompatActivity {
    private final String TAG = "DetailMusicActivity";
    private MyService.MusicBinder musicBinder;
    private ImageButton nextButton;
    private ImageButton lastButton;
    private static ImageButton startAndStopButton;
    private ImageButton playMode;
    private ImageButton songListButton;
    private ImageButton addLikeButton;
    private TextView artistText;
    private TextView titleText;
    private TextView updateText;
    private TextView durationText;
    private ImageView imageView;
    private List<Song> songList;
    private int currentIndex;
    private ImageButton backButton;
    private SeekBar seekBar;
    private int modelTag = 1;
    private Timer timer;
    private boolean addLike = true;
    private Animation mAnimation;
    UpdateDataInfo.UpdateDataInfoListener updateDataInfoListener = new UpdateDataInfo.UpdateDataInfoListener() {
        @Override
        public void updateInfo(int position) {
            initInfo();
        }

        @Override
        public void startMusicWhenClick(int position) {
            musicBinder.startPlayer(position);
            initInfo();
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                seekBar.setProgress(musicBinder.getCurrentProgress());
            }
        }
    };

    private static ButtonStateSubject stateSubject = new ButtonStateSubject() {
        @Override
        public void updateButtonState(int state) {
            if (state == ConstInterface.STARE_PAUSE) {
                startAndStopButton.setImageResource(R.drawable.start_music);
            } else if (state == ConstInterface.STARE_PLAYING) {
                startAndStopButton.setImageResource(R.drawable.stop_music);
            }
        }
    };
    //启动计时更新进度条
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            //正在播放音乐的时候更新进度条
            if (musicBinder.isPlaying()) {
                handler.sendEmptyMessage(1);
            }
        }
    };
    private ImageButton currentSongListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_music);
        timer = new Timer();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("MusicBundle");
        assert bundle != null;
        musicBinder = (MyService.MusicBinder) bundle.getBinder("MusicBinder");
        musicBinder.setUpdateInfoListener(new MyService.UpdateInfoListener() {
            @Override
            public void updateInfo(int index) {
                initInfo();
            }
        });
        musicBinder.getViewControlObserver().subscribe(stateSubject);
        currentIndex = musicBinder.getCurrentIndex();
        songList = musicBinder.getPlayMusicList();
        //开启动画
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        mAnimation.setInterpolator(interpolator);
        initView();
        //开启计时器
        timer.schedule(timerTask, 0, 500);
        int state = musicBinder.getViewControlObserver().getState();
        if (state == ConstInterface.STARE_PAUSE) {
            startAndStopButton.setImageResource(R.drawable.start_music);
        } else if (state == ConstInterface.STARE_PLAYING) {
            startAndStopButton.setImageResource(R.drawable.stop_music);
        }

    }

    private void initView() {
        nextButton = findViewById(R.id.next_song_ib);
        lastButton = findViewById(R.id.last_song_ib);
        startAndStopButton = findViewById(R.id.ib_start_stop_ib);
        playMode = findViewById(R.id.play_model_ib);
        songListButton = findViewById(R.id.song_list_ib);
        addLikeButton = findViewById(R.id.like_bt);
        artistText = findViewById(R.id.artist_tv);
        titleText = findViewById(R.id.title_tv);
        updateText = findViewById(R.id.time_update_tv);
        durationText = findViewById(R.id.duration_tv);
        imageView = findViewById(R.id.iv_image);
        backButton = findViewById(R.id.ib_back);
        seekBar = findViewById(R.id.seekBar3);
        initInfo();
        modelTag = musicBinder.getModelTag();
        //设置播放模式的样式
        setModelImage();
        startAndStopButton.setOnClickListener(new MyOnClickListener());
        nextButton.setOnClickListener(new MyOnClickListener());
        lastButton.setOnClickListener(new MyOnClickListener());
        playMode.setOnClickListener(new MyOnClickListener());
        backButton.setOnClickListener(new MyOnClickListener());
        seekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
        addLikeButton.setOnClickListener(new MyOnClickListener());
        songListButton.setOnClickListener(new MyOnClickListener());
    }

    private void initInfo() {
        if (musicBinder.getPlayMusicList().size() > 0) {
            if (imageView.getAnimation() != null) {
                imageView.clearAnimation();
            }
            currentIndex = musicBinder.getCurrentIndex();
            Bitmap bitmap = songList.get(currentIndex).loadingBitmap(songList.get(currentIndex).getUrl(), 250, 250);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.default_image);
            }
            artistText.setText(songList.get(currentIndex).getArtist());
            titleText.setText(songList.get(currentIndex).getTitle());
            durationText.setText(formatDate(songList.get(currentIndex).getDuration()));
            seekBar.setMax((int) songList.get(currentIndex).getDuration());
            seekBar.setProgress(musicBinder.getCurrentProgress());
            updateText.setText(formatDate(musicBinder.getCurrentProgress()));
            if (DataDo.getInstance(getApplicationContext()).isExitsTable(ConstInterface.LIKE_MUSIC_TABLE, songList.get(currentIndex))) {
                addLikeButton.setImageResource(R.drawable.like);
                addLike = true;
            } else {
                addLikeButton.setImageResource(R.drawable.dislike);
                addLike = false;
            }
            if (imageView.getAnimation() == null) {
                imageView.setAnimation(mAnimation);
            }
            //设置动画
            if (musicBinder.isPlaying()) {
                imageView.startAnimation(mAnimation);
            }
        }
    }

    private void setModelImage() {
        switch (musicBinder.getModelTag()) {
            case ConstInterface
                    .RANDOM_PLAY:
                musicBinder.setModelTag(ConstInterface.RANDOM_PLAY);
                playMode.setImageResource(R.drawable.random_paly);
                break;
            case ConstInterface.REPEAT_PLAy:
                musicBinder.setModelTag(ConstInterface.REPEAT_PLAy);
                playMode.setImageResource(R.drawable.repeat_play);
                break;

            case ConstInterface.LIST_PLAY:
                musicBinder.setModelTag(ConstInterface.LIST_PLAY);
                playMode.setImageResource(R.drawable.list_play);
                break;
            default:
                break;
        }
    }

    //时间格式化
    private String formatDate(long progress) {
        Date date = new Date(progress);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateDataInfo.getINSTANCE().registerUpdateInfoListener(updateDataInfoListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止动画
        imageView.clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateDataInfo.getINSTANCE().unRegisterUpdateBottomViewPageListener(updateDataInfoListener);
        mAnimation = null;
        musicBinder.setUpdateInfoListener(null);
        musicBinder.getViewControlObserver().unSubscribe(stateSubject);
        //防止内存泄露
        startAndStopButton=null;
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_start_stop_ib:
                    if (musicBinder.isPlaying()) {
                        musicBinder.pauseMusic();
                    } else {
                        musicBinder.startMusic();
                    }
                    break;
                case R.id.next_song_ib:
                    musicBinder.nextSong();
                    //initInfo();
                    break;
                case R.id.last_song_ib:
                    musicBinder.lastSong();
                    //initInfo();
                    break;
                case R.id.play_model_ib:
                    modelTag++;
                    if (modelTag > 3) {
                        modelTag = 1;
                    }
                    musicBinder.setModelTag(modelTag);
                    setModelImage();
                    break;
                case R.id.ib_back:
                    finish();
                    break;
                case R.id.like_bt:
                    if (addLike == true) {
                        addLikeButton.setImageResource(R.drawable.dislike);
                        addLike = false;
                    } else {
                        addLikeButton.setImageResource(R.drawable.like);
                        addLike = true;
                    }
                    if (addLike) {
                        DataDo.getInstance(getApplicationContext()).addMusicData(ConstInterface.LIKE_MUSIC_TABLE
                                , songList.get(currentIndex));
                    } else {
                        DataDo.getInstance(getApplicationContext()).deleteSong(ConstInterface.LIKE_MUSIC_TABLE,
                                songList.get(currentIndex));

                    }
                    break;
                case R.id.song_list_ib:
                    //显示弹窗
                    ShowPopupWindow showPopupWindow = new ShowPopupWindow();
                    showPopupWindow.setMusicBinder(musicBinder).setSongList(musicBinder.getPlayMusicList())
                            .setContext(DetailMusicActivity.this).showPopupWindow(v);
                    showPopupWindow.setDetailMusicActivity(true);
                    //回调播放模式改变
                    showPopupWindow.setPlayModelUpdateListener(new ShowPopupWindow.PlayModelUpdateListener() {
                        @Override
                        public void update() {
                            setModelImage();
                        }
                    });
                    break;
                default:
                    break;
            }


        }
    }

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        //滑动的改变
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //更新时间
            updateText.setText(formatDate(progress));
        }

        //开始滑动
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        //停止滑动
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            musicBinder.setStartMusicSeekTo(seekBar.getProgress());
        }
    }

}
