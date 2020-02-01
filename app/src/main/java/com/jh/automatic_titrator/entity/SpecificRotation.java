package com.jh.automatic_titrator.entity;

import com.jh.automatic_titrator.entity.common.Formula;

/**
 * Created by apple on 2017/2/19.
 */

public class SpecificRotation extends Formula {

    @Override
    public String getFormulaName() {
        return "比旋度";
    }

    @Override
    public String getUnit() {
//        return "°";
        return "";
    }

    @Override
    public String getSimpleName() {
        return "[α]";
    }

    @Override
    public boolean isShowPercent() {
        return false;
    }

    @Override
    public int getDecimal() {
        return 3;
    }

    @Override
    public double getDesRes(double res, double t, double c, double a, double l) {
        return res / l / c;
    }
}
