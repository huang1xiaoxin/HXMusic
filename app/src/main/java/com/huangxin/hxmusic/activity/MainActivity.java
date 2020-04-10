package com.huangxin.hxmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telecom.ConnectionService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.activity.adapter.MyPagerAdapter;
import com.huangxin.hxmusic.base.BasePager;
import com.huangxin.hxmusic.databinding.ActivityMainBinding;
import com.huangxin.hxmusic.findpager.pager.FindPager;
import com.huangxin.hxmusic.mvpager.pager.MVPager;
import com.huangxin.hxmusic.mymusic.fragment.MyMusicPager;
import com.huangxin.hxmusic.service.MyService;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;

import static com.huangxin.hxmusic.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    private ArrayList<BasePager> pagerArrayList;
    private  ActivityMainBinding binding;
    private int index;
    private MyService.MusicBinder musicBinder;
    private final static String TAG="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMainBinding.inflate(LayoutInflater.from(this));
        View view = binding.getRoot();
        setContentView(view);
        initViewPager();
        initPermission();
        binding.amViewpager.setAdapter(new MyPagerAdapter(pagerArrayList));
        binding.amViewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        binding.radioGroup.setOnCheckedChangeListener(new MyCheckedChangeListener());
        binding.radioGroup.check(R.id.rb_my_music);

    }
    /**
     * 初始化各个页面
     */
    public void initViewPager(){
        pagerArrayList=new ArrayList<>();
        pagerArrayList.add(new MyMusicPager(this));
        pagerArrayList.add(new FindPager(this));
        pagerArrayList.add(new MVPager(this));
    }


    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         监听组件的滑动。position为当前页面的索引，positionOffset为当前页面偏移的百分比，positionOffsetPixels为当前页面偏移的像素位置。
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        /**
         监听组件的页面变化。position为当前页面的索引。
         */
        @Override
        public void onPageSelected(int position) {
            index=position;
        }
        /**
         监听组件的滑动状态变化。state有3种取值：
         ViewPager.SCROLL_STATE_IDLE = 0; 空闲状态，也是初始状态，此时组件是静止的。
         ViewPager.SCROLL_STATE_DRAGGING = 1; 滑动状态，当手指在屏幕上滑动组件时的状态。
         ViewPager.SCROLL_STATE_SETTLING = 2; 滑动后自然沉降的状态，当手指离开屏幕后，组件继续滑动时的状态。
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            //滑动结束的时候改变radioButton的状态
            if(state==0){
                int id=R.id.rb_my_music;
                switch (index){
                    case 0:
                        id=R.id.rb_my_music;
                        break;
                    case 1:
                        id=R.id.rb_find_music;
                        break;
                    case 2:
                        id=R.id.rb_mv;
                        break;
                    default:
                        break;
                }
                binding.radioGroup.check(id);
            }

        }
    }
    private class MyCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    index=0;
                    break;
                case R.id.rb_find_music:
                    index=1;
                    break;
                case R.id.rb_mv:
                    index=2;
                    break;
            }
            binding.amViewpager.setCurrentItem(index,false);

        }
    }
    //动态申请存储读取的权限
    private void initPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE )!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE )!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
    }

    public MyService.MusicBinder getMusicBinder() {
        return musicBinder;
    }
}
