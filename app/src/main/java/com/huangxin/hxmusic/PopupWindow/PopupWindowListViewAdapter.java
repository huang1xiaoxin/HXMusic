package com.huangxin.hxmusic.PopupWindow;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.Song;
import com.huangxin.hxmusic.utils.UpdateDataInfo;

import java.util.List;

public class PopupWindowListViewAdapter extends ArrayAdapter<Song> {
    private Context context;
    private int resource;
    private List<Song> songList;
    private MyService.MusicBinder musicBinder;
    private final String TAG=" PopupWindowListViewAdapter";
    private UpdateSizeTextListener updateSizeTextListener;
    public PopupWindowListViewAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        songList=objects;
    }
    @Override
    public int getCount() {
        return songList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;
        final Song song=songList.get(position);
        if (convertView==null){
            view= LayoutInflater.from(context).inflate(resource,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.artistTextView=view.findViewById(R.id.pw_lw_artist_tv);
            viewHolder.titleTextView=view.findViewById(R.id.pw_lw_title_tv);
            viewHolder.removeButton=view.findViewById(R.id.pw_lw_remove_ib);
            viewHolder.linearLayout=view.findViewById(R.id.item_background_pw);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.artistTextView.setText("-"+song.getArtist());
        viewHolder.titleTextView.setText(song.getTitle());
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songList.remove(position);
                notifyDataSetChanged();
                if (position<musicBinder.getCurrentIndex()){
                    musicBinder.setCurrentIndex(musicBinder.getCurrentIndex()-1);
                }else if (position==musicBinder.getCurrentIndex()){
                    //下一首歌曲
                    musicBinder.setCurrentIndex(musicBinder.getCurrentIndex()-1);
                    if (musicBinder.isPlaying()) {
                        musicBinder.nextSong();
                    } else {
                        //如果前音乐暂停播放的时候，切换下一首歌曲时也应该暂停播放
                        musicBinder.nextSong();
                        musicBinder.pauseMusic();
                    }

                }
                //更新底部的ViewPager
                UpdateDataInfo.getINSTANCE().updateDataInfoListener.updateInfo();
                updateSizeTextListener.updateSizeText();
                Log.e(TAG, "onClick: 移当前播放列表中的歌曲:"+song.getTitle());
            }
        });
        //设置背景
        if (musicBinder.getCurrentIndex()==position&&(musicBinder.isPlaying()||musicBinder.getCurrentProgress()!=0)){
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#685CBFF1"));
        }else {
            viewHolder.linearLayout.setBackgroundColor(Color.parseColor("#88FFFFFF"));
        }

        return view;
    }

    public void setMusicBinder(MyService.MusicBinder musicBinder) {
        this.musicBinder = musicBinder;
    }
    class ViewHolder{
        TextView titleTextView;
        TextView artistTextView;
        ImageButton removeButton;
        LinearLayout linearLayout;
    }

    public void setUpdateSizeTextListener(UpdateSizeTextListener updateSizeTextListener) {
        this.updateSizeTextListener = updateSizeTextListener;
    }

    public interface UpdateSizeTextListener {
        void updateSizeText();
    }

}
