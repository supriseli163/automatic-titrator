package com.jh.automatic_titrator.ui.chat.interfaces.dataprovider;

import android.graphics.RectF;

import com.jh.automatic_titrator.ui.chat.data.ChartData;
import com.jh.automatic_titrator.ui.chat.formatter.ValueFormatter;
import com.jh.automatic_titrator.ui.chat.utils.MPPointF;


public interface ChartInterface {

    /**
     * Returns the minimum x value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    float getXChartMin();

    /**
     * Returns the maximum x value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    float getXChartMax();

    float getXRange();

    /**
     * Returns the minimum y value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    float getYChartMin();

    /**
     * Returns the maximum y value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    float getYChartMax();

    /**
     * Returns the maximum distance in scren dp a touch can be away from an entry to cause it to get highlighted.
     *
     * @return
     */
    float getMaxHighlightDistance();

    int getWidth();

    int getHeight();

    MPPointF getCenterOfView();

    MPPointF getCenterOffsets();

    RectF getContentRect();

    ValueFormatter getDefaultValueFormatter();

    ChartData getData();

    int getMaxVisibleCount();
}
