package com.asiatravel.thirdlogin;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by MrBoudar on 16/8/19.
 */
public class Util {

    public static Toast toast;

    public static void showNormalToast(Context context,String res){
        if(toast == null){
            toast = Toast.makeText(context,res,Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
