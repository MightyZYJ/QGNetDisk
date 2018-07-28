package com.mobile.qg.qgnetdisk.activities;

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
import com.mobile.qg.qgnetdisk.util.VerificationUtil;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private String            firstNewPassword;
    private String            secondNewPassword;
    private TextInputEditText et_reset_first_password;
    private TextInputLayout   til_reset_first_password;
    private TextInputEditText et_reset_second_password;
    private TextInputLayout   til_reset_second_password;
    private Button            btn_reset_password;
    private String            mPassword;
    private String            mEmail;
    private String            mUserName;
    private static final String TAG = "ResetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
    }

    private void initView() {
        et_reset_first_password = (TextInputEditText) findViewById(R.id.et_reset_first_password);
        til_reset_first_password = (TextInputLayout) findViewById(R.id.til_reset_first_password);
        et_reset_second_password = (TextInputEditText) findViewById(R.id.et_reset_second_password);
        til_reset_second_password = (TextInputLayout) findViewById(R.id.til_reset_second_password);
        btn_reset_password = (Button) findViewById(R.id.btn_reset_password);
        
        btn_reset_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_password:
                firstNewPassword=et_reset_first_password.getText().toString();
                secondNewPassword =et_reset_second_password.getText().toString();
                if (firstNewPassword.equals(secondNewPassword)){

                    mPassword=firstNewPassword;
                    Intent intent=getIntent();
                    mEmail=intent.getStringExtra("email");
                    Log.e(TAG, "邮箱="+mEmail+"新密码="+mPassword );
                    /*这里获得了邮箱和新密码，接下来写网络请求的部分*/

                    if (VerificationUtil.PasswordCorrect(mPassword,this)){
                        /*只有密码的格式正确，才能进行接下来的网络请求*/
                        Toast.makeText(this, "重设密码成功", Toast.LENGTH_SHORT).show();
                    }
                    
                } else{
                    Toast.makeText(this, "两次输入的密码不一致，请重新检查", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



  
}
