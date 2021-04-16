package com.cdel.accmobile.httpcapture.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cdel.accmobile.httpcapture.R;
import com.cdel.accmobile.httpcapture.adapter.CaptureInterfaceItemAdapter;
import com.cdel.accmobile.httpcapture.base.BaseActivity;
import com.cdel.accmobile.httpcapture.model.CaptureInterfaceItemBean;
import com.cdel.accmobile.httpcapture.manager.CaptureConstants;
import com.cdel.accmobile.httpcapture.manager.HttpCaptureData;
import com.cdel.accmobile.httpcapture.util.ListUtils;
import com.cdel.accmobile.httpcapture.util.WifiUtil;
import com.cdel.accmobile.httpcapture.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口调试列表页
 *
 * @author zhangbaoyu
 * @time 1/21/21 4:38 PM
 */
public class HttpCaptureListActivity extends BaseActivity {

    private RecyclerView mDebugRecyclerView;

    private CaptureInterfaceItemAdapter captureInterfaceItemAdapter;

    private List<CaptureInterfaceItemBean> captureInterfaceItemBeanList;

    private List<CaptureInterfaceItemBean> searchList;

    private SearchView mSearchView;

    @Override
    public int setLayoutId() {
        return R.layout.activity_debug_list;
    }

    @Override
    public void initViews() {
        mSearchView = findViewById(R.id.search_view);
        mDebugRecyclerView = findViewById(R.id.debugRecyclerView);
        searchList = new ArrayList<>();
        captureInterfaceItemAdapter = new CaptureInterfaceItemAdapter();
        mDebugRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDebugRecyclerView.setAdapter(captureInterfaceItemAdapter);
    }

    @Override
    public void initToolBar() {
        getTitleView().setText(WifiUtil.getCurrentWifiName(this));
    }

    @Override
    public void setListeners() {
        getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        captureInterfaceItemAdapter.setOnItemClickListener(new CaptureInterfaceItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CaptureInterfaceItemBean item = captureInterfaceItemAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(CaptureConstants.METHOD_KEY, item.getHost());
                bundle.putString(CaptureConstants.HOST_KEY, item.getHost());
                bundle.putString(CaptureConstants.URL_KEY, item.getUrl());
                bundle.putString(CaptureConstants.REQUEST_KEY, item.getRequestStr());
                bundle.putString(CaptureConstants.RESPONSE_KEY, item.getResponseStr());
                startActivity(bundle, HttpCaptureDetailActivity.class);
            }
        });
        mSearchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSearch(String content) {
                searchList.clear();
                if (TextUtils.isEmpty(content)) {
                    searchList = captureInterfaceItemBeanList;
                } else {
                    // 先搜索url
                    if (!ListUtils.isEmpty(captureInterfaceItemBeanList)) {
                        for (int i = 0; i < captureInterfaceItemBeanList.size(); i++) {
                            CaptureInterfaceItemBean captureInterfaceItemBean = captureInterfaceItemBeanList.get(i);
                            if (captureInterfaceItemBean != null) {
                                if (captureInterfaceItemBean.getUrl().toLowerCase().contains(content.toLowerCase())) {
                                    searchList.add(captureInterfaceItemBean);
                                }
                            }
                        }
                    }
                    // 后搜索host
                    if (ListUtils.isEmpty(searchList)) {
                        for (int i = 0; i < captureInterfaceItemBeanList.size(); i++) {
                            CaptureInterfaceItemBean captureInterfaceItemBean = captureInterfaceItemBeanList.get(i);
                            if (captureInterfaceItemBean != null) {
                                if (!TextUtils.isEmpty(captureInterfaceItemBean.getHost())) {
                                    if (captureInterfaceItemBean.getHost().toLowerCase().contains(content.toLowerCase())) {
                                        searchList.add(captureInterfaceItemBean);
                                    }
                                }
                            }
                        }
                    }
                }
                captureInterfaceItemAdapter.setList(searchList);
                captureInterfaceItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSearchFocusChange(View v, boolean hasFocus) {

            }

            @Override
            public void onTextChange(String text) {

            }
        });
    }

    @Override
    public void initData() {
        captureInterfaceItemBeanList = HttpCaptureData.getSingleton().httpCaptureList;
        captureInterfaceItemAdapter.setList(captureInterfaceItemBeanList);
        captureInterfaceItemAdapter.notifyDataSetChanged();
    }

}