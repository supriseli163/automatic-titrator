package com.jh.automatic_titrator.entity.common;

import java.util.List;

/**
 * Created by apple on 2017/1/7.
 */

public class Formula {

    private int id;

    private String formulaName;

    private String simpleName;

    private String unit;

    private int decimal;

    private boolean showPercent;

    private String createDate;

    private String desc;

    private String creator;

    private List<Conversion> conversions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDecimal() {
        return decimal;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public boolean isShowPercent() {
        return showPercent;
    }

    public void setShowPercent(boolean showPercent) {
        this.showPercent = showPercent;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<Conversion> getConversions() {
        return conversions;
    }

    public void setConversions(List<Conversion> conversions) {
        this.conversions = conversions;
    }

    public double getDesRes(double res, double t, double c, double a, double l) {
        for (Conversion conversion : conversions) {
            if (res > conversion.getStart() && res <= conversion.getEnd()) {
                double desres = conversion.getP0();
                desres += conversion.getP1() * res;
                desres += conversion.getP2() * Math.pow(res, 2);
                desres += conversion.getP3() * Math.pow(res, 3);
                desres += conversion.getP4() * Math.pow(res, 4);
                desres += conversion.getP5() * Math.pow(res, 5);
                return desres;
            }
        }
        return 0;
    }
}
