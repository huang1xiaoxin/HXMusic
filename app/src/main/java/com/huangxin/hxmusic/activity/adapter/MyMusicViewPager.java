package com.huangxin.hxmusic.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.activity.DetailMusicActivity;
import com.huangxin.hxmusic.imageloader.ImageResizer;
import com.huangxin.hxmusic.mymusic.LocalMusicActivity;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.Song;

import java.util.ArrayList;
import java.util.List;

public class MyMusicViewPager extends PagerAdapter{

        private List<Song> songs;
        private Context context;
        private MyService.MusicBinder musicBinder;

        public MyMusicViewPager(List<Song> songs, Context context, MyService.MusicBinder musicBinder){
            this.songs=songs;
            this.context=context;
            this.musicBinder=musicBinder;
        }


    @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
        //初始化Item


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Song song=songs.get(position);
            View view= LayoutInflater.from(context).inflate(R.layout.view_pager_item,null);
            TextView textView=view.findViewById(R.id.tv_view_pager);
            ImageView imageView=view.findViewById(R.id.im_view_pager);
            textView.setText(song.getTitle()+"-"+song.getArtist());
            Bitmap bitmap=song.loadingBitmap(song.getUrl(),40,40);
            if (bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }else {
                imageView.setImageResource(R.drawable.default_image);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DetailMusicActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putBinder("MusicBinder",musicBinder);
                    intent.putExtra("MusicBundle",bundle);
                    context.startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }

        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }

        public List<Song> getSongs() {
            return songs;
        }
        //防止在更新数据时失效
        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
}
