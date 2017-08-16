package com.haoxueren.start;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoxueren.start.bean.HaoApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView 的 Adapter
 */
public class HaoAppAdapter extends RecyclerView.Adapter<HaoAppAdapter.HaoAppViewHolder> {

    private List<HaoApp> list;

    private OnItemClickListener listener;

    public HaoAppAdapter(List<HaoApp> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    /**
     * 条目点击监听器
     */
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }

    /**
     * ViewHolder
     */
    static class HaoAppViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.appNameTextView)
        TextView appNameTextView;
        @BindView(R.id.appIconImageView)
        ImageView appIconImageView;

        public HaoAppViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onItemLongClick(getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public HaoAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_haoapp, null);
        return new HaoAppViewHolder(itemView, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(HaoAppViewHolder holder, int position) {
        HaoApp haoApp = list.get(position);
        holder.appNameTextView.setText(haoApp.getAppName());
        Drawable appIcon = haoApp.getAppIcon();
        if (appIcon == null) {
            appIcon = getAppIcon(holder.itemView.getContext(), haoApp.getPackageName());
        }
        holder.appIconImageView.setImageDrawable(appIcon);
    }

    /**
     * 获取APP图标；
     */
    public Drawable getAppIcon(Context context, String packageName) {
        // 通过API从应用内获取；
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            return resolveInfo.loadIcon(packageManager);
        }
        return new BitmapDrawable();
    }

}

