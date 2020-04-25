package com.jh.automatic_titrator.common.utils;

import android.view.View;

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
}