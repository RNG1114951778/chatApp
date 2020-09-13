package com.example.framework.db;

import org.litepal.crud.LitePalSupport;

/**
 * FileName : NewFriend
 * Founder  : jyt
 * Create Date : 2020/9/12 3:32 PM
 * Profile :
 */
public class NewFriend extends LitePalSupport {

    //留言
    private String msg;
    //对方id
    private String userId;

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    //time
    private long saveTime;
    //状态 -1:待确认 0：统一 1 拒绝
    private int isAgree = -1;

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }


    public int getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(int isAgree) {
        this.isAgree = isAgree;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
