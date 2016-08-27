package com.example.mrboudar.playboy.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyRoundImageView extends ImageView {
    private static final String TAG = MyRoundImageView.class.getSimpleName();

    protected Context mContext;

    //DST_IN = first draw src,then draw circleBg
    //SRC_IN = first draw circleBg,then draw src
    private static final Xfermode sXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;
    private Paint mPaint;

    public MyRoundImageView(Context context) {
        this(context, null);
    }

    public MyRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        sharedConstructor(context);
    }

    private void sharedConstructor(Context context) {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void invalidate() {
        //  mWeakBitmap = null;
        if (mMaskBitmap != null) {
            mMaskBitmap.recycle();
        }
        super.invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            try {
                // Bitmap not loaded.
                Bitmap bitmap = null;
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    // Allocation onDraw but it's ok because it will not always be called.
                    //first,draw picture
                    bitmap = Bitmap.createBitmap(getWidth(),
                            getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas bitmapCanvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, getWidth(), getHeight());
                    //将图片绘制到画布(bitmapCanvas)上
                    drawable.draw(bitmapCanvas);
                    //seconed draw  ciclebg
                    mMaskBitmap = getBitmap();
                    // draw cicle bitmap.
                    mPaint.reset();
                    mPaint.setFilterBitmap(false);
                    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                    bitmapCanvas.drawBitmap(mMaskBitmap, 0.0f, 0.0f, mPaint);
                }
                // Bitmap already loaded.
                if (bitmap != null) {
                    //重置xfermode 将xfermode后的画到系统的canvas上
                    mPaint.setXfermode(null);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
                    return;
                }
            } catch (Exception e) {
                System.gc();
            }
        }
    }

    public static Bitmap getBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        //canvas.drawOval(new RectF(0.0f, 0.0f, width, height), paint);
        float rx = Math.min(width, height) / 2;
        float ry = rx;
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, width, height), rx, ry, paint);

        return bitmap;
    }

    public Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight());
    }


    //Drawable转化为Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
