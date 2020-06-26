package com.huangxin.hxmusic.findpager.ui;
/**
 * 已丢失 因为glide自带含有
 */

/*
import java.security.MessageDigest;

public class RoundRectCrop extends BitmapTransformation {
    private float mBorderWidth;
    private  int mColor;
    private Context mContext;
    private BitmapShader.TileMode mTitleMode;
    private float mRoundRadius;
    public RoundRectCrop(Context context, int borderWidth,int mRoundRadius, int color, BitmapShader.TileMode tileMode) {
        super(context);
        mContext=context;
        mBorderWidth =dip2dx(borderWidth);
        mColor=color;
        mTitleMode=tileMode;
        this.mRoundRadius=dip2dx(mRoundRadius);
    }
    public RoundRectCrop(Context context,int mRoundRadius){
        super(context);
        mContext=context;
        this.mRoundRadius=dip2dx(mRoundRadius);
    }
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int width=toTransform.getWidth();
        int height=toTransform.getHeight();
        Bitmap toReuse=pool.get(width,height,Bitmap.Config.ARGB_8888);
        Bitmap result;
        if (toReuse!=null){
            result=toReuse;
        }else {
            result =Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        }
        createRoundRect(toTransform, width, height, result);
        if (toReuse != null && !pool.put(toReuse)) {
            //尝试将toReuse放到缓冲池去
            toReuse.recycle();
        }
        return result;
    }
/*
    /**
     * 圆角矩形
     * @param toTransform
     * @param width
     * @param height
     * @param result
     */
  /*  private void createRoundRect(Bitmap toTransform, int width, int height, Bitmap result) {
        BitmapShader shader=new BitmapShader(toTransform,mTitleMode,mTitleMode);
        Canvas canvas=new Canvas(result);
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setShader(shader);
        RectF bitmapRectF=new RectF();
        bitmapRectF.set(0.0f,0.0f,width,height);
        //画图像得
        canvas.drawRoundRect(bitmapRectF, mRoundRadius, mRoundRadius, paint);
    }

    /**
     * 带边框圆角矩形
     * @param toTransform
     * @param width
     * @param height
     * @param result
     */
  /*  private void createRoundRectWithBorder(Bitmap toTransform, int width, int height, Bitmap result) {
        float doubleBorderWidth= mBorderWidth *2.0f;
        float halfBorderWidth= mBorderWidth /2.0f;
        float dx=(width-doubleBorderWidth)/width;
        float dy=(height-doubleBorderWidth)/height;

        Matrix matrix=new Matrix();
        matrix.setScale(dx,dy);
        BitmapShader shader=new BitmapShader(toTransform,mTitleMode,mTitleMode);
        shader.setLocalMatrix(matrix);

        Canvas canvas=new Canvas(result);
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Paint borderPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(mBorderWidth);
        //如果是不设置边框得 使边框得画笔变为透明
        borderPaint.setColor(mBorderWidth !=0?mColor: Color.TRANSPARENT);

        RectF borderRectF=new RectF();
        RectF bitmapRectF=new RectF();
        borderRectF.set(halfBorderWidth,halfBorderWidth,width-halfBorderWidth,height-halfBorderWidth);
        bitmapRectF.set(0.0f,0.0f,width-doubleBorderWidth,height-doubleBorderWidth);

        float bitmapRadius = Math.max((mRoundRadius - mBorderWidth), 0.0f);
        float rectRadius = Math.max((mRoundRadius - halfBorderWidth), 0.0f);

        //画边边框
        canvas.drawRoundRect(borderRectF, rectRadius, rectRadius, borderPaint);
        //画布平移
        canvas.translate(mBorderWidth, mBorderWidth);
        //画图像得
        canvas.drawRoundRect(bitmapRectF, bitmapRadius, bitmapRadius, paint);
    }

    public RoundRectCrop(Context context, int borderWidth,int mRoundRadius,int color) {
        this(context,borderWidth,mRoundRadius,color,BitmapShader.TileMode.CLAMP);
    }

    @Override
    public String getId() {
        return "com.huangxin.hxmusic.findpager.ui.RoundRectCrop";
    }
    private  float dip2dx(int dip){
        float scale =mContext.getResources().getDisplayMetrics().density;
        return dip*scale+0.5f;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
*/