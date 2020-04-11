package com.jh.automatic_titrator.entity.common.titrator;

/**
 * 滴定方法
 */
public class TitratorMethod {
    //测试id
    private int id;
    //测试方法Id
    private String titratorMethodId;
    //滴定类型
    private String titratorType;
    //方法名
    private String methodName;
    //滴定管体积
    private double buretteVolume;
    //工作电级
    private double workingElectrode;
    //参比电级(参考电极)
    private double referenceElectrode;
    //样品计量单位
    private String sampleMeasurementUnit;
    //滴定显示单位
    private String titrationDisplayUnit;
    //补液速度
    private String replenishmentSpeed;
    //搅拌速度
    private String stiringSpeed;
    //电极平衡时间
    private String electroedEquilibrationTime;
    //电极平衡电位
    private String electroedEquilibriumPotential;
    //预搅拌时间
    private String preStiringTime;
    //每次添加体积
    private String perAddVolume;
    //结束体积
    private String endVolume;
    //滴定速度
    private String titrationSpeed;
    //慢滴体积
    private String slowTitrationVolume;
    //快滴体积
    private String fastTitrationVolume;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitratorMethodId() {
        return titratorMethodId;
    }

    public void setTitratorMethodId(String titratorMethodId) {
        this.titratorMethodId = titratorMethodId;
    }

    public String getTitratorType() {
        return titratorType;
    }

    public void setTitratorType(String titratorType) {
        this.titratorType = titratorType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public double getBuretteVolume() {
        return buretteVolume;
    }

    public void setBuretteVolume(double buretteVolume) {
        this.buretteVolume = buretteVolume;
    }

    public double getWorkingElectrode() {
        return workingElectrode;
    }

    public void setWorkingElectrode(double workingElectrode) {
        this.workingElectrode = workingElectrode;
    }

    public double getReferenceElectrode() {
        return referenceElectrode;
    }

    public void setReferenceElectrode(double referenceElectrode) {
        this.referenceElectrode = referenceElectrode;
    }

    public String getSampleMeasurementUnit() {
        return sampleMeasurementUnit;
    }

    public void setSampleMeasurementUnit(String sampleMeasurementUnit) {
        this.sampleMeasurementUnit = sampleMeasurementUnit;
    }

    public String getTitrationDisplayUnit() {
        return titrationDisplayUnit;
    }

    public void setTitrationDisplayUnit(String titrationDisplayUnit) {
        this.titrationDisplayUnit = titrationDisplayUnit;
    }

    public String getReplenishmentSpeed() {
        return replenishmentSpeed;
    }

    public void setReplenishmentSpeed(String replenishmentSpeed) {
        this.replenishmentSpeed = replenishmentSpeed;
    }

    public String getStiringSpeed() {
        return stiringSpeed;
    }

    public void setStiringSpeed(String stiringSpeed) {
        this.stiringSpeed = stiringSpeed;
    }

    public String getElectroedEquilibrationTime() {
        return electroedEquilibrationTime;
    }

    public void setElectroedEquilibrationTime(String electroedEquilibrationTime) {
        this.electroedEquilibrationTime = electroedEquilibrationTime;
    }

    public String getElectroedEquilibriumPotential() {
        return electroedEquilibriumPotential;
    }

    public void setElectroedEquilibriumPotential(String electroedEquilibriumPotential) {
        this.electroedEquilibriumPotential = electroedEquilibriumPotential;
    }

    public String getPreStiringTime() {
        return preStiringTime;
    }

    public void setPreStiringTime(String preStiringTime) {
        this.preStiringTime = preStiringTime;
    }

    public String getPerAddVolume() {
        return perAddVolume;
    }

    public void setPerAddVolume(String perAddVolume) {
        this.perAddVolume = perAddVolume;
    }

    public String getEndVolume() {
        return endVolume;
    }

    public void setEndVolume(String endVolume) {
        this.endVolume = endVolume;
    }

    public String getTitrationSpeed() {
        return titrationSpeed;
    }

    public void setTitrationSpeed(String titrationSpeed) {
        this.titrationSpeed = titrationSpeed;
    }

    public String getSlowTitrationVolume() {
        return slowTitrationVolume;
    }

    public void setSlowTitrationVolume(String slowTitrationVolume) {
        this.slowTitrationVolume = slowTitrationVolume;
    }

    public String getFastTitrationVolume() {
        return fastTitrationVolume;
    }

    public void setFastTitrationVolume(String fastTitrationVolume) {
        this.fastTitrationVolume = fastTitrationVolume;
    }
}
