package com.example.mrboudar.playboy.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by MrBoudar on 16/8/12.
 */
public abstract class BaseCoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        onCreateOverride(savedInstanceState);
        initToolbars();
    }


    protected abstract void onCreateOverride(Bundle savedInstanceState);

    protected abstract int getLayoutResId();

    protected abstract void initToolbars();


    protected abstract void showLoading();

    protected abstract void closeLoading();

}
