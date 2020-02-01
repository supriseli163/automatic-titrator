package com.jh.automatic_titrator.entity.common;

/**
 * 主滴定剂
 */
public class MainTitrant {
    //试剂名称
    private String reagentName;
    //理论浓度
    private double theoreticalConcentration;

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
