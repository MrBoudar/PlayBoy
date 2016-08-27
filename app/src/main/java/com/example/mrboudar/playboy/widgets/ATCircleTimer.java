package com.example.mrboudar.playboy.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


import com.example.mrboudar.playboy.R;

import java.lang.ref.WeakReference;

/**
 * Created by MrBoudar on 16/7/23.
 */
public class ATCircleTimer extends View {
    private static final int DEFAULT_W = 200;
    private static final int DEFAULT_H = 100;
    private int mWidth;
    private int mHeight;
    private int radius;
    private int mProgress;
    private int mSpeed;
    private int circleBg;
    private int progressColor;
    private int textSize;
    private int cirCleWidth;
    private int textColor;
    private String text;
    WeakRefHandler weakRefHandler;
    private int[] doughnutColors = {Color.parseColor("#FF0000"), Color.parseColor("#00FF00"), Color.parseColor("#0000FF")};

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public ATCircleTimer(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public ATCircleTimer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating. For example, a Button class's
     * constructor would call this version of the super class constructor and
     * supply <code>R.attr.buttonStyle</code> for <var>defStyleAttr</var>; this
     * allows the theme's button style to modify all of the base view attributes
     * (in particular its background) as well as the Button class's attributes.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     */
    public ATCircleTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.circleTimer);

        int indexCount = array.getIndexCount();

        for (int i = 0; i < indexCount; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.circleTimer_circleBg:
                    circleBg = array.getResourceId(attr,Color.BLACK);
                    break;
                case R.styleable.circleTimer_paintWidth:
                    cirCleWidth = array.getDimensionPixelSize(attr, 1);
                    break;
                case R.styleable.circleTimer_progressColor:
                    progressColor = array.getResourceId(attr, Color.RED);
                    break;
                case R.styleable.circleTimer_progress:
                    mSpeed = array.getResourceId(attr, 1);
                    break;
                case R.styleable.circleTimer_textsize:
                    textSize = array.getDimensionPixelSize(attr, 20);
                    break;
                case R.styleable.circleTimer_textcolor:
                    textColor = array.getResourceId(attr, Color.WHITE);
                    break;
                case R.styleable.circleTimer_text:
                    text = array.getString(attr);
                    break;
            }

        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.radius = h / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            this.mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_W, getResources().getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        }
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            this.mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_H,getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.mHeight, MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(this.mWidth,this.mHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(cirCleWidth);
        paint.setColor(getResources().getColor(circleBg));
        int cirRadius = mHeight / 2;
        int arcRadius = radius - cirCleWidth / 2;

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(getResources().getColor(textColor));
        textPaint.setTextSize(textSize);
        textPaint.clearShadowLayer();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textX = mWidth - textPaint.measureText(text);
//        1. 基准点是baseline
//        2. Ascent是baseline之上至字符最高处的距离
//        3. Descent是baseline之下至字符最低处的距离
//        4. Leading文档说的很含糊，其实是上一行字符的descent到下一行的ascent之间的距离 也即是换行后上行与下行的行间距
//        5. Top指的是指的是最高字符到baseline的值，即ascent的最大值
//        6. 同上，bottom指的是最下字符到baseline的值，即descent的最大值
        float textY = mHeight - (fontMetrics.bottom + fontMetrics.top);
        //draw circle ----》 bg
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, paint);
        //switch textPaint color to draw text on circle
        canvas.drawText(text, textX / 2, textY / 2, textPaint);
        //switch paint color red to draw progress
        paint.setColor(getResources().getColor(progressColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(new SweepGradient(cirCleWidth,cirCleWidth,doughnutColors,null));
        // progress ranger
        RectF rectF = new RectF(cirRadius - arcRadius, cirRadius - arcRadius, cirRadius + arcRadius, cirRadius + arcRadius);
        if (mProgress != 360) {
            canvas.drawArc(rectF, -90, mProgress, false, paint);
        } else if (mProgress == 360 && null != mOnProgressChangeListener) {
            canvas.drawArc(rectF, -90, mProgress, false, paint);
            mOnProgressChangeListener.progressDone();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        weakRefHandler = new WeakRefHandler(ATCircleTimer.this);
        weakRefHandler.post(progressRunnable);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(null != weakRefHandler){
            weakRefHandler.removeCallbacksAndMessages(null);
        }
        Log.i("ATCircleTimer","onDetachedFromWindow");
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mProgress != 360 && mProgress < 360) {
                mProgress += mSpeed;
                weakRefHandler.postDelayed(this, 10);
                weakRefHandler.sendMessage(weakRefHandler.obtainMessage());
            } else if (mProgress >= 360) {
                mProgress = 360;
            }
        }
    };
    private static class WeakRefHandler extends Handler {
        WeakReference<ATCircleTimer> weakReference;

        WeakRefHandler(ATCircleTimer circleTimer) {
            weakReference = new WeakReference<ATCircleTimer>(circleTimer);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != weakReference) {
                ATCircleTimer circleTimer = weakReference.get();
                circleTimer.postInvalidate();
            }
        }
    }

    private onProgressChangeListener mOnProgressChangeListener;

    public interface onProgressChangeListener {
        void progressDone();
    }

    public void setOnProgressChangeListener(onProgressChangeListener onProgressChangeListener) {
        this.mOnProgressChangeListener = onProgressChangeListener;
    }

}
