package com.jh.automatic_titrator.entity.common.titrator;

import java.util.Date;

/**
 * 辅助试剂
 */
public class AuxiliaryReagent {
    private int id;
    //滴定管
    private String burette;
    //试剂名称
    private String reagentName;
    //试剂浓度
    private String reagentConcentration;
    //单位
    private String unit;
    //添加体积
    private String addVolume;
    //速度
    private String addSpeed;
    //添加时间
    private String addTime;

    //创建时间
    private Date crateTime;
    //更新时间
    private Date updateTime;
    //操作人
    private String operator;

    public String getBurette() {
        return burette;
    }

    public void setBurette(String burette) {
        this.burette = burette;
    }

    public String getReagentName() {
        return reagentName;
    }

    public void setReagentName(String reagentName) {
        this.reagentName = reagentName;
    }

    public String getReagentConcentration() {
        return reagentConcentration;
    }

    public void setReagentConcentration(String reagentConcentration) {
        this.reagentConcentration = reagentConcentration;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAddVolume() {
        return addVolume;
    }

    public void setAddVolume(String addVolume) {
        this.addVolume = addVolume;
    }

    public String getAddSpeed() {
        return addSpeed;
    }

    public void setAddSpeed(String addSpeed) {
        this.addSpeed = addSpeed;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
