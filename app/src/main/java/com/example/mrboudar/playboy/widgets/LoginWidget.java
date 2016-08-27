package com.example.mrboudar.playboy.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by MrBoudar on 16/8/26.
 */
public class LoginWidget extends View{
    private int mWidth;
    private int mHeight;

    public LoginWidget(Context context) {
        this(context,null);
    }

    public LoginWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
