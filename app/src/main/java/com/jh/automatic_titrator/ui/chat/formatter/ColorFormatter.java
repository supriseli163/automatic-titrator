package com.jh.automatic_titrator.ui.chat.formatter;

import com.jh.automatic_titrator.ui.chat.data.Entry;
import com.jh.automatic_titrator.ui.chat.interfaces.datasets.IDataSet;


public interface ColorFormatter {

    /**
     * Returns the color to be used for the given Entry at the given index (in the entries array)
     *
     * @param index index in the entries array
     * @param e     the entry to color
     * @param set   the DataSet the entry belongs to
     * @return
     */
    int getColor(int index, Entry e, IDataSet set);
}