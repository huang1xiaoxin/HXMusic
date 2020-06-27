package com.huangxin.hxmusic.mymusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huangxin.hxmusic.Database.DataDo;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.base.BasePagerFragment;
import com.huangxin.hxmusic.mymusic.LocalMusicActivity;
import com.huangxin.hxmusic.mymusic.adapter.ImageAdapter;
import com.huangxin.hxmusic.service.MyService;
import com.huangxin.hxmusic.utils.ConstInterface;
import com.huangxin.hxmusic.utils.Song;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的音乐的页面;
 */
public class MyMusicPagerFragment extends BasePagerFragment {

    private static final String TAG = "MyViewPager";
    private Button loaclMusicButton;
    private Button likeMusicButton;
    private Button historyMusicButton;
    private MyService.MusicBinder musicBinder;
    private Banner banner;
    private GridView historyGridView;
    private GridView likeGridView;
    private Button moreLikeSongButton;
    private Button moreHistorySongButton;

    public MyMusicPagerFragment(MyService.MusicBinder musicBinder) {
        this.musicBinder = musicBinder;
    }


    @Override
    protected void fragmentFirstLoadingData() {
        //加载数据
        List<Song> likeSongList = DataDo.getInstance(getContext()).queryThreeSong(ConstInterface.LIKE_MUSIC_TABLE);
        List<Song> historySongList = DataDo.getInstance(getContext()).queryThreeSong(ConstInterface.HISTORY_MUSIC_TABLE);
        if (likeSongList.size() > 0) {
            likeGridView.setAdapter(new GridViewAdapter(getContext(), R.layout.griw_view_item, likeSongList));
        }
        if (historySongList.size() > 0) {
            historyGridView.setAdapter(new GridViewAdapter(getContext(), R.layout.griw_view_item, historySongList));
        }
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_music_fragment, container, false);
        Log.e(TAG, "初始化界面");
        initViewData(view);
        return view;
    }

    private void initViewData(View view) {
        banner = view.findViewById(R.id.ll_adevert_bar);
        //设置指示器
        banner.setIndicator(new CircleIndicator(getContext()));
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
        banner.setAdapter(new ImageAdapter(initImageList(), getContext()));
        //设置圆弧
        banner.setBannerRound(20);
        //开始轮播
        banner.start();
        loaclMusicButton = view.findViewById(R.id.bt_local_music);
        likeMusicButton = view.findViewById(R.id.bt_like_music);
        historyMusicButton = view.findViewById(R.id.bt_history_music);
        historyGridView = view.findViewById(R.id.history_gw);
        moreLikeSongButton = view.findViewById(R.id.bt_more_like);
        moreHistorySongButton = view.findViewById(R.id.bt_more);
        likeGridView = view.findViewById(R.id.like_gw);
        loaclMusicButton.setOnClickListener(new MyButtonOnClickListener());
        historyMusicButton.setOnClickListener(new MyButtonOnClickListener());
        likeMusicButton.setOnClickListener(new MyButtonOnClickListener());
        moreHistorySongButton.setOnClickListener(new MyButtonOnClickListener());
        moreLikeSongButton.setOnClickListener(new MyButtonOnClickListener());
    }


    public List<Integer> initImageList() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.image);
        list.add(R.drawable.image1);
        list.add(R.drawable.image2);
        list.add(R.drawable.image3);
        list.add(R.drawable.image4);
        return list;
    }

    public Banner getBanner() {
        return banner;
    }

    private class MyButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), LocalMusicActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBinder("MusicBinder", musicBinder);
            intent.putExtra("MusicBundle", bundle);
            switch (v.getId()) {
                case R.id.bt_local_music:
                    intent.putExtra("tag", 1);
                    break;
                case R.id.bt_like_music:
                case R.id.bt_more_like:
                    intent.putExtra("tag", 2);
                    break;
                case R.id.bt_history_music:
                case R.id.bt_more:
                    intent.putExtra("tag", 3);
                    break;
                default:
                    break;
            }
            getContext().startActivity(intent);
        }
    }

}

/**
 * gridView的适配器
 */
class GridViewAdapter extends ArrayAdapter<Song> {
    private int resourceId;
    private List<Song> songList;

    public GridViewAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
        songList = objects;
        resourceId = resource;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Nullable
    @Override
    public Song getItem(int position) {
        return songList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        Song song = songList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder.textView = view.findViewById(R.id.title_grid_view_item);
            viewHolder.imageView = view.findViewById(R.id.image_grid_view_item);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Bitmap bitmap = song.loadingBitmap(song.getUrl(), 80, 80);
        if (bitmap != null) {
            viewHolder.imageView.setImageBitmap(bitmap);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.default_image);
        }
        viewHolder.textView.setText(song.getTitle());
        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}