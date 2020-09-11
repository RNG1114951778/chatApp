package com.example.framework.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

public class SystemUI {
    //沉浸式实现

    public static void fixsystemUI(Activity mActivity){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //获取顶层view
            mActivity.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            mActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
    }
}
