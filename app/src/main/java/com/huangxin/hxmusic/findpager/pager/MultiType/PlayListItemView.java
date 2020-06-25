package com.huangxin.hxmusic.findpager.pager.MultiType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drakeet.multitype.ItemViewBinder;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.findpager.ui.CirCleCrop;

import org.jetbrains.annotations.NotNull;

public class PlayListItemView extends ItemViewBinder<PlayListData, PlayListItemView.ViewHolder> {

    private Context mContext;

    public PlayListItemView(Context context) {
        mContext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_row3_item_view, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, PlayListData playListData) {
        if (playListData.listBeanList != null & playListData.listBeanList.size() > 2) {
            //设置数据
            viewHolder.textView1.setText(playListData.listBeanList.get(0).getPlaylist().getName());
            viewHolder.textView2.setText(playListData.listBeanList.get(1).getPlaylist().getName());
            viewHolder.textView3.setText(playListData.listBeanList.get(2).getPlaylist().getName());
            Glide.with(mContext).load(playListData.listBeanList.get(0).getPlaylist().getCoverImgUrl()).transform(new CirCleCrop(mContext)).into(
                    viewHolder.imageView1
            );
            Glide.with(mContext).load(playListData.listBeanList.get(1).getPlaylist().getCoverImgUrl()).transform(new CirCleCrop(mContext)).into(
                    viewHolder.imageView2
            );
            Glide.with(mContext).load(playListData.listBeanList.get(2).getPlaylist().getCoverImgUrl()).transform(new CirCleCrop(mContext)).into(
                    viewHolder.imageView3
            );
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1;
        TextView textView1;
        ImageView imageView2;
        TextView textView2;
        ImageView imageView3;
        TextView textView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.image_view1);
            imageView2 = itemView.findViewById(R.id.image_view2);
            imageView3 = itemView.findViewById(R.id.image_view3);
            textView1 = itemView.findViewById(R.id.text_view_desc1);
            textView2 = itemView.findViewById(R.id.text_view_desc2);
            textView3 = itemView.findViewById(R.id.text_view_desc3);
        }
    }
}
