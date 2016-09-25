package com.done.extractapk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.done.extractapk.R;
import com.done.extractapk.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Done on 2016/9/24 0024.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> implements View.OnClickListener {

    private ArrayList<AppInfo> appInfoList;

    public void addList(ArrayList<AppInfo> appList) {
        if (appInfoList == null) {
            appInfoList = new ArrayList<>();
        }
        appInfoList.addAll(appList);
    }
    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_info_item, parent, false);
        AppViewHolder vh = new AppViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        holder.appName.setText(appInfoList.get(position).appName);
        holder.appIcon.setImageDrawable(appInfoList.get(position).appIcon);
        holder.versionName.setText(appInfoList.get(position).versionName);
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(appInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return appInfoList== null ? 0 : appInfoList.size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder {
        TextView appName, versionName;
        ImageView appIcon;
        public AppViewHolder(View itemView) {
            super(itemView);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            versionName = (TextView) itemView.findViewById(R.id.app_version_name);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , AppInfo data);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(AppInfo) v.getTag());
        }
    }
}
