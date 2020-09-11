package com.example.meet.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.framework.base.BaseActivity;
import com.example.framework.bmob.MyData;
import com.example.framework.utils.LogUtils;
import com.example.meet.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * FileName : Test2Activity
 * Founder  : jyt
 * Create Date : 2020/9/4 12:25 PM
 * Profile :
 */
public class Test2Activity extends BaseActivity implements View.OnClickListener {

    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test);
        initView();
    }

    private void initView() {
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt3 = (Button) findViewById(R.id.bt3);
        bt4 = (Button) findViewById(R.id.bt4);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                final MyData myData = new MyData();
                myData.setName("san");
                myData.setSex(0);
                myData.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            Log.d("test" , s);
                        }
                    }
                });

                break;
                //c602909c89
            case R.id.bt2:
                MyData data = new MyData();
                data.setObjectId("c602909c89");
                data.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {

                    }
                });
                break;
            case R.id.bt3:

                break;
            case R.id.bt4:

                break;
        }
    }
}
