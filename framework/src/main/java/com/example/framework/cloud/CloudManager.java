package com.example.framework.cloud;

import android.net.Uri;
import android.util.Log;

import com.example.framework.manager.HttpManager;
import com.example.framework.utils.LogUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
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

    public static final String MSG_TEXT_NAME = "RC:TxtMsg";
    public static final String MSG_IMAGE_NAME = "RC:ImgMsg";
    public static final String MSG_LOCATION_NAME = "RC:LBSMsg";
    private static volatile CloudManager mInstance = null;

    //Msg Type

    //普通消息
    public static final String TYPE_TEXT = "TYPE_TEXT";
    //添加好友消息
    public static final String TYPE_ADD_FRIEND = "TYPE_ADD_FRIEND";
    //同意添加好友的消息
    public static final String TYPE_ARGEED_FRIEND = "TYPE_ARGEED_FRIEND";

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



    public void sendTextMessage(String msg, String type, String targeId){

        JSONObject jsonObject = new JSONObject();
        try{

            jsonObject.put("msg",msg);
            jsonObject.put("type",type);
            sendTextMessage(jsonObject.toString(),targeId);

        }catch (Exception e){

            e.printStackTrace();
        }
    }


    /**
     * 发送图片消息
     *
     * @param targetId 对方ID
     * @param file     文件
     */
    public void sendImageMessage(String targetId, File file) {
        ImageMessage imageMessage = ImageMessage.obtain(Uri.fromFile(file), Uri.fromFile(file), true);
        RongIMClient.getInstance().sendImageMessage(
                Conversation.ConversationType.PRIVATE,
                targetId,
                imageMessage,
                null,
                null,
                sendImageMessageCallback);
    }

    /**
     * 发送位置信息
     *
     * @param mTargetId
     * @param lat
     * @param lng
     * @param poi
     */
    public void sendLocationMessage(String mTargetId, double lat, double lng, String poi) {
        LocationMessage locationMessage = LocationMessage.obtain(lat, lng, poi, null);
        io.rong.imlib.model.Message message = io.rong.imlib.model.Message.obtain(
                mTargetId, Conversation.ConversationType.PRIVATE, locationMessage);
        RongIMClient.getInstance().sendLocationMessage(message,
                null, null, iSendMessageCallback);
    }

    private RongIMClient.SendImageMessageCallback sendImageMessageCallback = new RongIMClient.SendImageMessageCallback() {
        @Override
        public void onAttached(Message message) {
            LogUtils.i("onAttached");
        }

        @Override
        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            LogUtils.i("onError:" + errorCode);
        }

        @Override
        public void onSuccess(Message message) {
            LogUtils.i("onSuccess");
        }

        @Override
        public void onProgress(Message message, int i) {
            LogUtils.i("onProgress:" + i);
        }
    };


    public void getConversationList(RongIMClient.ResultCallback<List<Conversation>> callback) {
        RongIMClient.getInstance().getConversationList(callback);
    }

    /**
     * 加载本地的历史记录
     *
     * @param targetId
     * @param callback
     */
    public void getHistoryMessages(String targetId, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE
                , targetId, -1, 1000, callback);
    }

    /**
     * 获取服务器的历史记录
     *
     * @param targetId
     * @param callback
     */
    public void getRemoteHistoryMessages(String targetId, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getRemoteHistoryMessages(Conversation.ConversationType.PRIVATE
                , targetId, 0, 20, callback);
    }




}
