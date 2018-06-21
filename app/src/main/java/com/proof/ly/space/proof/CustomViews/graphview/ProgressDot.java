package com.proof.ly.space.proof.CustomViews.graphview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.MBarUtils;


public class ProgressDot {

    private RectF mBackRect;
    private RectF mRect;
    private Paint mBackPaint;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private ValueAnimator mTouchAnimator;
    private float mProgress;
    private float mPosition;
    private float mRadius = 0;
    private float mBackRectRadius = 0;
    private float mLeftMargin = 0;
    private float mRightMargin = 0;
    private View mView;
    private float mMaxValue = 0;
    private int mMaxPoints = 20;
    private float mTextPadding = 0;
    private float mDefaultY;
    private int mFillColor = 0;
    private int mEndColor;
    private boolean mIsAnimated = false;
    private String mDate, mPercent, mPoints;



    public ProgressDot(View view) {
        mView = view;

        mValueAnimator = new ValueAnimator();
        mTouchAnimator = new ValueAnimator();

        mBackRect = new RectF(); // Для того, чтобы увеличить область нажатия, он будет невидимым на зданем плане.
        mBackPaint = new Paint();
        mBackPaint.setAlpha(0);

        mRect = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);

        mEndColor = mView.getResources().getColor(R.color.colorAccent);



    }

    public void draw(Canvas canvas) {


        mBackRect.set(mView.getPaddingStart() + mLeftMargin - mBackRectRadius/2,
                mView.getPaddingTop() + mTextPadding + mMaxValue - mPosition - mBackRectRadius/2,
                mView.getPaddingEnd() + mRightMargin + mBackRectRadius ,
                mView.getPaddingBottom() + mTextPadding + mBackRectRadius + mMaxValue - mPosition);

        mRect.set(mView.getPaddingStart() + mLeftMargin,
                mView.getPaddingTop() + mTextPadding + mMaxValue - mPosition,
                mView.getPaddingEnd() + mRightMargin + mRadius,
                mView.getPaddingBottom() + mTextPadding + mRadius + mMaxValue - mPosition);

        canvas.drawOval(mBackRect, mBackPaint);
        canvas.drawOval(mRect, mPaint);

    }
    public void startTouchAnimation() {
        mIsAnimated = true;
        mTouchAnimator.setIntValues(mFillColor, mEndColor);
        mTouchAnimator.setEvaluator(new ArgbEvaluator());
        mTouchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaint.setColor((int) animation.getAnimatedValue());
                mView.invalidate();
            }
        });
        mTouchAnimator.start();
    }
    public void stopTouchAnimation(){
        mIsAnimated = false;
        mTouchAnimator.setIntValues(mEndColor, mFillColor);
        mTouchAnimator.setEvaluator(new ArgbEvaluator());
        mTouchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaint.setColor((int) animation.getAnimatedValue());
                mView.invalidate();
            }
        });
        mTouchAnimator.start();
    }

    public boolean isAnimated() {
        return mIsAnimated;
    }

    public void setDuration(long duration) {
        mValueAnimator.setDuration(duration);
    }

    public void setFillColor(int fillColor) {
        mFillColor = fillColor;
        mPaint.setColor(fillColor);
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
        mBackRectRadius = mRadius * 3f;
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
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
        mValueAnimator.start();
    }

    public float getLeft() {
        return mRect.left;
    }

    public void setLeftMargin(float leftMargin) {
        mLeftMargin = leftMargin;
    }

    public void setRightMargin(float rightMargin) {
        mRightMargin = rightMargin;
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

    public RectF getBackRect() {
        return mBackRect;
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




    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setPercent(String percent) {
        mPercent = percent;
    }

    public String getPercent() {
        return mPercent;
    }

    public void setPoints(String points) {
        mPoints = points;
    }

    public String getPoints() {
        return mPoints;
    }
}
