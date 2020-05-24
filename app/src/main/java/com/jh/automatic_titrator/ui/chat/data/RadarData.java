
package com.jh.automatic_titrator.ui.chat.data;

import com.jh.automatic_titrator.ui.chat.highlight.Highlight;
import com.jh.automatic_titrator.ui.chat.interfaces.datasets.IRadarDataSet;

import java.util.Arrays;
import java.util.List;


public class RadarData extends ChartData<IRadarDataSet> {

    private List<String> mLabels;

    public RadarData() {
        super();
    }

    public RadarData(List<IRadarDataSet> dataSets) {
        super(dataSets);
    }

    public RadarData(IRadarDataSet... dataSets) {
        super(dataSets);
    }

    /**
     * Sets the labels that should be drawn around the RadarChart at the end of each web line.
     *
     * @param labels
     */
    public void setLabels(List<String> labels) {
        this.mLabels = labels;
    }

    /**
     * Sets the labels that should be drawn around the RadarChart at the end of each web line.
     *
     * @param labels
     */
    public void setLabels(String... labels) {
        this.mLabels = Arrays.asList(labels);
    }

    public List<String> getLabels() {
        return mLabels;
    }

    @Override
    public Entry getEntryForHighlight(Highlight highlight) {
        return getDataSetByIndex(highlight.getDataSetIndex()).getEntryForIndex((int) highlight.getX());
    }
}
