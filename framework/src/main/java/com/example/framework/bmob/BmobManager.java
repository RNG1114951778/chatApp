package com.example.framework.bmob;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.framework.Framework;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * FileName : BmobManager
 * Founder  : jyt
 * Create Date : 2020/9/4 11:30 AM
 * Profile :Bmob管理类
 */
public class BmobManager {

    private volatile static BmobManager mInstance = null;
    private static final String BMOB_SDK_ID = "e1a8e2c718ff577c1de8a8991a11a833";
   //private static final String BMOB_SDK_ID = "f8efae5debf319071b44339cf51153fc";

    private BmobManager() {


    }

    public static BmobManager getInstance() {
        if (mInstance == null) {
            synchronized (BmobManager.class) {
                if (mInstance == null) {
                    mInstance = new BmobManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化Bmob
     * @param mContext
     */
    public void initBmob(Context mContext){

        Bmob.initialize(mContext,BMOB_SDK_ID);
    }

    /**
     * 获取本地对象
     * @return
     */
    public IMUser getUser(){
        return BmobUser.getCurrentUser(IMUser.class);
    }

    /**
     * 发送短信验证码
     * @param phone 手机号码
     * @param listener 回调
     */
    public void requestSMS(String phone, QueryListener<Integer> listener){
        BmobSMS.requestSMSCode(phone,"",listener);

    }

    /**
     * 通过手机号注册或登录
     * @param phone
     * @param code
     * @param listener
     */
    public void signOrLoginByMobilePhone(String phone, String code, LogInListener<IMUser> listener) {


        BmobUser.signOrLoginByMobilePhone(phone, code, listener);

    }

    public boolean isLogin() {
        return BmobUser.isLogin();
    }

    public void uploadFirstPhoto(final String nickName, File file, final OnUploadPhotoListner listener) {
        /**
         * 1.上传文件拿到地址
         * 2.更新用户信息
         */
        final IMUser imUser = getUser();
        //独立域名 设置 - 应用配置 - 独立域名 一年/100
        //免费的办法： 使用我的Key
        //你如果怕数据冲突
        //解决办法：和我的类名不一样即可
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //上传成功
                    imUser.setNickName(nickName);
                    imUser.setPhoto(bmobFile.getFileUrl());

                    imUser.setTokenNickName(nickName);
                    imUser.setTokenPhoto(bmobFile.getFileUrl());
                    Log.d("wtf", "done: ");
                    //更新用户信息
                    imUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                listener.OnUpdateDone();
                            } else {
                                listener.OnupdateFalse(e);
                            }
                        }
                    });
                } else {
                    listener.OnupdateFalse(e);
                }
            }
        });
    }


    public interface OnUploadPhotoListner{
        void OnUpdateDone();
        void OnupdateFalse(BmobException e);
    }

    public void queryPhoneUsr(String phone, FindListener<IMUser> listener){

        baseQuery("mobilePhoneNumber",phone,listener);

    }

    public void queryAllUser (FindListener<IMUser> listener){
        BmobQuery<IMUser> query = new BmobQuery<>();
        query.findObjects(listener);
    }

    public void baseQuery(String key,String values, FindListener<IMUser> listener){

        BmobQuery<IMUser> query = new BmobQuery<>();
        query.addWhereEqualTo(key,values);
        query.findObjects(listener);
    }
}
