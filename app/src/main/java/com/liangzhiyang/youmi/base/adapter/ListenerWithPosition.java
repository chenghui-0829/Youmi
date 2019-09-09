package com.liangzhiyang.youmi.base.adapter;

import android.view.View;

/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */

public class ListenerWithPosition implements View.OnClickListener {

    private int mPosition;
    private Object mHolder;
    private OnClickWithPositionListener mOnClickListener;

    public ListenerWithPosition(int position, Object holder) {
        this.mPosition = position;
        this.mHolder = holder;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null)
            mOnClickListener.onClick(v, mPosition, mHolder);
    }

    public interface OnClickWithPositionListener<T> {
        void onClick(View v, int position, T holder);
    }

    public void setOnClickListener(OnClickWithPositionListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }
}
