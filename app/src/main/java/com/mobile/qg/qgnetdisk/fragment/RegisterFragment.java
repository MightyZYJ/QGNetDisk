package com.mobile.qg.qgnetdisk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.activities.VerificationCodeActivity;
import com.mobile.qg.qgnetdisk.util.VerificationUtil;

/**
 * Created by 93922 on 2018/7/27.
 * 描述：注册页面的Fragment
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private View              mView;
    private TextInputEditText et_register_email;
    private TextInputLayout   til_register_email;
    private TextInputEditText et_register_userName;
    private TextInputLayout   til_register_userName;
    private TextInputEditText et_register_password;
    private TextInputLayout   til_register_password;
    private Button            btn_register_login;
    private Context           mContext;
    private String            email;
    private String            userName;
    private String            password;
    private boolean           successRegister ;
    private String            flag;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_register, container,false);
        initView(mView);
        return mView;
    }

    private void initView(View mView) {
        mContext=getContext();
        et_register_email = (TextInputEditText) mView.findViewById(R.id.et_register_email);
        til_register_email = (TextInputLayout) mView.findViewById(R.id.til_register_email);
        et_register_userName = (TextInputEditText) mView.findViewById(R.id.et_register_userName);
        til_register_userName = (TextInputLayout) mView.findViewById(R.id.til_register_userName);
        et_register_password = (TextInputEditText) mView.findViewById(R.id.et_register_password);
        til_register_password = (TextInputLayout) mView.findViewById(R.id.til_register_password);
        btn_register_login = (Button) mView.findViewById(R.id.btn_register_verificationCode);
        flag="register";
        btn_register_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_verificationCode:
                email=et_register_email.getText().toString();
                userName=et_register_userName.getText().toString();
                password=et_register_password.getText().toString();

                boolean emailCorrect= VerificationUtil.EmailCorrect(email,mContext);
                boolean userNameCorrect=VerificationUtil.UserNameCorrect(userName,mContext);
                boolean passwordCorrect=VerificationUtil.PasswordCorrect(password,mContext);
                //只有密码、昵称、邮箱格式都正确才能跳转活动
                successRegister=emailCorrect&&passwordCorrect&&userNameCorrect;
                if (successRegister){
                    Intent intent=new Intent(getActivity(), VerificationCodeActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("userName",userName);
                    intent.putExtra("password",password);
                    intent.putExtra("flag",flag);
                    startActivity(intent);
                }

                break;
        }
    }



}
