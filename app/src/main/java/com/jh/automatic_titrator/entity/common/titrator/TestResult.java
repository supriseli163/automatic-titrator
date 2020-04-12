package com.jh.automatic_titrator.entity.common.titrator;

/**
 * 测试结果
 */
public class TestResult {
    //PK
    private int id;
    //测试id
    private int testId;
    //测试方法id
    private int titratorMethodId;
    //终点序号
    private int endNumber;
    //体积
    private double volume;
    //滴定剂浓度
    private double titrantConcentration;
    //空白体积
    private double blankVolume;
    //样品量
    private double sampleSize;
    //样品系数
    private double sampleCoefficient;
    //结果
    private double result;

}
