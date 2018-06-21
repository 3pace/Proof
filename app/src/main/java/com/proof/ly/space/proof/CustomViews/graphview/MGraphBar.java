package com.proof.ly.space.proof.CustomViews.graphview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.proof.ly.space.proof.Data.StatsData;
import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.MBarUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * CUSTOM VIEW BY AMAN
 * CREATED ON 20.05.2018
 * WRITE ON 21.05.2018 00:002
 */

public class MGraphBar extends View {


    private ArrayList<ProgressDot> mProgressDots;
    private Paint mPaintHorizontalLine, mPaintText, mPaintVerticalLine;
    private final int MAX_SHOW_COUNT = 10;
    private int mMaxPoints = 20;
    private int mDotColor = 0;
    private float mTextHeight = 0;
    private Path mHorizontalPath, mVerticalPath;
    private float mRadius = 0;
    private float mMaxValue = 0;
    private float mMarginBetween = 0;
    private float mProgressAnimateDuration = 0;
    private PopupWindow mPopupWindow;
    private CardView mPopupCardView;
    private TextView  mPopupTextViewPercent, mPopupTextViewDate, mPopupTextViewPoints;
    private ValueAnimator mPopupAnimator;
    private CountDownTimer mTimer;
    private final int POPUP_SHOW_TIME = 2000;
    private int mShowCount = 0;
    private int[] mLocation = new int[2];

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
        mPaintHorizontalLine.setStrokeWidth(MBarUtils.dip2px(context, 2));
        mPaintHorizontalLine.setStrokeJoin(Paint.Join.ROUND);
        mPaintHorizontalLine.setDither(true);


        mPaintVerticalLine = new Paint();
        mPaintVerticalLine.setAntiAlias(true);
        mPaintVerticalLine.setColor(getResources().getColor(R.color.colorPrimary));
        mPaintVerticalLine.setStyle(Paint.Style.STROKE);
        mPaintVerticalLine.setStrokeWidth(MBarUtils.dip2px(context, 1));
        mPaintVerticalLine.setPathEffect(new DashPathEffect(new float[]{MBarUtils.dip2px(context, 3), MBarUtils.dip2px(context, 3)}, 0));
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


        /*for (int i = 0; i < MAX_SHOW_COUNT; i++) {
            mProgressDot = new ProgressDot(this);
            mProgressDot.setFillColor(getResources().getColor(R.color.grey));
            int random = (int) (Math.random() * 500);
            mProgressDot.setProgress(random);
            mProgressDot.setDuration((random - (70 * random / 100)) * 20);
            mProgressDot.setMaxPoints(mMaxPoints);

            mProgressDots.add(mProgressDot);

        }*/

        mRadius = MBarUtils.dip2px(context, 10);
        mMaxValue = MBarUtils.dip2px(context, 100);
        mMarginBetween = MBarUtils.dip2px(context, 30);
        //mTextHeight = MBarUtils.dip2px(context, mTextHeight);
        mProgressAnimateDuration = MBarUtils.dip2px(context, 25);

        mPopupAnimator = new ValueAnimator();
        mPopupAnimator.setDuration(500);
        mPopupAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                mPopupCardView.setAlpha((float) animation.getAnimatedValue());
            }
        });

        View view = View.inflate(getContext(), R.layout.progress_popup_container, null);
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(null);
        mPopupCardView = view.findViewById(R.id.cardView_popup);
        mPopupTextViewPoints = view.findViewById(R.id.textViewPopupPoints);
        mPopupTextViewPercent = view.findViewById(R.id.textViewPopupPercent);
        mPopupTextViewDate = view.findViewById(R.id.textViewPopupDate);
        mPopupWindow.setContentView(view);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHorizontalPath.reset();
        mVerticalPath.reset();

        for (int i = 0; i < mShowCount; i++) {

            float startX = mProgressDots.get(i).getX();
            float startY = mProgressDots.get(i).getY();
            float endX = i < mShowCount - 1 ?
                    mProgressDots.get(i + 1).getX() : mProgressDots.get(i).getX();
            float endY = i < mShowCount - 1 ?
                    mProgressDots.get(i + 1).getY() : mProgressDots.get(i).getY();


            mVerticalPath.reset();
            mVerticalPath.moveTo(startX, mProgressDots.get(i).getDefaultY());
            mVerticalPath.lineTo(startX, startY);
            canvas.drawPath(mVerticalPath, mPaintVerticalLine);

            mHorizontalPath.reset();
            mHorizontalPath.moveTo(startX, startY);
            mHorizontalPath.lineTo(endX, endY);
            canvas.drawPath(mHorizontalPath, mPaintHorizontalLine);

/*            canvas.drawLine(startX, mProgressDots.get(i).getDefaultY(), startX, startY, mPaintVerticalLine);
            canvas.drawLine(startX, startY, endX, endY, mPaintHorizontalLine);*/


            int progress = (int) mProgressDots.get(i).getAnimatedProgess();
            String text = String.valueOf(progress);
            canvas.drawText(text, startX - mPaintText.measureText(text) / 2, startY - mRadius, mPaintText);

/*            float sameMargin;
            if (i > 0 && i < MAX_SHOW_COUNT - 1){
                sameMargin = mMarginBetween * i;
                mProgressDots.get(i).setLeftMargin(sameMargin);
                mProgressDots.get(i).setRightMargin(sameMargin);
            } else if (i == 0) {
                sameMargin = 0;
                mProgressDots.get(i).setLeftMargin(sameMargin);
                mProgressDots.get(i).setRightMargin(sameMargin);
            } else if (i == MAX_SHOW_COUNT - 1){
                sameMargin = mMarginBetween * (MAX_SHOW_COUNT - 1);
                mProgressDots.get(i).setLeftMargin(sameMargin);
                mProgressDots.get(i).setRightMargin(sameMargin);
            }*/

            float margin = i > 0 && i < mShowCount - 1 ? mMarginBetween * i : mMarginBetween * i;
            mProgressDots.get(i).setLeftMargin(margin);
            mProgressDots.get(i).setRightMargin(margin);


//            mProgressDots.get(i).setLeftMargin(i < MAX_SHOW_COUNT - 1 ? mMarginBetween * (i + 1) : mMarginBetween * i);
//            mProgressDots.get(i).setRightMargin(i < MAX_SHOW_COUNT - 1 ? mMarginBetween * (i + 1) : mMarginBetween * i);
            mProgressDots.get(i).setTextPadding(mTextHeight + mRadius);
            mProgressDots.get(i).draw(canvas);
        }


    }



    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
/*                performClick();
                for (ProgressDot progressDot : mProgressDots)
                    if (progressDot.getBackRect().contains(x, y)) {
                        showPopup(progressDot.getRect().centerX(), progressDot.getRect().centerY(), progressDot.getPercent());
                    }*/
                break;

            case MotionEvent.ACTION_DOWN:
                performClick();
                for (ProgressDot progressDot : mProgressDots)
                    if (progressDot.isAnimated()) {
                        progressDot.stopTouchAnimation();
                        break;
                    }
                for (ProgressDot progressDot : mProgressDots) {
                    if (progressDot.getBackRect().contains(x, y)) {
                        progressDot.startTouchAnimation();
                        showPopup(progressDot.getRect().centerX(), progressDot.getRect().centerY(), progressDot.getPercent(), progressDot.getPoints(), progressDot.getDate());
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                dismissDialog();
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float maxWidth = (mShowCount - 1) * mMarginBetween + mRadius;
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

    public void setDataList(final ArrayList<StatsData> arrayList) {
        mProgressDots.clear();
        mShowCount = arrayList.size() < MAX_SHOW_COUNT ? arrayList.size() : MAX_SHOW_COUNT;

        for (StatsData statsData : arrayList) {
            ProgressDot progressDot = new ProgressDot(this);
            // Делаем так, для того, чтобы считать 500 как mMaxPoints (40)
            // 40       - 500
            // progress - X
            // Пропорция
            float progressValue = statsData.getCorrect() * mMaxValue / mMaxPoints;
            progressDot.setProgress(statsData.getCorrect());
            progressDot.setDuration((long) (progressValue - (mProgressAnimateDuration * progressValue / 100)) * 20);
            progressDot.setMaxPoints(mMaxPoints);
            progressDot.setFillColor(mDotColor);
            progressDot.setRadius(mRadius);
            progressDot.setMaxValue(mMaxValue);

            progressDot.setPercent(statsData.getPercent() + "% (" + statsData.getCorrect() + " из " + statsData.getCount() + ")");
            progressDot.setPoints(statsData.getPoint() + getWord(statsData.getPoint(), " баллов"," балл", " балла"));
            progressDot.setDate(statsData.getDate());

            mProgressDots.add(progressDot);
        }
        for (int i = 0; i < mProgressDots.size(); i++) {
            mProgressDots.get(i).start();
        }

    }

    private void showPopup(float x, float y, String percent, String points, String date) {


        mPopupWindow.dismiss();
        mPopupTextViewPoints.setText(points);
        mPopupTextViewPercent.setText(percent);
        mPopupTextViewDate.setText(getTimeAgo(Long.valueOf(date)));

        mPopupCardView.measure(0,0); //Измеряем размер после setText
        float popupWidth = mPopupCardView.getMeasuredWidth();
        float popupHeight = mPopupCardView.getMeasuredHeight();
        this.getLocationInWindow(mLocation);
        int offsetX = mLocation[0]  + (int) (x - popupWidth / 2);
        int offsetY = mLocation[1] + (int) (y - popupHeight);
        mPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, offsetX, offsetY);

        mPopupAnimator.setFloatValues(0f, 1f);
        mPopupAnimator.start();
    }

    private void dismissDialog() {
        if (mTimer != null)
            mTimer.cancel();
        mTimer = new CountDownTimer(POPUP_SHOW_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mPopupAnimator.setFloatValues(mPopupTextViewDate.getAlpha(), 0f);
                mPopupAnimator.start();
                mPopupAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if ((float) mPopupAnimator.getAnimatedValue() == 0) {
                            mPopupWindow.dismiss();
                            for (ProgressDot progressDot : mProgressDots)
                                if (progressDot.isAnimated())
                                    progressDot.stopTouchAnimation();
                            System.out.println("dismiss");
                        }
                    }
                });
            }
        }.start();

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
        mPopupTextViewPoints.setTypeface(typeface);
        mPopupTextViewPercent.setTypeface(typeface);
        mPopupTextViewDate.setTypeface(typeface);
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

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "";
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Только что";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "Минуту назад";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return (diff / MINUTE_MILLIS) + getWord((int) (diff / MINUTE_MILLIS), " минут назад"," минуту назад", " минуты назад");
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "Час назад";
        } else if (diff < 24 * HOUR_MILLIS) {
            return (diff / HOUR_MILLIS) + getWord((int) (diff / HOUR_MILLIS), " часов назад"," час назад", " часа назад");
        } else if (diff < 48 * HOUR_MILLIS) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            StringBuilder hours = new StringBuilder(String.valueOf(h));
            StringBuilder minutes = new StringBuilder(String.valueOf(m));
            if (h < 10)
                hours.insert(0,"0");
            if (m < 10)
                minutes.insert(0,"0");
            return "Вчера (" + hours + ":" + minutes + ")";
        } else {
            return (diff / DAY_MILLIS) + getWord((int) (diff / DAY_MILLIS), " дней назад"," день назад", " дня назад");
        }
    }

    public String getWord(int count, String word0, String word1, String word2) {
        int rem = count % 100;
        if (rem < 11 || rem > 14) {
            rem = count % 10;
            if (rem == 1) return word1;
            if (rem >= 2 && rem <= 4) return word2;
        }
        return word0;
    }
}
