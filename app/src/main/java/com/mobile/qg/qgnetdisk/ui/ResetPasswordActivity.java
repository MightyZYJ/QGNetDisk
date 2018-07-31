package com.mobile.qg.qgnetdisk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.entity.ClientUser;
import com.mobile.qg.qgnetdisk.http.UserHttpHelper;
import com.mobile.qg.qgnetdisk.util.ToastUtil;
import com.mobile.qg.qgnetdisk.util.VerificationUtil;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText et_reset_first_password;
    private TextInputEditText et_reset_second_password;
    private static final String TAG = "ResetPasswordActivity";
    private final static String FLAG_RESET = "reset";
    private final static String FLAG_MODIFY = "modify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        et_reset_first_password = findViewById(R.id.et_reset_first_password);
        et_reset_second_password = findViewById(R.id.et_reset_second_password);
        findViewById(R.id.reset_confirm).setOnClickListener(this);
        findViewById(R.id.reset_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_confirm:
                final String firstNewPassword = et_reset_first_password.getText().toString();
                final String secondNewPassword = et_reset_second_password.getText().toString();
                boolean isEquals = firstNewPassword.equals(secondNewPassword);
                boolean isFormatCorrect = VerificationUtil.PasswordCorrect(firstNewPassword, this);
                if (isEquals && isFormatCorrect) {

                    Intent intent = getIntent();
                    String flag = intent.getStringExtra("flag");
                    if (flag == null) return;

                    if (flag.equals(FLAG_RESET)) {
                        final String email = intent.getStringExtra("email");
                        if (email == null) return;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int result = new UserHttpHelper().resetPassword(email, firstNewPassword);
                                if (result == 200) {
                                    notifyError("重设密码成功");
                                    startActivity(new Intent(ResetPasswordActivity.this, LoginAndRegisterActivity.class));
                                } else {
                                    notifyError("重设密码失败，请检查网络连接");
                                }
                            }
                        }).start();

                    } else if (flag.equals(FLAG_MODIFY)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int result = new UserHttpHelper().modifyPassword(firstNewPassword);
                                if (result == 200) {
                                    notifyError("重设密码成功");
                                    finish();
                                } else {
                                    notifyError("重设密码失败，请检查网络连接");
                                }
                            }
                        }).start();
                    }

                } else if (!isEquals) {
                    ToastUtil.CenterToast(this, "两次输入的密码不一致，请重新检查");
                }

                break;
            case R.id.reset_back:
                finish();
                break;
        }
    }

    private void notifyError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.CenterToast(ResetPasswordActivity.this, error);
            }
        });
    }

}
