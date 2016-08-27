package com.example.mrboudar.playboy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import com.example.mrboudar.playboy.common.Utils;
import com.example.mrboudar.playboy.core.BaseCoreActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MrBoudar on 16/8/17.
 * 0 看图片
 * 1 看视频
 */
public class GuideActivity extends BaseCoreActivity{

    @Override
    protected void onCreateOverride(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initToolbars() {

    }

    @Override
    protected void showLoading() {

    }

    @Override
    protected void closeLoading() {

    }
    @OnClick(R.id.id_video)
    void clickVideoView(){
        Utils.startIntent(this,VideoListActivity.class);
    }
    @OnClick(R.id.id_pic)
    void clickPicView(){
        Utils.startIntent(this,HomeActivity.class);
    }
}
