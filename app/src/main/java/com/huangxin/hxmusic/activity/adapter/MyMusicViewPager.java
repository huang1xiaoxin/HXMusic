package com.huangxin.hxmusic.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.imageloader.ImageResizer;
import com.huangxin.hxmusic.utils.Song;

import java.util.List;

public class MyMusicViewPager extends PagerAdapter{

        private List<Song> songs;
        private Context context;

        public MyMusicViewPager(List<Song> songs, Context context){
            this.songs=songs;
            this.context=context;
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
            container.addView(view);
            return view;
        }

        public void setSongs(List<Song> songs) {
            this.songs = songs;
        }

        public List<Song> getSongs() {
            return songs;
        }


}
