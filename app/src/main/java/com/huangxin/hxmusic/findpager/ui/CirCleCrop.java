package com.huangxin.hxmusic.findpager.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class CirCleCrop extends BitmapTransformation {


    public CirCleCrop(Context context) {
        super(context);
    }

    public CirCleCrop(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        //计算直径
        int diameter = Math.min(toTransform.getWidth(), toTransform.getHeight());
        //从缓冲池中获取可重用的Bitmap对象
        final Bitmap toReuse = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        final Bitmap result;
        if (toReuse != null) {
            result = toReuse;
        } else {
            //根据直径来创建Bitmap
            result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        }
        int sx = diameter / toTransform.getWidth();
        int sy = diameter / toTransform.getHeight();
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        /**
         *  ClAMP ： Bitmap以其内容的最后一行像素填充剩余的高的空白或者最后一列像素填充剩余宽空白
         *  MIRROR ：Bitmap以其内容以镜像的方式填充剩余空白
         *  REPEAT ：Bitmap以其内容以重复的方式填充剩余空白
         *
         */
        BitmapShader shader = new BitmapShader(toTransform, BitmapShader.TileMode.MIRROR, BitmapShader.TileMode.MIRROR);
        if (sx != 0 || sy != 0) {
            //设置缩放比例
            Matrix matrix = new Matrix();
            matrix.setTranslate(sx, sy);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        //设置抗锯齿
        paint.setAntiAlias(true);
        //计算出半径
        float radius = diameter / 2f;
        //画圆
        canvas.drawCircle(radius, radius, radius, paint);
        if (toReuse != null && !pool.put(toReuse)) {
            //尝试将toReuse放到缓冲池去
            toReuse.recycle();
        }
        return result;
    }

    @Override
    public String getId() {
        return "com.huangxin.hxmusic.findpager.ui.CircleCrop";
    }
}
