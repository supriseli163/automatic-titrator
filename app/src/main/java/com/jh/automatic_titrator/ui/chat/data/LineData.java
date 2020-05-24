
package com.jh.automatic_titrator.ui.chat.data;

import com.jh.automatic_titrator.ui.chat.interfaces.datasets.ILineDataSet;

import java.util.List;


public class LineData extends BarLineScatterCandleBubbleData<ILineDataSet> {

    public LineData() {
        super();
    }

    public LineData(ILineDataSet... dataSets) {
        super(dataSets);
    }

    public LineData(List<ILineDataSet> dataSets) {
        super(dataSets);
    }
}
