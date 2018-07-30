package com.mobile.qg.qgnetdisk.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.entity.User;
import com.mobile.qg.qgnetdisk.http.UserHttpHelper;
import com.mobile.qg.qgnetdisk.widget.VerificationCodeView;

public class VerificationCodeActivity extends AppCompatActivity {

    private static final String TAG = "VerificationCodeActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        final Intent intent = getIntent();
        final String flag = intent.getStringExtra("flag");

        VerificationCodeView verificationcodeview = findViewById(R.id.verificationcodeview);

        verificationcodeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {

            @Override
            public void onComplete(String content) {


                final String verifyCode = content;//验证码

                if (flag.equals("forgetPassword")) {
                    /*这里是由忘记密码跳转进来的，填完验证码后会跳转下一个活动：重设密码的活动*/

                } else if (flag.equals("register")) {
                    /*这里是由注册跳转进来的*/
                    String email = intent.getStringExtra("email");
                    String nickname = intent.getStringExtra("nickname");
                    String password = intent.getStringExtra("password");
                    Log.e(TAG, "邮箱=" + email + "昵称=" + nickname + "密码=" + password + "验证码=" + verifyCode);
                    final User user = new User();
                    user.setEmail(email);
                    user.setNickName(nickname);
                    user.setPassword(password);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int result = new UserHttpHelper().register(user, verifyCode);
                            if (result == 200) {
                                Intent successIntent = new Intent(VerificationCodeActivity.this, FileListActivity.class);
                                startActivity(successIntent);

                            }
                        }
                    }).start();


                }


            }
        });
    }


}
