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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.ConstInterface;
import com.huangxin.hxmusic.utils.Song;

import java.util.List;

public class ShowPopupWindow {
    private final String TAG="PopupWindow";
    private MyService.MusicBinder musicBinder;
    private List<Song> songList;
    private Context context;
    private ImageButton playModelImageButton;
    private int modelTag;
    private ListView listView;
    private boolean isDetailMusicActivity = false;
    private PlayModelUpdateListener playModelUpdateListener;
    public void showPopupWindow(View view){
        if (songList == null) {
            return;
        }
        //获取屏幕的宽和高
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int width=dm.widthPixels;
        int height=dm.widthPixels;
        View contentView= LayoutInflater.from(context).inflate(R.layout.popup_window_layout,null);
        listView = contentView.findViewById(R.id.list_view_pw);
        playModelImageButton = contentView.findViewById(R.id.pw_model_play_ib);
        final TextView countText = contentView.findViewById(R.id.pd_count);
        countText.setText("("+songList.size()+")");
        PopupWindowListViewAdapter adapter=new PopupWindowListViewAdapter(context,R.layout.popup_window_listview_item,songList);
        adapter.setMusicBinder(musicBinder);
        listView.setAdapter(adapter);
        initPlayModelButton();
        playModelImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置点击事件
                modelTag++;
                if (modelTag > 3) {
                    modelTag = 1;
                }
                musicBinder.setModelTag(modelTag);
                initPlayModelButton();
            }
        });
        adapter.setUpdateSizeTextListener(new PopupWindowListViewAdapter.UpdateSizeTextListener() {
            @Override
            public void updateSizeText() {
                countText.setText("(" + songList.size() + ")");
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
        //设置弹窗消失时点的事件，用于更新详情页的播放模式
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {


            @Override
            public void onDismiss() {
                //其他页面不更新
                if (isDetailMusicActivity) {
                    playModelUpdateListener.update();
                }
            }
        });
        Log.e(TAG, " 弹出窗口显示" );
    }

    /**
     * 更据已设置的播放模式，来更改设置按钮的样式
     */
    private void initPlayModelButton() {
        switch (musicBinder.getModelTag()) {
            case ConstInterface.LIST_PLAY:
                //列表播放
                playModelImageButton.setImageResource(R.drawable.list_play_text);
                break;
            case ConstInterface.RANDOM_PLAY:
                //随机播放
                playModelImageButton.setImageResource(R.drawable.random_paly_text);
                break;
            case ConstInterface.REPEAT_PLAy:
                playModelImageButton.setImageResource(R.drawable.single_play_text);
                //单曲循环
                break;
        }
        modelTag = musicBinder.getModelTag();
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

    public void setDetailMusicActivity(boolean detailMusicActivity) {
        isDetailMusicActivity = detailMusicActivity;
    }

    public void setPlayModelUpdateListener(PlayModelUpdateListener playModelUpdateListener) {
        this.playModelUpdateListener = playModelUpdateListener;
    }

    /**
     * 更新播放模式的接口
     */
    public interface PlayModelUpdateListener {
        void update();
    }
}
