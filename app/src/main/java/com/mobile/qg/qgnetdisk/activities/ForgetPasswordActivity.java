package com.mobile.qg.qgnetdisk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.util.VerificationUtil;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText et_forget_email;
    private Button            btn_forget_verificationCode;
    private TextInputLayout   til_forget_email;
    private String            mEmail;
    private String            flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        et_forget_email = (TextInputEditText) findViewById(R.id.et_forget_email);
        btn_forget_verificationCode = (Button) findViewById(R.id.btn_forget_verificationCode);
        til_forget_email = (TextInputLayout) findViewById(R.id.til_forget_email);
        flag="forgetPassword";
        btn_forget_verificationCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget_verificationCode:
                mEmail=et_forget_email.getText().toString();
                if (VerificationUtil.EmailCorrect(mEmail,this)){
                Intent intent=new Intent(ForgetPasswordActivity.this,VerificationCodeActivity.class);
                intent.putExtra("email",mEmail);
                intent.putExtra("flag",flag);
                startActivity(intent);}
                break;
        }
    }



}
