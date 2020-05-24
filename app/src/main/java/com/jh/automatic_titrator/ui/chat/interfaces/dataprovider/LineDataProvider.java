package com.jh.automatic_titrator.ui.chat.interfaces.dataprovider;

import com.jh.automatic_titrator.ui.chat.components.YAxis;
import com.jh.automatic_titrator.ui.chat.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
