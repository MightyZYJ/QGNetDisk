package com.mobile.qg.qgnetdisk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.entity.ClientFile;
import com.mobile.qg.qgnetdisk.util.FileUtil;
import com.mobile.qg.qgnetdisk.util.Parameter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 11234 on 2018/7/30.
 */
public class BottomSelectionView extends RelativeLayout {

    public interface OnConfirmListener {
        void onConfirm(ArrayList<ClientFile> files);
    }

    private OnConfirmListener mConfirmListener;

    private void setOnConfirmListener(OnConfirmListener listener) {
        mConfirmListener = listener;
    }

    private final static String AMOUNT = "(*/9)";

    public void refresh(ArrayList<ClientFile> files) {
        int amount = 0;
        long size = 0L;
        for (ClientFile file : files) {
            amount++;
            size = size + file.getFilesize();
        }
        mTextSize.setText(FileUtil.transformLength(size));
        mTextAmount.setText(new Parameter(AMOUNT).setParameter(String.valueOf(amount)));
        if (amount == 0) {
            mButtonConfirm.setEnabled(false);
        } else {
            mButtonConfirm.setEnabled(true);
        }
    }

    private ArrayList<ClientFile> mFiles;

    private TextView mTextSize;
    private TextView mTextAmount;
    private Button mButtonConfirm;

    public BottomSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_multiselection, this);
        mTextSize = findViewById(R.id.multi_size);
        mTextAmount = findViewById(R.id.multi_amount);
        mButtonConfirm = findViewById(R.id.multi_confirm);

        mButtonConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null) {
                    mConfirmListener.onConfirm(mFiles);
                }
            }
        });
        mFiles = new ArrayList<>();

    }

}
