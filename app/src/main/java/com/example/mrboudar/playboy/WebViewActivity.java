package com.example.mrboudar.playboy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.example.mrboudar.playboy.core.BaseCoreActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MrBoudar on 16/8/9.
 */
public class WebViewActivity extends BaseCoreActivity {
    public static final String WV_TITILE = "title";
    public static final String WV_URL = "url";
    @Bind(R.id.id_webview)
    WebView webView;
    @Bind(R.id.id_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreateOverride(Bundle savedInstanceState) {
        initWebView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
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

    void initWebView(){
        Intent intent = getIntent();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(intent.getStringExtra(WV_URL));
        setSupportActionBar(toolbar);
        toolbar.setTitle(intent.getStringExtra(WV_TITILE));
    }
}
