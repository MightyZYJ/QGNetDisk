package com.mobile.qg.qgnetdisk.entity;

import java.io.Serializable;

/**
 * 用户本地的文件显示
 * （图标）
 * Created by 11234 on 2018/7/27.
 */
public class ClientFile extends NetFile implements Serializable {

    private int iconId;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
