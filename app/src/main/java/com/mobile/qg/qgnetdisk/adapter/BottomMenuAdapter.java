package com.mobile.qg.qgnetdisk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;

/**
 * Created by 11234 on 2018/7/28.
 */
public class BottomMenuAdapter extends BaseAdapter {

    public final static int DOWNLOAD = 0;
    public final static int MOVE = 1;
    public final static int COPY = 2;
    public final static int RENAME = 3;
    public final static int DELETE = 4;

    private final static String[] MENU = {"下载到本地", "移动到...", "复制到...", "重命名", "删除"};

    private Context mContext;

    public BottomMenuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return MENU.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bottom, null);
        TextView operation = view.findViewById(R.id.bottom_content);
        operation.setText(MENU[position]);

        if (position == MENU.length - 1) {
            view.findViewById(R.id.bottom_divider).setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
