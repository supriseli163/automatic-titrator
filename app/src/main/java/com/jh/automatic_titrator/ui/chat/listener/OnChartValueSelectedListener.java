package com.jh.automatic_titrator.ui.chat.listener;

import com.jh.automatic_titrator.ui.chat.data.Entry;
import com.jh.automatic_titrator.ui.chat.highlight.Highlight;


public interface OnChartValueSelectedListener {

    /**
     * Called when a value has been selected inside the chart.
     *
     * @param e The selected Entry
     * @param h The corresponding highlight object that contains information
     *          about the highlighted position such as dataSetIndex, ...
     */
    void onValueSelected(Entry e, Highlight h);

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    void onNothingSelected();
}
