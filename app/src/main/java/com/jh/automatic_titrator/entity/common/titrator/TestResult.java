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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getTitratorMethodId() {
        return titratorMethodId;
    }

    public void setTitratorMethodId(int titratorMethodId) {
        this.titratorMethodId = titratorMethodId;
    }

    public int getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(int endNumber) {
        this.endNumber = endNumber;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getTitrantConcentration() {
        return titrantConcentration;
    }

    public void setTitrantConcentration(double titrantConcentration) {
        this.titrantConcentration = titrantConcentration;
    }

    public double getBlankVolume() {
        return blankVolume;
    }

    public void setBlankVolume(double blankVolume) {
        this.blankVolume = blankVolume;
    }

    public double getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(double sampleSize) {
        this.sampleSize = sampleSize;
    }

    public double getSampleCoefficient() {
        return sampleCoefficient;
    }

    public void setSampleCoefficient(double sampleCoefficient) {
        this.sampleCoefficient = sampleCoefficient;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
