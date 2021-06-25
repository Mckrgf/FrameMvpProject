package com.supcon.common.view.base.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by wangshizhan on 16/12/1.
 */
public abstract class BaseDataFrameLayout<T> extends BaseFrameLayout {
    public BaseDataFrameLayout(Context context) {
        super(context);
    }

    public BaseDataFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void update(T data);
}
