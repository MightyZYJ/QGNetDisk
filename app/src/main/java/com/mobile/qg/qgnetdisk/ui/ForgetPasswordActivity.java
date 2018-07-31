package com.mobile.qg.qgnetdisk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.http.HttpStatus;
import com.mobile.qg.qgnetdisk.http.UserHttpHelper;
import com.mobile.qg.qgnetdisk.util.VerificationUtil;

public class ForgetPasswordActivity extends AppCompatActivity implements Runnable, View.OnClickListener {

    private final static String FLAG_RESET = "reset";
    private TextInputEditText mEditText_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mEditText_Email = findViewById(R.id.et_forget_email);
        findViewById(R.id.forget_back).setOnClickListener(this);
        findViewById(R.id.forget_verificationCode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_back:
                finish();
                break;
            case R.id.forget_verificationCode:
                new Thread(this).start();
        }
    }

    @Override
    public void run() {
        String email = mEditText_Email.getText().toString();
        if (VerificationUtil.EmailCorrect(email, ForgetPasswordActivity.this)) {
            int result = new UserHttpHelper().sendResetVerifyCode(email);
            if (result == HttpStatus.SUCCESS) {
                Intent intent = new Intent(ForgetPasswordActivity.this, VerifyCodeActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("flag", FLAG_RESET);
                startActivity(intent);
            }
        }
    }

}
