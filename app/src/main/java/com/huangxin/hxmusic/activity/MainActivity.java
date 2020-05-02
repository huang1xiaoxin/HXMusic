package com.huangxin.hxmusic.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.huangxin.hxmusic.PopupWindow.ShowPopupWindow;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.activity.adapter.MyMusicViewPager;
import com.huangxin.hxmusic.activity.adapter.MyPagerAdapter;
import com.huangxin.hxmusic.base.BasePager;
import com.huangxin.hxmusic.findpager.pager.FindPager;
import com.huangxin.hxmusic.mvpager.pager.MVPager;
import com.huangxin.hxmusic.mymusic.fragment.MyMusicPager;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.Song;
import com.huangxin.hxmusic.utils.UpdateDataInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private ArrayList<BasePager> pagerArrayList;
    private int index;
    private MyService.MusicBinder musicBinder;
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private MyMusicPager myMusicPager;
    private ImageButton startAndStopButton;
    private ViewPager changeSongViewPager;
    private LinearLayout sampleStartLinearLayout;
    private MyMusicViewPager musicViewPager;
    private List<Song> songList;
    private boolean isRestartActivity = false;
    private ImageButton currentSongList;
    private UpdateDataInfo.UpdateDataInfoListener listener = new UpdateDataInfo.UpdateDataInfoListener() {
        @Override
        public void updateInfo(int position) {
            musicViewPager.notifyDataSetChanged();
            Log.e(TAG, "updateViewPager: 更新底部播放栏的数据");

        }
    };
    private UpdateDataInfo.UpdateDataInfoListener onListViewClickItemListener = new UpdateDataInfo.UpdateDataInfoListener() {
        @Override
        public void updateInfo(int position) {
            changeSongViewPager.setCurrentItem(position, false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //先获取bundle对象，然后再通过Bundle对象获得MusicBinder对象
        Bundle musicBundle = getIntent().getBundleExtra("MusicBundle");
        musicBinder = (MyService.MusicBinder) musicBundle.getBinder("MusicBinder");
        initViewPager();
        initPermission();
        viewPager = findViewById(R.id.am_viewpager);
        radioGroup = findViewById(R.id.radio_group);
        viewPager.setAdapter(new MyPagerAdapter(pagerArrayList));
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        radioGroup.setOnCheckedChangeListener(new MyCheckedChangeListener());
        radioGroup.check(R.id.rb_my_music);
        startAndStopButton = findViewById(R.id.ib_start_stop);
        changeSongViewPager = findViewById(R.id.vp_change_song);
        currentSongList = findViewById(R.id.current_list_pw);
        sampleStartLinearLayout = findViewById(R.id.ly_sample_start_music);
        sampleStartLinearLayout.setVisibility(View.GONE);
        musicViewPager = new MyMusicViewPager(songList, MainActivity.this, musicBinder);
        changeSongViewPager.addOnPageChangeListener(new MyMusicOnPageChangeListener());
        startAndStopButton.setOnClickListener(new MyMainActivityOnClickListener());
        musicBinder.setUpdateInfoInMainActivity(new MyService.UpdateInfoInMainActivity() {
            @Override
            public void updateInfo(int index) {
                changeSongViewPager.setCurrentItem(index, false);
            }
        });
        currentSongList.setOnClickListener(new MyMainActivityOnClickListener());
    }

    /**
     * 初始化各个页面
     */
    public void initViewPager() {
        pagerArrayList = new ArrayList<>();
        myMusicPager = new MyMusicPager(this, musicBinder);
        pagerArrayList.add(myMusicPager);
        pagerArrayList.add(new FindPager(this));
        pagerArrayList.add(new MVPager(this));
    }

    //动态申请存储读取的权限
    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (musicBinder.getPlayMusicList() != null && musicBinder.getPlayMusicList().size() > 0) {
            sampleStartLinearLayout.setVisibility(View.VISIBLE);
            songList = musicBinder.getPlayMusicList();
            musicViewPager.setSongs(songList);
            changeSongViewPager.setAdapter(musicViewPager);
        }
        if (isRestartActivity) {
            changeSongViewPager.setCurrentItem(musicBinder.getCurrentIndex(), false);
            isRestartActivity = false;
            if (musicBinder.isPlaying()) {
                startAndStopButton.setImageResource(R.drawable.stop);
            } else {
                startAndStopButton.setImageResource(R.drawable.start);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestartActivity = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicBinder.setMainActivityShow(true);
        if (myMusicPager != null && myMusicPager.getBanner() != null) {
            myMusicPager.getBanner().start();
        }
        UpdateDataInfo.getINSTANCE().registerUpdateInfoListener(listener);
        UpdateDataInfo.getINSTANCE().registerUpdateInfoListener(onListViewClickItemListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicBinder.setMainActivityShow(false);
        UpdateDataInfo.getINSTANCE().unRegisterUpdateBottomViewPageListener(listener);
        UpdateDataInfo.getINSTANCE().unRegisterUpdateBottomViewPageListener(onListViewClickItemListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myMusicPager.getBanner().stop();
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 监听组件的滑动。position为当前页面的索引，positionOffset为当前页面偏移的百分比，positionOffsetPixels为当前页面偏移的像素位置。
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        /**
         * 监听组件的页面变化。position为当前页面的索引。
         */
        @Override
        public void onPageSelected(int position) {
            index = position;
        }

        /**
         * 监听组件的滑动状态变化。state有3种取值：
         * ViewPager.SCROLL_STATE_IDLE = 0; 空闲状态，也是初始状态，此时组件是静止的。
         * ViewPager.SCROLL_STATE_DRAGGING = 1; 滑动状态，当手指在屏幕上滑动组件时的状态。
         * ViewPager.SCROLL_STATE_SETTLING = 2; 滑动后自然沉降的状态，当手指离开屏幕后，组件继续滑动时的状态。
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            //滑动结束的时候改变radioButton的状态
            if (state == 0) {
                int id = R.id.rb_my_music;
                switch (index) {
                    case 0:
                        id = R.id.rb_my_music;
                        break;
                    case 1:
                        id = R.id.rb_find_music;
                        break;
                    case 2:
                        id = R.id.rb_mv;
                        break;
                    default:
                        break;
                }
                radioGroup.check(id);
            }

        }
    }

    private class MyCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                default:
                    index = 0;
                    myMusicPager.getBanner().start();
                    break;
                case R.id.rb_find_music:
                    //当现实其他页面的时候停止切换
                    myMusicPager.getBanner().stop();
                    index = 1;
                    break;
                case R.id.rb_mv:
                    //当现实其他页面的时候停止切换
                    myMusicPager.getBanner().stop();
                    index = 2;
                    break;
            }
            viewPager.setCurrentItem(index, false);

        }
    }

    private class MyMusicOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (!isRestartActivity) {
                changeSongViewPager.setCurrentItem(position, false);
                musicBinder.startPlayer(position);
                startAndStopButton.setImageResource(R.drawable.stop);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyMainActivityOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_start_stop:
                    if (musicBinder.isPlaying()) {
                        musicBinder.pauseMusic();
                        startAndStopButton.setImageResource(R.drawable.start);
                    } else {
                        musicBinder.startMusic();
                        startAndStopButton.setImageResource(R.drawable.stop);
                    }
                    break;
                case R.id.current_list_pw:
                    //显示弹窗
                    new ShowPopupWindow().setMusicBinder(musicBinder).setSongList(musicBinder.getPlayMusicList())
                            .setContext(MainActivity.this).showPopupWindow(v);
                default:
                    break;
            }
        }
    }
}
