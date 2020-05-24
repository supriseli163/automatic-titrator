package com.jh.automatic_titrator.ui.chat.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jh.automatic_titrator.ui.chat.interfaces.datasets.IScatterDataSet;
import com.jh.automatic_titrator.ui.chat.utils.Utils;
import com.jh.automatic_titrator.ui.chat.utils.ViewPortHandler;

/**
 * Created by wajdic on 15/06/2016.
 * Created at Time 09:08
 */
public class XShapeRenderer implements IShapeRenderer
{


    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                            float posX, float posY, Paint renderPaint) {

        final float shapeHalf = dataSet.getScatterShapeSize() / 2f;

        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1f));

        c.drawLine(
                posX - shapeHalf,
                posY - shapeHalf,
                posX + shapeHalf,
                posY + shapeHalf,
                renderPaint);
        c.drawLine(
                posX + shapeHalf,
                posY - shapeHalf,
                posX - shapeHalf,
                posY + shapeHalf,
                renderPaint);

    }

}