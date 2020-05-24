package com.jh.automatic_titrator.ui.chat.formatter;

import com.jh.automatic_titrator.ui.chat.data.Entry;
import com.jh.automatic_titrator.ui.chat.utils.ViewPortHandler;


@Deprecated
public interface IValueFormatter
{

    /**
     * Called when a value (from labels inside the chart) is formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value           the value to be formatted
     * @param entry           the entry the value belongs to - in e.g. BarChart, this is of class BarEntry
     * @param dataSetIndex    the index of the DataSet the entry in focus belongs to
     * @param viewPortHandler provides information about the current chart state (scale, translation, ...)
     * @return the formatted label ready for being drawn
     *
     * @deprecated Extend {@link ValueFormatter} and override an appropriate method
     */
    @Deprecated
    String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler);
}
