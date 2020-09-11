package com.example.framework.bmob;

import cn.bmob.v3.BmobObject;

/**
 * FileName : MyDate
 * Founder  : jyt
 * Create Date : 2020/9/4 11:48 AM
 * Profile :测试云函数用
 */
public class MyData extends BmobObject {

    //姓名
    private String name;
    //sex 0 nan  1 nv
    private int sex;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
