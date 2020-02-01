package com.jh.automatic_titrator.ui.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.SimpleRes;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by apple on 2017/1/8.
 */
public class Chart extends View {

    private List<SimpleRes> simpleReses;

    private Paint xValueDescPaint;

    private int maxValue = 10;

    private long startTime;

    private Paint baseLinePaint;

    private Paint chartLinePaint;

    private int interval = 5;

    private int startIndex = 0;

    private ReentrantLock reentrantLock;

    public Chart(Context context) {
        super(context);
        reentrantLock = new ReentrantLock();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (baseLinePaint == null) {
            baseLinePaint = new Paint();
            baseLinePaint.setTextSize(20);
            baseLinePaint.setColor(0xFF888888);//字用灰色
        }
        if (chartLinePaint == null) {
            chartLinePaint = new Paint();
            chartLinePaint.setColor(0xFFFF1100);//线用红色
        }
        if (xValueDescPaint == null) {
            xValueDescPaint = new Paint();
            xValueDescPaint.setTextSize(15);
            xValueDescPaint.setColor(0xFF0088FF);//x轴描述
        }
        float width = getWidth();
        float useWidth = width - 40;
        float height = getHeight();
        float useHeight = height - 40;
        int widthOffSet = 40;
        int xLines = maxValue / interval * 2 + 1;
        float pointYOffset = useHeight / (maxValue * 2);
        float lineOffset = pointYOffset * interval;
        float firstLineOffset = maxValue % interval * pointYOffset;
        int firstValue = maxValue - (maxValue % interval);
        if (firstLineOffset == 0) {
            firstLineOffset = interval * pointYOffset;
            firstValue = maxValue - interval;
            xLines = maxValue / interval * 2 - 1;
        }

        for (int i = 0; i < xLines; i++) {
            canvas.drawText(firstValue + "", 0, firstLineOffset, baseLinePaint);
            canvas.drawLine(widthOffSet, firstLineOffset, width, firstLineOffset, baseLinePaint);
            firstLineOffset += lineOffset;
            firstValue -= interval;
        }

        canvas.drawLine(widthOffSet, 0, widthOffSet, useHeight, baseLinePaint);

        reentrantLock.lock();
        try {
            if (simpleReses != null && simpleReses.size() > 0) {
                float pointXOffSet = useWidth / (simpleReses.size() - startIndex - 1);
                float lastPointX = widthOffSet;
                float lastPointY = (maxValue - simpleReses.get(startIndex).getRes()) * pointYOffset;
                for (int i = startIndex + 1; i < simpleReses.size(); i++) {
                    float currentX = widthOffSet + pointXOffSet * (i - startIndex);
                    float currentY = pointYOffset * (maxValue - simpleReses.get(i).getRes());
                    canvas.drawLine(lastPointX, lastPointY, currentX, currentY, chartLinePaint);
                    lastPointX = currentX;
                    lastPointY = currentY;
                }
                canvas.drawText(TimeTool.dateFormatter(simpleReses.get(startIndex).getTime(), "HH:mm:ss"), widthOffSet / 2 + 5, useHeight + 40, xValueDescPaint);
                if (simpleReses.size() - startIndex > 1) {
                    canvas.drawText(TimeTool.dateFormatter(simpleReses.get(simpleReses.size() - 1).getTime(), "HH:mm:ss"), width - 60, useHeight + 40, xValueDescPaint);
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public int getMaxValue() {
        return maxValue;
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
}
