package com.jh.automatic_titrator.entity.common;

/**
 * 主滴定剂
 */
public class PreTitrant {
    // 预滴定
    private double preAddVolume;

    // 预滴定后搅拌时间
    private double preAfterstiringTime;

    public double getPreAddVolume() {
        return preAddVolume;
    }

    public void setPreAddVolume(double preAddVolume) {
        this.preAddVolume = preAddVolume;
    }

    public double getPreAfterstiringTime() {
        return preAfterstiringTime;
    }

    public void setPreAfterstiringTime(double preAfterstiringTime) {
        this.preAfterstiringTime = preAfterstiringTime;
    }
}
