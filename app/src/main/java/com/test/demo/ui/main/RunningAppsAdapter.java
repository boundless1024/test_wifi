package com.test.demo.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.demo.R;
import com.test.demo.bean.AppInfoItem;

import java.util.List;

/**
 * 说明：//TODO
 * 文件名称：RunningAppsAdapter
 * 创建者: hallo
 * 邮箱: hallo@xxx.xx
 * 时间: 2021/3/10 3:46 PM
 * 版本：V1.0.0
 */
public class RunningAppsAdapter extends RecyclerView.Adapter<RunningAppsAdapter.VH> {

    private List<AppInfoItem> appInfoItemList;

    public RunningAppsAdapter(List<AppInfoItem> appInfoItemList) {
        this.appInfoItemList = appInfoItemList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, null);
        childView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new VH(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        AppInfoItem appInfoItem = appInfoItemList.get(position);

        holder.ivIcon.setImageDrawable(appInfoItem.getIcon());
        holder.tvName.setText(appInfoItem.getAppName());
        holder.tvPkg.setText(appInfoItem.getPkg());
        long size = Long.parseLong(appInfoItem.getMemSize()) * 1000L;
        holder.tvSize.setText(Formatter.formatFileSize(holder.tvSize.getContext(), size));


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", appInfoItem.getPkg(), null));
            v.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return appInfoItemList.size();
    }

    public static class VH extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvSize;
        TextView tvPkg;
        ImageView ivIcon;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvSize = itemView.findViewById(R.id.size);
            tvPkg = itemView.findViewById(R.id.pkg);
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }
    }
}
