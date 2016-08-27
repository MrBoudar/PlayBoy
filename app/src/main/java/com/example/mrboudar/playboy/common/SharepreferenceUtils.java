package com.example.mrboudar.playboy.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MrBoudar on 16/8/19.
 */
public class SharepreferenceUtils {

    public static final String SHARE_APP_NAME = "share_app_name";

    public static final String IS_FIRST_IN = "is_first_in";

    public static final String USER_NICK = "user_nick";

    public static final String USER_AVATAR = "user_avatar";

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;
    public SharepreferenceUtils(Context context){
        sharedPreferences = context.getSharedPreferences(SHARE_APP_NAME,Context.MODE_PRIVATE);
        editor =  sharedPreferences.edit();
    }
    public void setIsFirstIn(boolean isFirstIn){
        editor.putBoolean(IS_FIRST_IN,isFirstIn).commit();
    }
    public void setUserNick(String userNick){
        editor.putString(USER_NICK,userNick).commit();
    }
    public void setUserAvatar(String userAvatar){
        editor.putString(USER_AVATAR,userAvatar).commit();
    }
    public boolean getIsFirstIn(){
        return sharedPreferences.getBoolean(IS_FIRST_IN,false);
    }
    public String  getUserNick(){
        return sharedPreferences.getString(USER_NICK,"");
    }
    public String  getUserAvatar(){
        return sharedPreferences.getString(USER_AVATAR,"");
    }
}
