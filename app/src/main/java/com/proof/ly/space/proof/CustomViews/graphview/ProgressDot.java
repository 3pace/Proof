package com.proof.ly.space.proof.CustomViews.graphview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;


public class ProgressDot {

    private RectF mRect;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private float mProgress;
    private float mPosition;
    private float mRadius = 0;
    private float mMargin = 0;
    private View mView;
    private float mMaxValue = 0;
    private int mMaxPoints = 20;
    private float mTextPadding = 0;
    private float mDefaultY;


    public ProgressDot(View view) {
        mView = view;

        mValueAnimator = new ValueAnimator();

        mRect = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);




    }

    public void setDuration(long duration) {
        mValueAnimator.setDuration(duration);
    }

    public void setFillColor(int fillColor) {
        mPaint.setColor(fillColor);
    }

    public void draw(Canvas canvas) {
        mRect.set(mView.getPaddingStart()  + mMargin,
                (mView.getPaddingTop() + mTextPadding) + mMaxValue - mPosition + mRadius,
                mView.getPaddingStart() + mMargin + mRadius,
                (mView.getPaddingBottom() + mTextPadding) + mMaxValue - mPosition);
        canvas.drawOval(mRect, mPaint);
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    public float getProgress() {
        return mProgress;
    }

    public void setTextPadding(float textPadding) {
        mTextPadding = textPadding;
        mDefaultY = mMaxValue + mView.getPaddingTop() + mTextPadding + mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public float getPosition() {
        return mPosition;
    }

    public void setMaxPoints(int maxPoints) {
        mMaxPoints = maxPoints;

    }

    public float getAnimatedProgess() {
        return mPosition * mMaxPoints / mMaxValue;
    }

    public void start() {
        mPosition = mMaxValue;
        mValueAnimator.setFloatValues(0, (mProgress * mMaxValue / mMaxPoints));
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPosition = (float) animation.getAnimatedValue();
                mView.invalidate();
            }
        });
        mValueAnimator.start();
    }

    public float getLeft() {
        return mRect.left;
    }

    public void setMargin(float margin) {
        mMargin = margin;
    }

    public float getX() {
        return mRect.centerX();
    }

    public float getY() {
        return mRect.centerY();
    }

    public RectF getRect() {
        return mRect;
    }

    public float getWidth() {
        return mRect.width();
    }

    public float getDefaultY() {
        return mDefaultY;
    }

    public void setDefaultY(float defaultY) {
        mDefaultY = defaultY;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }
}
