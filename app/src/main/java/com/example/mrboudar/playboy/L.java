package com.example.mrboudar.playboy;

import android.util.Log;

/**
 * Created by MrBoudar on 16/8/7.
 */
public class L {
    private static final boolean DEBUG = true;
    public static void i(String tag,String log){
        if(DEBUG){
            Log.i(tag,log);
        }
    }
    public static void v(String tag,String log){
        if(DEBUG){
            Log.v(tag,log);
        }
    }
    public static void d(String tag,String log){
        if(DEBUG){
            Log.d(tag,log);
        }
    }
    public static void e(String tag,String log){
        if(DEBUG){
            Log.e(tag,log);
        }
    }


}
