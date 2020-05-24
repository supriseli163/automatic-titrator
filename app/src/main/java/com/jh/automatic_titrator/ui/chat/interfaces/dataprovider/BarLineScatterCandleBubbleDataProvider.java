package com.jh.automatic_titrator.ui.chat.interfaces.dataprovider;

import com.jh.automatic_titrator.ui.chat.components.YAxis.AxisDependency;
import com.jh.automatic_titrator.ui.chat.data.BarLineScatterCandleBubbleData;
import com.jh.automatic_titrator.ui.chat.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
