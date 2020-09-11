package com.example.meet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.framework.base.BaseActivity;
import com.example.framework.bmob.BmobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.entity.Constants;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.SpUtils;
import com.example.framework.view.DialogView;
import com.example.framework.view.LodingView;
import com.example.framework.view.TouchPictureV;
import com.example.meet.MainActivity;
import com.example.meet.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * FileName : LoginActivity
 * Founder  : jyt
 * Create Date : 2020/9/2 3:45 PM
 * Profile : 登陆页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_phone;
    private EditText et_code;
    private Button btn_send_code;
    private Button btn_login;
    private TextView tv_test_login;
    private TextView tv_user_agreement;
    private static final int H_TIME = 1001;

    private static  int TIME = 20;

    private DialogView mCodeView;
    private TouchPictureV mPictureV;

    private LodingView mlodingView;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage( Message msg) {
            switch (msg.what){
                case H_TIME:
                    TIME--;
                    btn_send_code.setText(TIME + "秒");
                    if(TIME > 0){
                        mHandler.sendEmptyMessageDelayed(H_TIME,1000);
                    }
                    else {

                    btn_send_code .setEnabled(true);
                    btn_send_code.setText(getString(R.string.text_login_send));
                    TIME = 20;

                    }


                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        initDialogView();

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_test_login = (TextView) findViewById(R.id.tv_test_login);
        tv_user_agreement = (TextView) findViewById(R.id.tv_user_agreement);

        btn_send_code.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        String phone = SpUtils.getInstance().getString(Constants.SP_PHONE,"");
        if(!TextUtils.isEmpty(phone)){
            et_phone.setText(phone);
        }

    }

    private void initDialogView() {

        mlodingView = new LodingView(this);
        mCodeView =  DialogManager.getInstance().initView(this,R.layout.dialog_code_view);
        mPictureV = mCodeView.findViewById(R.id.mPictureV);

        mPictureV.setViewResultListener(new TouchPictureV.OnViewResultListener() {
            @Override
            public void onResult() {
                DialogManager.getInstance().hide(mCodeView);
                sendSMS();


            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_send_code:

                DialogManager.getInstance().show(mCodeView);

                btn_send_code.setEnabled(false);
                mHandler.sendEmptyMessage(H_TIME);


                break;
            case R.id.btn_login:
                login();

                break;
        }
    }

    private void login() {

        String phone = et_phone.getText().toString().trim();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, getString(R.string.text_login_phone_null), Toast.LENGTH_SHORT).show();
            return;

        }

        String code = et_code.getText().toString().trim();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, getString(R.string.text_login_code_null), Toast.LENGTH_SHORT).show();
            return;

        }

        mlodingView.show("正在登陆......");

        BmobManager.getInstance().signOrLoginByMobilePhone(phone, code, new LogInListener<IMUser>() {
            @Override
            public void done(IMUser imUser, BmobException e) {
                if(e == null){

                    mlodingView.hide();
                    //succeed
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //手机号保留下次使用
                    SpUtils.getInstance().putString(Constants.SP_PHONE,phone);
                    finish();
                }else
                {
                    Toast.makeText(LoginActivity.this , "出错了" + e.toString(),Toast.LENGTH_SHORT);

                }
            }
        });


    }

    /**
     * 发送短信验证码
     */
    private void sendSMS() {

        //获取手机号
        String phone = et_phone.getText().toString().trim();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, getString(R.string.text_login_phone_null), Toast.LENGTH_SHORT).show();
            return;

        }


        //请求短信验证码
        BmobManager.getInstance().requestSMS(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {

                if (e == null){
                    btn_send_code.setEnabled(false);
                    mHandler.sendEmptyMessage(H_TIME);
                    Toast.makeText(LoginActivity.this, "这二维码 不要也罢", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(LoginActivity.this, "区区赘婿也敢阻挠我发二维码 再试一次", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
