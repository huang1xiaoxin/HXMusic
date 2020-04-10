package com.huangxin.hxmusic.mymusic.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huangxin.hxmusic.imageloader.ImageResizer;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.adapter.IViewHolder;

import java.util.List;

public class ImageAdapter extends BannerAdapter<Integer,ImageHolder > {
    private Context context;
    //长宽高
    int reqWidth=300;
    int reqHeight=150;
    public ImageAdapter(List<Integer> mDatas,Context context){
        //设置数据，也可以将调用的banner提供的方法，或者自己在
        //adapter中实现
        super(mDatas);
        this.context=context;
    }
    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView=new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
        .MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageHolder(imageView);
    }

    @Override
    public void onBindView(ImageHolder holder, Integer data, int position, int size) {

        Log.e("ImageAdapter",reqHeight+"   "+reqWidth);
        Bitmap bitmap= new ImageResizer().decodeSampledBitmapFromResource(context.getResources(),data,reqWidth,reqHeight);
        //设置资源Id
        holder.imageView.setImageBitmap(bitmap);
        //更据采样率压缩图片，防止OOM
    }
}
 class ImageHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public ImageHolder(@NonNull View view) {
        super(view);
        this.imageView = (ImageView) view;
    }
}