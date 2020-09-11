package com.example.meet.test;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.framework.base.BaseActivity;
import com.example.framework.view.TouchPictureV;
import com.example.meet.R;

/**
 * FileName : TestActivity
 * Founder  : jyt
 * Create Date : 2020/9/3 8:57 PM
 * Profile :
 */
public class TestActivity extends BaseActivity {
    private com.example.framework.view.TouchPictureV TouchPictureV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {

        TouchPictureV = (TouchPictureV) findViewById(R.id.TouchPictureV);
        TouchPictureV.setViewResultListener(new TouchPictureV.OnViewResultListener() {
            @Override
            public void onResult() {

                Toast.makeText(TestActivity.this,"验证通过",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
