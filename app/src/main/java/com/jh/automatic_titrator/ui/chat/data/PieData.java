
package com.jh.automatic_titrator.ui.chat.data;

import android.util.Log;

import com.jh.automatic_titrator.ui.chat.highlight.Highlight;
import com.jh.automatic_titrator.ui.chat.interfaces.datasets.IPieDataSet;

import java.util.List;

public class PieData extends ChartData<IPieDataSet> {

    public PieData() {
        super();
    }

    public PieData(IPieDataSet dataSet) {
        super(dataSet);
    }

    /**
     * Sets the PieDataSet this data object should represent.
     *
     * @param dataSet
     */
    public void setDataSet(IPieDataSet dataSet) {
        mDataSets.clear();
        mDataSets.add(dataSet);
        notifyDataChanged();
    }

    /**
     * Returns the DataSet this PieData object represents. A PieData object can
     * only contain one DataSet.
     *
     * @return
     */
    public IPieDataSet getDataSet() {
        return mDataSets.get(0);
    }

    @Override
    public List<IPieDataSet> getDataSets() {
        List<IPieDataSet> dataSets = super.getDataSets();

        if (dataSets.size() < 1) {
            Log.e("MPAndroidChart",
                    "Found multiple data sets while pie chart only allows one");
        }

        return dataSets;
    }

    /**
     * The PieData object can only have one DataSet. Use getDataSet() method instead.
     *
     * @param index
     * @return
     */
    @Override
    public IPieDataSet getDataSetByIndex(int index) {
        return index == 0 ? getDataSet() : null;
    }

    @Override
    public IPieDataSet getDataSetByLabel(String label, boolean ignorecase) {
        return ignorecase ? label.equalsIgnoreCase(mDataSets.get(0).getLabel()) ? mDataSets.get(0)
                : null : label.equals(mDataSets.get(0).getLabel()) ? mDataSets.get(0) : null;
    }

    @Override
    public Entry getEntryForHighlight(Highlight highlight) {
        return getDataSet().getEntryForIndex((int) highlight.getX());
    }

    /**
     * Returns the sum of all values in this PieData object.
     *
     * @return
     */
    public float getYValueSum() {

        float sum = 0;

        for (int i = 0; i < getDataSet().getEntryCount(); i++)
            sum += getDataSet().getEntryForIndex(i).getY();


        return sum;
    }
}
