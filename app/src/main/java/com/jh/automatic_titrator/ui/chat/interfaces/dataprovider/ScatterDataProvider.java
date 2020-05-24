package com.jh.automatic_titrator.ui.chat.interfaces.dataprovider;

import com.jh.automatic_titrator.ui.chat.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
