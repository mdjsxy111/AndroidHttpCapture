package com.accmobile.httpcapturedemo.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accmobile.httpcapturedemo.R;
import com.accmobile.httpcapturedemo.adapter.VideoItemAdapter;
import com.accmobile.httpcapturedemo.http.AccOkHttpUtil;
import com.accmobile.httpcapturedemo.http.StringResultCallBack;
import com.accmobile.httpcapturedemo.entity.VideoBean;
import com.cdel.accmobile.httpcapture.base.BaseActivity;
import com.cdel.accmobile.httpcapture.util.HttpCaptureGsonUtil;
import com.lzy.okhttputils.model.HttpParams;

import java.util.List;

/**
 * @author zhangbaoyu
 * @time 2/19/21 5:58 PM
 */
public class PostVideoListActivity extends BaseActivity {

    private RecyclerView videoRecyclerView;

    private VideoItemAdapter videoItemAdapter;

    @Override
    public int setLayoutId() {
        return R.layout.activity_video_list;
    }

    @Override
    public void initViews() {
        videoRecyclerView = findViewById(R.id.video_recyclerview);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoItemAdapter = new VideoItemAdapter();
        videoRecyclerView.setAdapter(videoItemAdapter);
    }

    @Override
    public void initToolBar() {
        getTitleView().setText(R.string.post_agent);
    }

    @Override
    public void setListeners() {
        getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("id", 14);
        AccOkHttpUtil.postVideoList(httpParams, new StringResultCallBack() {
            @Override
            public void onStringResult(String sResult) {
                VideoBean videoBean = HttpCaptureGsonUtil.jsonToObject(sResult, VideoBean.class);
                List<VideoBean.Result> videoList = videoBean.getResult();
                videoItemAdapter.setList(videoList);
                videoItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStringError() {

            }
        });
    }
}
