package com.jh.automatic_titrator.entity.common;

import java.io.Serializable;

/**
 * Created by apple on 2016/10/18.
 */
public class SingleResult implements Serializable {

    private String resType;

    private double optical;

    private double res;

    private String resUnit;

    private int decimal;

    private String wantedTemperature;

    private String realTemperature;

    private String temperatureType;

    private String date;

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public double getOptical() {
        return optical;
    }

    public void setOptical(double optical) {
        this.optical = optical;
    }

    public double getRes() {
        return res;
    }

    public void setRes(double res) {
        this.res = res;
    }

    public String getWantedTemperature() {
        return wantedTemperature;
    }

    public void setWantedTemperature(String wantedTemperature) {
        this.wantedTemperature = wantedTemperature;
    }

    public String getRealTemperature() {
        return realTemperature;
    }

    public void setRealTemperature(String realTemperature) {
        this.realTemperature = realTemperature;
    }

    public String getTemperatureType() {
        return temperatureType;
    }

    public void setTemperatureType(String temperatureType) {
        this.temperatureType = temperatureType;
    }

    public String getResUnit() {
        return resUnit;
    }

    public void setResUnit(String resUnit) {
        this.resUnit = resUnit;
    }

    public int getDecimal() {
        return decimal;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
