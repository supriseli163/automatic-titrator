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
}
