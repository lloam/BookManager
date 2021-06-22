package com.mao.bookmanage.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2021/6/11.
 */

public class ToastUtils {
    private static Toast mToast;
    public static void showToast(Context mContext,String toastText){
        if (mToast != null) {
            mToast.setText(toastText);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        } else
        {
            mToast = Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
