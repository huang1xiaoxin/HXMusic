package com.huangxin.hxmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.ModelInterface;
import com.huangxin.hxmusic.utils.Song;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DetailMusicActivity extends AppCompatActivity {

    private MyService.MusicBinder musicBinder;
    private ImageButton nextButton;
    private ImageButton lastButton;
    private ImageButton startAndStopButton;
    private ImageButton playMode;
    private ImageButton songListButton;
    private ImageButton addLikeButton;
    private TextView artistText;
    private TextView titleText;
    private TextView updateText;
    private TextView durationText;
    private ImageView imageView;

    private  int currentIndex;
    private List<Song> songList;
    private int modelTag=1;
    private ImageButton backButton;
    private SeekBar seekBar;

    private Timer timer;

    //启动计时更新进度条
    private TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            //正在播放音乐的时候更新进度条
            if (musicBinder.isPlaying()){
               handler.sendEmptyMessage(1);
            }
        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                seekBar.setProgress(musicBinder.getCurrentProgress());
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_music);
        timer=new Timer();
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("MusicBundle");
        assert bundle != null;
        musicBinder= (MyService.MusicBinder) bundle.getBinder("MusicBinder");
        musicBinder.setUpdateInfoListener(new MyService.UpdateInfoListener() {
            @Override
            public void updateInfo(int index) {
                initInfo();
            }
        });
        currentIndex=musicBinder.getCurrentIndex();
        songList=musicBinder.getPlayMusicList();
        initView();
        //开启计时器
        timer.schedule(timerTask,0,500);

    }

    private void initView() {
        nextButton=findViewById(R.id.next_song_ib);
        lastButton=findViewById(R.id.last_song_ib);
        startAndStopButton=findViewById(R.id.ib_start_stop_ib);
        playMode=findViewById(R.id.play_model_ib);
        songListButton=findViewById(R.id.song_list_ib);
        addLikeButton=findViewById(R.id.like_bt);
        artistText=findViewById(R.id.artist_tv);
        titleText=findViewById(R.id.title_tv);
        updateText=findViewById(R.id.time_update_tv);
        durationText =findViewById(R.id.duration_tv);
        imageView=findViewById(R.id.iv_image);
        backButton=findViewById(R.id.ib_back);
        seekBar=findViewById(R.id.seekBar3);
        initInfo();
        modelTag=musicBinder.getModelTag();
        //设置播放模式的样式
        setModelImage();

        startAndStopButton.setOnClickListener(new MyOnClickListener());
        nextButton.setOnClickListener(new MyOnClickListener());
        lastButton.setOnClickListener(new MyOnClickListener());
        playMode.setOnClickListener(new MyOnClickListener());
        backButton.setOnClickListener(new MyOnClickListener());
        seekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

    }

    private void initInfo() {
        currentIndex=musicBinder.getCurrentIndex();
        Bitmap bitmap=songList.get(currentIndex).loadingBitmap(songList.get(currentIndex).getUrl(),250,250);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else {
            imageView.setImageResource(R.drawable.default_image);
        }
        if (musicBinder.isPlaying()){
            startAndStopButton.setImageResource(R.drawable.stop_music);
        }else {
            startAndStopButton.setImageResource(R.drawable.start_music);
        }
        artistText.setText(songList.get(currentIndex).getArtist());
        titleText.setText(songList.get(currentIndex).getTitle());
        durationText.setText(formatDate(songList.get(currentIndex).getDuration()));
        seekBar.setMax((int) songList.get(currentIndex).getDuration());
        seekBar.setProgress(musicBinder.getCurrentProgress());
        updateText.setText(formatDate(musicBinder.getCurrentProgress()));
    }

    class  MyOnClickListener implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_start_stop_ib:
                    if (musicBinder.isPlaying()){
                        musicBinder.pauseMusic();
                        startAndStopButton.setImageResource(R.drawable.start_music);
                    }else {
                        musicBinder.startMusic();
                        startAndStopButton.setImageResource(R.drawable.stop_music);
                    }
                break;
                case R.id.next_song_ib:
                    musicBinder.nextSong();
                    initInfo();
                    break;
                case R.id.last_song_ib:
                    musicBinder.lastSong();
                    initInfo();
                    break;
                case R.id.play_model_ib:
                    modelTag++;
                    if(modelTag>3){
                        modelTag=1;
                    }
                    setModelImage();
                    break;
                case R.id.ib_back:
                    finish();
                    break;
                default:
                    break;
            }

        }
    }

    private void setModelImage() {
        switch (modelTag){
            case ModelInterface
                    .RANDOM_PLAY:
                musicBinder.setModelTag(ModelInterface.RANDOM_PLAY);
             playMode.setImageResource(R.drawable.random_paly);
             break;
            case ModelInterface.REPEAT_PLAy:
                musicBinder.setModelTag(ModelInterface.REPEAT_PLAy);
                playMode.setImageResource(R.drawable.repeat_play);
                break;

            case ModelInterface.LIST_PLAY:
                    musicBinder.setModelTag(ModelInterface.LIST_PLAY);
                    playMode.setImageResource(R.drawable.list_play);
                    break;
            default:
                break;
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
    //时间格式化
    private String formatDate(long progress) {
        Date date=new Date(progress);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date);
    }
}
