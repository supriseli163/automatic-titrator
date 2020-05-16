package com.jh.automatic_titrator.entity.common.titrator;

/**
 * 滴定终点设置
 */
public class EndPointSetting {
    //主键id
    private int id;
    //滴定方法id
    private int titratorMethodId;
    //滴定管体积，枚举类型 5ml、10ml,25ml
    private int burette;
    //试剂名称
    private String reagentName;
    //试剂浓度
    private double reagentConcentration;
    //试剂浓度
    private String reagentConcentrationUnit;
    //添加体积
    private double addVolume;
    //添加速度(枚举类型): 1、2、3
    private int addSpeed;
    //添加时间(枚举类型):滴定前、滴定中、滴定后
    private String addTime;
    //参考终点(枚举类型):0、1、2、3、4、5
    private int referenceEndPoint;
    //延时时间(s)
    private int delayTime;

    // TODO: 2020-05-16需要补充数据库

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTitratorMethodId() {
        return titratorMethodId;
    }

    public void setTitratorMethodId(int titratorMethodId) {
        this.titratorMethodId = titratorMethodId;
    }

    public int getBurette() {
        return burette;
    }

    public void setBurette(int burette) {
        this.burette = burette;
    }

    public String getReagentName() {
        return reagentName;
    }

    public void setReagentName(String reagentName) {
        this.reagentName = reagentName;
    }

    public double getReagentConcentration() {
        return reagentConcentration;
    }

    public void setReagentConcentration(double reagentConcentration) {
        this.reagentConcentration = reagentConcentration;
    }

    public String getReagentConcentrationUnit() {
        return reagentConcentrationUnit;
    }

    public void setReagentConcentrationUnit(String reagentConcentrationUnit) {
        this.reagentConcentrationUnit = reagentConcentrationUnit;
    }

    public double getAddVolume() {
        return addVolume;
    }

    public void setAddVolume(double addVolume) {
        this.addVolume = addVolume;
    }

    public int getAddSpeed() {
        return addSpeed;
    }

    public void setAddSpeed(int addSpeed) {
        this.addSpeed = addSpeed;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getReferenceEndPoint() {
        return referenceEndPoint;
    }

    public void setReferenceEndPoint(int referenceEndPoint) {
        this.referenceEndPoint = referenceEndPoint;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }
}
