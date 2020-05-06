package com.jh.automatic_titrator.common.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.R;

public class ViewUtils {

    public static void setViewVisibleOrGone(View mView, boolean show) {
        if (mView != null) {
            mView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public static void setViewVisibleOrInVisible(View mView, boolean show) {
        if (mView != null) {
            mView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }


    public static int dpToPx(int dp) {
        float density;
        density = BaseApplication.getApplication().getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static void setTextViewColor(int color, TextView tv) {
        tv.setTextColor(BaseApplication.getApplication().getResources().getColor(color));
        tv.setGravity(Gravity.CENTER);
    }
}