package com.example.meet.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.example.framework.Framework;

import io.rong.imlib.RongIMClient;

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
      //  RongIMClient.init(this,"8luwapkv84zpl");
        Framework.getFramework().initFramework(this);

        /**
         * Application优化
         * 1.必要的组件在程序主页去初始化
         * 2.如果组件一定要在APP中初始化，那么尽可能的延迟
         * 3.非必要的组件在子线程中初始化
         */

        //只在主进程中初始化
        if (getApplicationInfo().packageName.equals(
                getCurProcessName(getApplicationContext()))) {
            //获取渠道
            //String flavor = FlavorHelper.getFlavor(this);
            //Toast.makeText(this, "flavor:" + flavor, Toast.LENGTH_SHORT).show();
            Framework.getFramework().initFramework(this);
        }


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
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess :
                activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
