package com.jh.automatic_titrator.entity.common.titrator;

/**
 * 滴定终点
 */
public class TitratorEndPoint {
    //PK
    private int id;
    //滴定方法id
    private int titratorMethodId;
    //终点值对象
    private double endPointValue;
    //预控值
    private double preControlvalue;
    //相关系数
    private double correlationCoefficient;
    //测试结构单位(枚举类型): %、g/ml、mol/L
    private String resultUnit;

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

    public double getEndPointValue() {
        return endPointValue;
    }

    public void setEndPointValue(double endPointValue) {
        this.endPointValue = endPointValue;
    }

    public double getPreControlvalue() {
        return preControlvalue;
    }

    public void setPreControlvalue(double preControlvalue) {
        this.preControlvalue = preControlvalue;
    }

    public double getCorrelationCoefficient() {
        return correlationCoefficient;
    }

    public void setCorrelationCoefficient(double correlationCoefficient) {
        this.correlationCoefficient = correlationCoefficient;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }
}
