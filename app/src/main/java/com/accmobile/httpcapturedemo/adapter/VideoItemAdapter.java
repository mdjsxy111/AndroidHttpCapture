package com.accmobile.httpcapturedemo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accmobile.httpcapturedemo.R;
import com.accmobile.httpcapturedemo.entity.VideoBean;
import com.bumptech.glide.Glide;
import com.cdel.accmobile.httpcapture.util.ListUtils;
import com.cdel.accmobile.httpcapture.util.ViewUtil;

import java.util.List;

/**
 * @author zhangbaoyu
 * @time 2/19/21 6:12 PM
 */
public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoItemHolder> {
    private List<VideoBean.Result> mList;

    public void setList(List<VideoBean.Result> list) {
        mList = list;
    }

    @NonNull
    @Override
    public VideoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = ViewUtil.getItemView(parent.getContext(), R.layout.item_video_list, null);
        return new VideoItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemHolder holder, int position) {
        VideoBean.Result result = mList.get(position);
        if (result != null) {
            holder.mTvVideoTitle.setText(result.getData().getHeader().getTitle());
            Glide.with(holder.mIvIcon.getContext()).load(result.getData().getHeader().getIcon()).into(holder.mIvIcon);
        }
    }

    @Override
    public int getItemCount() {
        if (ListUtils.isEmpty(mList)) {
            return 0;
        }
        return mList.size();
    }

    static class VideoItemHolder extends RecyclerView.ViewHolder {

        private final TextView mTvVideoTitle;

        private final ImageView mIvIcon;

        VideoItemHolder(View itemView) {
            super(itemView);
            mTvVideoTitle = itemView.findViewById(R.id.tv_video_title);
            mIvIcon = itemView.findViewById(R.id.iv_video_icon);
        }
    }
}
