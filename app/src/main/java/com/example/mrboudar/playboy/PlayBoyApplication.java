package com.example.mrboudar.playboy;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.example.mrboudar.playboy.common.Common;

import java.io.File;

/**
 * Created by MrBoudar on 16/7/30.
 */
public class PlayBoyApplication extends Application {

    private static Context context;
    private static PlayBoyApplication application;

    public static PlayBoyApplication get(Context context) {

        return (PlayBoyApplication) context.getApplicationContext();
    }

    public static PlayBoyApplication newInstance() {
        if (null == application) {
            application = new PlayBoyApplication();
        }
        return application;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        createCatch();
    }

    private void createCatch() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), Common.PLAY_BOY_HEAD_CATCH);
            if(!file.exists()){
                file.mkdirs();
            }
        }
    }


    public static Context getAppContext() {
        return context;
    }
}
