package com.jh.automatic_titrator.entity;

import com.jh.automatic_titrator.entity.common.Formula;

/**
 * Created by apple on 2017/2/19.
 */
public class OpticalRotation extends Formula {

    public static final int DEFAULT_DECIMAL = 4;

    @Override
    public String getFormulaName() {
        return "旋光度";
    }

    @Override
    public String getUnit() {
//        return "°";
        return "";
    }

    @Override
    public String getSimpleName() {
        return "α";
    }

    @Override
    public boolean isShowPercent() {
        return false;
    }

    @Override
    public int getDecimal() {
        return DEFAULT_DECIMAL;
    }

    @Override
    public double getDesRes(double res, double t, double c, double a, double l) {
        return res;
    }
}
