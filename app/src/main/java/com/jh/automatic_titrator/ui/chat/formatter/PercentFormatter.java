package com.jh.automatic_titrator.ui.chat.formatter;

import com.jh.automatic_titrator.ui.chat.charts.PieChart;
import com.jh.automatic_titrator.ui.chat.data.PieEntry;

import java.text.DecimalFormat;


public class PercentFormatter extends ValueFormatter
{

    public DecimalFormat mFormat;
    private PieChart pieChart;
    private boolean percentSignSeparated;

    public PercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
        percentSignSeparated = true;
    }

    // Can be used to remove percent signs if the chart isn't in percent mode
    public PercentFormatter(PieChart pieChart) {
        this();
        this.pieChart = pieChart;
    }

    // Can be used to remove percent signs if the chart isn't in percent mode
    public PercentFormatter(PieChart pieChart, boolean percentSignSeparated) {
        this(pieChart);
        this.percentSignSeparated = percentSignSeparated;
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + (percentSignSeparated ? " %" : "%");
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            // Converted to percent
            return getFormattedValue(value);
        } else {
            // raw value, skip percent sign
            return mFormat.format(value);
        }
    }

}
