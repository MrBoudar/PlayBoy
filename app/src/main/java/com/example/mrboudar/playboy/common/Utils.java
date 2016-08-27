package com.example.mrboudar.playboy.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.mrboudar.playboy.PlayBoyApplication;

import java.util.Collection;
import java.util.List;

/**
 * Created by MrBoudar on 16/7/30.
 */
public class Utils {


    public  int getScreenHeight(){
        return 0;
    }





    public static boolean isCollectionEmpty(Collection collection){
        if(null == collection){
            return false;
        }
        return collection.isEmpty();
    }

    public static void startIntent(Context context,Class cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
    }
}
