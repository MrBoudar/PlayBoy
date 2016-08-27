package com.example.mrboudar.playboy.widgets;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mrboudar.playboy.R;


/**
 * Created by MrBoudar on 16/7/30.
 */
public class MoMoHomeLayout extends ViewGroup {
    int time_interval = 100;
    int mScreenHeight;
    int mScreenWidth;
    //行数
    int rows = 5;
    int singleHeight = 120;
    int singleWidth;
    //列数
    int columns = 4;
    //child 总数
    int count;

    int cIndex = -1;

    int[] colors = new int[]{R.mipmap.one, R.mipmap.three, R.mipmap.two, R.mipmap.five, R.mipmap.four, R.mipmap.six, R.mipmap.seven};

    boolean layoutOnce = false;
    boolean measureOnce = false;

    public MoMoHomeLayout(Context context) {
        this(context, null);
    }

    public MoMoHomeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoMoHomeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MoMoHomeLayout);
        int attributeCount = array.getIndexCount();
        for (int i = 0; i < attributeCount; i++) {

            int attr = array.getIndex(i);
            switch (attr) {

                case R.styleable.MoMoHomeLayout_columns:
                    columns = array.getInteger(attr, columns);
                    break;

                case R.styleable.MoMoHomeLayout_rows:
                    rows = array.getInteger(attr, rows);
                    break;

                case R.styleable.MoMoHomeLayout_sigleHeight:
                    singleHeight = array.getDimensionPixelSize(attr, singleHeight);
                    break;

                case R.styleable.MoMoHomeLayout_timer:
                    time_interval = array.getInteger(attr, time_interval);
                    break;

            }

        }
        initViews();
    }

    private void initViews() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mScreenWidth = metrics.widthPixels;
        this.mScreenHeight = metrics.heightPixels;
        singleWidth = mScreenWidth / columns;
        singleHeight = singleWidth;
        columns = mScreenHeight / singleHeight;
        rows = columns;
        count = columns * rows;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.i("MoMoHomeLayout","onWindowFocusChanged");
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        Log.i("MoMoHomeLayout","onWindowVisibilityChanged");
    }

    @Override
    public void onWindowSystemUiVisibilityChanged(int visible) {
        super.onWindowSystemUiVisibilityChanged(visible);
        Log.i("MoMoHomeLayout","onWindowSystemUiVisibilityChanged");
    }


    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        Log.i("MoMoHomeLayout","onFinishTemporaryDetach");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("MoMoHomeLayout","onSizeChanged width = "+ w + " height = "+ h + "oldw = "+ oldw + "oldh = " + oldh);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.i("MoMoHomeLayout","onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i("MoMoHomeLayout","onFinishInflate");
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("MoMoHomeLayout","onMeasure");
        if(!measureOnce){
            addChildView();
            measureOnce = true;
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i("MoMoHomeLayout","onLayout");
        if(!layoutOnce){
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                int[] position = getLetTop(i);
                int mLeft = singleWidth * position[1];
                int mTop = singleHeight * position[0];
                view.layout(mLeft, mTop, mLeft + singleWidth, singleHeight + mTop);
                layoutOnce = true;
            }
        }
    }

    private void addChildView(){
        removeAllViews();
        for (int i = 0; i < count; i++) {
            addView(createImageView(getContext()));
        }
    }
    private int[] getLetTop(int childIndex) {
        int[] LeftTop = new int[2];
        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < columns; j++) {

                if (i * columns + j == childIndex) {
                    LeftTop[0] = i;
                    LeftTop[1] = j;
                    Log.i("LeftTop", "row is " + i + "columns is " + j);
                    break;
                }
            }

        }
        return LeftTop;

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("MoMoHomeLayout","onAttachedToWindow");
        post(runnable);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            changeAlpha();
        }
    };

    private void changeAlpha(){
        int randowN = (int) (Math.random() * (rows * columns - 1));
        if(cIndex == -1){
            cIndex = randowN;
            View view = getChildAt(randowN);
            ObjectAnimator animator = ObjectAnimator.ofFloat(view,
                    "alpha", 1.0F);
            animator.setDuration(500);
            animator.start();
        }else{
            View oldView = getChildAt(cIndex);
            cIndex = randowN;
            View newView = getChildAt(randowN);
            ObjectAnimator oldAnimator = ObjectAnimator.ofFloat(oldView,
                    "alpha", 0.4f);
            oldAnimator.setDuration(500);
            oldAnimator.start();
            ObjectAnimator newAnimator = ObjectAnimator.ofFloat(newView,
                    "alpha", 1.0f);
            newAnimator.setDuration(500);
            newAnimator.start();
        }
        postDelayed(runnable,2000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i("MoMoHomeLayout","onDetachedFromWindow");
    }

    private ImageView createImageView(Context context) {
        //Math.random()---> 0 - 1之间的随机小数
        int randomI = (int) ((colors.length - 1) * Math.random());
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(colors[randomI]);
        imageView.setAlpha(0.4F);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(generateParams());
        return imageView;
    }

    private ViewGroup.LayoutParams generateParams() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(singleWidth, singleHeight);
        return layoutParams;
    }

}
