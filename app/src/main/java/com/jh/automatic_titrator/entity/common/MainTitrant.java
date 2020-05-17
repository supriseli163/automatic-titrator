package com.jh.automatic_titrator.entity.common;

/**
 * 主滴定剂
 */
public class MainTitrant {
    // 试剂名称
    private String reagentName;
    // 理论浓度
    private double theoreticalConcentration;
    // 理论浓度单位
    private String theoreticalConcentrationUnit;

    public String getTheoreticalConcentrationUnit() {
        return theoreticalConcentrationUnit;
    }

    public void setTheoreticalConcentrationUnit(String theoreticalConcentrationUnit) {
        this.theoreticalConcentrationUnit = theoreticalConcentrationUnit;
    }

    public String getReagentName() {
        return reagentName;
    }

    public void setReagentName(String reagentName) {
        this.reagentName = reagentName;
    }

    public double getTheoreticalConcentration() {
        return theoreticalConcentration;
    }

    public void setTheoreticalConcentration(double theoreticalConcentration) {
        this.theoreticalConcentration = theoreticalConcentration;
    }
}
