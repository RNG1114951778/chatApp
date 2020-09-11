package com.example.meet.base;

import android.app.Application;
import android.os.Handler;

import com.example.framework.Framework;

/**
 * FileName : BaseApp
 * Founder  : jyt
 * Create Date : 2020/9/4 11:44 AM
 * Profile :
 */
public class BaseApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Framework.getFramework().initFramework(this);

        /**
         * Application优化
         * 1.必要的组件在程序主页去初始化
         * 2.如果组件一定要在APP中初始化，那么尽可能的延迟
         * 3.非必要的组件在子线程中初始化
         */


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Framework.getFramework().initFramework(BaseApp.this);
//            }
//        }).start();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Framework.getFramework().initFramework(BaseApp.this);
//            }
//        },2000);

    }
}
