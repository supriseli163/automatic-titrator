package com.jh.automatic_titrator.ui.execute.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.databinding.TatratorTupuFragmentBinding;
import com.jh.automatic_titrator.ui.component.Chart;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class AtlasView extends LinearLayout {
    private TatratorTupuFragmentBinding binding;
    private Chart chart;

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
        chart = new Chart(context);
        resetChartBaseValue();
        binding.testChartLayout.addView(chart);
    }

    private void resetChartBaseValue() {
        int maxValue = (Cache.getTestMethod().getAtlasY() + 1) * 10;
        chart.setMaxValue(maxValue);
    }
}
