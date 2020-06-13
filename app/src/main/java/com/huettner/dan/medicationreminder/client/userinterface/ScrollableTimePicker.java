package com.huettner.dan.medicationreminder.client.userinterface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TimePicker;

/**
 * Custom implementation of {@link DatePicker} to fix issue with {@link android.widget.ScrollView}.
 * Disallows touch event for parent when touched on picker.
 *
 * Creator: <lukasz.izmajlowicz@mydriver.com>
 * Date: 9/19/13
 *
 */
public class ScrollableTimePicker extends TimePicker {

    public ScrollableTimePicker(Context context) {
        super(context);
    }

    public ScrollableTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ViewParent parentView = getParent();

            if (parentView != null) {
                parentView.requestDisallowInterceptTouchEvent(true);
                ScrollView parentScrollView = (ScrollView) parentView.getParent();
                parentScrollView.requestDisallowInterceptTouchEvent(true);
                parentScrollView.requestFocusFromTouch();
                parentScrollView.requestFocus();
                int height = parentScrollView.getHeight();
                parentScrollView.scrollTo(0, height - 1);
            }

        return false;
    }

    @Override
    public boolean onDragEvent(DragEvent ev) {
        ViewParent parentView = getParent();

            if (parentView != null) {
                parentView.requestDisallowInterceptTouchEvent(true);
                ScrollView parentScrollView = (ScrollView) parentView.getParent();
                parentScrollView.requestDisallowInterceptTouchEvent(true);
                parentScrollView.requestFocusFromTouch();
                parentScrollView.requestFocus();
                int height = parentScrollView.getHeight();
                parentScrollView.scrollTo(0, height - 1);
            }

        return false;
    }

}