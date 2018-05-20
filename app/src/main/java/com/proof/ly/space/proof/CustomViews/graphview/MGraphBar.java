package com.proof.ly.space.proof.CustomViews.graphview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.proof.ly.space.proof.Data.Progress;
import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.MBarUtils;

import java.util.ArrayList;

/**
 * CUSTOM VIEW BY AMAN
 * CREATED ON 20.05.2018
 * WRITE ON 21.05.2018 00:002
 */

public class MGraphBar extends View {


    private ArrayList<ProgressDot> mProgressDots;
    private Paint mPaintHorizontalLine, mPaintText, mPaintVerticalLine;
    private final int SHOW_COUNT = 7;
    private int mMaxPoints = 20;
    private int mDotColor = 0;
    private float mTextHeight = 0;
    private Path mHorizontalPath, mVerticalPath;
    private float mRadius = 0;
    private float mMaxValue = 0;
    private float mMarginBetween = 0;
    private float mProgressAnimateDuration = 0;

    public MGraphBar(Context context) {
        super(context);


    }

    public MGraphBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setClickable(true);
        setFocusable(true);
        mProgressDots = new ArrayList<>();
        mPaintHorizontalLine = new Paint();
        mPaintHorizontalLine.setAntiAlias(true);
        mPaintHorizontalLine.setColor(getResources().getColor(R.color.white100));
        mPaintHorizontalLine.setStyle(Paint.Style.STROKE);
        mPaintHorizontalLine.setStrokeWidth(5f);
        mPaintHorizontalLine.setStrokeJoin(Paint.Join.ROUND);
        mPaintHorizontalLine.setDither(true);


        mPaintVerticalLine = new Paint();
        mPaintVerticalLine.setAntiAlias(true);
        mPaintVerticalLine.setColor(getResources().getColor(R.color.colorPrimary));
        mPaintVerticalLine.setStyle(Paint.Style.STROKE);
        mPaintVerticalLine.setStrokeWidth(3f);
        mPaintVerticalLine.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        mPaintVerticalLine.setDither(true);
        mPaintVerticalLine.setStrokeJoin(Paint.Join.ROUND);
        mPaintVerticalLine.setDither(true);


        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(getResources().getColor(R.color.grey));
        mPaintText.setTextSize(context.getResources().getDimension(R.dimen.small_txt_size));
        Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;

        mHorizontalPath = new Path();
        mHorizontalPath.setFillType(Path.FillType.EVEN_ODD);
        mVerticalPath = new Path();


        /*for (int i = 0; i < SHOW_COUNT; i++) {
            mProgressDot = new ProgressDot(this);
            mProgressDot.setFillColor(getResources().getColor(R.color.grey));
            int random = (int) (Math.random() * 500);
            mProgressDot.setProgress(random);
            mProgressDot.setDuration((random - (70 * random / 100)) * 20);
            mProgressDot.setMaxPoints(mMaxPoints);

            mProgressDots.add(mProgressDot);

        }*/

        mRadius = MBarUtils.dip2px(context, 10);
        mMaxValue = MBarUtils.dip2px(context, 200);
        mMarginBetween = MBarUtils.dip2px(context, 30);
        mProgressAnimateDuration = 70;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHorizontalPath.reset();
        mVerticalPath.reset();

        for (int i = 0; i < SHOW_COUNT; i++) {

            float startX = mProgressDots.get(i).getX();
            float startY = mProgressDots.get(i).getY();
            float endX = i < SHOW_COUNT - 1 ?
                    mProgressDots.get(i + 1).getX() : mProgressDots.get(i).getX();
            float endY = i < SHOW_COUNT - 1 ?
                    mProgressDots.get(i + 1).getY() : mProgressDots.get(i).getY();


            mHorizontalPath.reset();
            mHorizontalPath.moveTo(startX, startY);
            mHorizontalPath.lineTo(endX, endY);
            canvas.drawPath(mHorizontalPath, mPaintHorizontalLine);

            mVerticalPath.reset();
            mVerticalPath.moveTo(startX, mProgressDots.get(i).getDefaultY());
            mVerticalPath.lineTo(startX, startY);
            canvas.drawPath(mVerticalPath, mPaintVerticalLine);

/*            canvas.drawLine(startX, mProgressDots.get(i).getDefaultY(), startX, startY, mPaintVerticalLine);
            canvas.drawLine(startX, startY, endX, endY, mPaintHorizontalLine);*/


            int progress = (int) mProgressDots.get(i).getAnimatedProgess();
            String text = String.valueOf(progress);
            canvas.drawText(text, startX - mPaintText.measureText(text) / 2, startY - mRadius, mPaintText);

            mProgressDots.get(i).setMargin(i * mMarginBetween);
            mProgressDots.get(i).setTextPadding(mTextHeight + mRadius);
            mProgressDots.get(i).draw(canvas);
        }


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println(mProgressDots.size());


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float maxWidth = SHOW_COUNT * mMarginBetween + mRadius;
        float maxHeight = mMaxValue + mTextHeight + mRadius * 2;

        int desiredWidth = Math.round(maxWidth + getPaddingLeft() + getPaddingRight());
        int desiredHeight = Math.round(maxHeight + getPaddingTop() + getPaddingBottom());

        int measuredWidth = getMeasurement(desiredWidth, widthMeasureSpec);
        int measuredHeight = getMeasurement(desiredHeight, heightMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    private int getMeasurement(int contentSize, int measureSpec) {
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                System.out.println("EXACTLY");
                return specSize;
            case MeasureSpec.AT_MOST:

                if (contentSize < specSize) {
                    System.out.println("AT_MOST + contentSize");
                    return contentSize;
                } else {
                    System.out.println("AT_MOST + specSize");
                    return specSize;
                }
            case MeasureSpec.UNSPECIFIED:
                System.out.println("UNSPECIFIED");
                return contentSize;
            default:
                System.out.println("DEFAULT");
                return contentSize;
        }

    }

    public void setDataList(ArrayList<Progress> arrayList) {
        for (Progress progress : arrayList) {
            ProgressDot progressDot = new ProgressDot(this);
            // Делаем так, для того, чтобы считать 500 как mMaxPoints (40)
            // 40 - 500
            // progress - X
            // Пропорция
            float progressValue = progress.getProgress() * mMaxValue / mMaxPoints;
            progressDot.setProgress(progress.getProgress());
            progressDot.setDuration((long) (progressValue - (mProgressAnimateDuration * progressValue / 100)) * 20);
            progressDot.setMaxPoints(mMaxPoints);
            progressDot.setFillColor(mDotColor);
            progressDot.setRadius(mRadius);
            progressDot.setMaxValue(mMaxValue);


            mProgressDots.add(progressDot);
        }
        for (int i = 0; i < mProgressDots.size(); i++) {
            mProgressDots.get(i).start();

        }
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public void setMaxPoints(int maxPoints) {
        mMaxPoints = maxPoints;
    }

    public void setHorizontalLineColor(int color) {
        mPaintHorizontalLine.setColor(color);
    }

    public void setVerticalLineColor(int color) {
        mPaintVerticalLine.setColor(color);
    }

    public void setDotColor(int color) {
        mDotColor = color;
    }

    public void setTextColor(int color) {
        mPaintText.setColor(color);
    }

    public void setTextSize(float size) {
        mPaintText.setTextSize(size);
    }

    public void setTypeface(Typeface typeface) {
        mPaintText.setTypeface(typeface);
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    public void setMarginBetween(float marginBetween) {
        mMarginBetween = marginBetween;
    }

    public void setProgressAnimateDuration(float progressAnimateDuration) {
        mProgressAnimateDuration = progressAnimateDuration;
    }

}
