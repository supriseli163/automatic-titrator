package com.jh.automatic_titrator.ui.execute.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.databinding.TatratorTupuFragmentBinding;
import com.jh.automatic_titrator.ui.chat.components.XAxis;
import com.jh.automatic_titrator.ui.chat.components.YAxis;
import com.jh.automatic_titrator.ui.chat.data.Entry;
import com.jh.automatic_titrator.ui.chat.data.LineData;
import com.jh.automatic_titrator.ui.chat.data.LineDataSet;
import com.jh.automatic_titrator.ui.chat.interfaces.datasets.ILineDataSet;
import com.jh.automatic_titrator.ui.chat.test.ThreadTest;
import com.jh.automatic_titrator.ui.chat.view.MyMarkerView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class AtlasView extends LinearLayout {
    private ArrayList<Entry> values1;
    private TatratorTupuFragmentBinding binding;
    private XAxis xAxis;
    private float x = 0;

    public AtlasView(Context context) {
        this(context, null);
    }

    public AtlasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AtlasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.tatrator_tupu_fragment, this, true);
        initChart(context);
        setData(getTestData());
    }

    private void initChart(Context context) {
        values1 = new ArrayList<>();
        binding.chart.getDescription().setEnabled(false);
        binding.chart.setDrawGridBackground(false);
        MyMarkerView mv = new MyMarkerView(context);

        // Set the marker to the chart
        mv.setChartView(binding.chart);
        binding.chart.setMarker(mv);

        xAxis = binding.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisMaximum(2);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(2);

        YAxis leftAxis = binding.chart.getAxisLeft();
        leftAxis.setLabelCount(20, true);
        leftAxis.setAxisMaximum(10);
        leftAxis.setAxisMinimum(-10);

        YAxis rightAxis = binding.chart.getAxisRight();
        rightAxis.setLabelCount(20, false);
        rightAxis.setDrawGridLines(false);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setAxisMaximum(200);
        rightAxis.setAxisMinimum(0);
        // do not forget to refresh the chart
        // holder.chart.invalidate();
        binding.chart.setData(new LineData());
        binding.chart.animateX(750);
    }

    public void setData(LineData mChartData) {
        // set data
        binding.chart.setData(mChartData);
        addData();
    }

    private LineData getTestData() {
        values1.add(new Entry(0.1f, (int) (Math.random() * 10 - Math.random() * 10)));
        LineDataSet d1 = new LineDataSet(values1, "滴定仪");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.GRAY);
        d1.setDrawValues(true);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        return new LineData(sets);
    }

    private void addData() {
//        LineData lineData = binding.chart.getLineData();
//        List<ILineDataSet> setList = lineData.getDataSets();
//        ArrayList<Entry> values1 = setList.get(0).geten();
        ThreadTest.getInstance().start(new ThreadTest.OnDataOperateListener() {
            @Override
            public void onDataChange() {
                x = x + 0.1f;
                values1.add(new Entry(x, (int) (Math.random() * 10 - Math.random() * 10)));
                if (x > 2) {
                    xAxis.setAxisMaximum(x);
                    xAxis.setAxisMinimum(x-2);
                    xAxis.setLabelCount(10);
                    binding.chart.notifyDataSetChanged();
                }
                Log.d("AtlasView", "onDataChange: " +x);
                binding.chart.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.chart.invalidate();
                    }
                });
            }
        });
    }
}
