package com.huangxin.hxmusic.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.service.MyService;

public class LauncherActivity extends AppCompatActivity {
    private boolean isStarting = false;
    private MyService.MusicBinder musicBinder;
    //服务连接，通过回调的方法获得Binder的类
    //当Context初始化没有完成的时候，不会执行onServiceConnected
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("", "服务已绑定");
            //获得MusicBinder的对象
            musicBinder = (MyService.MusicBinder) service;
            // myMusicPager.setMediaPlayerBinder(musicBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
        //绑定服务

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        //延迟两秒钟启动
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity();
            }
        }, 2000);
        Intent intent = new Intent(LauncherActivity.this, MyService.class);
        // BIND_AUTO_CREATE代表若服务不存在实例则创建实例
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void startActivity() {
        if (!isStarting) {
            isStarting = true;
            Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBinder("MusicBinder", musicBinder);
            intent.putExtra("MusicBundle", bundle);
            startActivity(intent);
            //结束当前的Activity
            finish();
        }
    }
}
