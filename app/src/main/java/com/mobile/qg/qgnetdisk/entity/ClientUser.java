package com.mobile.qg.qgnetdisk.entity;

import android.util.Log;

import static android.support.constraint.Constraints.TAG;

/**
 * 用于本地显示的用户类
 * 单例
 * Created by 11234 on 2018/7/26.
 */
public class ClientUser extends User {

    private static ClientUser mInstance = new ClientUser();

    private ClientUser() {

    }

    public static ClientUser getInstance() {
        return mInstance;
    }

    public static void reset() {
        mInstance = new ClientUser();
    }

    public synchronized static void clone(User user) {
        mInstance.setEmail(user.getEmail());
        mInstance.setNickName(user.getNickName());
        if (user.getPassword().length() < 20) {
            //密码没有被加密
            mInstance.setPassword(user.getPassword());
        }
        mInstance.setStatus(user.getStatus());
        mInstance.setUserId(user.getUserId());
    }


}
