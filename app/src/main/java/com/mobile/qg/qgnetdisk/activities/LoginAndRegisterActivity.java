package com.mobile.qg.qgnetdisk.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.adapter.MyFragmentAdapter;
import com.mobile.qg.qgnetdisk.fragment.LoginFragment;
import com.mobile.qg.qgnetdisk.fragment.RegisterFragment;

import java.util.ArrayList;

public class LoginAndRegisterActivity extends AppCompatActivity {

    private ImageView           img_login;
    private TextView            tv_login;
    private ImageView           img_register;
    private TextView            tv_register;
    private ImageView           img_cloud;
    private ArrayList<Fragment> mFragments;
    private MyFragmentAdapter   mAdapter;
    private ViewPager           mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*隐藏顶部导航栏*/
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login_and_register);
        initView();
        onClick();

    }



    private void initView() {
        img_login = (ImageView) findViewById(R.id.img_login);
        tv_login = (TextView) findViewById(R.id.tv_login);
        img_register = (ImageView) findViewById(R.id.img_register);
        tv_register = (TextView) findViewById(R.id.tv_register);
        img_cloud = (ImageView) findViewById(R.id.img_cloud);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragments = new ArrayList<>();
        mFragments.add(new LoginFragment());
        mFragments.add(new RegisterFragment());

        mAdapter = new MyFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tv_login.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                        img_login.setVisibility(View.VISIBLE);
                        tv_register.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        img_register.setVisibility(View.GONE);

                        break;
                    case 1:
                        tv_register.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                        img_register.setVisibility(View.VISIBLE);
                        tv_login.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        img_login.setVisibility(View.GONE);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

    private void onClick() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_login.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                img_login.setVisibility(View.VISIBLE);
                tv_register.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                img_register.setVisibility(View.GONE);
                mViewPager.setCurrentItem(0);
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_register.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                img_register.setVisibility(View.VISIBLE);
                tv_login.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                img_login.setVisibility(View.GONE);
                mViewPager.setCurrentItem(1);
            }
        });
    }


}
