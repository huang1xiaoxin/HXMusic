package com.huangxin.hxmusic.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

public class ImageResizer {
    private static final String TAG="ImageResizer";

    public Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight){
        //将BitmapFactory.Options的inJusDecodeBounds参数设计为true
        //此时BitmapFactory只会解析图片的原始宽和高的信息，并不会正真地去加载图片，所有这
        //操作时轻量级的
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,options);
        //根据采样率的规则并结合目标的View的所需大小计算出采样率inSampleSize
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        //根据采样率获得图片的bitmap
        //真正加载图片
        options.inJustDecodeBounds=false;
        return  BitmapFactory.decodeResource(res,resId,options);
    }
    public Bitmap decodeSampledBitmapFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight){
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);

    }
    public Bitmap decodeSampledBitmapArrayByte(byte[] bytes, int reqWidth, int reqHeight){
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWith, int reqHeight) {
        if (reqHeight==0||reqWith==0){
            return 1;
        }
        //图片的原始高度
        final int height=options.outHeight;
        final int width=options.outWidth;
        int inSampleSize=1;
        if(height>reqHeight||width>reqHeight){
            final  int halfHeight=height/2;
            final  int halfWidth=width/2;
            //大小以采样率的2次方形式进行递减
            //采样率必须大于1才会有效果
            while((halfHeight/inSampleSize)>=reqHeight&&
                    (halfWidth/inSampleSize)>=reqWith){
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }
}
