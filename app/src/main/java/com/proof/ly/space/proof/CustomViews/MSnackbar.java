package com.proof.ly.space.proof.CustomViews;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.MSnackUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;
/**
 * Created by aman on 4/28/18.
 */

public class MSnackbar {
    private enum Type {
        DEFAULT(null, null, null);

        private Integer mColor;
        private Integer mIconResId;
        private Integer mStandardTextColor;

        Type(@ColorInt Integer color, @DrawableRes Integer iconResId, @ColorInt Integer standardTextColor) {
            this.mColor = color;
            this.mIconResId = iconResId;
            this.mStandardTextColor = standardTextColor;
        }

        public Integer getColor() {
            return mColor;
        }

        public Drawable getIcon(Context context) {
            if (mIconResId == null) return null;
            Drawable drawable = ContextCompat.getDrawable(context, mIconResId);
            if (drawable != null) {
                drawable = MSnackUtils.tintDrawable(drawable, mStandardTextColor);
            }
            return drawable;
        }


        public Integer getStandardTextColor() {
            return mStandardTextColor;
        }
    }

    private final Builder mBuilder;

    private MSnackbar(Builder builder) {
        this.mBuilder = builder;
    }

    private Snackbar make() {

        Snackbar snackbar = Snackbar.make(mBuilder.view, mBuilder.text, mBuilder.duration);

        if (mBuilder.actionClickListener != null || mBuilder.actionText != null) {
            if (mBuilder.actionClickListener == null) mBuilder.actionClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
            snackbar.setAction(mBuilder.actionText, mBuilder.actionClickListener);
            if (mBuilder.actionTextColor == null) mBuilder.actionTextColor = mBuilder.type.getStandardTextColor();
            if (mBuilder.actionTextColors != null) snackbar.setActionTextColor(mBuilder.actionTextColors);
            else if (mBuilder.actionTextColor != null) snackbar.setActionTextColor(mBuilder.actionTextColor);

        }

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        if (mBuilder.backgroundColor == null) mBuilder.backgroundColor = mBuilder.type.getColor();
        if (mBuilder.backgroundColor != null) snackbarLayout.setBackgroundColor(mBuilder.backgroundColor);

        TextView actionText = snackbarLayout.findViewById(android.support.design.R.id.snackbar_action);
        if (mBuilder.actionTextSize != null) {
            if (mBuilder.actionTextSizeUnit != null) actionText.setTextSize(mBuilder.actionTextSizeUnit, mBuilder.actionTextSize);
            else actionText.setTextSize(mBuilder.actionTextSize);
        }
        Typeface actionTextTypeface = actionText.getTypeface();
        if (mBuilder.actionTextTypeface != null)
            actionTextTypeface = mBuilder.actionTextTypeface;
        if (mBuilder.actionTextTypefaceStyle != null) {
            actionText.setTypeface(actionTextTypeface, mBuilder.actionTextTypefaceStyle);
        } else {
            actionText.setTypeface(actionTextTypeface);
        }


        TextView text = snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);

        if (mBuilder.textSize != null) {
            if (mBuilder.textSizeUnit != null) text.setTextSize(mBuilder.textSizeUnit, mBuilder.textSize);
            else text.setTextSize(mBuilder.textSize);
        }

        Typeface textTypeface = text.getTypeface();
        if (mBuilder.textTypeface != null)
            textTypeface = mBuilder.textTypeface;
        if (mBuilder.textTypefaceStyle != null) {
            text.setTypeface(textTypeface, mBuilder.textTypefaceStyle);
        } else {
            text.setTypeface(textTypeface);
        }


        if (mBuilder.textColor == null) mBuilder.textColor = mBuilder.type.getStandardTextColor();
        if (mBuilder.textColors != null) text.setTextColor(mBuilder.textColors);
        else if (mBuilder.textColor != null) text.setTextColor(mBuilder.textColor);
        text.setMaxLines(mBuilder.maxLines);
        text.setGravity(mBuilder.centerText ? Gravity.CENTER : Gravity.CENTER_VERTICAL);
        if (mBuilder.centerText && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        if (mBuilder.icon == null) mBuilder.icon = mBuilder.type.getIcon(mBuilder.view.getContext());
        if (mBuilder.icon != null) {
            Drawable transparentHelperDrawable = null;
            if (mBuilder.centerText && TextUtils.isEmpty(mBuilder.actionText)) {
                transparentHelperDrawable = MSnackUtils.makeTransparentDrawable(mBuilder.view.getContext(),
                        mBuilder.icon.getIntrinsicWidth(),
                        mBuilder.icon.getIntrinsicHeight());
            }
            text.setCompoundDrawablesWithIntrinsicBounds(mBuilder.icon, null, transparentHelperDrawable, null);
            text.setCompoundDrawablePadding(text.getResources()
                    .getDimensionPixelOffset(R.dimen.snacky_icon_padding));
        }


        return snackbar;
    }

    public static Builder builder() {
        return new Builder();
    }

    @RestrictTo(LIBRARY_GROUP)
    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @IntRange(from = 1)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    public static final int LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE;
    public static final int LENGTH_SHORT      = Snackbar.LENGTH_SHORT;
    public static final int LENGTH_LONG       = Snackbar.LENGTH_LONG;


    public static class Builder {

        private View                 view                    = null;
        private Type                 type                    = Type.DEFAULT;
        private int                  duration                = Snackbar.LENGTH_SHORT;
        private CharSequence         text                    = "";
        private int                  textResId               = 0;
        private Integer              textColor               = null;
        private ColorStateList       textColors              = null;
        private Integer              textSizeUnit            = null;
        private Float                textSize                = null;
        private Integer              textTypefaceStyle       = null;
        private Typeface             textTypeface            = null;
        private Integer              actionTextSizeUnit      = null;
        private Float                actionTextSize          = null;
        private CharSequence         actionText              = "";
        private int                  actionTextResId         = 0;
        private Integer              actionTextTypefaceStyle = null;
        private Typeface             actionTextTypeface      = null;
        private View.OnClickListener actionClickListener     = null;
        private Integer              actionTextColor         = null;
        private ColorStateList       actionTextColors        = null;
        private int                  maxLines                = Integer.MAX_VALUE;
        private boolean              centerText              = false;
        private Drawable             icon                    = null;
        private int                  iconResId               = 0;
        private Integer              backgroundColor         = null;

        private Builder() {
        }

        public Builder setActivity(Activity activity) {
            return setView(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public Builder setText(@StringRes int resId) {
            this.textResId = resId;
            return this;
        }

        public Builder setText(CharSequence text) {
            this.textResId = 0;
            this.text = text;
            return this;
        }

        public Builder setTextColor(@ColorInt int color) {
            this.textColor = color;
            return this;
        }

        public Builder setTextColor(ColorStateList colorStateList) {
            this.textColor = null;
            this.textColors = colorStateList;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSizeUnit = null;
            this.textSize = textSize;
            return this;
        }

        public Builder setTextSize(int unit, float textSize) {
            this.textSizeUnit = unit;
            this.textSize = textSize;
            return this;
        }

        public Builder setTextTypeface(Typeface typeface) {
            this.textTypeface = typeface;
            return this;
        }

        public Builder setTextTypefaceStyle(int style) {
            this.textTypefaceStyle = style;
            return this;
        }

        public Builder centerText() {
            this.centerText = true;
            return this;
        }

        public Builder setActionTextColor(ColorStateList colorStateList) {
            this.actionTextColor = null;
            this.actionTextColors = colorStateList;
            return this;
        }

        public Builder setActionTextColor(@ColorInt int color) {
            this.actionTextColor = color;
            return this;
        }

        public Builder setActionText(@StringRes int resId) {
            this.actionTextResId = resId;
            return this;
        }

        public Builder setActionText(CharSequence text) {
            this.textResId = 0;
            this.actionText = text;
            return this;
        }

        public Builder setActionTextSize(float textSize) {
            this.actionTextSizeUnit = null;
            this.actionTextSize = textSize;
            return this;
        }

        public Builder setActionTextSize(int unit, float textSize) {
            this.actionTextSizeUnit = unit;
            this.actionTextSize = textSize;
            return this;
        }

        public Builder setActionTextTypeface(Typeface typeface) {
            this.actionTextTypeface = typeface;
            return this;
        }


        public Builder setActionTextTypefaceStyle(int style) {
            this.actionTextTypefaceStyle = style;
            return this;
        }

        public Builder setActionClickListener(View.OnClickListener listener) {
            this.actionClickListener = listener;
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            this.maxLines = maxLines;
            return this;
        }

        public Builder setDuration(@Duration int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setIcon(@DrawableRes int resId) {
            this.iconResId = resId;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.icon = drawable;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        public Snackbar build() {
            return make();
        }

        public Snackbar success() {
            type = Type.DEFAULT;
            return make();
        }

        public Snackbar info() {
            type = Type.DEFAULT;
            return make();
        }

        public Snackbar warning() {
            type = Type.DEFAULT;
            return make();
        }

        public Snackbar error() {
            type = Type.DEFAULT;
            return make();
        }

        private Snackbar make() {
            if (view == null)
                throw new IllegalStateException("Snacky Error: You must set an Activity or a View before making a snack");
            if (textResId != 0) text = view.getResources()
                    .getText(textResId);
            if (actionTextResId != 0) actionText = view.getResources()
                    .getText(actionTextResId);
            if (iconResId != 0) icon = ContextCompat.getDrawable(view.getContext(), iconResId);
            return new MSnackbar(this).make();
        }
    }


}

