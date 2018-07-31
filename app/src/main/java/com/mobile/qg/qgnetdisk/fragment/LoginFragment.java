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
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.activities.ForgetPasswordActivity;
import com.mobile.qg.qgnetdisk.util.ToastUtil;
import com.mobile.qg.qgnetdisk.util.VerificationUtil;

/**
 * Created by 93922 on 2018/7/27.
 * 描述：登录界面的Fragment
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private View      mView;
    private Context   mContext;
    private TextInputEditText et_email;
    private TextInputLayout   til_email;
    private TextInputEditText et_password;
    private TextInputLayout   til_password;
    private Button            btn_login;
    private TextView          tv_forget;
    private  boolean successLogin;
    private String            email;
    private String            password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_login, container, false);
        initView(mView);
        return mView;
    }

    private void initView(View mView) {
        mContext=getContext();
        et_email = (TextInputEditText) mView.findViewById(R.id.et_email);
        til_email = (TextInputLayout) mView.findViewById(R.id.til_email);
        et_password = (TextInputEditText) mView.findViewById(R.id.et_password);
        til_password = (TextInputLayout) mView.findViewById(R.id.til_password);
        btn_login = (Button) mView.findViewById(R.id.btn_login);
        tv_forget = (TextView) mView.findViewById(R.id.tv_forget);

        btn_login.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                /*各种判断*/
                password=et_password.getText().toString();
                email=et_email.getText().toString();
                successLogin=true;
                boolean emailCorrect= VerificationUtil.EmailCorrect(email,mContext);
                boolean passwordCorrect=VerificationUtil.PasswordCorrect(password,mContext);
                successLogin=emailCorrect&&passwordCorrect;

                if (successLogin){
                    /*这里已经获取到密码和邮箱，可以写网络请求的部分了*/
                    /*参数 ↓ */
                    /*邮箱：email */
                    /*密码： password*/
                    /*如果密码错误，按照设计师的设计图，就使用这一个带图的toast： ToastUtil.PasswordErrorToast(mContext);  */
                    ToastUtil.CenterToast(mContext,email+password);
                }
                break;
            case R.id.tv_forget:
                Intent intentToForget=new Intent(getActivity(), ForgetPasswordActivity.class);
                startActivity(intentToForget);
                break;
        }
    }





}
