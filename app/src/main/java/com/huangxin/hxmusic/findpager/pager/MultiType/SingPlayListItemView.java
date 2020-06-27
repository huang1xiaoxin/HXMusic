package com.huangxin.hxmusic.findpager.pager.MultiType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.huangxin.hxmusic.R;
import com.huangxin.hxmusic.findpager.pager.bean.PlayListBean;
import com.huangxin.hxmusic.utils.GlideUtill;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingPlayListItemView extends ItemViewBinder<SinglePlayListData, SingPlayListItemView.ViewHolder> {
    private Context mContext;

    public SingPlayListItemView(Context context) {
        this.mContext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.single_play_list_view, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, SinglePlayListData singlePlayListData) {
        viewHolder.recyclerView.setAdapter(new RecycleViewAdapter(singlePlayListData.getPlayListBean().getPlaylist().getTracks()));
        //设置横向移动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        viewHolder.recyclerView.setLayoutManager(linearLayoutManager);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycle_view)
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
        private List<PlayListBean.PlaylistBean.TracksBean> list;

        public RecycleViewAdapter(List<PlayListBean.PlaylistBean.TracksBean> tracks) {
            list = tracks;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_view_song_item, null, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String imageUrl = list.get(position).getAl().getPicUrl();
            GlideUtill.LoadingRoundRect(mContext, imageUrl, holder.imageView, 10);
            String temp = list.get(position).getAl().getName() + "-" + list.get(position).getAr().get(0).getName();
            holder.textView.setText(temp);
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.image_view)
            ImageView imageView;

            @BindView(R.id.song_name)
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
