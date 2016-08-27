package com.example.mrboudar.playboy.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.mrboudar.playboy.R;

/**
 * Created by MrBoudar on 16/8/25.
 */
public class CircleLoadingView extends View {
    private static final int DEFAULT_H = 50;
    private static final int DEFAULT_W = 50;
    private static final int DEFAULT_SWEEP_SIZE = 135;
    private static final int DEFAULT_REVERT_SWEEP_SIZE = -135;
    private Paint bgPaint;
    private Paint circlePaint;
    private Paint arcPaint;
    private Paint revertArcPaint;
    private float startAngle;
    private float revertAngle;
    private int mWidth, mHeight;
    ValueAnimator valueAnimator;

    private RectF rectBg;

    private RectF rectOval;

    int rectBgColor;
    int rectBgRadius;
    int rectBgStrokeWidth;
    int cicleStrokeWidth;
    int circleLoadingSize;
    int circleLoadingSweepColor;
    int circleLoadingColor;

    public CircleLoadingView(Context context) {
        this(context,null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleLoadingView, defStyleAttr, R.style.default_circle_loadingview);
        int indexCount = typedArray.getIndexCount();
        for(int i = 0;i < indexCount;i++){
          int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.CircleLoadingView_circle_loading_color:
                    circleLoadingColor = typedArray.getColor(attr,Color.WHITE);
                    break;

                case R.styleable.CircleLoadingView_circle_loading_size:
                    circleLoadingSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,25,getResources().getDisplayMetrics()));
                    break;

                case R.styleable.CircleLoadingView_circle_loading_sweep_color:
                    circleLoadingSweepColor = typedArray.getColor(attr,Color.BLACK);
                    break;

                case R.styleable.CircleLoadingView_rect_bg_color:
                    rectBgColor = typedArray.getColor(attr,Color.BLACK);
                    break;

                case R.styleable.CircleLoadingView_rect_bg_radius:
                    rectBgRadius = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CircleLoadingView_circle_loading_stroke_width:
                    cicleStrokeWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,getResources().getDisplayMetrics()));

                    break;
                case R.styleable.CircleLoadingView_rect_bg_stroke_width:
                   rectBgStrokeWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,getResources().getDisplayMetrics()));

                    break;

            }

        }
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        initDimension();
    }

    private void initDimension() {
        rectBg = new RectF(0, 0, mWidth, mHeight);
        float cicleWidth = mWidth / 2;
        float cicleHeight = mHeight / 2;
        float left = mWidth / 2;
        float top = mHeight / 2;
        // 坐标系计算,让椭圆居中,这是小学加减法
        rectOval = new RectF(-cicleWidth / 2 + left, -cicleHeight / 2 + top, left + cicleWidth / 2, top + cicleHeight / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            mWidth = DEFAULT_W;
        } else if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            mHeight = DEFAULT_H;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawOval(canvas);
        drawArc(canvas);
        drawRevertArc(canvas);
    }

    private void drawBg(Canvas canvas) {
        bgPaint = createPaint();
        bgPaint.setColor(rectBgColor);
        bgPaint.setStrokeWidth(rectBgStrokeWidth);
        bgPaint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawRoundRect(rectBg, rectBgRadius, rectBgRadius, bgPaint);
    }

    private void drawOval(Canvas canvas) {
        circlePaint = createPaint();
        circlePaint.setStrokeWidth(cicleStrokeWidth);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(circleLoadingColor);
        canvas.drawOval(rectOval, circlePaint);
    }

    private void drawArc(Canvas canvas) {
        arcPaint = createPaint();
        arcPaint.setStrokeWidth(cicleStrokeWidth);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(circleLoadingSweepColor);
        arcPaint.setDither(true);
        canvas.drawArc(rectOval, startAngle,DEFAULT_SWEEP_SIZE, false, arcPaint);
    }

    private void drawRevertArc(Canvas canvas) {
        revertArcPaint = createPaint();
        revertArcPaint.setStrokeWidth(cicleStrokeWidth);
        revertArcPaint.setStyle(Paint.Style.STROKE);
        revertArcPaint.setColor(circleLoadingSweepColor);
        revertArcPaint.setDither(true);
        canvas.drawArc(rectOval,-revertAngle,DEFAULT_REVERT_SWEEP_SIZE, false, revertArcPaint);
    }

    private Paint createPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        return paint;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoading();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }

    private void initAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0, 1.F);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                startAngle = 360 * Float.valueOf(valueAnimator.getAnimatedValue().toString());
                revertAngle = 360 * Float.valueOf(valueAnimator.getAnimatedValue().toString());
                invalidate();
            }
        });
        valueAnimator.setDuration(1800);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }
    private void startLoading(){
        initAnimation();
        if(null != valueAnimator){
            valueAnimator.start();
        }
    }
    private void stopLoading() {
        if(null != valueAnimator){
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }
}
