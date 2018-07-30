package com.mobile.qg.qgnetdisk.adapter;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.entity.ClientFile;
import com.mobile.qg.qgnetdisk.util.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by 11234 on 2018/7/28.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    /**
     * 接口：对外公开多选模式的打开和关闭
     */
    public interface OnMultiSelectionChangeListener {
        void OnMultiSelectionOpen();

        void OnMultiSelectionClose();
    }

    private OnMultiSelectionChangeListener mSelectionChangeListener;

    public void setOnSelectionChangeListener(OnMultiSelectionChangeListener listener) {
        this.mSelectionChangeListener = listener;
    }

    /**
     * 接口：对外公开Item的点击
     */
    public interface OnItemFileClickListener {
        void OnFileItemClick(ClientFile clientFile);

        void OnRequestBottomMenu(ClientFile clientFile);
    }

    private OnItemFileClickListener mFileClickListener;

    public void setOnFileClickListener(OnItemFileClickListener listener) {
        this.mFileClickListener = listener;
    }


    /**
     * ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox select;
        ImageView icon;
        TextView name;
        TextView length;
        TextView time;
        View operation;

        ViewHolder(View itemView) {
            super(itemView);
            select = itemView.findViewById(R.id.file_select);
            icon = itemView.findViewById(R.id.file_icon);
            name = itemView.findViewById(R.id.file_name);
            length = itemView.findViewById(R.id.file_length);
            time = itemView.findViewById(R.id.file_time);
            operation = itemView.findViewById(R.id.file_operate);
        }
    }

    /**
     * 多选、单选模式
     */
    public static final int MULTI_SELECTION = 0;
    public static final int SINGLE_SELECTION = 1;
    private int mode = SINGLE_SELECTION;

    public int selectionMode() {
        return mode;
    }

    /**
     *
     */
    private Context mContext;
    private Vibrator mVibrator;
    private ArrayList<ClientFile> mClientFileArrayList;
    private Map<Integer, Boolean> mMap;//MULTI SELECTION

    public FileAdapter(ArrayList<ClientFile> clientFileArray) {
        mClientFileArrayList = clientFileArray;
        mMap = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (mVibrator == null) {
            mVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        //点击整个item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (mode == MULTI_SELECTION) {
                    holder.select.setChecked(!mMap.containsKey(position));
                } else {
                    Toast.makeText(mContext, "文件预览功能正在开发中。。。", Toast.LENGTH_SHORT).show();
                    if (mFileClickListener != null) {
                        mFileClickListener.OnFileItemClick(mClientFileArrayList.get(position));
                    }
                }

            }
        });

        //长按整个item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                mVibrator.vibrate(80);
                if (mSelectionChangeListener != null) {
                    mMap.clear();
                    if (mode == SINGLE_SELECTION) {
                        mSelectionChangeListener.OnMultiSelectionOpen();
                        mMap.put(position, true);
                    } else {
                        mSelectionChangeListener.OnMultiSelectionClose();
                    }
                }
                mode = SINGLE_SELECTION - mode;
                notifyDataSetChanged();
                return true;
            }
        });

        //点击多选框
        holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = holder.getAdapterPosition();
                if (isChecked && !mMap.containsKey(position)) {
                    mMap.put(position, true);
                } else if (!isChecked && mMap.containsKey(position)) {
                    mMap.remove(position);
                }

                Log.e(TAG, "FileAdapter 测试Map的数据集: " + mMap.keySet());

            }
        });

        holder.operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFileClickListener != null) {
                    Log.e(TAG, "onClick: ");
                    mFileClickListener.OnRequestBottomMenu((mClientFileArrayList.get(holder.getAdapterPosition())));
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClientFile clientFile = mClientFileArrayList.get(position);
        holder.icon.setImageResource(clientFile.getIconId());
        holder.name.setText(clientFile.getFilename());
        holder.length.setText(FileUtil.transformLength(clientFile.getFilesize()));
        holder.time.setText(clientFile.getModifytime());

        CheckBox checkBox = holder.select;
        int vis = checkBox.getVisibility();
        if (mode == MULTI_SELECTION) {
            if (vis != View.VISIBLE) {
                checkBox.setVisibility(View.VISIBLE);
            }
            checkBox.setChecked(mMap.containsKey(position));
        } else {
            if (vis == View.VISIBLE) {
                checkBox.setVisibility(View.GONE);
            }
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mClientFileArrayList.size();
    }

    public void close() {
        mMap.clear();
        mode = SINGLE_SELECTION;
        notifyDataSetChanged();
    }


}
