package com.huangxin.hxmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.huangxin.hxmusic.R;

public class LauncherActivity extends AppCompatActivity {
    private boolean isStarting=false;
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
        },2000);
    }

    private void startActivity() {
        if(!isStarting){
            isStarting=true;
            Intent intent=new Intent(LauncherActivity.this, MainActivity.class);
            startActivity(intent);
            //结束当前的Activity
            finish();
        }
    }
}
