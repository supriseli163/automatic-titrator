package com.jh.automatic_titrator.ui.chat.interfaces.dataprovider;

import com.jh.automatic_titrator.ui.chat.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
