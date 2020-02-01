package com.jh.automatic_titrator.entity;

import com.jh.automatic_titrator.entity.common.Formula;

/**
 * Created by apple on 2017/2/19.
 */

public class Concentration extends Formula {

    @Override
    public String getFormulaName() {
        return "浓度";
    }

    @Override
    public boolean isShowPercent() {
        return false;
    }

    @Override
    public String getSimpleName() {
        return "C";
    }

    @Override
    public String getUnit() {
        return "g/100ml";
    }

    @Override
    public int getDecimal() {
        return 3;
    }

    @Override
    public double getDesRes(double res, double t, double c, double a, double l) {
        return Math.abs(res / a / l);
    }
}
