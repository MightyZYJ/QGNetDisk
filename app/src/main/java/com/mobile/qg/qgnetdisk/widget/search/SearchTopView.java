package com.mobile.qg.qgnetdisk.widget.search;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;

/**
 * Created by 11234 on 2018/7/28.
 */
public class SearchTopView extends RelativeLayout {

    /**
     * 接口：
     * 返回
     * 搜索
     */
    public interface OnTopActListener {
        void onReturnPressed();

        void onSearch(String key);
    }

    private OnTopActListener mTopActListener;

    public void setTopActListener(OnTopActListener listener) {
        mTopActListener = listener;
    }

    public SearchTopView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_search_top, this);

        findViewById(R.id.search_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTopActListener != null) {
                    mTopActListener.onReturnPressed();
                }
            }
        });


        final EditText editText = findViewById(R.id.search_edit);
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setCursorVisible(true);
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    editText.setCursorVisible(false);
                    if (mTopActListener != null) {
                        mTopActListener.onSearch(editText.getText().toString());
                    }
                }
                return false;
            }
        });


    }


}
