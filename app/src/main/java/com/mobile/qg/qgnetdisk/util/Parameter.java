package com.mobile.qg.qgnetdisk.util;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by 11234 on 2018/7/28.
 */
public class Parameter {

    private String root;

    public String setParameter(String... params) {

        String[] splits = root.split("\\*");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < splits.length && i < params.length; i++) {
            stringBuilder.append(splits[i]);
            stringBuilder.append(params[i]);
        }
        Log.e(TAG, stringBuilder.toString());
        return stringBuilder.toString();
    }

    public Parameter(String root) {
        this.root = root;
    }

}
