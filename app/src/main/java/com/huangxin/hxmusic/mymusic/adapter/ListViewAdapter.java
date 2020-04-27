package com.huangxin.hxmusic.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.utils.Song;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Song> {
    private int resourceId;
    private  List<Song> listSong;

    public ListViewAdapter(@NonNull Context context, int resource,  @NonNull List<Song> objects) {
        super(context, resource,  objects);
        this.resourceId=resource;
        listSong=objects;
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public void setListSong(List<Song> listSong) {
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        Song song=listSong.get(position);
        ViewHolder holder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder=new ViewHolder();
            holder.size=view.findViewById(R.id.size_tv);
            holder.image=view.findViewById(R.id.lv_image_view);
            holder.title=view.findViewById(R.id.title_tv);
            holder.artist=view.findViewById(R.id.artist_tv);
            view.setTag(holder);
        }else {
            view=convertView;
            holder=(ViewHolder) view.getTag();
        }
        holder.size.setText(song.getSize());
        holder.artist.setText(song.getArtist());
        holder.title.setText(song.getTitle());
        if(song.loadingBitmap(song.getUrl(),70,70)!=null){
            holder.image.setImageBitmap(song.loadingBitmap(song.getUrl(),70,70));
        }else{
            holder.image.setImageResource(R.drawable.default_image);
        }

        return view;
    }



    class ViewHolder{
        TextView size;
        TextView title;
        TextView artist;
        ImageView image;
    }

}
