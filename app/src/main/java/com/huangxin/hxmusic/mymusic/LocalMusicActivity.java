package com.huangxin.hxmusic.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.activity.adapter.MyMusicViewPager;
import com.huangxin.hxmusic.mymusic.adapter.ListViewAdapter;
import com.huangxin.hxmusic.mymusic.scanmusic.ScanMusic;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocalMusicActivity extends AppCompatActivity {


    public List<Song> songList;
    private ListView listView;
    private ProgressBar progressBar;
    private TextView textView;
    private final static String TAG="LocalMusicActivity";
    private MyService.MusicBinder musicBinder;
    private ViewPager viewPager;
    private MyMusicViewPager adapter;
    private int songIndex;
    public ImageButton startAndStopButton;
    private boolean isOnRestartUpdateViewPage=false;
    private boolean isPlayingFromMainActivity=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loacl_music);
        songList=new ArrayList<>();
        //先获取bundle对象，然后再通过Bundle对象获得MusicBinder对象
        Bundle musicBundle= getIntent().getBundleExtra("MusicBundle");
        musicBinder=(MyService.MusicBinder)musicBundle.getBinder("MusicBinder");
        //设置播放的列表
        assert musicBinder != null;
        listView=findViewById(R.id.lm_list_view);
        progressBar=findViewById(R.id.lm_pb);
        textView=findViewById(R.id.lm_text);
        viewPager=findViewById(R.id.vp_change_song);
        startAndStopButton=findViewById(R.id.ib_start_stop);
        adapter =new MyMusicViewPager(songList,getApplicationContext(),musicBinder);
        viewPager.setAdapter(adapter);
        LoadingAsyncTask asyncTask=new LoadingAsyncTask();
        //开始执行
        asyncTask.execute();
        listView.setOnItemClickListener(new MyOnItemClickListener());
        viewPager.addOnPageChangeListener(new MyAddOnPageChangeListener());
        startAndStopButton.setOnClickListener(new MyOnClickListener());
        musicBinder.setUpdateInfoInLocalActivity(new MyService.UpdateInfoInLocalActivity() {
            @Override
            public void updateInfo(int index) {
                viewPager.setCurrentItem(index,false);
            }
        });
        if (musicBinder.getPlayMusicList()!=null&&musicBinder.getPlayMusicList().size()>0){
            adapter.setSongs(musicBinder.getPlayMusicList());
            viewPager.setAdapter(adapter);
            if (musicBinder.isPlaying()){
                startAndStopButton.setImageResource(R.drawable.stop);
            }else {
                startAndStopButton.setImageResource(R.drawable.start);
            }
            isPlayingFromMainActivity=true;
            isOnRestartUpdateViewPage=true;
            viewPager.setCurrentItem(musicBinder.getCurrentIndex(),false);
        }

    }

    class LoadingAsyncTask extends AsyncTask<Void,Void,List<Song>>{
        //在任务开始前，主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        //在后台执行
        @Override
        protected List<Song> doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new ScanMusic().ScanMusicList(getContentResolver());
        }
        //在任务结束后，主线程
        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            songList.clear();
            songList=songs;
            Log.e(TAG,"加载本地歌曲");
            ListViewAdapter adapter=new ListViewAdapter(getApplicationContext(),
                    R.layout.list_view_item,songList);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * 列表的点击事件
     */
    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(adapter.getSongs()!=songList){
                //更新列表
                Log.e(TAG,"更新底部的歌曲数据");
                adapter.setSongs(songList);
                musicBinder.setPlayMusicList(songList);
                adapter.notifyDataSetChanged();
            }
            //更新ViewPager
            viewPager.setCurrentItem(position,false);


        }
    }
    //当重新退回到当前Activity时，更新底部的ViewPager

    @Override
    protected void onRestart() {
        super.onRestart();
        isOnRestartUpdateViewPage=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnRestartUpdateViewPage){
            viewPager.setCurrentItem(musicBinder.getCurrentIndex(),false);
            if (musicBinder.isPlaying()){
                startAndStopButton.setImageResource(R.drawable.stop);
            }else {
                startAndStopButton.setImageResource(R.drawable.start);
            }
            isOnRestartUpdateViewPage=false;
        }
        musicBinder.setLocalActivityShow(true);
    }

    private class MyAddOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



        }

        @Override
        public void onPageSelected(int position) {
            if (!isOnRestartUpdateViewPage||!isPlayingFromMainActivity){
                viewPager.setCurrentItem(position,false);
                musicBinder.startPlayer(position);
                startAndStopButton.setImageResource(R.drawable.stop);
            }
            isPlayingFromMainActivity=false;
        }

        @Override
        public void onPageScrollStateChanged(int state) {


        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        musicBinder.setLocalActivityShow(false);
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_start_stop:
                    if(musicBinder.isPlaying()){
                        musicBinder.pauseMusic();
                        startAndStopButton.setImageResource(R.drawable.start);
                    }else {
                        musicBinder.startMusic();
                        startAndStopButton.setImageResource(R.drawable.stop);
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
