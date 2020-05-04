package com.jh.automatic_titrator.common.utils;

import android.view.View;

import com.jh.automatic_titrator.BaseApplication;

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
}