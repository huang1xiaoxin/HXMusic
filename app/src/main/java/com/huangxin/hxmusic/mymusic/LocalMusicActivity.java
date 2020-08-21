package com.huangxin.hxmusic.mymusic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.huangxin.hxmusic.Database.DataDo;
import com.huangxin.hxmusic.PopupWindow.ShowPopupWindow;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.activity.adapter.MyMusicViewPager;
import com.huangxin.hxmusic.mymusic.adapter.ListViewAdapter;
import com.huangxin.hxmusic.mymusic.scanmusic.ScanMusic;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.ButtonStateSubject;
import com.huangxin.hxmusic.utils.ConstInterface;
import com.huangxin.hxmusic.utils.Song;
import com.huangxin.hxmusic.utils.UpdateDataInfo;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends AppCompatActivity {


    private final static String TAG = "LocalMusicActivity";
    public List<Song> songList;
    public static ImageButton startAndStopButton;
    private ListView listView;
    private ProgressBar progressBar;
    private TextView textView;
    private MyService.MusicBinder musicBinder;
    private ViewPager viewPager;
    private MyMusicViewPager adapter;
    private int songIndex;
    private static ButtonStateSubject stateSubject = new ButtonStateSubject() {
        @Override
        public void updateButtonState(int state) {
            if (state == ConstInterface.STARE_PAUSE) {
                startAndStopButton.setImageResource(R.drawable.start);
            } else if (state == ConstInterface.STARE_PLAYING) {
                startAndStopButton.setImageResource(R.drawable.stop);
            }
        }
    };
    private boolean isOnStartUpdateViewPage = false;
    private TextView tipText;
    private int activityTag = 1;
    private ListViewAdapter listViewAdapter;
    private ImageButton currentSongListButton;
    private boolean isRestart = false;
    private LinearLayout bottomLayout;
    private UpdateDataInfo.UpdateDataInfoListener listener = new UpdateDataInfo.UpdateDataInfoListener() {
        @Override
        public void updateInfo(int position) {
            adapter.notifyDataSetChanged();
            Log.e(TAG, "updateViewPager: 更新底部播放栏的数据");

        }

        @Override
        public void startMusicWhenClick(int position) {
            viewPager.setCurrentItem(position, false);
        }
    };

    private void copyArrayList() {
        for (int i = 0; i < listViewAdapter.getListSong().size(); i++) {
            songList.add(listViewAdapter.getListSong().get(i));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestart = true;

        listViewAdapter.clear();
        LoadingAsyncTask asyncTask = new LoadingAsyncTask();
        //开始执行
        asyncTask.execute(activityTag);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (musicBinder.getPlayMusicList() != null && musicBinder.getPlayMusicList().size() > 0 && isRestart) {
            adapter.setSongs(musicBinder.getPlayMusicList());
            adapter.notifyDataSetChanged();
            isRestart = false;
        }
        isOnStartUpdateViewPage = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //处理从其他页面跳转过来的时候设置底部的ui
        viewPager.setCurrentItem(musicBinder.getCurrentIndex(), false);
        isOnStartUpdateViewPage = false;
        musicBinder.setLocalActivityShow(true);
        //设置在弹窗中更改数据时 更新ViewPager
        UpdateDataInfo.getINSTANCE().registerUpdateInfoListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicBinder.setLocalActivityShow(false);
        //取消注册
        UpdateDataInfo.getINSTANCE().unRegisterUpdateBottomViewPageListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loacl_music);
        songList = new ArrayList<>();
        //先获取bundle对象，然后再通过Bundle对象获得MusicBinder对象
        Bundle musicBundle = getIntent().getBundleExtra("MusicBundle");
        musicBinder = (MyService.MusicBinder) musicBundle.getBinder("MusicBinder");
        activityTag = getIntent().getIntExtra("tag", 1);
        //设置播放的列表
        assert musicBinder != null;
        listView = findViewById(R.id.lm_list_view);
        progressBar = findViewById(R.id.lm_pb);
        textView = findViewById(R.id.lm_text);
        viewPager = findViewById(R.id.vp_change_song);
        adapter = new MyMusicViewPager(songList, getApplicationContext(), musicBinder);
        viewPager.setAdapter(adapter);
        startAndStopButton = findViewById(R.id.ib_start_stop);
        bottomLayout = findViewById(R.id.ly_sample_start_music);
        tipText = findViewById(R.id.tv_tip);
        listView.setOnItemClickListener(new MyOnItemClickListener());
        viewPager.addOnPageChangeListener(new MyAddOnPageChangeListener());
        startAndStopButton.setOnClickListener(new MyOnClickListener());
        currentSongListButton = findViewById(R.id.current_list_pw);
        currentSongListButton.setOnClickListener(new MyOnClickListener());
        LoadingAsyncTask asyncTask = new LoadingAsyncTask();
        //开始执行
        asyncTask.execute(activityTag);
        musicBinder.setUpdateInfoInLocalActivity(new MyService.UpdateInfoInLocalActivity() {
            @Override
            public void updateInfo(int index) {
                viewPager.setCurrentItem(index, false);
            }
        });
        musicBinder.getViewControlObserver().subscribe(stateSubject);
        bottomLayout.setVisibility(View.GONE);
        if (musicBinder.getPlayMusicList() != null && musicBinder.getPlayMusicList().size() > 0) {
            bottomLayout.setVisibility(View.VISIBLE);
            adapter.setSongs(musicBinder.getPlayMusicList());
            adapter.notifyDataSetChanged();
            isOnStartUpdateViewPage = true;
            viewPager.setCurrentItem(musicBinder.getCurrentIndex(), false);
        }
        isRestart = false;
        if (musicBinder.getViewControlObserver().getState() == ConstInterface.STARE_PAUSE) {
            startAndStopButton.setImageResource(R.drawable.start);
        } else if (musicBinder.getViewControlObserver().getState() == ConstInterface.STARE_PLAYING) {
            startAndStopButton.setImageResource(R.drawable.stop);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicBinder.getViewControlObserver().unSubscribe(stateSubject);
        startAndStopButton=null;
    }

    /**
     * 列表的点击事件
     */
    private class MyOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (songList.size() > 0) {
                songList.clear();
            }
            //主要是让listViewAdapter.getListSong()是不同的引用
            copyArrayList();
            //更新列表
            Log.e(TAG, "更新底部的歌曲数据");
            adapter.setSongs(songList);
            musicBinder.setPlayMusicList(songList);
            isOnStartUpdateViewPage = false;
            adapter.notifyDataSetChanged();
            //更新ViewPager
            viewPager.setCurrentItem(position, false);
            if (position == 0) {
                //点击第一项时出现无法播放的bug
                musicBinder.startPlayer(position);
            }
            if (bottomLayout.getVisibility() != View.VISIBLE) {
                bottomLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    class LoadingAsyncTask extends AsyncTask<Integer, Void, List<Song>> {

        //在任务开始前，主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }

        //在后台执行
        @Override
        protected List<Song> doInBackground(Integer... integers) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (integers[0] == 1) {
                //本地歌曲
                return new ScanMusic().ScanMusicList(getContentResolver());
            } else if (integers[0] == 2) {
                //我喜欢
                return DataDo.getInstance(getApplicationContext()).getAllMusicFromTable(ConstInterface.LIKE_MUSIC_TABLE);
            } else {
                //音乐播放记录
                return DataDo.getInstance(getApplicationContext()).getAllMusicFromTable(ConstInterface.HISTORY_MUSIC_TABLE);
            }
        }

        //在任务结束后，主线程
        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            Log.e(TAG, "加载本地歌曲");
            listViewAdapter = new ListViewAdapter(getApplicationContext(),
                    R.layout.list_view_item, songs);
            listView.setAdapter(listViewAdapter);
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            if (songs.size() <= 0) {
                tipText.setVisibility(View.VISIBLE);
            }
        }
    }

    private class MyAddOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //防止是从别的地方跳转过来，重新播放设置了ViewPage，然后重新播放
            if (!isOnStartUpdateViewPage) {
                viewPager.setCurrentItem(position, false);
                musicBinder.startPlayer(position);
                isOnStartUpdateViewPage = false;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_start_stop:
                    if (musicBinder.isPlaying()) {
                        musicBinder.pauseMusic();
                    } else {
                        musicBinder.startMusic();
                    }
                    break;
                case R.id.current_list_pw:
                    //显示弹窗
                    new ShowPopupWindow().setMusicBinder(musicBinder).setSongList(musicBinder.getPlayMusicList())
                            .setContext(LocalMusicActivity.this).showPopupWindow(v);
                    break;
                default:
                    break;
            }
        }
    }
}
