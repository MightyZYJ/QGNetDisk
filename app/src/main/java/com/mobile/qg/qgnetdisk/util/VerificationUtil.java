package com.mobile.qg.qgnetdisk.util;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 93922 on 2018/7/28.
 * 描述：
 */

public class VerificationUtil {


    public static boolean UserNameCorrect(String userName, Context context) {
        boolean userNameCorrect = true;

        if (userName.equals("")) {
            Toast.makeText(context, "昵称不能为空", Toast.LENGTH_SHORT).show();
            userNameCorrect = false;
        } else if (userName.length() > 12) {
            Toast.makeText(context, "昵称长度不能超过十二个字", Toast.LENGTH_SHORT).show();
            userNameCorrect = false;
        }
        return userNameCorrect;
    }

    public static boolean EmailCorrect(String email, Context context) {
        boolean emailCorrect = true;

        if (email.equals("")) {
            /*til_email.setError("");*/
            Toast.makeText(context, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            emailCorrect = false;
        } else if (!verifyEmailByRegex(email)) {
            /* til_email.setError("邮箱格式错误");*/
            Toast.makeText(context, "邮箱格式错误", Toast.LENGTH_SHORT).show();
            emailCorrect = false;
        }
        return emailCorrect;

    }

    public static boolean PasswordCorrect(String password, Context context) {

        boolean passwordCorrect = true;
        String content = "";
        if (password.equals("")) {
            content = "密码不能为空";
            passwordCorrect = false;
        } else if (password.length() < 6) {
            content = "密码由6-14位的英文+数字组成";
            passwordCorrect = false;
        } else if (password.length() > 14) {
            content = "密码长度超出14位";
            passwordCorrect = false;
        } else if (!verifyPasswordByRegex(password)) {
            content = "密码只能包含英语或数字";
            passwordCorrect = false;
        }
        if (!passwordCorrect) {
            ToastUtil.CenterToast(context, content);
        }
        return passwordCorrect;
    }


    public static boolean verifyEmailByRegex(String email) {
        Pattern pattern = Pattern.compile("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean verifyPasswordByRegex(String password) {
        /*密码格式为6-16位字母或者密码*/
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
