package com.jh.automatic_titrator.entity.common;

import java.util.List;

/**
 * Created by apple on 2016/12/19.
 */

public class FactoryInitInfo {
    private double temperature;

    private List<Integer> waveLengths;

    private boolean showLogo;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public List<Integer> getWaveLengths() {
        return waveLengths;
    }

    public void setWaveLengths(List<Integer> waveLengths) {
        this.waveLengths = waveLengths;
    }

    public boolean isShowLogo() {
        return showLogo;
    }

    public void setShowLogo(boolean showLogo) {
        this.showLogo = showLogo;
    }
}
