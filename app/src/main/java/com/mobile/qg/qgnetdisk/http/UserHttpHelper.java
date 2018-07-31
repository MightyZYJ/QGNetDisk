package com.mobile.qg.qgnetdisk.http;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.qg.qgnetdisk.entity.ClientUser;
import com.mobile.qg.qgnetdisk.entity.User;
import com.mobile.qg.qgnetdisk.util.Parameter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by 11234 on 2018/7/26.
 */
public class UserHttpHelper {

    /**
     * QGNetDisk根目录
     */
    private final static String ROOT = "http://192.168.31.248:8080/qgnetdisk/";

    /**
     * User用户模块
     */
    private final static String USER = "user/";

    private final static String REGISTER = "register";
    private final static String SEND_VERIFYCODE = "sendverifycode?email=*";
    private final static String IS_REGISTER = "&isregister";
    private final static String REGISTER_POST = "{\n" +
            "  \"email\":\"用户邮箱\",\n" +
            "  \"password\":\"用户密码\",\n" +
            "  \"nickname\":\"昵称\",\n" +
            "  \"verifycode\":\"邮箱验证码\"\n" +
            "}";

    private final static String LOGIN = "login";
    private final static String LOGIN_POST = "{\n" +
            "  \"email\":\"用户邮箱\",\n" +
            "  \"password\":\"用户密码\"\n" +
            "}";

    private final static String VALIDATE = "validateverifycode";
    private final static String VALIDATE_POST = "{\n" +
            "  \"email\":\"用户邮箱\",\n" +
            "  \"verifycode\":\"验证码\"\n" +
            "}";
    private final static String RESET_PASSWORD = "resetpassword";
    private final static String RESET_PASSWORD_POST = "{\n" +
            "  \"email\":\"用户邮箱\",\n" +
            "  \"password\":\"新密码\"\n" +
            "}";

    private final static String MODIFY_NICKNAME = "modifynickname?newnickname=*&userid=*";

    private final static String MODIFY_PASSWORD = "modifypassword";
    private final static String MODIFY_PASSWORD_POST = "{\n" +
            "  \"password\":\"新密码\",\n" +
            "  \"userid\":\"用户ID\"\n" +
            "}";

    private final static String MODIFY_STATUS = "modifystatus?operatorid=*&userid=*";

    private final static String USER_LIST = "listuser";

    private final static int CONNECT_TIMEOUT = 5000;

    /**
     * 读取输入流并关闭
     *
     * @param inputStream 输入流
     * @return JSON字段
     * @throws IOException IO
     */
    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = reader.readLine()) != null) {
            Log.e(TAG, line);
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    /**
     * JSON -> ClientUser
     *
     * @param data
     */
    private void JSON2User(String data) {
        User user = new Gson().fromJson(data, User.class);
        Log.e(TAG, "JSON2User: " + user.toString());
        ClientUser.clone(user);
    }

    /**
     * 与post有关的请求
     *
     * @param user       单例本地用户
     * @param verifyCode 验证码
     * @param action     动作
     * @return 状态码
     */
    private int post(User user, String verifyCode, String action) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(ROOT + USER + action).openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            JSONObject object;
            switch (action) {
                case REGISTER://注册
                    object = new JSONObject(REGISTER_POST);
                    object.put("email", user.getEmail())
                            .put("password", user.getPassword())
                            .put("nickname", user.getNickName())
                            .put("verifycode", verifyCode);
                    break;
                case MODIFY_PASSWORD://修改密码
                    object = new JSONObject(MODIFY_PASSWORD_POST);
                    object.put("userid", user.getUserId())
                            .put("password", user.getPassword());
                    break;
                case LOGIN://登陆
                    object = new JSONObject(LOGIN_POST);
                    object.put("email", user.getEmail())
                            .put("password", user.getPassword());
                    break;
                case VALIDATE://（忘记密码）检查验证码
                    object = new JSONObject(VALIDATE_POST);
                    object.put("email", user.getEmail())
                            .put("verifycode", verifyCode);
                case RESET_PASSWORD://重置密码
                    object = new JSONObject(RESET_PASSWORD_POST);
                    object.put("email", user.getEmail())
                            .put("password", user.getPassword());
                    break;
                default:
                    return -1;
            }

            Log.e(TAG, "post: " + object.toString());

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(object.toString().getBytes());

            JSONObject jsonObject = new JSONObject(readInputStream(connection.getInputStream()));
            Log.e(TAG, "post: " + connection.getResponseCode());

            outputStream.close();
            connection.disconnect();

            int status = Integer.parseInt(jsonObject.getString("status"));
            JSONObject data = jsonObject.getJSONObject("data");
            if (status == HttpStatus.SUCCESS && data != null) {
                JSON2User(data.toString());
            }
            return status;

        } catch (IOException e) {
            e.printStackTrace();
            return 500;
        } catch (JSONException e) {
            return 600;
        }
    }

    //---------------------注册

    /**
     * 注册时发送验证码
     *
     * @param email 邮箱
     * @return 状态码200/500/501邮箱已存在
     */
    public int sendRegisterVerifyCode(String email) {
        return sendResetVerifyCode(email + IS_REGISTER);
    }

    /**
     * 注册
     *
     * @param user       本地用户
     * @param verifyCode 验证码
     * @return 状态码200/500/502验证码错误
     */
    public int register(User user, String verifyCode) {
        return post(user, verifyCode, REGISTER);
    }

    //---------------------登陆

    /**
     * 登陆
     *
     * @param user 本地用户
     * @return 状态码 200/500/503邮箱不存在/504密码错误
     */
    public int login(User user) {
        return post(user, "", LOGIN);
    }

    //---------------------忘记密码

    /**
     * 忘记密码时发送邮箱验证码
     *
     * @param email 邮箱
     * @return 状态码200/500/503邮箱不存在
     */
    public int sendResetVerifyCode(String email) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(ROOT + USER + new Parameter(SEND_VERIFYCODE).setParameter(email)).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            return Integer.parseInt(new JSONObject(readInputStream(connection.getInputStream())).getString("status"));
        } catch (IOException e) {
            return 500;
        } catch (JSONException e) {
            return 600;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 判断邮箱和密码是否匹配
     * 若发送验证码返回200则执行该方法
     *
     * @param email      邮箱
     * @param verifyCode 验证码
     * @return 状态码200/500/502邮箱不存在
     */
    public int validateVerifyCode(String email, String verifyCode) {
        User user = new User();
        user.setEmail(email);
        return post(user, verifyCode, VALIDATE);
    }

    /**
     * 登陆界面：忘记密码
     * 设置新密码
     *
     * @param newPassword 新密码
     * @return 状态码200/500/508
     */
    public int resetPassword(String email, String newPassword) {
        User user = new User();
        user.setPassword(newPassword);
        user.setEmail(email);
        return post(user, "", RESET_PASSWORD);
    }

    //---------------------修改密码

    /**
     * 用户界面：修改密码
     *
     * @param newPassword 新密码
     * @return 状态码200/500/508
     */
    public int modifyPassword(String newPassword) {
        User user = new User();
        user.setUserId(ClientUser.getInstance().getUserId());
        user.setPassword(newPassword);
        return post(user, "", MODIFY_PASSWORD);
    }

    //---------------------修改昵称

    /**
     * 用户界面：修改昵称
     *
     * @param user 本地用户(Id+Nickname)
     * @return 状态码
     */
    public int modifyNickname(User user) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(ROOT + USER +
                    new Parameter(MODIFY_NICKNAME).setParameter(user.getNickName(), String.valueOf(user.getUserId())))
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            InputStream inputStream = connection.getInputStream();
            JSONObject jsonObject = new JSONObject(readInputStream(inputStream));
            inputStream.close();
            connection.disconnect();
            JSON2User(jsonObject.getJSONObject("data").toString());
            return Integer.parseInt(jsonObject.getString("status"));
        } catch (IOException | JSONException e) {
            return 500;
        }
    }

    public int modifyNickname(String newNickname) {
        User user = new User();
        user.setUserId(ClientUser.getInstance().getUserId());
        user.setNickName(newNickname);
        return modifyNickname(user);
    }

    //---------------------修改权限

    /**
     * 用户界面：权限修改
     * 获取本地User作为操作者，参数中的User为被操作者
     *
     * @param targetUser 目标用户
     * @return 状态码200/500/507没有权限
     */
    public int modifyStatus(User targetUser) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(ROOT + USER +
                    new Parameter(MODIFY_STATUS)
                            .setParameter(String.valueOf(ClientUser.getInstance().getUserId()),
                                    String.valueOf(targetUser.getUserId())))
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            InputStream inputStream = connection.getInputStream();
            JSONObject jsonObject = new JSONObject(readInputStream(inputStream));
            inputStream.close();
            connection.disconnect();
            return Integer.parseInt(jsonObject.getString("status"));
        } catch (IOException | JSONException e) {
            return 500;
        }
    }

    //---------------------申请展示用户列表

    /**
     * 用户界面：展示用户列表
     * Nullable
     *
     * @return ArrayList User
     */
    public ArrayList<User> userList() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(ROOT + USER + USER_LIST).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            InputStream inputStream = connection.getInputStream();
            JSONObject jsonObject = new JSONObject(readInputStream(inputStream));
            inputStream.close();
            connection.disconnect();

            ArrayList<User> arrayList = new ArrayList<>();

            JSONArray userArray = jsonObject.getJSONObject("data").getJSONArray("users");
            for (int i = 0; i < userArray.length(); i++) {
                JSONObject userJSON = userArray.getJSONObject(i);
                User user = new Gson().fromJson(userJSON.toString(), User.class);
                arrayList.add(user);
            }

            return arrayList;
        } catch (IOException | JSONException e) {
            return new ArrayList<>();
        }
    }


}
