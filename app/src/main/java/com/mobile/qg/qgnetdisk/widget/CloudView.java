package com.mobile.qg.qgnetdisk.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by 11234 on 2018/7/30.
 */
public class CloudView extends android.support.v7.widget.AppCompatImageView {


    public CloudView(Context context) {
        super(context);
        startAnimator();
    }

    public CloudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        startAnimator();
    }

    public CloudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startAnimator();
    }

    private void startAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        animator.setDuration(30000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.start();
    }


}
