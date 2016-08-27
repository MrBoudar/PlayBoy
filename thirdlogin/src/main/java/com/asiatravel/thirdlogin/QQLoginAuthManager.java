package com.asiatravel.thirdlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by MrBoudar on 16/8/18.
 */
public class QQLoginAuthManager {

    private  Tencent mTencent;

    private  UserInfo mUserInfo;

    private  Context mContext;

    private OverrideOnActivityResult overrideOnActivityResult;

    private QQLoginAuthManager() {
    }

    private  ATLoginCallBack atLoginCallBack;

    public void setOnLoginCallBack(ATLoginCallBack atLoginCallBack) {
        this.atLoginCallBack = atLoginCallBack;
    }

    private static class SigletonInstance {
        private static QQLoginAuthManager instance = new QQLoginAuthManager();
    }

    public static QQLoginAuthManager getInstance() {
        return SigletonInstance.instance;
    }

    public void realeaseInstance() {
        SigletonInstance.instance = null;
    }


    public void registerAppId(Context context, String appId) {
        mContext = context;
        mTencent = Tencent.createInstance(appId, mContext);
        qqLogin();
    }

    public void qqLogin() {
        mTencent.login((Activity) mContext, "all", loginListener);
    }

    public boolean isSessionVaild(){
        if(null != mTencent){
            return mTencent.isSessionValid();
        }
        return false;
    }
    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    if (null != atLoginCallBack) {
                        atLoginCallBack.faile();
                    }
                }

                @Override
                public void onComplete(final Object response) {
                    JSONObject json = (JSONObject) response;
                    if (null != atLoginCallBack) {
                        atLoginCallBack.sucess(json);
                    }
                }
                @Override
                public void onCancel() {
                    if (null != atLoginCallBack) {
                        atLoginCallBack.faile();
                    }
                }
            };
            mUserInfo = new UserInfo(mContext, mTencent.getQQToken());
            mUserInfo.getUserInfo(listener);
        } catch (Exception e) {
        }
    }

     private IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showNormalToast(mContext, "返回为空,登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showNormalToast(mContext, "返回为空,登录失败");
                return;
            }
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.showNormalToast(mContext, "onError: " + e.errorDetail);
        }

        @Override
        public void onCancel() {
            Util.showNormalToast(mContext, "onCancel: ");
        }
    }

    public interface OverrideOnActivityResult{
        public void onActivityResult(int requestCode, int resultCode, Intent data);
    }
    public void addOnOverrideActivityResult(OverrideOnActivityResult result){
        overrideOnActivityResult = result;
    }

    public void superGetDataResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }

}
