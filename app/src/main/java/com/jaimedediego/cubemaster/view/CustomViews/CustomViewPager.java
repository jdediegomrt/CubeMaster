package com.jaimedediego.cubemaster.view.CustomViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class CustomViewPager extends ViewPager{

    private boolean swipeable;

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        swipeable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.swipeable && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.swipeable && super.onInterceptTouchEvent(event);
    }

    public void setSwipeable(boolean swipeable){
        this.swipeable = swipeable;
    }

    public void setScrollDuration(int scrollDuration) {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new SmoothScroller(getContext(), scrollDuration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SmoothScroller extends Scroller {

        private int scrollDuration;

        SmoothScroller(Context context, int scrollDuration) {
            super(context, new DecelerateInterpolator());
            this.scrollDuration = scrollDuration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, scrollDuration);
        }
    }
}
