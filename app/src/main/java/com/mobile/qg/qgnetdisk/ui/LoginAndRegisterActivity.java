package com.mobile.qg.qgnetdisk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.entity.User;
import com.mobile.qg.qgnetdisk.http.HttpStatus;
import com.mobile.qg.qgnetdisk.http.UserHttpHelper;
import com.mobile.qg.qgnetdisk.widget.login.LoginView;

public class LoginAndRegisterActivity extends AppCompatActivity {

    private static final String TAG = "LoginAndRegisterActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        /*隐藏顶部导航栏*/
        getSupportActionBar().hide();

        LoginView loginView = findViewById(R.id.login_view);
        loginView.setOnSubmitListener(new LoginView.OnSubmitListener() {
            @Override
            public void onLogin(String email, String password) {
                final User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result = new UserHttpHelper().login(user);
                        if (result == HttpStatus.SUCCESS) {
                            Intent intent = new Intent(LoginAndRegisterActivity.this, FileListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).start();
            }

            @Override
            public void onForgetPassword() {
                Intent intent = new Intent(LoginAndRegisterActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRegister(final String email, final String nickname, final String password) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result = new UserHttpHelper().sendVerifyCode(email);
                        if (result == HttpStatus.SUCCESS) {
                            Intent intent = new Intent(LoginAndRegisterActivity.this, VerificationCodeActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("nickname", nickname);
                            intent.putExtra("password", password);
                            intent.putExtra("flag", "register");
                            startActivity(intent);
                        }
                    }
                }).start();
            }
        });

    }
}
