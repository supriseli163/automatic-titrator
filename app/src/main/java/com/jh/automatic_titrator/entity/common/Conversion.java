package com.jh.automatic_titrator.entity.common;

/**
 * Created by apple on 2017/1/7.
 */

public class Conversion {

    private int id;

    private int formulaId;

    private Double start;

    private Double end;

    private double p5 = 1;

    private double p4 = 1;

    private double p3 = 1;

    private double p2 = 1;

    private double p1 = 1;

    private double p0 = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(int formulaId) {
        this.formulaId = formulaId;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public Double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
    }

    public double getP5() {
        return p5;
    }

    public void setP5(double p5) {
        this.p5 = p5;
    }

    public double getP4() {
        return p4;
    }

    public void setP4(double p4) {
        this.p4 = p4;
    }

    public double getP3() {
        return p3;
    }

    public void setP3(double p3) {
        this.p3 = p3;
    }

    public double getP2() {
        return p2;
    }

    public void setP2(double p2) {
        this.p2 = p2;
    }

    public double getP1() {
        return p1;
    }

    public void setP1(double p1) {
        this.p1 = p1;
    }

    public double getP0() {
        return p0;
    }

    public void setP0(double p0) {
        this.p0 = p0;
    }

    public static Conversion defaultConversion() {
        Conversion conversion = new Conversion();
        conversion.setFormulaId(-1);
        return conversion;
    }
}
