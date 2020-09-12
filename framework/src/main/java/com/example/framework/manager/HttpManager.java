package com.example.framework.manager;

import com.example.framework.utils.SHA1;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * FileName : HttpManager
 * Founder  : jyt
 * Create Date : 2020/9/11 4:26 PM
 * Profile :
 */
public class HttpManager {
    private static final String TOKEN_URL = "https://api-cn.ronghub.com/user/getToken.json";
    private static final String CLOUD_kEY = "8luwapkv84zpl";
    private static final String CLOUD_SECRET = "v1XyyMToA85MMm";
    private static volatile HttpManager mInstance = null;




    private OkHttpClient okHttpClient;

    private HttpManager(){
        okHttpClient = new OkHttpClient();

    }

    public static HttpManager getInstance(){

        if(mInstance == null){
            synchronized (HttpManager.class){
                if(mInstance == null){
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;

    }

    public String postCloudToken(HashMap<String,String> map){


        String Timestamp = String.valueOf(System.currentTimeMillis()/1000);
        String Nonce = String.valueOf(Math.floor(Math.random()*100000));
        String Signature = SHA1.sha1(CLOUD_SECRET+Nonce+Timestamp);


        FormBody.Builder builder = new FormBody.Builder();
        for(String key :map.keySet()){
            builder.add(key,map.get(key));

        }

        RequestBody requestBody = builder.build();
        //添加签名规则
        Request request = new Request.Builder().url(TOKEN_URL)
                .addHeader("Timestamp",Timestamp)
                .addHeader("App-Key",CLOUD_kEY)
                .addHeader("Nonce",Nonce).
                addHeader("Signature", Signature)
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)
                .build();

        //同步

        try {
            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return"";
    }
}
