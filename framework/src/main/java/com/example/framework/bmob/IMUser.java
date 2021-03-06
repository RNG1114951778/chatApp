package com.example.framework.bmob;

import cn.bmob.v3.BmobUser;

/**
 * FileName : IMUser
 * Founder  : jyt
 * Create Date : 2020/9/4 4:04 PM
 * Profile :用户类
 */
public class IMUser extends BmobUser {

    //
    private String tokenPhoto;

    private String tokenNickName;

    private String nickName;
    private String photo;


    //性别 true = 男 false = 女
    private boolean sex = true;
    //简介
    private String desc;
    //年龄
    private int age = 0;
    //生日
    private String birthday;
    //星座
    private String constellation;
    //爱好
    private String hobby;
    //单身状态
    private String status;

    public String getTokenPhoto() {
        return tokenPhoto;
    }

    public void setTokenPhoto(String tokenPhoto) {
        this.tokenPhoto = tokenPhoto;
    }

    public String getTokenNickName() {
        return tokenNickName;
    }

    public void setTokenNickName(String tokenNickName) {
        this.tokenNickName = tokenNickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IMUser{" +
                "tokenPhoto='" + tokenPhoto + '\'' +
                ", tokenNickName='" + tokenNickName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", photo='" + photo + '\'' +
                ", sex=" + sex +
                ", desc='" + desc + '\'' +
                ", age=" + age +
                ", birthday='" + birthday + '\'' +
                ", constellation='" + constellation + '\'' +
                ", hobby='" + hobby + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
