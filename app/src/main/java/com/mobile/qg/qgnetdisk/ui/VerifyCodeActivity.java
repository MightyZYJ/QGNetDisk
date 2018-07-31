package com.mobile.qg.qgnetdisk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.entity.User;
import com.mobile.qg.qgnetdisk.http.HttpStatus;
import com.mobile.qg.qgnetdisk.http.UserHttpHelper;
import com.mobile.qg.qgnetdisk.util.ToastUtil;
import com.mobile.qg.qgnetdisk.widget.VerificationCodeView;

/**
 * 注册时带着flag email nickname password 进来
 * 忘记密码时带着flag email进来
 */
public class VerifyCodeActivity extends AppCompatActivity {

    private static final String TAG = "验证码活动";
    private final static String FLAG_RESET = "reset";
    private final static String FLAG_REGISTER = "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        final Intent intent = getIntent();
        final String flag = intent.getStringExtra("flag");

        ((VerificationCodeView) findViewById(R.id.verificationcodeview)).setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {

            @Override
            public void onComplete(String content) {
                final String verifyCode = content;//验证码
                if (flag.equals(FLAG_RESET)) {
                    /*这里是由忘记密码跳转进来的，填完验证码后会跳转下一个活动：重设密码的活动*/
                    final String email = intent.getStringExtra("email");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int result = new UserHttpHelper().validateVerifyCode(email, verifyCode);
                            if (result == HttpStatus.SUCCESS) {
                                Intent resetIntent = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                                resetIntent.putExtra("flag", FLAG_RESET);
                                resetIntent.putExtra("email", email);
                                startActivity(resetIntent);
                            } else if (result == HttpStatus.VERIFYCODE_WRONG) {
                                notifyError("验证码错误");
                            } else {
                                notifyError("请检查网络连接");
                            }
                        }
                    }).start();

                } else if (flag.equals(FLAG_REGISTER)) {
                    Log.e(TAG, "onComplete: register");
                    /*这里是由注册跳转进来的*/
                    String email = intent.getStringExtra("email");
                    String nickname = intent.getStringExtra("nickname");
                    String password = intent.getStringExtra("password");

                    if (email == null || nickname == null || password == null) {
                        Log.e(TAG, "onComplete: 验证码活动 有值为空");
                        return;
                    }
                    Log.e(TAG, "邮箱=" + email + "昵称=" + nickname + "密码=" + password + "验证码=" + verifyCode);

                    final User user = new User();
                    user.setEmail(email);
                    user.setNickName(nickname);
                    user.setPassword(password);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int result = new UserHttpHelper().register(user, verifyCode);
                            if (result == HttpStatus.SUCCESS) {
                                Intent successIntent = new Intent(VerifyCodeActivity.this, FileListActivity.class);
                                startActivity(successIntent);
                            } else if (result == HttpStatus.EMAIL_EXIST) {
                                notifyError("邮箱已被注册");
                            } else if (result == HttpStatus.VERIFYCODE_WRONG) {
                                notifyError("验证码错误");
                            } else {
                                notifyError("请检查网络连接");
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private void notifyError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.CenterToast(VerifyCodeActivity.this, error);
            }
        });
    }

}
