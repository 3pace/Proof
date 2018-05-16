package com.proof.ly.space.proof.CustomViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by aman on 17.03.18.
 */

public class MViewPager extends ViewPager {

    private float mInitialXValue;

    public enum SwipeDirection {
        all, left, right, none
    }

    private SwipeDirection mDirection;

    public MViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.IsSwipeAllowed(event) && super.onTouchEvent(event);


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.IsSwipeAllowed(event) && super.onInterceptTouchEvent(event);


    }


    private boolean IsSwipeAllowed(MotionEvent event) {
        if (this.mDirection == SwipeDirection.all) return true;

        if (mDirection == SwipeDirection.none)//disable any swipe
            return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mInitialXValue = event.getX();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - mInitialXValue;
                if (diffX > 0 && mDirection == SwipeDirection.right) {
                    // swipe from left to right detected
                    return false;
                } else if (diffX < 0 && mDirection == SwipeDirection.left) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.mDirection = direction;
    }
}
