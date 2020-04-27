package com.huangxin.hxmusic.PopupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.Song;

import java.util.List;

public class ShowPopupWindow {
    private final String TAG="PopupWindow";
    private MyService.MusicBinder musicBinder;
    private List<Song> songList;
    private Context context;
    public void showPopupWindow(View view){
        //获取屏幕的宽和高
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int width=dm.widthPixels;
        int height=dm.widthPixels;
        View contentView= LayoutInflater.from(context).inflate(R.layout.popup_window_layout,null);
        ListView listView=contentView.findViewById(R.id.list_view_pw);
        ImageButton imageButton=contentView.findViewById(R.id.imageButton);
        TextView countText=contentView.findViewById(R.id.pd_count);
        countText.setText("("+songList.size()+")");
        PopupWindowListViewAdapter adapter=new PopupWindowListViewAdapter(context,R.layout.popup_window_listview_item,songList);
        adapter.setMusicBinder(musicBinder);
        listView.setAdapter(adapter);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        PopupWindow popupWindow=new PopupWindow(contentView,width-60,height,true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.RED));
        //设置动画
        popupWindow.setAnimationStyle(R.style.MyPopupWIndow_Anim);
        //设置在底部弹出
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
        Log.e(TAG, " 弹出窗口显示" );
    }
    public ShowPopupWindow setMusicBinder(MyService.MusicBinder musicBinder){
        this.musicBinder=musicBinder;
        return this;
    }
    public ShowPopupWindow setSongList(List<Song> songList){
        this.songList=songList;
        return this;
    }

    public ShowPopupWindow setContext(Context context) {
        this.context = context;
        return  this;
    }

}
