package com.example.mrboudar.playboy.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by MrBoudar on 16/8/15.
 */
public class PlayRecycleView extends RecyclerView implements SwipeRefreshLayout.OnRefreshListener{
    private View headerView;
    private View footerView;

    public PlayRecycleView(Context context) {
        super(context);
    }

    public PlayRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LoadingMoreFooter footer = new LoadingMoreFooter(getContext());
        footer.setProgressStyle(ProgressStyle.SysProgress);
        addView(footer);
        footer.getChildAt(0).setVisibility(View.GONE);
    }
    @Override
    public void onRefresh() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }
}
