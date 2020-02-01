package com.jh.automatic_titrator.ui.test;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.Observer;
import com.jh.automatic_titrator.common.formula.BaseFormulaUtil;
import com.jh.automatic_titrator.common.trunk.TrunkConst;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.entity.common.SingleResult;
import com.jh.automatic_titrator.entity.common.TestMethod;
import com.jh.automatic_titrator.service.MethodService;
import com.jh.automatic_titrator.service.TestMethodService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 16/9/17.
 */
public class TestTestListFragment extends Fragment {

    private static final int TEMPERATURE_CHANGE = 0x00FF0000;

    private TextView test_test_list_frame_name;

    private TextView test_test_list_frame_no;

    private TextView test_test_list_frame_length;

    private TextView test_test_list_frame_length1;

    private TextView test_test_list_frame_temperature;

    private TextView test_test_list_frame_temperature1;

    private TextView test_test_list_frame_testCount;

    private TextView test_test_list_testtype;

    private ListView test_test_list_res_lv;

    private TextView test_test_list_res_avg_tv;

    private TextView test_test_list_res_rsp_tv;

    private List<SingleResult> singleResults;

    private String[] temperatureTypes;

    private String currentTemperatureType;

    private String currentResultDescType;

    private String currentResultUnitType;

    private TestTestListAdapter adapter;

    private int decimal = 3;

    private TestTestListHandler handler;

    private TrunkUtil trunkUtil;

    private int temperature;

    private ReadWriteLock readWriteLock;
    private boolean running = true;
    private long lastModify = 0;
    private Thread refreshThread = new Thread() {
        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(50);
                    if (lastModify != Cache.getLastModify()) {
                        lastModify = Cache.getLastModify();
                        singleResults.clear();
                        singleResults.addAll(Cache.getSingleResults());
                        Message.obtain(handler, Observer.ADD).sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_test_list_fragment, container, false);

        readWriteLock = new ReentrantReadWriteLock();
        temperatureTypes = this.getActivity().getResources().getStringArray(R.array.temperature_type);

        test_test_list_frame_name = (TextView) view.findViewById(R.id.test_test_list_frame_name);
        test_test_list_frame_no = (TextView) view.findViewById(R.id.test_test_list_frame_no);
        test_test_list_frame_length = (TextView) view.findViewById(R.id.test_test_list_frame_length);
        test_test_list_frame_length1 = (TextView) view.findViewById(R.id.test_test_list_frame_length1);
        test_test_list_frame_temperature = (TextView) view.findViewById(R.id.test_test_list_frame_temperature);
        test_test_list_frame_temperature1 = (TextView) view.findViewById(R.id.test_test_list_frame_temperature1);
        test_test_list_testtype = (TextView) view.findViewById(R.id.test_test_list_testtype);
        test_test_list_frame_testCount = (TextView) view.findViewById(R.id.test_test_list_frame_test_times);
        test_test_list_res_lv = (ListView) view.findViewById(R.id.test_test_list_res_lv);

        test_test_list_res_avg_tv = (TextView) view.findViewById(R.id.test_test_list_res_avg_tv);
        test_test_list_res_rsp_tv = (TextView) view.findViewById(R.id.test_test_list_res_rsp_tv);

        this.singleResults = new ArrayList<>();
        adapter = new TestTestListAdapter(singleResults, this.getActivity());
        test_test_list_res_lv.setAdapter(adapter);

        trunkUtil = TrunkUtil.getInstance();
        trunkUtil.addListener(new TrunkListener() {
            @Override
            public int getListenType() {
                return TrunkConst.TYPE_TEMPRETURE_CHANGE;
            }

            @Override
            public void notifyData(TrunkData trunkData) {
                temperature = (int) trunkData.getData();
                Message.obtain(handler, TEMPERATURE_CHANGE, temperature).sendToTarget();
            }
        }, R.id.test_test_list_frame_temperature1);
        handler = new TestTestListHandler();
        MethodService.getInstance().addListener(R.id.test_test_list_res_lv, new MethodService.MethodChangeListener() {
            @Override
            public void onChanged(TestMethod testMethod, int type) {
                Message.obtain(handler, Observer.REFRESH_BASE).sendToTarget();
            }
        });
        TestMethodService.getInstance().addTestMethodListener(R.id.test_test_ceshi, new TestMethodService.TestMethodChangedListener() {
            @Override
            public void onChange() {
                Message.obtain(handler, Observer.REFRESH_BASE).sendToTarget();
            }
        });
        refresh();
        refreshThread.start();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
    }

    private void resetAvgAndRsp() {
        List<Double> values = new ArrayList<>();
        if (singleResults.size() == 0) {
            test_test_list_res_avg_tv.setText("AVG: " + 0 + " " + currentResultUnitType);
            test_test_list_res_rsp_tv.setText("RSP: " + 0 + " " + currentResultUnitType);
            return;
        }
        double sum = 0;
        for (SingleResult singleResult : singleResults) {
            values.add(singleResult.getRes());
            sum += singleResult.getRes();
        }
        double avg = sum / values.size();
        double sum1 = 0;
        for (Double value : values) {
            double temp = value - avg;
            sum1 += temp * temp;
        }
        double rsp = Math.sqrt(sum1 / values.size());
        avg = Double.parseDouble(String.format("%." + decimal + "f", avg));
        rsp = Double.parseDouble(String.format("%." + decimal + "f", rsp));
        test_test_list_res_avg_tv.setText(String.format("AVG: %." + decimal + "f %s", avg, currentResultUnitType));
        test_test_list_res_rsp_tv.setText(String.format("RSP: %." + decimal + "f %s", rsp, currentResultUnitType));
    }

    private void refresh() {
        Log.d("TestTestList", "refresh: baseInfo");
        TestMethod testMethod = Cache.getTestMethod();
        String testName = Cache.getTestName();
        String testId = Cache.getTestId();
        Formula formula = Cache.getCurrentFormula();
        currentResultDescType = formula.getFormulaName();
        currentResultUnitType = formula.getUnit();
        currentTemperatureType = temperatureTypes[testMethod.getTemperatureType()];

        test_test_list_frame_name.setText(getString(R.string.sample_name) + ": " + testName);
        test_test_list_frame_no.setText(getString(R.string.sample_no) + ": " + testId);
        if (testMethod.getTestTubeLength() != null) {
            test_test_list_frame_length.setText(getString(R.string.testtubelength) + ": " + testMethod.getTestTubeLength() + "cm");
        } else {
            test_test_list_frame_length.setText(getString(R.string.testtubelength) + ": - -");
        }
        test_test_list_frame_length1.setText(getString(R.string.wavelength) + ": " + testMethod.getWaveLength());
        int temperatureType = testMethod.getTemperatureType();
        if (testMethod.isUseTemperature()) {
            double temperatureValue = BaseFormulaUtil.changeTemperature(temperatureType, testMethod.getTemperature());
            test_test_list_frame_temperature.setText(getString(R.string.setting_temperature) + ": " + temperatureValue + " " + currentTemperatureType);
        } else {
            test_test_list_frame_temperature.setText(getString(R.string.setting_temperature) + ": - -");
        }
        double temperatureValue1 = BaseFormulaUtil.changeTemperature(temperatureType, temperature / 10.0);
        test_test_list_frame_temperature1.setText(getString(R.string.real_temperature) + ": " + temperatureValue1 + " " + currentTemperatureType);

        if (testMethod.isAutoTest()) {
            test_test_list_frame_testCount.setText(getString(R.string.test_count) + ": " + testMethod.getAutoTestTimes());
        } else {
            test_test_list_frame_testCount.setText(getString(R.string.test_count) + ": " + testMethod.getTestCount());
        }
        test_test_list_testtype.setText(currentResultDescType);
    }

    public class TestTestListHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Observer.ADD:
                    adapter.notifyDataSetChanged();
                    resetAvgAndRsp();
                    return;
                case Observer.REFRESH_BASE:
                    refresh();
                    break;
                case TEMPERATURE_CHANGE:
                    int temperature = (int) msg.obj;
                    int temperatureType = Cache.getTestMethod().getTemperatureType();
                    double temperatureValue = BaseFormulaUtil.changeTemperature(temperatureType, temperature / 10.0);
                    test_test_list_frame_temperature1.setText("实时温度：" + temperatureValue + " " + currentTemperatureType);
                    break;
            }
        }
    }
}
