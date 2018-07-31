package com.mobile.qg.qgnetdisk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.entity.User;

/**
 * Created by 11234 on 2018/7/30.
 */
public class NavHeadView extends RelativeLayout {

    private final static int[] STATUS = {R.drawable.icon_userlevel_user,
            R.drawable.icon_userlevel_administrator,
            R.drawable.icon_userlevel_superadministrator};

    private final static String[] DESCRIPTION = {"普通成员", "管理员", "超级管理员"};

    private TextView mHeadName;
    private TextView mNickName;
    private ImageView mStatus;
    private TextView mStatusDescription;

    public NavHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_nav, this);

        mHeadName = findViewById(R.id.nav_head_name);
        mNickName = findViewById(R.id.nav_user_name);
        mStatus = findViewById(R.id.nav_status);
        mStatusDescription = findViewById(R.id.nav_status_description);

    }

    public void initNav(User user) {
        String name = user.getNickName();
        name = name.length() == 1 ? name : name.substring(name.length() - 2, name.length());
        mHeadName.setText(name);

        mNickName.setText(user.getNickName());
        mStatus.setImageResource(STATUS[user.getStatus() - 1]);
        mStatusDescription.setText(DESCRIPTION[user.getStatus() - 1]);
    }

}
