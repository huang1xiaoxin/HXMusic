package com.huangxin.hxmusic.PopupWindow;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.mymusic.adapter.ListViewAdapter;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.Song;

import java.util.List;

public class PopupWindowListViewAdapter extends ArrayAdapter<Song> {
    private Context context;
    private int resource;
    private List<Song> songList;
    private MyService.MusicBinder musicBinder;
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        Song song=songList.get(position);
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
}
