package com.example.framework.base;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import com.example.framework.utils.SpUtils;
import com.example.framework.utils.SystemUI;

public class BaseUIActivity extends BaseActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpUtils.getInstance().initSp(this);
        SystemUI.fixsystemUI(this);
    }
}
