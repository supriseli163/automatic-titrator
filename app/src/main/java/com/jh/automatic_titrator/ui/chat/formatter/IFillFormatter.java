package com.jh.automatic_titrator.ui.chat.formatter;

import com.jh.automatic_titrator.ui.chat.interfaces.dataprovider.LineDataProvider;
import com.jh.automatic_titrator.ui.chat.interfaces.datasets.ILineDataSet;

public interface IFillFormatter
{

    /**
     * Returns the vertical (y-axis) position where the filled-line of the
     * LineDataSet should end.
     * 
     * @param dataSet the ILineDataSet that is currently drawn
     * @param dataProvider
     * @return
     */
    float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider);
}
