package com.cdel.accmobile.httpcapture.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.cdel.accmobile.httpcapture.R;

/**
 * 搜索view
 *
 * @author zhangbaoyu
 * @time 2/25/21 10:43 PM
 */
public class SearchView extends RelativeLayout {
    private Context mContext;

    private EditText mEdtSearch;

    private ImageView mIvCancel;

    private RelativeLayout mSearchLayout;

    private InputMethodManager mInputMethodManager;

    protected OnSearchListener onSearchListener;

    private OnClickListener ivCancelonClickListener;

    public SearchView(Context context) {
        super(context);
        initView(context);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(getContext()).inflate(
            R.layout.search_view_layout, this, true);
        mEdtSearch = view.findViewById(R.id.et_search_words);
        mIvCancel = view.findViewById(R.id.iv_search_cancle_icon);
        mSearchLayout = view.findViewById(R.id.rl_search);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        initListener();
        hideSoftInput();
    }

    private void initListener() {
        // 监听焦点
        mEdtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onSearchListener != null) {
                    onSearchListener.onSearchFocusChange(v, hasFocus);
                }
            }
        });
        mEdtSearch.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    search();
                    hideSoftInput();
                }
                return false;
            }
        });
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (str.length() > 0) {
                    if (mIvCancel.getVisibility() != VISIBLE) {
                        mIvCancel.setVisibility(VISIBLE);
                    }
                } else {
                    if (mIvCancel.getVisibility() != GONE) {
                        mIvCancel.setVisibility(GONE);
                    }
                }
                if (onSearchListener != null) {
                    onSearchListener.onTextChange(str);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mIvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivCancelonClickListener != null) {
                    ivCancelonClickListener.onClick(v);
                } else {
                    clearSearch();
                }
            }
        });
    }

    /**
     * 搜索
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:44 PM
     */
    public void search() {
        if (onSearchListener != null) {
            onSearchListener.onSearch(getSearchContent());
        }
    }

    /**
     * 获取搜索框的内容
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:44 PM
     */
    public String getSearchContent() {
        if (mEdtSearch == null) {
            return null;
        }
        return mEdtSearch.getText().toString();
    }

    /**
     * 让EditText失去焦点
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:45 PM
     */
    public void lostRocus() {
        if (mEdtSearch != null) {
            mEdtSearch.setFocusable(false);
        }
    }

    /**
     * 清除搜索内容
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:45 PM
     */
    public void clearSearch() {
        mEdtSearch.setText("");
    }

    /**
     * 设置搜索listener
     *
     * @param onSearchListener 搜索监听
     * @author zhangbaoyu
     * @time 2/25/21 10:46 PM
     */
    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public EditText getEdtSearch() {
        return mEdtSearch;
    }

    public ImageView getIvCancel() {
        return mIvCancel;
    }

    /**
     * 设置X图片点击事件
     *
     * @param onClickListener 点击事件
     * @author zhangbaoyu
     * @time 2/25/21 10:46 PM
     */
    public void setIvCancelClickListener(OnClickListener onClickListener) {
        this.ivCancelonClickListener = onClickListener;
    }

    /**
     * 隐藏键盘
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:47 PM
     */
    public void hideSoftInput() {
        if (mInputMethodManager != null) {
            mInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    /**
     * 搜索事件
     *
     * @author zhangbaoyu
     * @time 2/25/21 10:47 PM
     */
    public interface OnSearchListener {
        void onSearch(String content);

        void onSearchFocusChange(View v, boolean hasFocus);

        void onTextChange(String text);
    }
}
