package com.jh.automatic_titrator.entity.common;

/**
 * Created by apple on 2016/10/16.
 */
public class TestMethod {

    private int id;

    private String testName;

    private int testCount;

    private Double concentration;

    private int concentrationType;

    private int accuracy;

    private String formulaName;

    private String waveLength;

    private int decimals;

    private Double testTubeLength;

    private Double specificRotation;

    private int atlasX;

    private int atlasY;

    private boolean useTemperature;

    private int temperatureType;

    private double temperature;

    private boolean autoTest;

    private int autoTestInterval;

    private int autoTestTimes;

    private String createDate;

    private String creator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }

    public Double getConcentration() {
        return concentration;
    }

    public void setConcentration(Double concentration) {
        this.concentration = concentration;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getWaveLength() {
        return waveLength;
    }

    public void setWaveLength(String waveLength) {
        this.waveLength = waveLength;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public Double getTestTubeLength() {
        return testTubeLength;
    }

    public void setTestTubeLength(Double testTubeLength) {
        this.testTubeLength = testTubeLength;
    }

    public Double getSpecificRotation() {
        return specificRotation;
    }

    public void setSpecificRotation(Double specificRotation) {
        this.specificRotation = specificRotation;
    }

    public int getAtlasX() {
        return atlasX;
    }

    public void setAtlasX(int atlasX) {
        this.atlasX = atlasX;
    }

    public int getAtlasY() {
        return atlasY;
    }

    public void setAtlasY(int atlasY) {
        this.atlasY = atlasY;
    }

    public boolean isUseTemperature() {
        return useTemperature;
    }

    public void setUseTemperature(boolean useTemperature) {
        this.useTemperature = useTemperature;
    }

    public int getTemperatureType() {
        return temperatureType;
    }

    public void setTemperatureType(int temperatureType) {
        this.temperatureType = temperatureType;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public boolean isAutoTest() {
        return autoTest;
    }

    public void setAutoTest(boolean autoTest) {
        this.autoTest = autoTest;
    }

    public int getConcentrationType() {
        return concentrationType;
    }

    public void setConcentrationType(int concentrationType) {
        this.concentrationType = concentrationType;
    }

    public int getAutoTestInterval() {
        return autoTestInterval;
    }

    public void setAutoTestInterval(int autoTestInterval) {
        this.autoTestInterval = autoTestInterval;
    }

    public int getAutoTestTimes() {
        return autoTestTimes;
    }

    public void setAutoTestTimes(int autoTestTimes) {
        this.autoTestTimes = autoTestTimes;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
