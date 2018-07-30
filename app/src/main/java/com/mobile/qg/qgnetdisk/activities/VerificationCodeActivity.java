package com.mobile.qg.qgnetdisk.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.view.VerificationCodeView;

public class VerificationCodeActivity extends AppCompatActivity {

    private String            mEmail;
    private String            mUserName;
    private String            mPassword;
    private String mVerificationCode;
    private static final String TAG = "VerificationCodeActivity";
    private String              flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);


        VerificationCodeView verificationcodeview = findViewById(R.id.verificationcodeview);

        verificationcodeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(String content) {
                Intent intent=getIntent();
                mEmail=intent.getStringExtra("email");
                flag=intent.getStringExtra("flag");

                mVerificationCode=content;//验证码

                if (flag.equals("forgetPassword")){
                    /*这里是由忘记密码跳转进来的，填完验证码后会跳转下一个活动：重设密码的活动*/
                    Intent intentReset=new Intent(VerificationCodeActivity.this,ResetPasswordActivity.class);
                    intentReset.putExtra("email",mEmail);
                    startActivity(intentReset);
                }else if (flag.equals("register")){
                    /*这里是由注册跳转进来的*/
                    mUserName=intent.getStringExtra("userName");
                    mPassword=intent.getStringExtra("password");
                    Log.e(TAG,"邮箱="+mEmail+"昵称="+mUserName+"密码="+mPassword+"验证码="+mVerificationCode  );

                }



            }
        });
    }







}
