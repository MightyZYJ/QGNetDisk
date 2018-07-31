package com.mobile.qg.qgnetdisk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;

import static android.content.ContentValues.TAG;

/**
 * Created by 11234 on 2018/7/30.
 */
public class TitleView extends RelativeLayout implements View.OnClickListener {

    public interface OnTiTleOperationListener {
        void onBack();

        void onDownload();

        void onSearch();
    }

    private OnTiTleOperationListener mOperationListener;

    public void setOnTitleOperationListener(OnTiTleOperationListener listener) {
        mOperationListener = listener;
    }

    private TYPE mType;

    public enum TYPE {
        QG,
        FOLDER
    }

    private TextView title;
    private TextView user;

    public void setTitle(String content) {
        Log.e(TAG, "setTitle: " + content);
        title.setText(content);
    }

    public void setHead(String name) {
        name = name.length() == 1 ? name : name.substring(name.length() - 2, name.length());
        user.setText(name);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_title, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        int type = typedArray.getInt(R.styleable.TitleView_type, TYPE.FOLDER.ordinal());
        if (type == 0) {
            mType = TYPE.QG;
        } else {
            mType = TYPE.FOLDER;
        }
        typedArray.recycle();
        initView();
    }

    private void initView() {
        title = findViewById(R.id.title_text);
        user = findViewById(R.id.title_user);
        findViewById(R.id.title_download).setOnClickListener(this);
        findViewById(R.id.title_search).setOnClickListener(this);
        if (mType == TYPE.QG) {
            findViewById(R.id.title_back).setVisibility(GONE);
            findViewById(R.id.title_head).setVisibility(VISIBLE);
        } else {
            ImageView back = findViewById(R.id.title_back);
            back.setVisibility(VISIBLE);
            back.setOnClickListener(this);
            findViewById(R.id.title_head).setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                if (mOperationListener != null) {
                    mOperationListener.onBack();
                }
                break;
            case R.id.title_download:
                if (mOperationListener != null) {
                    mOperationListener.onDownload();
                }
                break;
            case R.id.title_search:
                if (mOperationListener != null) {
                    mOperationListener.onSearch();
                }
                break;
        }
    }

}
