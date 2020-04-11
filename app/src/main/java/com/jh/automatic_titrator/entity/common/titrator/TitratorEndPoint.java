package com.jh.automatic_titrator.entity.common.titrator;

/**
 * 滴定终点
 */
public class TitratorEndPoint {
    private int id;
    private String pointJump;
    private String range;
    private String delayTime;
    private String resultUnit;

    public String getPointJump() {
        return pointJump;
    }

    public void setPointJump(String pointJump) {
        this.pointJump = pointJump;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }
}
