package com.jh.automatic_titrator.ui.chat.interfaces.dataprovider;

import com.jh.automatic_titrator.ui.chat.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isHighlightFullBarEnabled();
}
