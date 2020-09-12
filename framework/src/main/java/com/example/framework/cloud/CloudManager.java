package com.example.framework.cloud;

import android.util.Log;

import com.example.framework.manager.HttpManager;
import com.example.framework.utils.LogUtils;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import okhttp3.OkHttpClient;

/**
 * FileName : CloudManager
 * Founder  : jyt
 * Create Date : 2020/9/11 7:46 PM
 * Profile :
 */
public class CloudManager {

    private static final String TOKEN_URL = "https://api-cn.ronghub.com/user/getToken.json";
    private static final String CLOUD_kEY = "8luwapkv84zpl";
    private static final String CLOUD_SECRET = "v1XyyMToA85MMm";

    private static final String MSG_TEXT_NAME = "RC:TxtMsg";
    private static final String MSG_IMAGE_NAME = "RC:ImgMsg";
    private static final String MSG_LOCATION_NAME = "RC:LBSMsg";
    private static volatile CloudManager mInstance = null;

    private CloudManager() {


    }

    public static CloudManager getInstance() {

        if (mInstance == null) {
            synchronized (CloudManager.class) {
                if (mInstance == null) {
                    mInstance = new CloudManager();
                }
            }
        }
        return mInstance;

    }

    public void initCloud(){


    }



    public void connect(String token) {RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
        @Override
        public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
            //消息数据库打开，可以进入到主页面
            Log.d("message:", "open");
        }

        @Override
        public void onSuccess(String s) {
            //连接成功
            Log.d("message:", "success");
            CloudManager.getInstance().sendTextMessage("Test","777ac52b3");
        }

        @Override
        public void onError(RongIMClient.ConnectionErrorCode errorCode) {
            if(errorCode.equals(RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT)) {
                //从 APP 服务获取新 token，并重连
                Log.d("message:", "error");
            } else {
                //无法连接 IM 服务器，请根据相应的错误码作出对应处理
                Log.d("message:", "shibai");
            }
        }
    });
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        RongIMClient.getInstance().disconnect();
    }

    /**
     * 退出登录
     */
    public void logout() {
        RongIMClient.getInstance().logout();
    }

    public void setOnReceiveMessageListener(RongIMClient.OnReceiveMessageListener listener) {
        RongIMClient.setOnReceiveMessageListener(listener);
    }

    /**
     * 发送文本
     * @param msg
     * @param targeId
     */
    public void sendTextMessage(String msg,String targeId){
        LogUtils.i("sendTextMessage");
        TextMessage textMessage = TextMessage.obtain(msg);
        RongIMClient.getInstance().sendMessage(
                Conversation.ConversationType.PRIVATE,
                targeId,textMessage,
                null,null,
                iSendMessageCallback);

    }

    private IRongCallback.ISendMessageCallback iSendMessageCallback
            = new IRongCallback.ISendMessageCallback() {

        @Override
        public void onAttached(Message message) {
            // 消息成功存到本地数据库的回调
        }

        @Override
        public void onSuccess(Message message) {
            // 消息发送成功的回调
            LogUtils.i("sendMessage onSuccess");


        }

        @Override
        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            // 消息发送失败的回调
            LogUtils.e("sendMessage onError:" + errorCode);
        }
    };


}
