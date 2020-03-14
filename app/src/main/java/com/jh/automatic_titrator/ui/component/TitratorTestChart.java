package com.jh.automatic_titrator.ui.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.jh.automatic_titrator.entity.common.SimpleRes;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class TitratorTestChart extends View {

    public Paint baseLinePaint;

    public Paint chartLinePaint;

    public Paint xValueescPaint;

    private int interval = 5;
    private int startIndex = 0;
    private int maxValue = 10;

    public List<SimpleRes> simpleReses;

    private ReentrantLock reentrantLock;

    public TitratorTestChart(Context context) {
        super(context);
        reentrantLock = new ReentrantLock();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(baseLinePaint == null) {
            baseLinePaint = new Paint();
            baseLinePaint.setTextSize(20);
            baseLinePaint.setColor(0xFF888888); //字用灰色
        }
        if(chartLinePaint == null) {
            chartLinePaint = new Paint();
            chartLinePaint.setColor(0xFFFF1100);   //线用红色
        }

        if(xValueescPaint == null) {
            xValueescPaint = new Paint();
            xValueescPaint.setTextSize(15);
            xValueescPaint.setColor(0xFF0088FF);    //x轴描述
        }

        float width = getWidth();
        float useWidth = width - 40;
        float height = getHeight();
        float useHeight = height - 40;

        reentrantLock.lock();
        try {

        } finally {
            reentrantLock.unlock();
        }

    }

    public List<SimpleRes> getSimpleReses() {
        return simpleReses;
    }

    public void setSimpleReses(List<SimpleRes> simpleReses) {
        reentrantLock.lock();
        try {
            this.simpleReses = simpleReses;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        if (1 < maxValue && maxValue <= 5) {
            interval = 1;
        }
        if (maxValue > 5 && maxValue <= 20) {
            interval = 5;
        }
        if (maxValue > 20 && maxValue <= 40) {
            interval = 10;
        }
        if (maxValue > 40 && maxValue <= 80) {
            interval = 20;
        }
        if (maxValue > 80 && maxValue <= 120) {
            interval = 30;
        }
        if (maxValue > 120) {
            interval = maxValue / 3;
        }
    }
}
