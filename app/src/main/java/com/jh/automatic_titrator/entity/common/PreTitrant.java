package com.jh.automatic_titrator.entity.common;

/**
 * 主滴定剂
 */
public class PreTitrant {
    // 预滴定添加体积
    private double preAddVolume;

    // 预滴定后搅拌时间
    private double preAfterStiringTime;

    public double getPreAddVolume() {
        return preAddVolume;
    }

    public void setPreAddVolume(double preAddVolume) {
        this.preAddVolume = preAddVolume;
    }

    public double getPreAfterStiringTime() {
        return preAfterStiringTime;
    }

    public void setPreAfterStiringTime(double preAfterstiringTime) {
        this.preAfterStiringTime = preAfterstiringTime;
    }
}
