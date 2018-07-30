package com.mobile.qg.qgnetdisk.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.mobile.qg.qgnetdisk.R;

/**
 * Created by 93922 on 2018/7/27.
 * 描述：关于toast的工具类，一个方法是带图片的toast，另一个是 把toast显示中屏幕中间的
 */

public class ToastUtil {

    public static void PasswordErrorToast(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.view_toast_image,null);
        Toast toast=new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void CenterToast(Context context,String string){
        Toast toast=Toast.makeText(context,string,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }


}
