package com.example.meet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.bmob.BmobManager;
import com.example.framework.entity.Constants;
import com.example.framework.utils.SpUtils;
import com.example.meet.MainActivity;
import com.example.meet.R;

/**
 * FileName : IndexActivity
 * Founder  : jyt
 * Create Date : 2020/9/2 3:45 PM
 * Profile : 启动页
 */
public class IndexActivity extends AppCompatActivity {

    /**
     * 1.把启动页全屏
     * 2.延迟进入主页
     * 3.根据具体逻辑是进入主页还是引导页还是登陆页
     * 4.适配刘海屏
     *
     */

    private static final int SKIP_MAIN = 1000;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage( Message msg) {
            switch (msg.what){
                case SKIP_MAIN:
                    startMain();
                    break;
            }
            return false;
        }
    });

    /**
     * 进入主页，根据不同情况进入不同主页
     */
    private void startMain() {
        SpUtils.getInstance().initSp(this);
        //1 是不是第一次
        boolean isFirstApp = SpUtils.getInstance().getBoolean(Constants.SP_IS_FIRST_APP,true);

        Intent intent = new Intent();


        if(isFirstApp){
            //跳转到引导页
            intent.setClass(this,GuideActivity.class);
            SpUtils.getInstance().putBoolean(Constants.SP_IS_FIRST_APP,false);
        }
        else {
            //2.非第一次，判断是否登陆过
            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN,"");
            if(TextUtils.isEmpty(token)){
                if(BmobManager.getInstance().isLogin()){
                //跳转至主页
                    intent.setClass(this, MainActivity.class);

            }
            else {
                    //跳转至登陆

                    intent.setClass(this, LoginActivity.class);
                }
            }else {
                    //跳转至登陆

                    intent.setClass(this,MainActivity.class);

                }


                startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mHandler.sendEmptyMessageDelayed(SKIP_MAIN,2000);
    }

    /**
     * 优化
     *
     * 冷启动
     * 1,第一次安装，加载并启动
     * 2.启动后显示空白的窗口，getWindow（）
     * 3.创建我们的应用进程
     *
     * App：
     * 1.创建Application
     * 2.启动主线程(Main/ui Thread)
     * 3.创建应用入口/LAUNCHER
     * 4.填充ViewGroup中的View
     * 5.绘制View measure ->  layout -> draw
     *
     * 优化的两种手段：
     * 1.视图优化
     *   1.设置主题透明
     *   2.设置启动图片
     *
     *
     * 2.代码优化
     *   1.优化Application
     *   2.布局的优化，不需要繁琐的布局
     *
     *
     * 检测app Activity的启动时间
     * 1. shell
     * ActivityManager  adb shell am start -S -W com.example.meet/com.example.meet.ui.GuideActivity
     *
     *
     * ThisTime: 662  最后一个Activity的启动耗时
     * TotalTime: 662  启动一连串Activity的总耗时
     * WaitTime: 677   应用的创建时间+TotalTime
     * 应用创建时间 WaitTime = TotalTime = 15ms
     *
     *
     * 2.Log
     *
     * Log TAG = displayed
     *
     */

}
