package com.example.framework.event;

/**
 * FileName : MessageEvent
 * Founder  : jyt
 * Create Date : 2020/9/13 10:09 AM
 * Profile :
 */
public class MessageEvent {

    public int getType() {
        return type;
    }

    private int type;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public MessageEvent(int type){

        this.type = type;


    }
}
