package com.example.mrboudar.playboy;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mrboudar.playboy.core.BaseCoreActivity;
import com.example.mrboudar.playboy.widgets.ATCircleTimer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MrBoudar on 16/7/31.
 */
public class SpalshActivity extends BaseCoreActivity {

    @Bind(R.id.circleTimer)
    ATCircleTimer circleTimer;

    @Bind(R.id.bgLayout)
    LinearLayout bgLayout;
    int[] girls = new int[]{R.mipmap.one, R.mipmap.three, R.mipmap.two, R.mipmap.five, R.mipmap.four, R.mipmap.six, R.mipmap.seven};

    @Override
    protected void onCreateOverride(Bundle savedInstanceState) {
        Log.i("SpalshActivity", "onCreateOverride");
        animationDone();
        randomBg();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_spalsh;
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

    private void randomBg() {
        int random = (int) (Math.random() * (girls.length - 1));
        bgLayout.setBackgroundResource(girls[random]);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("SpalshActivity", "onAttachedToWindow");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("SpalshActivity", "onWindowFocusChanged");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("SpalshActivity", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("SpalshActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("SpalshActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("SpalshActivity", "onStop");
        if (null != circleTimer) {
            circleTimer.onDetachedFromWindow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("SpalshActivity", "onDestroy");
        if (null != circleTimer) {
            circleTimer.onDetachedFromWindow();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i("SpalshActivity", "onDetachedFromWindow");
    }


    private void animationDone() {

        circleTimer.setOnProgressChangeListener(new ATCircleTimer.onProgressChangeListener() {
            @Override
            public void progressDone() {
                startActivity(new Intent(SpalshActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @OnClick(R.id.circleTimer)
    void gotoMainActivity() {
        startActivity(new Intent(SpalshActivity.this, MainActivity.class));
        finish();
    }
}
