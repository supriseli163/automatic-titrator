package com.jh.automatic_titrator.ui.test;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.entity.common.SimpleRes;
import com.jh.automatic_titrator.service.SimpleResService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.component.Chart;

import java.util.List;

/**
 * Created by apple on 16/9/17.
 */
public class TestTestTupuFragment extends Fragment implements View.OnClickListener {

    private TextView test_test_tupu_desc;

    private Chart chart;

    private LinearLayout chartLayout;

    private TestTupuHandler handler;

    private SimpleResService simpleResService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_test_tupu_fragment, container, false);

        test_test_tupu_desc = (TextView) view.findViewById(R.id.test_test_tupu_desc);
        test_test_tupu_desc.setRotation(90);

        chartLayout = (LinearLayout) view.findViewById(R.id.test_chart_layout);

        chart = new Chart(getActivity());
        resetChartBaseValue();
        chartLayout.addView(chart);
        simpleResService = SimpleResService.getInstance(3600);
        handler = new TestTupuHandler();
        simpleResService.addListener(R.id.test_chart_layout, new SimpleResService.SimpleResChangeListener() {
            @Override
            public void onChange(List<SimpleRes> simpleRes) {
                chart.setSimpleReses(simpleRes);
                resetChartBaseValue();
                chart.setStartIndex(0);
                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
            }
        });
        return view;
    }

    private void resetChartBaseValue() {
        int maxValue = (Cache.getTestMethod().getAtlasY() + 1) * 10;
        chart.setMaxValue(maxValue);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        refresh();
    }

    private void refresh() {
    }

    @Override
    public void onClick(View v) {

    }

    public class TestTupuHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    chart.invalidate();
                    break;
            }
        }
    }
}
