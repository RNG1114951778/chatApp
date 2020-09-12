package com.example.framework.gson;

/**
 * FileName: TokenBean
 * Founder: LiuGuiLin
 * Profile: Token
 */
public class TokenBean {

    /**
     * code : 200
     * userId : 777ac52b3d
     * token : NPDq6XSHkA9wI5yVNaBKLH3wdoO3oUwLvS1M1n8GRnc=@9qef.cn.rongnav.com;9qef.cn.rongcfg.com
     */

    private int code;
    private String userId;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
