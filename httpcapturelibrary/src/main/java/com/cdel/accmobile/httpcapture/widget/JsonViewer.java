package com.cdel.accmobile.httpcapture.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;

import com.cdel.accmobile.httpcapture.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JsonViewer extends LinearLayout {

    private final float PADDING = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
    @ColorInt
    private int textColorString;
    @ColorInt
    private int textColorBool;
    @ColorInt
    private int textColorNull;
    @ColorInt
    private int textColorNumber;

    public JsonViewer(Context context) {
        super(context);

        if (isInEditMode())
            initEditMode();
    }

    public JsonViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null)
            init(context, attrs);
        if (isInEditMode())
            initEditMode();
    }

    public JsonViewer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null)
            init(context, attrs);
        if (isInEditMode())
            initEditMode();
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.JsonViewer, 0, 0);
        Resources r = getResources();

        try {
            textColorString = a.getColor(R.styleable.JsonViewer_textColorString, r.getColor(R.color.jsonViewer_textColorString));
            textColorNumber = a.getColor(R.styleable.JsonViewer_textColorNumber, r.getColor(R.color.jsonViewer_textColorNumber));
            textColorBool = a.getColor(R.styleable.JsonViewer_textColorBool, r.getColor(R.color.jsonViewer_textColorBool));
            textColorNull = a.getColor(R.styleable.JsonViewer_textColorNull, r.getColor(R.color.jsonViewer_textColorNull));
        } finally {
            a.recycle();
        }
    }

    private void initEditMode() {

        String json = "{\"id\":1,\"name\":\"Title\",\"is\":true,\"value\":null,\"array\":[{\"item\":1,\"name\":\"One\"},{\"item\":2,\"name\":\"Two\"}],\"object\":{\"id\":1,\"name\":\"Title\"},\"simple_array\":[1,2,3]}";

        try {
            setJson(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill this view with a JSON. The old one will be erased and the view reset.
     *
     * @param json The JSON to add.
     * @see JSONObject
     * @see JSONArray
     */
    public void setJson(Object json) {

        if (!(json instanceof JSONArray || json instanceof JSONObject))
            throw new RuntimeException("JsonViewer: JSON must be a instance of org.json.JSONArray or org.json.JSONObject");

        super.setOrientation(VERTICAL);

        this.removeAllViews();
        addJsonNode(this, null, json, false);
    }

    public void setTextColorString(@ColorInt int color) {
        textColorString = color;
    }

    public void setTextColorNumber(@ColorInt int color) {
        textColorNumber = color;
    }

    public void setTextColorBool(@ColorInt int color) {
        textColorBool = color;
    }

    public void setTextColorNull(@ColorInt int color) {
        textColorNull = color;
    }

    /**
     * It will collapse all nodes, except the main one.
     */
    public void collapseJson() {
        for (int i = 0; i < this.getChildCount(); i++) {
            if (this.getChildAt(i) instanceof TextView &&
                    this.getChildAt(i + 1) instanceof ViewGroup &&
                    this.getChildAt(i + 2) instanceof TextView) {
                changeVisibility((ViewGroup) this.getChildAt(i + 1), VISIBLE);
                i += 2;
            }
        }
    }

    /**
     * It will expands all the json nodes.
     */
    public void expandJson() {
        changeVisibility(this, GONE);
    }

    /**
     * Switch collapse status of a node content.
     *
     * @param group         The view group to operate on.
     * @param oldVisibility Current visibility.
     */
    private void changeVisibility(ViewGroup group, int oldVisibility) {
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i) instanceof TextView &&
                    group.getChildAt(i + 1) instanceof ViewGroup &&
                    group.getChildAt(i + 2) instanceof TextView) {
                ViewGroup groupChild = (ViewGroup) group.getChildAt(i + 1);
                groupChild.setVisibility(oldVisibility);
//                groupChild.setLayoutTransition(null); // remove transition before mass change
                group.getChildAt(i).callOnClick();
//                groupChild.setLayoutTransition(new LayoutTransition());
                changeVisibility((ViewGroup) group.getChildAt(i + 1), oldVisibility);
                i += 2;
            }
        }
    }

    /**
     * Add a node to the view with header key and close footer. This method is call for every node in a node.
     *
     * @param content  Current view group.
     * @param nodeKey  key of the current node.
     * @param jsonNode Node to display.
     * @param haveNext If this node is followed by a other one.
     */
    private void addJsonNode(final LinearLayout content, @Nullable final Object nodeKey, final Object jsonNode, final boolean haveNext) {

        final boolean haveChild = ((jsonNode instanceof JSONObject && ((JSONObject) jsonNode).length() != 0) ||
                (jsonNode instanceof JSONArray && ((JSONArray) jsonNode).length() != 0));
        final TextView textViewHeader;

        textViewHeader = getHeader(nodeKey, jsonNode, haveNext, true, haveChild);

        content.addView(textViewHeader);

        if (haveChild) {
            final ViewGroup viewGroupChild = getJsonNodeChild(nodeKey, jsonNode);
            final TextView textViewFooter = getFooter(jsonNode, haveNext);

            content.addView(viewGroupChild);
            content.addView(textViewFooter);

            textViewHeader.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (viewGroupChild == null)
                        return;

                    int newVisibility;
                    boolean showChild;

                    if (viewGroupChild.getVisibility() == VISIBLE) {
                        newVisibility = GONE;
                        showChild = false;
                    } else {
                        newVisibility = VISIBLE;
                        showChild = true;
                    }
                    textViewHeader.setText(getHeaderText(nodeKey, jsonNode, haveNext, showChild, haveChild));
                    viewGroupChild.setVisibility(newVisibility);
                    if (textViewFooter != null) {
                        textViewFooter.setVisibility(newVisibility);
                    }
                }
            });
        }

    }

    /**
     * Create a view group for a node content and return it.
     *
     * @param nodeKey  Key of the node passed as parameter.
     * @param jsonNode Content of the node use to fill view.
     * @return View group contain all the childs of the node.
     */
    private ViewGroup getJsonNodeChild(Object nodeKey, Object jsonNode) {

        final LinearLayout content = new LinearLayout(getContext());

        content.setOrientation(VERTICAL);
        content.setPadding((int) PADDING, 0, 0, 0);
        if (nodeKey != null) {
            content.setBackgroundResource(R.drawable.background);
        }
        content.setLayoutTransition(new LayoutTransition());

        if (jsonNode instanceof JSONObject) {
            // setView key
            JSONObject object = (JSONObject) jsonNode;
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                // set view list
                try {
                    addJsonNode(content, key, object.get(key), iterator.hasNext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (jsonNode instanceof JSONArray) {
            // setView key
            JSONArray object = (JSONArray) jsonNode;
            for (int i = 0; i < object.length(); i++) {
                // set view list
                try {
                    addJsonNode(content, i, object.get(i), i + 1 < object.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    private TextView getHeader(Object key, @Nullable Object jsonNode, boolean haveNext, boolean childDisplayed, boolean haveChild) {
        TextView textView = new TextView(getContext());
        textView.setText(getHeaderText(key, jsonNode, haveNext, childDisplayed, haveChild));
        TextViewCompat.setTextAppearance(textView, R.style.JsonViewer_TextAppearance);
        textView.setFocusableInTouchMode(false);
        textView.setFocusable(false);

        return textView;
    }

    private SpannableStringBuilder getHeaderText(Object key, @Nullable Object jsonNode, boolean haveNext, boolean childDisplayed, boolean haveChild) {

        SpannableStringBuilder b = new SpannableStringBuilder();
        if (key instanceof String) {
            b.append("\"");
            b.append((String) key);
            b.append("\"");
            b.append(": ");
        }

        if (!childDisplayed) {
            if (jsonNode instanceof JSONArray)
                b.append("[ ... ]");
            else if (jsonNode instanceof JSONObject) {
                b.append("{ ... }");
            }
            if (haveNext)
                b.append(",");
        } else {
            if (jsonNode instanceof JSONArray) {
                b.append("[");
                if (!haveChild)
                    b.append(getFooterText(jsonNode, haveNext));
            } else if (jsonNode instanceof JSONObject) {
                b.append("{");
                if (!haveChild)
                    b.append(getFooterText(jsonNode, haveNext));
            } else if (jsonNode != null) {

                if (Build.VERSION.SDK_INT >= 21) {
                    if (jsonNode instanceof String)
                        b.append("\"" + jsonNode + "\"", new ForegroundColorSpan(textColorString), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    else if (jsonNode instanceof Integer || jsonNode instanceof Float || jsonNode instanceof Double)
                        b.append(jsonNode.toString(), new ForegroundColorSpan(textColorNumber), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    else if (jsonNode instanceof Boolean)
                        b.append(jsonNode.toString(), new ForegroundColorSpan(textColorBool), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    else if (jsonNode == JSONObject.NULL)
                        b.append(jsonNode.toString(), new ForegroundColorSpan(textColorNull), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    else
                        b.append(jsonNode.toString());
                } else
                    b.append(jsonNode.toString());

                if (haveNext)
                    b.append(",");

                LeadingMarginSpan span = new LeadingMarginSpan.Standard(0, (int) PADDING);
                b.setSpan(span, 0, b.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return b;
    }

    private TextView getFooter(@Nullable Object jsonNode, boolean haveNext) {


        TextView textView = new TextView(getContext());
        textView.setText(getFooterText(jsonNode, haveNext));
        TextViewCompat.setTextAppearance(textView, R.style.JsonViewer_TextAppearance);
        textView.setFocusableInTouchMode(false);
        textView.setFocusable(false);

        return textView;
    }

    private StringBuilder getFooterText(@Nullable Object jsonNode, boolean haveNext) {
        StringBuilder builder = new StringBuilder();
        if (jsonNode instanceof JSONObject) {
            builder.append("}");
        } else if (jsonNode instanceof JSONArray) {
            builder.append("]");
        } else
            return null;
        if (haveNext)
            builder.append(",");
        return builder;
    }
}
