package com.jh.automatic_titrator.common.formula;

import com.jh.automatic_titrator.entity.common.Conversion;
import com.jh.automatic_titrator.entity.common.Formula;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/1/7.
 */

public class FormulaUtil {

    public static final int CONVERSION_NULL = 0;

    public static final int RANGE_ILLEGAL = -1;

    public static final int LEGAL = 1;

    public static final double MAX_RANGE = 90;

    public static final double MIN_RANGE = -90;

    public static double opticalToSugar(double value) {
        return value * 2.888;
    }

    public static double sugarToOptical(double value) {
        return value * 0.34626;
    }

    public static int checkFormulaLegal(Formula formula) {
        if (formula.getConversions() == null || formula.getConversions().size() == 0) {
            return CONVERSION_NULL;
        }
        List<Double> ranges = new ArrayList<>();
        for (Conversion conversion : formula.getConversions()) {
            if (conversion.getStart() > conversion.getEnd()
                    || conversion.getEnd() > MAX_RANGE
                    || conversion.getStart() < MIN_RANGE) {
                return RANGE_ILLEGAL;
            }
            for (int i = 0; i < ranges.size(); i = i + 2) {
                if ((conversion.getStart() > ranges.get(i) && conversion.getStart() < ranges.get(i + 1))
                        || (conversion.getEnd() > ranges.get(i) && conversion.getEnd() < ranges.get(i + 1))) {
                    return RANGE_ILLEGAL;
                }
            }
        }

        return LEGAL;
    }

    public static double getFormulaRes(Formula formula, double res) {

        return 0;
    }

    public static double getConcentration(double res, double a, double c, double t) {
        return res / a * c;
    }

    public static double getSpecificRotation(double res, double l, double c, double t) {
        return res / l * c;
    }
}
