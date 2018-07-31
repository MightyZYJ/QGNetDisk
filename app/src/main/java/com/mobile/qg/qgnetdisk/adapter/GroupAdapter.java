package com.mobile.qg.qgnetdisk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.entity.ClientFile;

import java.util.ArrayList;

/**
 * Created by 11234 on 2018/7/30.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    /**
     * 接口：对外公开Item的点击
     */
    public interface OnGroupClickListener {
        void OnGroupClick(ClientFile clientFile);

        void OnRequestBottomMenu(ClientFile clientFile);
    }

    private OnGroupClickListener mGroupClickListener;

    public void setOnFileClickListener(OnGroupClickListener listener) {
        mGroupClickListener = listener;
    }

    private Context mContext;
    private ArrayList<ClientFile> mClientFileArrayList;

    public GroupAdapter(ArrayList<ClientFile> mClientFileArrayList) {
        this.mClientFileArrayList = mClientFileArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.group_icon);
            name = itemView.findViewById(R.id.group_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        final ViewHolder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_group, parent, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGroupClickListener != null) {
                    mGroupClickListener.OnGroupClick(mClientFileArrayList.get(holder.getAdapterPosition()));
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClientFile file = mClientFileArrayList.get(position);
        holder.name.setText(file.getFilename());
        holder.icon.setImageResource(file.getIconId());
    }

    @Override
    public int getItemCount() {
        return mClientFileArrayList.size();
    }


}
