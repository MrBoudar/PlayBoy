package com.asiatravel.thirdlogin;

import com.tencent.connect.UserInfo;

import org.json.JSONObject;

/**
 * Created by MrBoudar on 16/8/18.
 */
public interface ATLoginCallBack {
    public void sucess(JSONObject json);
    public void faile();
}
