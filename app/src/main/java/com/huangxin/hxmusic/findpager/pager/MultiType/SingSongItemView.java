package com.huangxin.hxmusic.findpager.pager.MultiType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;
import com.huangxin.hxmusic.utils.GlideUtill;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingSongItemView extends ItemViewBinder<SingSongItemData, SingSongItemView.ViewHolder> {
    private Context mContext;

    public SingSongItemView(Context context) {
        mContext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.song_row1_item_view, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, SingSongItemData singSongItemData) {
        PlayListBean.PlaylistBean.TracksBean tracksBean = singSongItemData.getmTracksBean();
        String imageUrl = tracksBean.getAl().getPicUrl();
        GlideUtill.LoadingRoundRect(mContext, imageUrl, viewHolder.imageView, 10);
        viewHolder.song.setText(tracksBean.getAl().getName());
        viewHolder.artist.setText(tracksBean.getAr().get(0).getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view)
        ImageView imageView;

        @BindView(R.id.artist_text_view)
        TextView artist;

        @BindView(R.id.song_text_view)
        TextView song;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
