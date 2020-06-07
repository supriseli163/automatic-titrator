package com.jh.automatic_titrator.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import com.jh.automatic_titrator.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by violet_k on 6/1 2020.
 */
public class RoundTextView extends AppCompatTextView {
    private int rtvBorderWidth, rtvBorderColor, rtvBgColor;
    private float rtvRadius;

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundTextView, defStyleAttr, 0);

        if (attributes != null) {

            rtvBorderWidth = attributes.getDimensionPixelSize(R.styleable.RoundTextView_rtvBorderWidth, 0);
            rtvBorderColor = attributes.getColor(R.styleable.RoundTextView_rtvBorderColor, Color.BLACK);
            rtvRadius = attributes.getDimension(R.styleable.RoundTextView_rtvRadius, 0);
            rtvBgColor = attributes.getColor(R.styleable.RoundTextView_rtvBgColor, Color.WHITE);
            attributes.recycle();
            setRTVRadius(rtvRadius);
        }
    }

    public void setRTVRadius(float rtvRadius) {
        this.rtvRadius = rtvRadius;
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(rtvBgColor);
        gd.setCornerRadius(rtvRadius);
        if (rtvBorderWidth > 0) {
            gd.setStroke(rtvBorderWidth, rtvBorderColor);
        }

        this.setBackground(gd);
    }

    public void setBackgroungColor(@ColorInt int color) {
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setColor(color);
    }
}
