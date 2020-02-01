package com.jh.automatic_titrator.common.formula;

/**
 * Created by apple on 2017/1/7.
 */

public class BaseFormulaUtil {

    public static double changeTemperature(int type, double value) {
        switch (type) {
            case 0:
                return value;
            case 1:
                return value * 1.8 + 32;
            case 2:
                return value + 273.15;
            default:
                return value;
        }
    }

    public static double changeTemperatureTo0(int type, double value) {
        switch (type) {
            case 0:
                return value;
            case 1:
                return (value - 32) / 1.8;
            case 2:
                return value - 273.15;
            default:
                return value;
        }
    }

    public static double changeConcentrationToGML(int type, double value) {
        switch (type) {
            case 0:
                return value;
            case 1:
                return value / 1000;
            case 2:
                return value / 10;
        }
        return value;
    }
}
