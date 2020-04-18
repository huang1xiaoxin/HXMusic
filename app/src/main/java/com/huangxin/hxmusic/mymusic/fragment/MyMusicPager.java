package com.huangxin.hxmusic.mymusic.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.activity.MainActivity;
import com.huangxin.hxmusic.base.BasePager;
import com.huangxin.hxmusic.mymusic.LocalMusicActivity;
import com.huangxin.hxmusic.mymusic.adapter.ImageAdapter;
import com.huangxin.hxmusic.service.MyService;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * 我的音乐的页面;
  */
public class MyMusicPager extends BasePager {

    private static final String TAG="MyViewPager";
    private Button loaclMusicButton;
    private Button likeMusicButton;
    private Button historyMusicButton;
    private MyService.MusicBinder musicBinder;


    public MyMusicPager(Context mContext, MyService.MusicBinder musicBinder) {
        super(mContext);
        this.musicBinder=musicBinder;
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.my_music_fragment,null);
        Log.e(TAG,"初始化界面");
        initViewData(view);
        return view;
    }

    private void initViewData(View view) {
        Banner banner=view.findViewById(R.id.ll_adevert_bar);
        //设置指示器
        banner.setIndicator(new CircleIndicator(context));
        //设置指示器的位置
        banner.setIndicatorGravity(IndicatorConfig.Direction.RIGHT);
        //设置是否自动播放
        banner.isAutoLoop(true);
        //设置被选择的时候的颜色
        banner.setIndicatorSelectedColorRes(R.color.viewpager_text_press);
        banner.setEnabled(false);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置适配器
        banner.setAdapter(new ImageAdapter(initImageList(),context));
        //设置圆弧
        banner.setBannerRound(20);
        loaclMusicButton=view.findViewById(R.id.bt_local_music);
        likeMusicButton=view.findViewById(R.id.bt_like_music);
        historyMusicButton=view.findViewById(R.id.bt_history_music);
        loaclMusicButton.setOnClickListener(new MyButtonOnClickListener());
        historyMusicButton.setOnClickListener(new MyButtonOnClickListener());
        likeMusicButton.setOnClickListener(new MyButtonOnClickListener());
    }
    //初始化数据
    @Override
    public void initDate() {
        super.initDate();

    }
    public List<Integer> initImageList(){
        List<Integer> list=new ArrayList<>();
        list.add(R.drawable.image);
        list.add(R.drawable.image1);
        list.add(R.drawable.image2);
        list.add(R.drawable.image3);
        list.add(R.drawable.image4);

        return list;
    }


    private class MyButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_local_music:
                    Intent intent=new Intent(context, LocalMusicActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putBinder("MusicBinder",musicBinder);
                    intent.putExtra("MusicBundle",bundle);
                    context.startActivity(intent);
                    break;
                case R.id.bt_like_music:
                    break;
                case R.id.bt_history_music:
                    break;
                default:
                    break;
            }
        }
    }
}
