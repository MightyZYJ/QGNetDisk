package com.mobile.qg.qgnetdisk.entity;

/**
 * 用于传输的基本User 用户实体类
 * Created by 11234 on 2018/7/26.
 */
public class User {

    /**
     * 用户ID
     */
    private int userid;

    /**
     * 用户邮箱也是用户名
     */
    private String email;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户权限字段
     */
    private int status;

    public int getUserId() {
        return userid;
    }

    public void setUserId(int userId) {
        this.userid = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", status=" + status +
                '}';
    }
}
