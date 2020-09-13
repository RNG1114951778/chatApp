package com.example.meet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.framework.SimulationData;
import com.example.framework.base.BaseUIActivity;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.entity.Constants;
import com.example.framework.gson.TokenBean;
import com.example.framework.manager.DialogManager;
import com.example.framework.manager.HttpManager;
import com.example.framework.utils.LogUtils;
import com.example.framework.utils.SpUtils;
import com.example.framework.view.DialogView;
import com.example.framework.view.LodingView;
import com.example.meet.fragment.ChatFragment;
import com.example.meet.fragment.MeFragment;
import com.example.meet.fragment.SquareFragment;
import com.example.meet.fragment.StarFragment;
import com.example.meet.service.CloudService;
import com.example.meet.ui.FirstUploadActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;


public class MainActivity extends BaseUIActivity implements View.OnClickListener {


    //星球
    private ImageView iv_star;
    private TextView tv_star;
    private LinearLayout ll_star;
    private StarFragment mStarFragment = null;
    private FragmentTransaction mStarTransaction = null;

    //广场
    private ImageView iv_square;
    private TextView tv_square;
    private LinearLayout ll_square;
    private SquareFragment mSquareFragment = null;
    private FragmentTransaction mSquareTransaction = null;

    //聊天
    private ImageView iv_chat;
    private TextView tv_chat;
    private LinearLayout ll_chat;
    private ChatFragment mChatFragment = null;
    private FragmentTransaction mChatTransaction = null;

    //我的
    private ImageView iv_me;
    private TextView tv_me;
    private LinearLayout ll_me;
    private MeFragment mMeFragment = null;
    private FragmentTransaction mMeTransaction = null;

    //跳转上传的回调用
    public static final int UPLOAD_REQUEST_CODE = 1002;

    public  Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RongIMClient.init(this,"8luwapkv84zpl");

        Log.d("message:", "start");
        setContentView(R.layout.activity_main);
        initView();

        IMUser imUser = BmobManager.getInstance().getUser();
        Toast.makeText(this, "imUser" + imUser.getMobilePhoneNumber(), Toast.LENGTH_SHORT).show();


    }

    private void initView() {

        requestPermiss();

        // mMainLayout = (FrameLayout) findViewById(R.id.mMainLayout);

        iv_star = findViewById(R.id.iv_star);
        tv_star = findViewById(R.id.tv_star);
        ll_star = findViewById(R.id.ll_star);

        iv_square = findViewById(R.id.iv_square);
        tv_square = findViewById(R.id.tv_square);
        ll_square = findViewById(R.id.ll_square);

        iv_chat = findViewById(R.id.iv_chat);
        tv_chat = findViewById(R.id.tv_chat);
        ll_chat = findViewById(R.id.ll_chat);

        iv_me = findViewById(R.id.iv_me);
        tv_me = findViewById(R.id.tv_me);
        ll_me = findViewById(R.id.ll_me);

        ll_star.setOnClickListener(this);
        ll_square.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_me.setOnClickListener(this);

        //设置文本
        tv_star.setText(getString(R.string.text_main_star));
        tv_square.setText(getString(R.string.text_main_square));
        tv_chat.setText(getString(R.string.text_main_chat));
        tv_me.setText(getString(R.string.text_main_me));


        initFragment();

        //切换默人选项卡
        checkMainTab(0);

        //检查Token
        checkToken();

        //模拟数据
       // SimulationData.testData();

    }

    /**
     * 检查Token
     * 获取Token需要三个参数
     * 1.用户ID
     * 2.头像地址
     * 3.昵称
     */
    private void checkToken() {
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            //启动容云服务
            startCloudService();

        }else {
            //1.有这三个参数
            String tokenPhoto = BmobManager.getInstance().getUser().getTokenPhoto();
            String nickName = BmobManager.getInstance().getUser().getNickName();
            if (!TextUtils.isEmpty(tokenPhoto) && !TextUtils.isEmpty(tokenPhoto)) {
                //创建Token
                createToken();
            }
            else{
                //创建上传提示框
                createUploadDialog();

            }
        }
    }

    private void startCloudService() {
        Log.d("message", "startCloudService: ");
        startService(new Intent(this, CloudService.class));

    }


    //上传提示框
    private void createUploadDialog() {

      final DialogView mUploadView = DialogManager.getInstance().initView(this,R.layout.dialog_first_upload);
       mUploadView.setCancelable(false);
       ImageView iv_go_upload = mUploadView.findViewById(R.id.iv_go_upload);

       iv_go_upload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               DialogManager.getInstance().hide(mUploadView);

               FirstUploadActivity.startActivity(MainActivity.this,RESULT_FIRST_USER);
           }
       });

       DialogManager.getInstance().show(mUploadView);


    }

    //上传Token
    private void createToken() {
        /**
         * 融云后台获取Token
         * 链接融云
         */

        HashMap<String,String> map = new HashMap<>();
        map.put("userId",BmobManager.getInstance().getUser().getObjectId());
        map.put("name",BmobManager.getInstance().getUser().getNickName());
        map.put("portraitUri",BmobManager.getInstance().getUser().getTokenPhoto());


        //通过OkHttp请求

        disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {


                //执行请求过程TokenBean.java
               String json = HttpManager.getInstance().postCloudToken(map);
                emitter.onNext(json);
                emitter.onComplete();

            }

            //线程调度

        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                       parseingCloudToken(s);
                        Log.d("thisbug", "s: "+s);
                    }
                });



        Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();

    }

    private void parseingCloudToken(String s) {
        try {
            LogUtils.i("parsingCloudToken:" + s);
            TokenBean tokenBean = new Gson().fromJson(s, TokenBean.class);
            if (tokenBean.getCode() == 200) {
                if (!TextUtils.isEmpty(tokenBean.getToken())) {
                    //保存Token
                    SpUtils.getInstance().putString(Constants.SP_TOKEN, tokenBean.getToken());
                    startCloudService();
                }
            } else if (tokenBean.getCode() == 2007) {
                Toast.makeText(this, "注册人数已达上限，请替换成自己的Key", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            LogUtils.i("parsingCloudToken:" + e.toString());
        }
    }
    /**
     * 初始化Fragment
     */

    private void initFragment() {


        //星球
        if (mStarFragment == null) {
            mStarFragment = new StarFragment();
            mStarTransaction = getSupportFragmentManager().beginTransaction();
            mStarTransaction.add(R.id.mMainLayout, mStarFragment);
            mStarTransaction.commit();
        }
        //广场
        if (mSquareFragment == null) {
            mSquareFragment = new SquareFragment();
            mSquareTransaction = getSupportFragmentManager().beginTransaction();
            mSquareTransaction.add(R.id.mMainLayout, mSquareFragment);
            mSquareTransaction.commit();
        }
        //聊天
        if (mChatFragment == null) {
            mChatFragment = new ChatFragment();
            mChatTransaction = getSupportFragmentManager().beginTransaction();
            mChatTransaction.add(R.id.mMainLayout, mChatFragment);
            mChatTransaction.commit();
        }
        //我的
        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeTransaction.add(R.id.mMainLayout, mMeFragment);
            mMeTransaction.commit();
        }


    }

    /**
     * 隐藏所有Fragment
     */


     private void hideAllFragment(FragmentTransaction transaction){

        if(mStarTransaction !=  null){
            transaction.hide(mStarFragment);
        }
         if(mChatTransaction !=  null){
             transaction.hide(mChatFragment);
         }
         if(mMeTransaction !=  null){
             transaction.hide(mMeFragment);
         }
         if(mSquareTransaction !=  null){
             transaction.hide(mSquareFragment);
         }
    }

    /**
     * 显示Fragment
     * @param fragment
     */
    public void showFragment(Fragment fragment){

        if (fragment != null) {


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
        }



    }

    /**
     * 切换主页选项卡
     * @param index
     * 0  星球
     * 1  广场
     * 2  聊天
     * 3  我的
     */
    private void checkMainTab(int index){

        switch (index){
            case 0:

                showFragment(mStarFragment);

                iv_star.setImageResource(R.drawable.img_star_p);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me);

                tv_star.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_square.setTextColor(Color.BLACK);
                tv_me.setTextColor(Color.BLACK);
                tv_chat.setTextColor(Color.BLACK);



                break;

            case 1:

                showFragment(mSquareFragment);

                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square_p);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me);

                tv_star.setTextColor(Color.BLACK);
                tv_square.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_me.setTextColor(Color.BLACK);
                tv_chat.setTextColor(Color.BLACK);
                break;

            case 2:

                showFragment(mMeFragment);

                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat);
                iv_me.setImageResource(R.drawable.img_me_p);

                tv_star.setTextColor(Color.BLACK);
                tv_square.setTextColor(Color.BLACK);
                tv_me.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_chat.setTextColor(Color.BLACK);

                break;

            case 3:


                showFragment(mChatFragment);

                iv_star.setImageResource(R.drawable.img_star);
                iv_square.setImageResource(R.drawable.img_square);
                iv_chat.setImageResource(R.drawable.img_chat_p);
                iv_me.setImageResource(R.drawable.img_me);

                tv_star.setTextColor(Color.BLACK);
                tv_square.setTextColor(Color.BLACK);
                tv_me.setTextColor(Color.BLACK);
                tv_chat.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
        }


    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if(mStarFragment != null && fragment instanceof StarFragment){

            mStarFragment = (StarFragment)fragment;

        }
        if(mSquareFragment != null && fragment instanceof SquareFragment){

            mSquareFragment = (SquareFragment) fragment;

        }
        if(mChatFragment != null && fragment instanceof ChatFragment){

            mChatFragment = (ChatFragment) fragment;

        }
        if(mMeFragment != null && fragment instanceof MeFragment){

            mMeFragment = (MeFragment) fragment;

        }
    }

    /**
     * Fragment的优化
     * 防止重叠，当应用的内存紧张的时候系统会回收Fragment对象
     * 再次进入时会重新创建Fragment
     * 非原来对象无法控制导致重叠
     *
     *
     *
     * 1.初始化Fragment
     * 2.显示Fragment
     * 3.隐藏所有的Fragment
     * 4.恢复Fragment
     *
     * 优化的手段
     *
     * @param v
     */






    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_star:

                checkMainTab(0);

                break;

            case R.id.ll_square:

                checkMainTab(1);

                break;

            case R.id.ll_me:

                checkMainTab(2);

                break;

            case R.id.ll_chat:


                checkMainTab(3);

                break;

        }


    }

    private void requestPermiss() {
        //危险权限
        request(new OnPermissionsResult() {
            @Override
            public void OnSuccess() {

            }

            @Override
            public void OnFail(List<String> noPermissions) {
                LogUtils.i("noPermissions:" + noPermissions.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == UPLOAD_REQUEST_CODE){
                checkToken();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed()) {

            disposable.dispose();
        }
    }

}