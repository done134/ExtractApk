package com.done.extractapk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.done.extractapk.R;

/**
 * Created by YZD on 2016/10/26.
 */

public class EmailNoticeEditText extends AutoCompleteTextView {

    private static String DOMAINS = null;
    private static int DEFAULT_DROP_DOWN_KEY_COLOR = Color.parseColor("#7281a3");
    private static int DEFAULT_DROP_DOWN_BG = 0;
    private static boolean DEFAULT_DROP_DOWN_DIVIDER = true;

    private static String[] emails = null;

    public EmailNoticeEditText(Context context) {
        super(context);
        initStyle(context, null);
    }

    public EmailNoticeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStyle(context, attrs);
    }

    public EmailNoticeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context, attrs);
    }

    private void initStyle(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoFillEmailEditText, 0, 0);
        DOMAINS = typedArray.getString(R.styleable.AutoFillEmailEditText_AutoFillEmailEditText_domains);
        DEFAULT_DROP_DOWN_KEY_COLOR = typedArray.getColor(R.styleable.AutoFillEmailEditText_AutoFillEmailEditText_default_drop_down_key_color, DEFAULT_DROP_DOWN_KEY_COLOR);
        DEFAULT_DROP_DOWN_BG = typedArray.getResourceId(R.styleable.AutoFillEmailEditText_AutoFillEmailEditText_default_drop_down_bg, 0);
        DEFAULT_DROP_DOWN_DIVIDER = typedArray.getBoolean(R.styleable.AutoFillEmailEditText_AutoFillEmailEditText_default_drop_down_divider, true);
        typedArray.recycle();
    }

}
