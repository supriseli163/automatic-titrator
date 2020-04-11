package com.jh.automatic_titrator.ui.execute;

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
import com.jh.automatic_titrator.ui.component.TitratorTestChart;

import java.util.List;


public class ExecuteGraphFragment extends Fragment implements View.OnClickListener {

    private TitratorTestChart titratorTestChart;

    private TextView test_test_tupu_desc;

    private LinearLayout linearLayout;

    private SimpleResService simpleResService;

    private ExecuteGraphHandler executeGraphHandler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_test_tupu_fragment, container ,false);

        test_test_tupu_desc.setRotation(90);
        titratorTestChart = new TitratorTestChart(getActivity());
        resetChartBaseValue();
        simpleResService = SimpleResService.getInstance(3600);
        executeGraphHandler = new ExecuteGraphHandler();
        simpleResService.addListener(R.id.test_chart_layout,
                new SimpleResService.SimpleResChangeListener() {
                    @Override
                    public void onChange(List<SimpleRes> simpleRes) {
                        titratorTestChart.setSimpleReses(simpleRes);
                        resetChartBaseValue();
                        titratorTestChart.setStartIndex(0);
                        Message.obtain(executeGraphHandler, BaseActivity.NEED_REFRESH).sendToTarget();
                    }
        });
        return view;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void resetChartBaseValue() {
        int maxValue = (Cache.getTestMethod().getAtlasY() + 1) * 10;
        titratorTestChart.setMaxValue(maxValue);
    }

    public class ExecuteGrahpHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    titratorTestChart.invalidate();
                    break;
            }
        }
    }

    private void refresh(){}

    public class ExecuteGraphHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    titratorTestChart.invalidate();
                    break;
            }
        }
    }
}
