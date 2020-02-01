package com.jh.automatic_titrator.ui.setting;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.StandardValueHelper;
import com.jh.automatic_titrator.common.db.WaveLengthHelper;
import com.jh.automatic_titrator.common.trunk.TrunkConst;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.SingleResult;
import com.jh.automatic_titrator.entity.common.StandardData;
import com.jh.automatic_titrator.entity.common.TestData;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.ui.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/9/24.
 */
public class SettingCorrectingFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final double THRESHOLD = 1.5;

    private static final int REFRESH_VALUE_LIST = 0xf0000001;

    private static final int REFRESH_DATA = 0xf0000000;

    private static final int REFRESH_TEMPERATURE = 0xf0000002;

    private Spinner waveLength_spinner;

    private WaveLengthHelper waveLengthHelper;

    private List<String> waveLengths;

    private ArrayAdapter<String> waveLengthsAdapter;

    private TrunkUtil trunkUtil;

    private TextView testRes_tv;

    private TextView testAvg_tv;

    private View firstText_layout;

    private TextView firstText_tv;

    private View secondText_layout;

    private TextView secondText_tv;

    private View thirdText_layout;

    private TextView thirdText_tv;

    private View test_btn;

    private View testClean_Btn;

    private View testSave_btn;

    private View standardValueAdd_btn;

    private EditText standardValueAdd_et;

    private View standardValueDelete_btn;

    private View standardValueSubmit_btn;

    private View reloadFactory_btn;

    private List<SingleResult> singleResults;

    private int oldWaveLength;

    private List<StandardData> standardDatas;

    private SettingCorrectingAdapter adapter;

    private ListView settingCorrecting_lv;

    private Dialog dialog;

    private SettingCorrectingHandler handler;

    private View view;

    private AuditHelper auditHelper;

    private StandardValueHelper standardValueHelper;

    private boolean cleaned = true;

    private boolean testing = false;

    private double lastTemperature;

    private TextView temperature_lv;

    private int waveLengthPosition = 1;

    private double avg;

    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_correcting_fragment, container, false);

        mToast = ToastUtil.createToast(getActivity());

        waveLengthHelper = DBService.getWaveLengthHelper(getActivity());
        auditHelper = DBService.getAuditHelper(getActivity());
        standardValueHelper = DBService.getStandardValueHelper(getActivity());

        waveLength_spinner = (Spinner) view.findViewById(R.id.setting_correcting_wavelength);
        waveLengths = waveLengthHelper.getWaveLengthStrs();
        waveLengthsAdapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_checked, waveLengths);
        waveLengthsAdapter.setDropDownViewResource(R.layout.spinner_item);
        waveLength_spinner.setAdapter(waveLengthsAdapter);
        waveLength_spinner.setOnItemSelectedListener(this);
        oldWaveLength = waveLength_spinner.getSelectedItemPosition();

        trunkUtil = TrunkUtil.getInstance();
        trunkUtil.sendCmd(TrunkConst.CORRECT_I);

        singleResults = new ArrayList<>();

        testRes_tv = (TextView) view.findViewById(R.id.setting_correcting_test_res);
        testAvg_tv = (TextView) view.findViewById(R.id.setting_correcting_test_avg);

        firstText_layout = view.findViewById(R.id.setting_correcting_first_test);
        firstText_tv = (TextView) view.findViewById(R.id.setting_correcting_first_test_tx);
        secondText_layout = view.findViewById(R.id.setting_correcting_second_test);
        secondText_tv = (TextView) view.findViewById(R.id.setting_correcting_second_test_tx);
        thirdText_layout = view.findViewById(R.id.setting_correcting_third_test);
        thirdText_tv = (TextView) view.findViewById(R.id.setting_correcting_third_test_tx);
        temperature_lv = (TextView) view.findViewById(R.id.setting_correcting_test_tmp);

        test_btn = view.findViewById(R.id.setting_correcting_test_btn);
        test_btn.setOnClickListener(this);
        testClean_Btn = view.findViewById(R.id.setting_correcting_test_delete_btn);
        testClean_Btn.setOnClickListener(this);
        testSave_btn = view.findViewById(R.id.setting_correcting_test_save_btn);
        testSave_btn.setOnClickListener(this);

        standardValueAdd_btn = view.findViewById(R.id.setting_correcting_standard_value_add_btn);
        standardValueAdd_btn.setOnClickListener(this);

        standardValueAdd_et = (EditText) view.findViewById(R.id.setting_correcting_standard_value_add);

        standardValueDelete_btn = view.findViewById(R.id.setting_correcting_standard_value_delete);
        standardValueDelete_btn.setOnClickListener(this);
        standardValueSubmit_btn = view.findViewById(R.id.setting_correcting_standard_value_submit);
        standardValueSubmit_btn.setOnClickListener(this);

        standardValueSubmit_btn = view.findViewById(R.id.setting_correcting_standard_value_submit);
        standardValueSubmit_btn.setOnClickListener(this);

        reloadFactory_btn = view.findViewById(R.id.setting_correcting_reload_factory);
        reloadFactory_btn.setOnClickListener(this);

        handler = new SettingCorrectingHandler();

        standardDatas = standardValueHelper.getStandardData(waveLengthPosition - 1);
        Collections.sort(standardDatas);
        adapter = new SettingCorrectingAdapter(standardDatas, getActivity(), getResources().getStringArray(R.array.temperature_type));
        settingCorrecting_lv = (ListView) view.findViewById(R.id.setting_correcting_lv);
        settingCorrecting_lv.setAdapter(adapter);

        refreshData();
        refreshTemperature();
        return view;
    }

    private void ensureBtn() {
        if (Cache.containsAuth("recovery")) {
            reloadFactory_btn.setVisibility(View.VISIBLE);
        } else {
            reloadFactory_btn.setVisibility(View.GONE);
        }
    }

    private void refreshTemperature() {
        trunkUtil.addListener(new TrunkListener() {
            @Override
            public int getListenType() {
                return TrunkConst.TYPE_TEMPRETURE_CHANGE;
            }

            @Override
            public void notifyData(TrunkData trunkData) {
                int temperature = (int) trunkData.getData();
                lastTemperature = temperature / 10.0;
                Message.obtain(handler, REFRESH_TEMPERATURE).sendToTarget();
            }
        }, R.id.setting_correcting_test_tmp);
    }

    private void refreshData() {
        trunkUtil.addListener(new TrunkListener() {
            @Override
            public int getListenType() {
                return TrunkConst.TYPE_CORRECT_TEST;
            }

            @Override
            public void notifyData(TrunkData trunkData) {
                TestData testData = (TestData) trunkData.getData();
                if (Cache.isAutoClean() && Math.abs(testData.getData() / 10000.0) < Cache.getAutoClean4() && testData.getType() == 1) {
                    testData.setData(0);
                }
                if (testData.getType() == 1 && testing && !Cache.isTesting()) {
                    testing = false;
                    SingleResult singleResult = new SingleResult();
                    singleResult.setRes(testData.getData() / 10000.0);
                    singleResults.add(singleResult);
                    Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                    if (singleResults.size() >= 3) {
                        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.correct_setting), getString(R.string.correct_setting_test), Cache.currentTime(), auditHelper);
//                        Cache.setCorrectTesting(false);
                        Message.obtain(handler, BaseActivity.HOLD_BUTTON_NO).sendToTarget();
                    } else {
                        doTest();
                    }
                } else if (testData.getType() == 1 && !testing && !Cache.isTesting() && cleaned && Math.abs(testData.getData()) >= 100) {
                    singleResults.clear();
                    SingleResult singleResult = new SingleResult();
                    singleResult.setRes(testData.getData() / 10000.0);
                    singleResults.add(singleResult);
                    Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                    if (singleResults.size() >= 3) {
                        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.correct_setting), getString(R.string.correct_setting_test), Cache.currentTime(), auditHelper);
//                        Cache.setCorrectTesting(false);
                        Message.obtain(handler, BaseActivity.HOLD_BUTTON_NO).sendToTarget();
                    } else {
                        doTest();
                    }
                }
                Message.obtain(handler, REFRESH_DATA, testData.getData()).sendToTarget();
            }
        }, R.id.setting_correcting_test_res);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        hide(hidden);
    }

    public void hide(boolean hide) {
        if (!hide) {
//            Cache.setCorrectTesting(true);
            waveLengths.clear();
            waveLengths.addAll(waveLengthHelper.getWaveLengthStrs());
            waveLengthsAdapter.notifyDataSetChanged();
            if (waveLengthPosition != Cache.getCurrentWaveLength()) {
                chooseWaveLength();
            }
            trunkUtil.sendCmd(TrunkConst.CORRECT_I);
        } else {
//            Cache.setCorrectTesting(false);
            trunkUtil.sendCmd(TrunkConst.CORRECT_O);
        }
    }

    @Override
    public void onClick(final View v) {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        switch (v.getId()) {
                            case R.id.setting_correcting_test_btn:
                                singleResults.clear();
//                                Cache.setCorrectTesting(true);
                                doTest();
                                break;
                            case R.id.setting_correcting_test_delete_btn:
                                doTestDelete();
                                break;
                            case R.id.setting_correcting_test_save_btn:
                                doTestSave();
                                break;
                            case R.id.setting_correcting_standard_value_add_btn:
                                doStandardAdd();
                                break;
                            case R.id.setting_correcting_standard_value_delete:
                                doStandardDelete();
                                break;
                            case R.id.setting_correcting_standard_value_submit:
                                doStandardSubmit();
                                break;
                            case R.id.setting_correcting_reload_factory:
                                doreloadFromFactory();
                                break;
                        }
                    }
                }
        );
    }

    private void doTest() {
        cleaned = false;
        testing = true;
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_YES).sendToTarget();
        trunkUtil.sendCmd(TrunkConst.TEST);
    }

    private void doTestDelete() {
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_NO, getString(R.string.clearing)).sendToTarget();

        trunkUtil.addListener(new TrunkListener() {
            @Override
            public int getListenType() {
                return TrunkConst.TYPE_CLEAN;
            }

            @Override
            public void notifyData(TrunkData trunkData) {
                singleResults.clear();
                testing = false;
//                Cache.setCorrectTesting(false);
                trunkUtil.removeListener(R.id.setting_correcting_test_delete_btn);
                cleaned = true;
//                AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.correct_setting), getString(R.string.correct_setting_clean), Cache.currentTime(), auditHelper);
                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                Message.obtain(handler, REFRESH_DATA, 0).sendToTarget();
                Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.clear_success)).sendToTarget();
            }
        }, R.id.setting_correcting_test_delete_btn);
        trunkUtil.sendCmd(TrunkConst.CLEAN);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_NO).sendToTarget();
    }

    private void doTestSave() {
        if (singleResults == null || singleResults.size() == 0) {
            return;
        }
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_YES).sendToTarget();
        for (StandardData standardData : standardDatas) {
            if (Math.abs(standardData.getStandardValue() - avg) <= THRESHOLD) {
                standardData.setTestValue(avg);
                standardData.setTemperature(lastTemperature);
                break;
            }
        }
//        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.correct_setting), getString(R.string.correct_setting_save), Cache.currentTime(), auditHelper);
        Message.obtain(handler, REFRESH_VALUE_LIST).sendToTarget();
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_NO).sendToTarget();
    }

    private void doStandardAdd() {
        if (standardValueAdd_et.getText().toString().length() == 0) {
            return;
        }
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_YES).sendToTarget();
        double value = Double.parseDouble(standardValueAdd_et.getText().toString());
        for (StandardData standardData : standardDatas) {
            if (Math.abs(standardData.getStandardValue() - value) < 0.0001) {
                Message.obtain(handler, REFRESH_VALUE_LIST).sendToTarget();
                Message.obtain(handler, BaseActivity.HOLD_BUTTON_NO).sendToTarget();
                return;
            }
        }
        StandardData standardData = new StandardData();
        standardData.setWaveIndex(waveLengthPosition - 1);
        standardData.setStandardValue(value);
        standardDatas.add(standardData);
        Collections.sort(standardDatas);
        adapter.resetCheckedPositions();
//        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.correct_setting), getString(R.string.correct_setting_add_standard), Cache.currentTime(), auditHelper);
        Message.obtain(handler, REFRESH_VALUE_LIST).sendToTarget();
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_NO).sendToTarget();
    }

    private void doStandardDelete() {
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_YES).sendToTarget();
        Set<Integer> removedList = adapter.getCheckedPositions();
        List<StandardData> newData = new ArrayList<>();
        for (int i = 0; i < standardDatas.size(); i++) {
            if (!removedList.contains(i)) {
                newData.add(standardDatas.get(i));
            }
        }
        standardDatas.clear();
        standardDatas.addAll(newData);
        Collections.sort(standardDatas);
        adapter.resetCheckedPositions();
//        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.correct_setting), getString(R.string.correct_setting_delete_standard), Cache.currentTime(), auditHelper);
        Message.obtain(handler, REFRESH_VALUE_LIST).sendToTarget();
        Message.obtain(handler, BaseActivity.HOLD_BUTTON_NO).sendToTarget();
    }

    private void doStandardSubmit() {
        Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, "正在保存").sendToTarget();
        List<StandardData> newData = new ArrayList<>();
        for (int i = 0; i < standardDatas.size(); i++) {
            if (standardDatas.get(i).getTestValue() != 0) {
                newData.add(standardDatas.get(i));
            }
        }
        standardDatas.clear();
        standardDatas.addAll(newData);
        adapter.resetCheckedPositions();
        standardValueHelper.refreshStandardDatas(standardDatas, waveLengthPosition - 1);
        Message.obtain(handler, REFRESH_VALUE_LIST).sendToTarget();
        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.correct_setting), getString(R.string.correct_setting_submit_standard), Cache.currentTime(), auditHelper);
        doSubmit(0);
    }

    public void doSubmit(final int i) {
        if (i == 0) {
            trunkUtil.addListener(new TrunkListener() {

                private int j = 0;

                @Override
                public int getListenType() {
                    return TrunkConst.TYPE_SET_STANDARD_VALUE;
                }

                @Override
                public void notifyData(TrunkData trunkData) {
                    if (j == 19) {
                        trunkUtil.removeListener(R.id.setting_correcting_standard_value_submit);
                        Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, "保存成功").sendToTarget();
                    } else {
                        j++;
                        doSubmit(j);
                    }
                }
            }, R.id.setting_correcting_standard_value_submit);
        }
        if (standardDatas.size() > i) {
            StandardData standardData = standardDatas.get(i);
            trunkUtil.sendStandardValue(standardData.getTestValue(),
                    standardData.getStandardValue(),
                    standardData.getTemperature(), waveLengthPosition, i + 1);
        } else {
            trunkUtil.sendStandardValue(0, 0, 0, waveLengthPosition, i + 1);
        }
    }

    private void doreloadFromFactory() {
        Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.recovering)).sendToTarget();
        standardDatas.clear();
        adapter.resetCheckedPositions();
        doreload(0);
        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.correct_setting), getString(R.string.correct_setting_recover), Cache.currentTime(), auditHelper);
        Message.obtain(handler, REFRESH_VALUE_LIST).sendToTarget();
    }

    private void doreload(int i) {
        if (i == 0) {
            trunkUtil.addListener(new TrunkListener() {

                private int j = 0;

                @Override
                public int getListenType() {
                    return TrunkConst.TYPE_RECOVER_STANDARD;
                }

                @Override
                public void notifyData(TrunkData trunkData) {
                    StandardData data = (StandardData) trunkData.getData();
                    if (data.getStandardValue() != 0) {
                        data.setWaveIndex(Cache.getWaveLengthPosition(waveLength_spinner.getSelectedItem().toString()));
                        standardDatas.add(data);
                        Collections.sort(standardDatas);
                        Message.obtain(handler, REFRESH_VALUE_LIST).sendToTarget();
                    }
                    if (j == 19) {
                        trunkUtil.removeListener(R.id.setting_correcting_reload_factory);
                        standardValueHelper.refreshStandardDatas(standardDatas, waveLengthPosition - 1);

                        Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.recover_success)).sendToTarget();
                    } else {
                        j++;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            //IGNORE
                        }
                        doreload(j);
                    }
                }
            }, R.id.setting_correcting_reload_factory);
        }
        trunkUtil.sendStandardRecover(waveLengthPosition, i + 1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int currentWaveLengthPosition = Cache.getWaveLengthPosition(waveLength_spinner.getSelectedItem().toString()) + 1;
        Cache.setCurrentWaveLength(currentWaveLengthPosition);
        if (waveLengthPosition != currentWaveLengthPosition) {
            waveLengthPosition = currentWaveLengthPosition;
            chooseWaveLength();
        }
    }

    private void chooseWaveLength() {
        Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.wavelength_choosing)).sendToTarget();
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        trunkUtil.addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_WAVELENGTH_CHOOSE;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                singleResults.clear();
                                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                                standardDatas.clear();
                                standardDatas.addAll(standardValueHelper.getStandardData(waveLengthPosition - 1));
                                Message.obtain(handler, REFRESH_VALUE_LIST).sendToTarget();
                                trunkUtil.removeListener(R.id.setting_correcting_wavelength);
                                Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.wavelength_choose_success)).sendToTarget();
                            }
                        }, R.id.setting_correcting_wavelength);
                        trunkUtil.chooseWaveLength(waveLengthPosition);
                    }
                }
        );
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class SettingCorrectingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.HOLD_WINDOW_YES:
                    showDialog(msg.obj);
                    break;
                case BaseActivity.HOLD_WINDOW_NO:
                    hideDialog(msg.obj);
                    break;
                case BaseActivity.HOLD_BUTTON_YES:
                    view.setEnabled(false);
                    break;
                case BaseActivity.HOLD_BUTTON_NO:
                    view.setEnabled(true);
                    break;
                case BaseActivity.NEED_REFRESH:
                    refresh();
                    break;
                case REFRESH_DATA:
                    freshData((int) msg.obj);
                    break;
                case REFRESH_VALUE_LIST:
                    freshStandartData();
                    break;
                case REFRESH_TEMPERATURE:
                    temperature_lv.setText(String.format("%.1fC", lastTemperature));
                    break;
            }
        }
    }

    private void freshStandartData() {
        adapter.notifyDataSetChanged();
        standardValueAdd_et.setText("");
    }

    private void freshData(int data) {
        testRes_tv.setText(String.format("%.4f", data / 10000.0));
    }

    private void refresh() {
        double sum = 0;
        int size = 0;
        if (singleResults.size() > 0) {
            firstText_tv.setText(String.format("%.4f", singleResults.get(0).getRes()));
            firstText_layout.setVisibility(View.VISIBLE);
            sum = singleResults.get(0).getRes();
            size++;
        } else {
            firstText_layout.setVisibility(View.INVISIBLE);
        }
        if (singleResults.size() > 1) {
            secondText_tv.setText(String.format("%.4f", singleResults.get(1).getRes()));
            secondText_layout.setVisibility(View.VISIBLE);
            sum += singleResults.get(1).getRes();
            size++;
        } else {
            secondText_layout.setVisibility(View.INVISIBLE);
        }
        if (singleResults.size() > 2) {
            thirdText_tv.setText(String.format("%.4f", singleResults.get(2).getRes()));
            thirdText_layout.setVisibility(View.VISIBLE);
            sum += singleResults.get(2).getRes();
            size++;
        } else {
            thirdText_layout.setVisibility(View.INVISIBLE);
        }
        avg = sum / size;
        if (size != 0) {
            testAvg_tv.setText(String.format("%.4f", avg));
        } else {
            testAvg_tv.setText("0");
        }
    }

    private void showDialog(Object obj) {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
        }
        dialog.setTitle(String.valueOf(obj));
        dialog.setCancelable(false);
        dialog.setDismissMessage(Message.obtain(handler));
        dialog.show();
    }

    private void hideDialog(Object obj) {
        if (dialog != null) {
            dialog.dismiss();
        }
        ToastUtil.toastShow(mToast, String.valueOf(obj));
    }
}
