package com.example.meet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.framework.bmob.BmobManager;
import com.example.framework.cloud.CloudManager;
import com.example.framework.db.LitePalHelper;
import com.example.framework.db.NewFriend;
import com.example.framework.entity.Constants;
import com.example.framework.event.EventManager;
import com.example.framework.event.MessageEvent;
import com.example.framework.gson.TextBean;
import com.example.framework.utils.CommonUtils;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.meet.fragment.chat.ChatRecordFragment;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * FileName : CloudService
 * Founder  : jyt
 * Create Date : 2020/9/8 10:17 AM
 * Profile :
 */
public class CloudService extends Service {
    private   Disposable disposable;



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
        Log.d("message", "Token: " + token);


//接收消息
        CloudManager.getInstance().setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {



                Log.d("receive:", message.toString());
                String objectname = message.getObjectName();
//                private static final String MSG_TEXT_NAME = "RC:TxtMsg";
//                private static final String MSG_IMAGE_NAME = "RC:ImgMsg";
//                private static final String MSG_LOCATION_NAME = "RC:LBSMsg";
                if (objectname.equals(CloudManager.MSG_TEXT_NAME)) {
                    //获取消息主体
                    TextMessage textMessage = (TextMessage) message.getContent();
                    String content = textMessage.getContent();
                    LogUtils.i("content" + content);

                    TextBean textBean = new Gson().fromJson(content, TextBean.class);

                    if (textBean.getType().equals(CloudManager.TYPE_TEXT)) {
                        MessageEvent messageEvent = new MessageEvent(EventManager.FLAG_SEND_TEXT);
                        messageEvent.setText(textBean.getMsg());

                        messageEvent.setUserId(message.getSenderUserId());
                        EventManager.post(messageEvent);


                    } else if (textBean.getType().equals(CloudManager.TYPE_ADD_FRIEND)) {
                        //添加好友消息
                        //存入本地数据库

                        //查询数据库 如果有重复的消息则不添加

                        disposable = Observable.create(new ObservableOnSubscribe<List<NewFriend>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<NewFriend>> emitter) throws Exception {
                                emitter.onNext(LitePalHelper.getInstance().queryNewFriend());
                                emitter.onComplete();

                            }
                        }).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<NewFriend>>() {
                                    @Override
                                    public void accept(List<NewFriend> newFriends) throws Exception {
                                        if (CommonUtils.isEmpty(newFriends)) {
                                            boolean isHave = false;


                                            for (int j = 0; j < newFriends.size(); j++) {
                                                NewFriend newFriend = newFriends.get(j);
                                                if (message.getSenderUserId().equals(newFriend.getId())) {
                                                    isHave = true;
                                                    break;

                                                }
                                            }
                                            //防止重复添加
                                            if (!isHave) {


                                                LitePalHelper.getInstance().saveNewFriend(textBean.getMsg(),
                                                        message.getSenderUserId());
                                            }
                                            LitePalHelper.getInstance().saveNewFriend(textBean.getMsg(),
                                                    message.getSenderUserId());
                                        }
                                    }
                                });


                    } else if (textBean.getType().equals(CloudManager.TYPE_ARGEED_FRIEND)) {


                        //添加到好友列表
                        BmobManager.getInstance().addFriend(message.getSenderUserId(), new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
                                    //刷新好友列表
                                    EventManager.post(EventManager.FLAG_UPDATE_FRIEND_LIST);
                                }
                            }
                        });
                    }


                }
                return false;
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable.isDisposed()){

            disposable.dispose();

        }
    }
}
