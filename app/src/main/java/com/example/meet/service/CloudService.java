package com.example.meet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.framework.cloud.CloudManager;
import com.example.framework.entity.Constants;
import com.example.framework.utils.SpUtils;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * FileName : CloudService
 * Founder  : jyt
 * Create Date : 2020/9/8 10:17 AM
 * Profile :
 */
public class CloudService extends Service {




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("message:", "onCreate");


        super.onCreate();



        Log.d("message:", "After on creat");

        linkCloudServer();
    }

    private void linkCloudServer() {
        //获取Token
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        Log.d("message", "linkCloudServer: ");
        //连接服务
        CloudManager.getInstance().connect(token);
        Log.d("message", "Token: "+token);



//接收消息
        CloudManager.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                Log.d("receive:", message.toString());
                return false;
            }
        });


    }
}
