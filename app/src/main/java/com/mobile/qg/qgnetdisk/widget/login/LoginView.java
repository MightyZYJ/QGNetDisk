package com.mobile.qg.qgnetdisk.widget.login;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.adapter.ViewPagerAdapter;
import com.mobile.qg.qgnetdisk.util.VerificationUtil;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by 11234 on 2018/7/29.
 */
public class LoginView extends LinearLayout implements ViewPager.OnPageChangeListener {

    /**
     * 接口
     * 回调登陆和注册的信息
     * 登陆：邮箱，密码
     * 注册：邮箱，昵称，密码
     */
    public interface OnSubmitListener {
        void onLogin(String email, String password);

        void onForgetPassword();

        void onRegister(String email, String nickname, String password);
    }

    private OnSubmitListener mSubmitListener;

    public void setOnSubmitListener(OnSubmitListener listener) {
        mSubmitListener = listener;
    }

    private ImageView img_cloud;
    private ViewPager mViewPager;

    private ImageView[] mImageViews;
    private TextView[] mTextViews;

    private LoginPager mLoginPager;
    private RegisterPager mRegisterPager;

    public LoginView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_login_register, this);

        mImageViews = new ImageView[]{findViewById(R.id.img_login), findViewById(R.id.img_register)};
        mTextViews = new TextView[]{findViewById(R.id.tv_login), findViewById(R.id.tv_register)};

        setOnclickListener(mTextViews[0], 0);
        setOnclickListener(mTextViews[1], 1);

        img_cloud = findViewById(R.id.img_cloud);
        mViewPager = findViewById(R.id.viewpager);

        View loginPage = LayoutInflater.from(context).inflate(R.layout.layout_login, null);
        View registerPage = LayoutInflater.from(context).inflate(R.layout.layout_register, null);
        ArrayList<View> pages = new ArrayList<>();
        pages.add(loginPage);
        pages.add(registerPage);
        mViewPager.setAdapter(new ViewPagerAdapter(pages));
        mViewPager.addOnPageChangeListener(this);

        mLoginPager = new LoginPager(loginPage);
        mLoginPager.init();

        mRegisterPager = new RegisterPager(registerPage);
        mRegisterPager.init();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switchPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 设置点击翻页事件
     *
     * @param view     两个TextView
     * @param position 页码
     */
    private void setOnclickListener(View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(position);
            }
        });
    }

    /**
     * 翻页
     *
     * @param position 页码
     */
    private void switchPage(int position) {
        mTextViews[position].setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        mImageViews[position].setVisibility(View.VISIBLE);
        mTextViews[1 - position].setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        mImageViews[1 - position].setVisibility(View.INVISIBLE);
        mViewPager.setCurrentItem(position);
    }

    private class LoginPager implements OnClickListener {
        private Context mContext;
        private TextInputEditText mEditText_Email;
        private TextInputEditText mEditText_Password;
        private Button mButton_Login;
        private TextView mTextView_Forget;
        private View mView;

        LoginPager(View view) {
            mView = view;
        }

        void init() {
            mContext = getContext();
            mEditText_Email = mView.findViewById(R.id.et_email);
            mEditText_Password = mView.findViewById(R.id.et_password);
            mButton_Login = mView.findViewById(R.id.btn_login);
            mTextView_Forget = mView.findViewById(R.id.tv_forget);
            mButton_Login.setOnClickListener(this);
            mTextView_Forget.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    String email = mEditText_Email.getText().toString();
                    String password = mEditText_Password.getText().toString();
                    Log.e(TAG, "onClick: ");
                    boolean emailCorrect = VerificationUtil.EmailCorrect(email, mContext);
                    boolean passwordCorrect = VerificationUtil.PasswordCorrect(password, mContext);
                    boolean successLogin = emailCorrect && passwordCorrect;

                    if (successLogin) {
                        /*如果密码错误，按照设计师的设计图，就使用这一个带图的toast： ToastUtil.PasswordErrorToast(mContext);  */
//                        ToastUtil.CenterToast(mContext, email + password);
                        if (mSubmitListener != null) {
                            mSubmitListener.onLogin(email, password);
                        }
                    }
                    break;
                case R.id.tv_forget:
                    if (mSubmitListener != null) {
                        mSubmitListener.onForgetPassword();
                    }
                    break;
            }
        }
    }

    private class RegisterPager implements OnClickListener {
        private Context mContext;
        private View mView;
        private TextInputEditText mEditText_Email;
        private TextInputEditText mEditText_Password;
        private TextInputEditText mEditText_Nickname;
        private Button mButton_Next;

        RegisterPager(View mView) {
            this.mView = mView;
        }

        /**
         * 初始化注册页面
         * 寻找控件
         * 添加点击监听器
         */
        void init() {
            mContext = getContext();
            mEditText_Email = mView.findViewById(R.id.edit_email);
            mEditText_Password = mView.findViewById(R.id.edit_password);
            mEditText_Nickname = mView.findViewById(R.id.edit_nick);
            mButton_Next = mView.findViewById(R.id.button_next);
            mButton_Next.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String email = mEditText_Email.getText().toString();
            String nickname = mEditText_Nickname.getText().toString();
            String password = mEditText_Password.getText().toString();

            boolean emailCorrect = VerificationUtil.EmailCorrect(email, mContext);
            boolean passwordCorrect = VerificationUtil.PasswordCorrect(password, mContext);
            boolean nickNameCorrect = VerificationUtil.UserNameCorrect(nickname, mContext);
            //只有密码、昵称、邮箱格式都正确才能跳转活动
            boolean successRegister = emailCorrect && passwordCorrect && nickNameCorrect;
            if (successRegister && mSubmitListener != null) {
                mSubmitListener.onRegister(email, nickname, password);
            }
        }
    }

}