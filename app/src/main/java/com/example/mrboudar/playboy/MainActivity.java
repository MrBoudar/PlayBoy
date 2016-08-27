package com.example.mrboudar.playboy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.asiatravel.thirdlogin.ATLoginCallBack;
import com.asiatravel.thirdlogin.QQLoginAuthManager;
import com.example.mrboudar.playboy.api.SinaUserApi;
import com.example.mrboudar.playboy.api.WeiBoUser;
import com.example.mrboudar.playboy.common.Common;
import com.example.mrboudar.playboy.common.SharepreferenceUtils;
import com.example.mrboudar.playboy.core.BaseCoreActivity;
import com.example.mrboudar.playboy.model.QQUserBean;
import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import butterknife.OnClick;

public class MainActivity extends BaseCoreActivity implements QQLoginAuthManager.OverrideOnActivityResult{
    private static final String TAG = "MainActivity";
    //sina login
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    QQLoginAuthManager qqLoginAuthFactory;

    @Override
    protected void onCreateOverride(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
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


    @OnClick(R.id.weibo_login)
    void weibo_login() {
        weiboLogin();
    }

    @OnClick(R.id.qq_login)
    void qq_login() {
        qqLogin();
    }

    /***
     * 微信需要申请开通微信登录 300元审核费
     */
    private void wechatLogin() {
        final IWXAPI api = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        api.registerApp(Common.WECHAT_APP_ID);
    }

    private void qqLogin() {
        SharepreferenceUtils sharepreferenceUtils = new SharepreferenceUtils(this);
        if (!sharepreferenceUtils.getIsFirstIn()) {
            qqLoginAuthFactory = QQLoginAuthManager.getInstance();
            qqLoginAuthFactory.addOnOverrideActivityResult(this);
            qqLoginAuthFactory.registerAppId(MainActivity.this, Common.QQ_APP_ID);
            qqLoginAuthFactory.setOnLoginCallBack(new ATLoginCallBack() {
                @Override
                public void sucess(JSONObject json) {
                    if (null != json && json.length() > 5) {
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        QQUserBean userBean = gson.fromJson(json.toString(), QQUserBean.class);
                        SharepreferenceUtils sharepreferenceUtils = new SharepreferenceUtils(MainActivity.this);
                        sharepreferenceUtils.setIsFirstIn(true);
                        sharepreferenceUtils.setUserAvatar(userBean.getFigureurl_qq_2());
                        sharepreferenceUtils.setUserNick(userBean.getNickname());
                        gotoGuideActivity();
                    }
                }

                @Override
                public void faile() {
                    Toast.makeText(MainActivity.this, "授权登录失败,请重试!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            gotoGuideActivity();
        }
    }

    public void gotoGuideActivity() {
        Intent intent = new Intent(MainActivity.this, GuideActivity.class);
        startActivity(intent);
    }
    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
         qqLoginAuthFactory.superGetDataResult(requestCode, resultCode, data);
    }

    private void weiboLogin() {
        final AuthInfo authInfo = new AuthInfo(MainActivity.this, Common.SINA_APP_KEY, Common.REDIRECT_URL, null);
        mSsoHandler = new SsoHandler(MainActivity.this, authInfo);
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                // 从 Bundle 中解析 Token
                mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                //从这里获取用户输入的 电话号码信息
                String phoneNum = mAccessToken.getPhoneNum();
                if (mAccessToken.isSessionValid()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SinaUserApi userApi = new SinaUserApi(MainActivity.this, Common.SINA_APP_KEY, mAccessToken);
                            userApi.showSync(Long.parseLong(mAccessToken.getUid()));
                            userApi.show(mAccessToken.getUid(), new SinaRequestLisner());
                        }
                    }).start();
                } else {
                    // 以下几种情况，您会收到 Code：
                    // 1. 当您未在平台上注册的应用程序的包名与签名时；
                    // 2. 当您注册的应用程序包名与签名不正确时；
                    // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                    String code = bundle.getString("code");
                    String message = getResources().getString(R.string.weibo_auth_fail);
                    if (!TextUtils.isEmpty(code)) {
                        message = message + "\nObtained the code: " + code;
                    }
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.weibo_auth_cancle), Toast.LENGTH_LONG).show();
            }
        });
    }


    private class SinaRequestLisner implements RequestListener {

        @Override
        public void onComplete(String s) {
            WeiBoUser weiBoUser = WeiBoUser.parse(s);
            L.e(getClass().getSimpleName(), weiBoUser.avatar_hd);
            Toast.makeText(MainActivity.this, weiBoUser.avatar_hd, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != qqLoginAuthFactory){
            qqLoginAuthFactory.realeaseInstance();
        }
    }
}
