package com.cdel.accmobile.httpcapture.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdel.accmobile.httpcapture.R;
import com.cdel.accmobile.httpcapture.model.CaptureInterfaceItemBean;
import com.cdel.accmobile.httpcapture.util.ListUtils;
import com.cdel.accmobile.httpcapture.util.ViewUtil;

import java.util.List;

/**
 * 接口调试列表适配器
 *
 * @author zhangbaoyu
 * @time 1/21/21 4:34 PM
 */
public class CaptureInterfaceItemAdapter extends RecyclerView.Adapter<CaptureInterfaceItemAdapter.DebugItemHolder> {

    private List<CaptureInterfaceItemBean> mList;

    private OnItemClickListener onItemClickListener;

    public void setList(List<CaptureInterfaceItemBean> list) {
        mList = list;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CaptureInterfaceItemBean getItem(int position) {
        return mList.get(position);
    }

    @NonNull
    @Override
    public DebugItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = ViewUtil.getItemView(parent.getContext(), R.layout.item_debug_request_list, null);
        return new DebugItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DebugItemHolder holder, int position) {
        holder.mDebugItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        CaptureInterfaceItemBean captureInterfaceItemBean = mList.get(position);
        if (captureInterfaceItemBean != null) {
            holder.mTvHost.setText(captureInterfaceItemBean.getHost());
            holder.mTvUrl.setText(captureInterfaceItemBean.getUrl());
            holder.mTvStatus.setText(captureInterfaceItemBean.getStatus());
            holder.mTvTime.setText(captureInterfaceItemBean.getTime());
            holder.mTvDate.setText(captureInterfaceItemBean.getDate());
        }
    }

    @Override
    public int getItemCount() {
        if (ListUtils.isEmpty(mList)) {
            return 0;
        }
        return mList.size();
    }

    static class DebugItemHolder extends RecyclerView.ViewHolder {

        private final TextView mTvUrl, mTvTime, mTvStatus, mTvDate, mTvHost;

        private final RelativeLayout mDebugItemLayout;

        DebugItemHolder(View itemView) {
            super(itemView);
            mDebugItemLayout = itemView.findViewById(R.id.debug_item_layout);
            mTvHost = itemView.findViewById(R.id.tv_host);
            mTvUrl = itemView.findViewById(R.id.tv_url);
            mTvStatus = itemView.findViewById(R.id.tv_status);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
