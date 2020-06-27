package com.huangxin.hxmusic.findpager.pager.MultiType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.drakeet.multitype.ItemViewBinder;
import com.huangxin.hxmusic.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TitleItemView extends ItemViewBinder<TitleItemData, TitleItemView.ViewHolder> {
    private Context mContext;

    public TitleItemView(Context context) {
        mContext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.title_item_view, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, TitleItemData titleItemData) {
        viewHolder.textView.setText(titleItemData.getmTitle());
        viewHolder.button.setText(titleItemData.getMoreText());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title)
        TextView textView;

        @BindView(R.id.bt_more)
        Button button;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
