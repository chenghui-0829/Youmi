package com.liangzhiyang.youmi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.liangzhiyang.youmi.R;
import com.liangzhiyang.youmi.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

public class MultiLineRadioGroup extends ViewGroup implements View.OnClickListener {
    public final int LEFT = 1;
    public final int CENTER = 0;
    public final int RIGHT = 2;
    private int mX, mY;
    private List<CheckBox> viewList;
    private int childMarginHorizontal = 0;
    private int childMarginVertical = 0;
    private int childResId = 0;
    private int childCount = 0;
    private int childValuesId = 0;
    private boolean singleChoice = false;
    private int mLastCheckedPosition = -1;
    private int rowNumber = 0;
    private int gravity = LEFT;
    private OnCheckedChangedListener listener;
    private List<String> childValues;
    private boolean forceLayout;

    public MultiLineRadioGroup(Context context, AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewList = new ArrayList<CheckBox>();
        childValues = new ArrayList<String>();
        TypedArray arr = context.obtainStyledAttributes(attrs,
                R.styleable.MultiLineRadioGroup);
        childMarginHorizontal = arr.getDimensionPixelSize(
                R.styleable.MultiLineRadioGroup_child_margin_horizontal, 15);
        childMarginVertical = arr.getDimensionPixelSize(
                R.styleable.MultiLineRadioGroup_child_margin_vertical, 5);
        childResId = arr.getResourceId(
                R.styleable.MultiLineRadioGroup_child_layout, 0);
        childCount = arr.getInt(R.styleable.MultiLineRadioGroup_child_count, 0);
        singleChoice = arr.getBoolean(
                R.styleable.MultiLineRadioGroup_single_choice, true);
        childValuesId = arr.getResourceId(
                R.styleable.MultiLineRadioGroup_child_values, 0);
        gravity = arr.getInt(R.styleable.MultiLineRadioGroup_gravity, LEFT);
        if (childResId == 0) {
            throw new RuntimeException(
                    "The attr 'child_layout' must be specified!");
        }
        if (childValuesId != 0) {
            String[] childValues_ = getResources()
                    .getStringArray(childValuesId);
            for (String str : childValues_) {
                childValues.add(str);
            }
        }
        if (childCount > 0) {
            boolean hasValues = childValues != null;
            for (int i = 0; i < childCount; i++) {
                CheckBox cb = getChild();
                viewList.add(cb);
                addView(cb);
                if (hasValues && i < childValues.size()) {
                    cb.setText(childValues.get(i));
                } else {
                    childValues.add(cb.getText().toString());
                }
                cb.setTag(i);
                cb.setOnClickListener(this);
            }
        } else {
            Log.d("tag", "childCount is 0");
        }
        arr.recycle();
    }

    public MultiLineRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiLineRadioGroup(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();
        int flagX = 0, flagY = 0, sheight = 0;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                measureChild(v, widthMeasureSpec, heightMeasureSpec);
                int w = v.getMeasuredWidth() + childMarginHorizontal * 2
                        + flagX + getPaddingLeft() + getPaddingRight();
                if (w > getMeasuredWidth()) {
                    flagY++;
                    flagX = 0;
                }
                sheight = v.getMeasuredHeight();
                flagX += v.getMeasuredWidth() + childMarginHorizontal * 2;
            }
            rowNumber = flagY;
        }
        int height = (flagY + 1) * (sheight + childMarginVertical)
                + childMarginVertical + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(getMeasuredWidth(), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed && !forceLayout) {
            Log.d("tag", "onLayout:unChanged");
            return;
        }
        childCount = getChildCount();
        int[] sX = new int[rowNumber + 1];
        if (childCount > 0) {
            if (gravity != LEFT) {
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    int w = v.getMeasuredWidth() + childMarginHorizontal * 2
                            + mX + getPaddingLeft() + getPaddingRight();
                    if (w > getWidth()) {
                        if (gravity == CENTER) {
                            sX[mY] = (getWidth() - mX) / 2;
                        } else { // right
                            sX[mY] = (getWidth() - mX);
                        }
                        mY++;
                        mX = 0;
                    }
                    mX += v.getMeasuredWidth() + childMarginHorizontal * 2;
                    if (i == childCount - 1) {
                        if (gravity == CENTER) {
                            sX[mY] = (getWidth() - mX) / 2;
                        } else { // right
                            sX[mY] = (getWidth() - mX);
                        }
                    }
                }
                mX = mY = 0;
            }
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                int w = v.getMeasuredWidth() + childMarginHorizontal * 2 + mX
                        + getPaddingLeft() + getPaddingRight();
                if (w > getWidth()) {
                    mY++;
                    mX = 0;
                }
                int startX = mX + childMarginHorizontal + getPaddingLeft()
                        + sX[mY];
                int startY = mY * v.getMeasuredHeight() + (mY + 1)
                        * childMarginVertical;
                v.layout(startX, startY, startX + v.getMeasuredWidth(), startY
                        + v.getMeasuredHeight());
                mX += v.getMeasuredWidth() + childMarginHorizontal * 2;
            }
        }
        mX = mY = 0;
        forceLayout = false;
    }

    private int mLast = -1;

    @Override
    public void onClick(View v) {
        try {
            if (singleChoice) { // singleChoice
                int i = (Integer) v.getTag();
                boolean checked = ((CheckBox) v).isChecked();
                if (mLastCheckedPosition != -1 && mLastCheckedPosition != i) {
                    viewList.get(mLastCheckedPosition).setChecked(false);
                }
                if (checked) {
                    mLastCheckedPosition = i;
//
                } else {
                    mLastCheckedPosition = -1;
                }
                setDrawalbes((CheckBox) viewList.get(i), true);
                if (mLast != -1 && mLast != i)
                    setDrawalbes((CheckBox) viewList.get(mLast), false);
                if (listener != null) {
                    listener.onItemChecked(this, i, checked);
                }
                mLast = i;
            } else { // multiChoice
                int i = (Integer) v.getTag();
                CheckBox cb = (CheckBox) v;
                if (null != listener) {
                    listener.onItemChecked(this, i, cb.isChecked());
                }
            }
        } catch (Exception e) {
        }
    }

    public void setOnCheckChangedListener(OnCheckedChangedListener l) {
        this.listener = l;
    }

    public boolean setItemChecked(int position) {
        if (position >= 0 && position < viewList.size()) {
            if (singleChoice) {
                if (position == mLastCheckedPosition) {
                    return true;
                }
                if (mLastCheckedPosition >= 0
                        && mLastCheckedPosition < viewList.size()) {
                    viewList.get(mLastCheckedPosition).setChecked(false);
//                    setDrawalbes((CheckBox)viewList.get(mLastCheckedPosition),false);
                }
                mLastCheckedPosition = position;
            }
            viewList.get(position).setChecked(true);
            return true;
        }
        setDrawalbes((CheckBox) viewList.get(mLastCheckedPosition), true);
        return false;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        forceLayout = true;
        requestLayout();
    }

    private CheckBox getChild() {
        View v = LayoutInflater.from(getContext()).inflate(childResId, this,
                false);
        if (!(v instanceof CheckBox)) {
            throw new RuntimeException(
                    "The attr child_layout's root must be a CheckBox!");
        }
        CheckBox cb = (CheckBox) v;
        return cb;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (null != viewList && viewList.size() > 0) {
            for (View v : viewList) {
                v.setEnabled(enabled);
            }
        }
    }

    public interface OnCheckedChangedListener {
        public void onItemChecked(MultiLineRadioGroup group, int position,
                                  boolean checked);
    }

    private void setDrawalbes(CheckBox checkBox, boolean check) {
        if (check) {
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_loans_select);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            checkBox.setCompoundDrawables(null, null, drawable, null);
            checkBox.setPadding(DisplayUtils.dip2px(getContext(),10),0,DisplayUtils.dip2px(getContext(),10),0);
        } else {
            checkBox.setCompoundDrawables(null, null, null, null);
            checkBox.setPadding(0,0,0,0);
        }

    }

    public void clearChecked() {
        if (singleChoice) {
            if (mLastCheckedPosition >= 0
                    && mLastCheckedPosition < viewList.size()) {
                viewList.get(mLastCheckedPosition).setChecked(false);
                mLastCheckedPosition = -1;
                return;
            }
        }
        for (CheckBox cb : viewList) {
            if (cb.isChecked()) {
                cb.setChecked(false);
            }
        }
    }

}