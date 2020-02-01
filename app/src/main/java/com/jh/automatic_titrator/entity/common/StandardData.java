package com.jh.automatic_titrator.entity.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by apple on 2016/12/31.
 */

public class StandardData implements Comparable<StandardData> {

    private int id;

    private double temperature;

    private double standardValue;

    private double testValue;

    private int waveIndex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(double standardValue) {
        this.standardValue = standardValue;
    }

    public double getTestValue() {
        return testValue;
    }

    public void setTestValue(double testValue) {
        this.testValue = testValue;
    }

    public int getWaveIndex() {
        return waveIndex;
    }

    public void setWaveIndex(int waveIndex) {
        this.waveIndex = waveIndex;
    }

    @Override
    public int compareTo(StandardData other) {
        if (Math.abs(this.getStandardValue()) < 0.0001 || other.getStandardValue() < this.getStandardValue()) {
            return -1;
        } else if (Math.abs(other.getStandardValue()) < 0.0001 || other.getStandardValue() > this.getStandardValue()) {
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        List<StandardData> standardDatas = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            StandardData standardData = new StandardData();
            if (i % 10 == 0) {
                standardData.setStandardValue(0.00001);
            } else if (i % 5 == 0) {
                standardData.setStandardValue(-0.00001);
            } else {
                standardData.setStandardValue(random.nextInt() / 10000.0);
            }
            standardDatas.add(standardData);
        }

        Collections.sort(standardDatas);
        for (int i = 0; i < 100; i++) {
            System.out.println(standardDatas.get(i).getStandardValue());
        }
    }
}
