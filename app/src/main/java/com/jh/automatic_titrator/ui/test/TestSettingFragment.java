package com.jh.automatic_titrator.ui.test;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.Observer;
import com.jh.automatic_titrator.common.ObserverAble;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.FormulaHelper;
import com.jh.automatic_titrator.common.db.TestMethodHelper;
import com.jh.automatic_titrator.common.db.WaveLengthHelper;
import com.jh.automatic_titrator.common.formula.BaseFormulaUtil;
import com.jh.automatic_titrator.common.trunk.TrunkConst;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.common.utils.JsonHelper;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.entity.common.SettingConfig;
import com.jh.automatic_titrator.entity.common.TestMethod;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.FormulaService;
import com.jh.automatic_titrator.service.TestMethodService;
import com.jh.automatic_titrator.service.WaveLengthService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.component.FormulaAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by apple on 16/9/17.
 */
public class TestSettingFragment extends Fragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, ObserverAble, AdapterView.OnItemSelectedListener {

    private static final int REFRESH_TESTMETHOD = 0xFFFFFFFE;

    private static final int REFRESH_WAVELENGTH = 0xFFFFFFFC;

    private static final int REFRESH_METHODS = 0xFFFFFFFA;

    private static final int TEMPERATURE_SAVING = 0xFFFFFFEE;

    private EditText testName_tv;

    private EditText testId_tv;

    private Spinner testMethod_tv;

    private EditText concentration_tv;

    private Spinner concentrationType_spinner;

    private Spinner testCount_spinner;

    private Spinner testAccuracy_spinner;

    private Spinner formula_spinner;

    private Spinner waveLength_spinner;

    private Spinner decimal_spinner;

    private EditText testTubeLength_tv;

    private EditText specificRotation_tv;

    private Spinner atlasX_spinner;

    private Spinner atlasY_spinner;

    private CheckBox useTemperature_cb;

    private EditText temperature_tv;

    private Spinner temperatureType_spinner;

    private CheckBox autoTest_cb;

    private EditText autoTestInterval_tv;

    private EditText autoTestTimes_tv;

    private WaveLengthHelper waveLengthHelper;

    private TestMethodHelper testMethodHelper;

    private TestSettingHandler handler;

    private View save_btn;

    private TestMethod testMethod;

    private View test_setting_fragment_view;

    private String canNotBeNull;

    private View view;

    private List<String> waveLengths;

    private ArrayAdapter<String> waveLengthsAdapter;

    private AuditHelper auditHelper;

    private ArrayAdapter<String> testMethodsAdapter;

    private List<String> testMethods;

    private int methodSelectIndex;

    private TestMethodService testMethodService;

    private TrunkUtil trunkUtil;

    private Toast mToast;

    private FormulaHelper mFormulaHelper;

    private FormulaAdapter formulaAdapter;

    private List<Formula> formulas;

    private List<Formula> baseFormula;

    private List<String> decimals;

    private ArrayAdapter<String> decimalsAdapter;

    private WaveLengthService.WaveLengthChangeListener waveLengthChangeListener = new WaveLengthService.WaveLengthChangeListener() {
        @Override
        public void onChanged() {
            reLoadWaveLength();
        }
    };

    private void reLoadWaveLength() {
        waveLengths.clear();
        waveLengths.addAll(waveLengthHelper.getWaveLengthStrs());
        Message.obtain(handler, REFRESH_WAVELENGTH).sendToTarget();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.test_setting_fragment, container, false);

        baseFormula = Cache.getBaseFormula();
        mToast = ToastUtil.createToast(getActivity());
        mFormulaHelper = DBService.getFormulaHelper(getActivity());
        waveLengthHelper = DBService.getWaveLengthHelper(this.getActivity());
        auditHelper = DBService.getAuditHelper(this.getActivity());
        testMethodService = TestMethodService.getInstance();
        trunkUtil = TrunkUtil.getInstance();

        test_setting_fragment_view = view.findViewById(R.id.test_setting_fragment_view);
        test_setting_fragment_view.setOnClickListener(this);

        testMethodHelper = DBService.getTestMethodHelper(this.getActivity());
        testMethod = new TestMethod();

        canNotBeNull = getString(R.string.can_not_be_null);

        testName_tv = (EditText) view.findViewById(R.id.test_setting_fragment_name);
        testId_tv = (EditText) view.findViewById(R.id.test_setting_fragment_id);

        testMethods = testMethodHelper.allMethodNames();
        testMethod_tv = (Spinner) view.findViewById(R.id.test_setting_fragment_method);
        testMethodsAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_checked, testMethods);
        testMethodsAdapter.setDropDownViewResource(R.layout.spinner_item);
        testMethod_tv.setAdapter(testMethodsAdapter);
        testMethod_tv.setOnItemSelectedListener(this);

        concentration_tv = (EditText) view.findViewById(R.id.test_setting_fragment_concentration);

        concentrationType_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_concentrationtype);
        String[] concentrationTypes = this.getActivity().getResources().getStringArray(R.array.concentration_type);
        ArrayAdapter<String> concentrationTypeAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_checked, concentrationTypes);
        concentrationTypeAdapter.setDropDownViewResource(R.layout.spinner_item);
        concentrationType_spinner.setAdapter(concentrationTypeAdapter);

        testCount_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_testcount);
        String[] testCountType = this.getActivity().getResources().getStringArray(R.array.number_test);
        ArrayAdapter<String> testCountTypeAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_checked, testCountType);
        testCountTypeAdapter.setDropDownViewResource(R.layout.spinner_item);
        testCount_spinner.setAdapter(testCountTypeAdapter);

        testAccuracy_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_accuracy);
        String[] testAccuracy = this.getActivity().getResources().getStringArray(R.array.test_accuracy);
        ArrayAdapter<String> testAccuracyAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_checked, testAccuracy);
        testAccuracyAdapter.setDropDownViewResource(R.layout.spinner_item);
        testAccuracy_spinner.setAdapter(testAccuracyAdapter);

        formulas = new ArrayList<>();
        formulas.addAll(Cache.getFormulas());
//        formulas.addAll(mFormulaHelper.listFormula());
        formula_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_formula);
        formulaAdapter = new FormulaAdapter(this.getActivity(), R.layout.spinner_checked, formulas);
        formulaAdapter.setDropDownViewResource(R.layout.spinner_item);
        formula_spinner.setAdapter(formulaAdapter);
        formula_spinner.setOnItemSelectedListener(this);

        waveLength_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_wavelength);
        waveLengths = waveLengthHelper.getWaveLengthStrs();
        waveLengthsAdapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_checked, waveLengths);
        waveLengthsAdapter.setDropDownViewResource(R.layout.spinner_item);
        waveLength_spinner.setAdapter(waveLengthsAdapter);

        decimal_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_showdecimals);
        decimals = new ArrayList<>();
        for (int i = 0; i < formulas.get(0).getDecimal(); i++) {
            decimals.add((i + 1) + "");
        }
        decimalsAdapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_checked, decimals);
        decimalsAdapter.setDropDownViewResource(R.layout.spinner_item);
        decimal_spinner.setAdapter(decimalsAdapter);
        decimal_spinner.setSelection(decimals.size() - 1);

        testTubeLength_tv = (EditText) view.findViewById(R.id.test_setting_fragment_testtubelength);
        specificRotation_tv = (EditText) view.findViewById(R.id.test_setting_fragment_specificrotation);

        atlasX_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_atlasX);
        String[] atlasXs = this.getActivity().getResources().getStringArray(R.array.test_atlasX);
        ArrayAdapter<String> atlasXsAdapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_checked, atlasXs);
        atlasXsAdapter.setDropDownViewResource(R.layout.spinner_item);
        atlasX_spinner.setAdapter(atlasXsAdapter);

        atlasY_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_atlasY);
        String[] atlasYs = this.getActivity().getResources().getStringArray(R.array.test_atlasY);
        ArrayAdapter<String> atlasYsAdapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_checked, atlasYs);
        atlasYsAdapter.setDropDownViewResource(R.layout.spinner_item);
        atlasY_spinner.setAdapter(atlasYsAdapter);

        useTemperature_cb = (CheckBox) view.findViewById(R.id.test_setting_fragment_usetemperature);
        useTemperature_cb.setOnCheckedChangeListener(this);
        temperature_tv = (EditText) view.findViewById(R.id.test_setting_fragment_temperature);
        temperature_tv.setEnabled(false);

        temperatureType_spinner = (Spinner) view.findViewById(R.id.test_setting_fragment_temperaturetype);
        String[] temperatureType = this.getActivity().getResources().getStringArray(R.array.temperature_type);
        ArrayAdapter<String> temperatureTypeAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_checked, temperatureType);
        temperatureTypeAdapter.setDropDownViewResource(R.layout.spinner_item);
        temperatureType_spinner.setAdapter(temperatureTypeAdapter);
        temperatureType_spinner.setEnabled(false);

        autoTest_cb = (CheckBox) view.findViewById(R.id.test_setting_fragment_autotest);
        autoTest_cb.setOnCheckedChangeListener(this);
        autoTestInterval_tv = (EditText) view.findViewById(R.id.test_setting_fragment_autotestinterval);
        autoTestInterval_tv.setEnabled(false);
        autoTestTimes_tv = (EditText) view.findViewById(R.id.test_setting_fragment_autotesttimes);
        autoTestTimes_tv.setEnabled(false);

        save_btn = view.findViewById(R.id.test_setting_frament_save_button);
        save_btn.setOnClickListener(this);
        handler = new TestSettingHandler();

        testMethodService.addTestMethodListener(R.id.test_setting_fragment_method, testMethodChangedListener);

        refreshValues();

        WaveLengthService.getInstance().addListener(R.id.test_setting_fragment_wavelength, waveLengthChangeListener);
        FormulaService.getInstance().addListener(R.id.test_setting_fragment_formula, formulaChangeListener);
        return view;
    }

    private int formulaSelection = -1;
    private int decimalSelection = -1;

    private FormulaService.FormulaChangeListener formulaChangeListener = new FormulaService.FormulaChangeListener() {
        @Override
        public void onChanged() {
            formulas.clear();
//            formulas.addAll(baseFormula);
            formulas.addAll(Cache.getFormulas());
            boolean formulaFounded = false;
            for (int i = 0; i < formulas.size(); i++) {
                if (formulas.get(i).getFormulaName().equals(Cache.getCurrentFormula().getFormulaName())) {
                    formulaSelection = i;
                    decimals.clear();
                    formulaFounded = true;
                    for (int j = 0; j < formulas.get(i).getDecimal(); j++) {
                        decimals.add((j + 1) + "");
                    }
                    if (testMethod.getDecimals() > decimals.size()) {
                        decimalSelection = decimals.size() - 1;
                    } else {
                        decimalSelection = testMethod.getDecimals();
                    }
                    break;
                }
            }

            if (formulaFounded == false) {
                Cache.setCurrentFormula(formulas.get(0));
                formulaSelection = 0;
                decimals.clear();
                for (int j = 0; j < formulas.get(0).getDecimal(); j++) {
                    decimals.add(j + "");
                }
                decimalSelection = formulas.get(0).getDecimal() - 1;
            }

            Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
        }
    };

    private TestMethodService.TestMethodChangedListener testMethodChangedListener = new TestMethodService.TestMethodChangedListener() {
        @Override
        public void onChange() {
            loadMethods();
            notifyObservers();
        }
    };

    private void loadMethods() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        testMethods.clear();
                        testMethods.addAll(testMethodHelper.allMethodNames());
                        for (int i = 0; i < testMethods.size(); i++) {
                            if (testMethods.get(i).equals(testMethod.getTestName())) {
                                methodSelectIndex = i;
                                break;
                            }
                        }
                        if (testMethods.size() == 0) {
                            testMethods.add(testMethod.getTestName());
                            methodSelectIndex = 0;
                        }
                        Message.obtain(handler, REFRESH_METHODS, testMethod).sendToTarget();
                    }
                }
        );
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.test_setting_fragment_usetemperature:
                if (isChecked) {
                    temperature_tv.setEnabled(true);
                    temperature_tv.setText("");
                    temperatureType_spinner.setEnabled(true);
                } else {
                    temperature_tv.setEnabled(false);
                    temperatureType_spinner.setEnabled(false);
                }
                break;
            case R.id.test_setting_fragment_autotest:
                if (isChecked) {
                    autoTestInterval_tv.setEnabled(true);
                    autoTestTimes_tv.setEnabled(true);
                    if (autoTestTimes_tv.getText().toString().length() == 0) {
                        autoTestTimes_tv.setText("10");
                        autoTestInterval_tv.setText("1");
                    }
                } else {
                    autoTestInterval_tv.setEnabled(false);
                    autoTestTimes_tv.setEnabled(false);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_setting_frament_save_button:
                doSave();
                break;
            case R.id.test_setting_fragment_view:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    private void doSave() {
        if (Cache.isTesting()) {
            ToastUtil.toastShow(mToast, getString(R.string.testing_delete_unlimit));
            return;
        }
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Message.obtain(handler, BaseActivity.SAVING).sendToTarget();
                            //判断参数是否为空，空会抛出异常。
                            checkFields();
                            //将本页的信息填入一个方法结构
                            buildTestMethod();
                            Cache.setTestId(testId_tv.getText().toString());
                            Cache.setTestMethod(testMethod);
                            Cache.setTestName(testName_tv.getText().toString());
                            //读取采用哪个公式（0，1，2为预置公式）
                            Formula formula = (Formula) formula_spinner.getSelectedItem();
                            //如果大于2，则是自定义公式，要按照自定义公式计算
                            if (formula_spinner.getSelectedItemPosition() >= 3) {
                                formula = DBService.getFormulaHelper(getActivity()).findFormula(formula.getFormulaName());
                            }
                            Cache.setCurrentFormula(formula);
                            //去数据库寻找这个名字的方法
                            TestMethod testMethod1 = testMethodHelper.findTestMethod(testMethod.getTestName());
                            //如果方法不存在，添加这个方法到方法库；
                            //存在，则设置这个已存在方法的id，替换设置中不规范的id，更新存储这个方法。
                            if (testMethod1 == null) {
                                testMethodHelper.addTestMethod(testMethod);
                            } else {
                                testMethod.setId(testMethod1.getId());
                                testMethodHelper.updateTestMethod(testMethod);
                            }
                            //在这个设置配置中，填充了每个成员，最后汇入writeCache中
                            SettingConfig settingConfig = new SettingConfig();
                            settingConfig.setTestId(Cache.getTestId());
                            settingConfig.setTestName(Cache.getTestName());
                            settingConfig.setTestMethod(Cache.getTestMethod());
                            //先得到当前用户，将包括整个参数配置的SettingConfig转化为一个string存入，这个用户的一个字段中；
                            //也就是说它实现了每个用户有一个特定的参数设置，它是将这一系列参数设置整合在一起存入一个Gson中。
                            writeCache(settingConfig);

                            if (settingMethod(Cache.getWaveLengthPosition(testMethod.getWaveLength()) + 1)) {
                                AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.test), getString(R.string.test_param_setting), getString(R.string.test_param_setting_save), Cache.currentTime(), auditHelper);
                                //不太懂这是做什么，莫非仅仅是为了不重复保存？ 应该是的相当于给他一个上了一个读写锁，如果再次被按下的键与这个不同
                                testMethodService.onChange(R.id.test_setting_frament_save_button);

                                Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
                                if (testMethod.isUseTemperature()) {
                                    Message.obtain(handler, TEMPERATURE_SAVING).sendToTarget();
                                }
                            } else {
                                Message.obtain(handler, BaseActivity.SAVE_FAILED).sendToTarget();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message.obtain(handler, BaseActivity.FIELD_CANNOT_BE_NULL, e.getMessage()).sendToTarget();
                        }
                    }
                }
        );
    }

    private boolean settingMethod(final int waveLength) {
        final Future<Void> future = ExecutorService.getInstance().submit(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        final ArrayBlockingQueue<TrunkData> trunkDatas = new ArrayBlockingQueue<TrunkData>(1);
                        trunkUtil.addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_TESTSETTING;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                trunkUtil.removeListener(R.id.test_setting_frament_save_button);
                                trunkDatas.offer(trunkData);
                                Log.d("recover", "setting method success");
                            }
                        }, R.id.test_setting_frament_save_button);
                        if (testMethod.isUseTemperature()) {
                            int temperature = (int) (BaseFormulaUtil.changeTemperature(testMethod.getTemperatureType(), testMethod.getTemperature()) * 10);
                            trunkUtil.setting(temperature, waveLength, testMethod.getAccuracy() == 0);
                        } else {
                            trunkUtil.setting(waveLength, testMethod.getAccuracy() == 0);
                        }

                        for (int i = 0; i < 3; i++) {
                            try {
                                TrunkData trunkData = trunkDatas.poll(1000, TimeUnit.MILLISECONDS);
                                Log.d("recover", "setting method");
                                if (trunkData != null) {
                                    Log.d("recover", "setting method success");
                                    return null;
                                }
                            } catch (Exception e) {
                                //IGNORE
                            }
                        }
                        throw new Exception("conn to trunk failed");
                    }
                }
        );
        try {
            future.get();
            return true;
        } catch (Exception e) {
            Log.e("test setting", e.getMessage());
        }
        return false;
    }

    //先得到当前用户，将包括整个参数配置的SettingConfig转化为一个string存入，这个用户的一个字段中；
    // 也就是说它实现了每个用户有一个特定的参数设置，它是将这一系列参数设置整合在一起存入一个Gson中。
    private void writeCache(SettingConfig settingConfig) {
        User user = Cache.getUser();
        user.setSettingConfig(JsonHelper.toJson(settingConfig));
        DBService.getUserHelper(getActivity()).updateUser(user);
    }

    private void buildTestMethod() {
        testMethod.setTestName(testMethod_tv.getSelectedItem().toString());
        testMethod.setTestCount(testCount_spinner.getSelectedItemPosition() + 1);
        String concentration = concentration_tv.getText().toString();
        if (concentration == null || concentration.length() == 0) {
            testMethod.setConcentration(null);
        } else {
            testMethod.setConcentration(Double.parseDouble(concentration));
        }
        testMethod.setConcentrationType(concentrationType_spinner.getSelectedItemPosition());
        testMethod.setAccuracy(testAccuracy_spinner.getSelectedItemPosition());
        testMethod.setFormulaName(((Formula) formula_spinner.getSelectedItem()).getFormulaName());
        testMethod.setWaveLength((String) (waveLength_spinner.getSelectedItem()));
        testMethod.setDecimals(decimal_spinner.getSelectedItemPosition() + 1);
        String testTubeLength = testTubeLength_tv.getText().toString();
        if (testTubeLength == null || testTubeLength.length() == 0) {
            testMethod.setTestTubeLength(null);
        } else {
            testMethod.setTestTubeLength(Double.parseDouble(testTubeLength));
        }
        String specificRotation = specificRotation_tv.getText().toString();
        if (specificRotation == null || specificRotation.length() == 0) {
            testMethod.setSpecificRotation(null);
        } else {
            testMethod.setSpecificRotation(Double.parseDouble(specificRotation));
        }
        testMethod.setAtlasX(atlasX_spinner.getSelectedItemPosition());
        testMethod.setAtlasY(atlasY_spinner.getSelectedItemPosition());
        testMethod.setUseTemperature(useTemperature_cb.isChecked());
        testMethod.setTemperatureType(temperatureType_spinner.getSelectedItemPosition());
        String temperature = temperature_tv.getText().toString();
        if (temperature == null || temperature.length() == 0) {
            temperature = "0";
        }
        testMethod.setTemperature(Double.parseDouble(temperature));
        testMethod.setAutoTest(autoTest_cb.isChecked());

        String autoTestTimes = autoTestTimes_tv.getText().toString();
        if (autoTestTimes == null || autoTestTimes.length() == 0) {
            autoTestTimes = "0";
        }
        testMethod.setAutoTestTimes(Integer.parseInt(autoTestTimes));
        String autoTestInterval = autoTestInterval_tv.getText().toString();
        if (autoTestInterval == null || autoTestInterval.length() == 0) {
            autoTestInterval = "0";
        }
        testMethod.setAutoTestInterval(Integer.parseInt(autoTestInterval));
        testMethod.setCreateDate(TimeTool.currentDate());
        testMethod.setCreator(Cache.getUser().getUserName());
    }

    private void checkFields() {
        if (StringUtils.isEmpty(testId_tv.getText().toString())) {
            String sample_no = getString(R.string.sample_no);
            throw new IllegalArgumentException(sample_no + canNotBeNull);
        }
        if (StringUtils.isEmpty(testName_tv.getText().toString())) {
            String sample_name = getString(R.string.sample_name);
            throw new IllegalArgumentException(sample_name + canNotBeNull);
        }
        if (StringUtils.isEmpty(testMethod_tv.getSelectedItem().toString())) {
            String test_method = getString(R.string.test_method);
            throw new IllegalArgumentException(test_method + canNotBeNull);
        }
        switch (formula_spinner.getSelectedItemPosition()) {
            case 1:
                if (StringUtils.isEmpty(concentration_tv.getText().toString())) {
                    String concentration = getString(R.string.concentration);
                    throw new IllegalArgumentException(concentration + canNotBeNull);
                }
                if (StringUtils.isEmpty(testTubeLength_tv.getText().toString())) {
                    String testtubelength = getString(R.string.testtubelength);
                    throw new IllegalArgumentException(testtubelength + canNotBeNull);
                }
                break;
            case 2:
                if (StringUtils.isEmpty(specificRotation_tv.getText().toString())) {
                    String specificrotation = getString(R.string.specificrotation);
                    throw new IllegalArgumentException(specificrotation + canNotBeNull);
                }
                if (StringUtils.isEmpty(testTubeLength_tv.getText().toString())) {
                    String testtubelength = getString(R.string.testtubelength);
                    throw new IllegalArgumentException(testtubelength + canNotBeNull);
                }
            default:
                break;
        }
        if (useTemperature_cb.isChecked()) {
            int temperatureType = temperatureType_spinner.getSelectedItemPosition();
            if (temperature_tv.getText().length() > 0) {
                double temperature = Double.parseDouble(temperature_tv.getText().toString());
                double temperature0 = BaseFormulaUtil.changeTemperatureTo0(temperatureType, temperature);
                if (temperature0 < 10 || temperature > 50) {
                    throw new IllegalArgumentException(getResources().getStringArray(R.array.temperature_range_err)[temperatureType]);
                }
            } else {
                throw new IllegalArgumentException(getResources().getStringArray(R.array.temperature_range_err)[temperatureType]);
            }
        }
        if (autoTest_cb.isChecked()) {
            if (autoTestTimes_tv.getText().length() > 0) {
                int autoTimes = Integer.parseInt(autoTestTimes_tv.getText().toString());
                if (autoTimes < 1 || autoTimes > 60) {
                    throw new IllegalArgumentException(getString(R.string.autotest_range));
                }
            } else {
                throw new IllegalArgumentException(getString(R.string.autotest_range));
            }
        }
    }

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.test_setting_fragment_method:
                reloadTestMethod();
                break;
            case R.id.test_setting_fragment_formula:
                reloadFormula(position);
                break;
        }

    }

    public void reloadTestMethod() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        TestMethod tempTestMethod = testMethodHelper.findTestMethod(testMethod_tv.getSelectedItem().toString());
                        if (tempTestMethod == null) {
                            testMethod.setTestName(testMethod_tv.getSelectedItem().toString());
                        } else {
                            testMethod = tempTestMethod;
                        }

                        Message.obtain(handler, REFRESH_TESTMETHOD, testMethod).sendToTarget();
                    }
                }
        );
    }

    public void reloadFormula(final int position) {
        switch (formula_spinner.getSelectedItemPosition()) {
            case 1:
                concentration_tv.setEnabled(true);
                specificRotation_tv.setText("");
                specificRotation_tv.setEnabled(false);
                break;
            case 2:
                concentration_tv.setText("");
                concentration_tv.setEnabled(false);
                specificRotation_tv.setEnabled(true);
                break;
            default:
                concentration_tv.setEnabled(true);
                specificRotation_tv.setEnabled(true);
                break;
        }
        Formula formula = formulas.get(position);
        decimals.clear();
        for (int i = 0; i < formula.getDecimal(); i++) {
            decimals.add((i + 1) + "");
        }
        decimalsAdapter.notifyDataSetChanged();
        decimal_spinner.setSelection(formula.getDecimal() - 1);
        decimalSelection = formula.getDecimal() - 1;
        formulaSelection = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class TestSettingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.FIELD_CANNOT_BE_NULL:
                    Toast.makeText(getActivity(), String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
                    break;
                case BaseActivity.SAVE_SUCCESS:
                    notifyObservers();
                    Toast.makeText(getActivity(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                    break;
                case BaseActivity.SAVE_FAILED:
                    Toast.makeText(getActivity(), getString(R.string.save_failed), Toast.LENGTH_SHORT).show();
                    break;
                case BaseActivity.NEED_REFRESH:
                    refreshValues();
                    break;
                case REFRESH_TESTMETHOD:
                    refreshTestMethod((TestMethod) msg.obj);
                    break;
                case REFRESH_WAVELENGTH:
                    waveLengthsAdapter.notifyDataSetChanged();
                    if (testMethod != null) {
                        waveLength_spinner.setSelection(Cache.getWaveLengthPosition(testMethod.getWaveLength()));
                    }
                    break;
                case REFRESH_METHODS:
                    testMethodsAdapter.notifyDataSetChanged();
                    testMethod_tv.setSelection(methodSelectIndex);
                    reloadTestMethod();
                    break;
                case TEMPERATURE_SAVING:
                    Toast.makeText(getActivity(), getString(R.string.temperature_saving), Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }

    private void notifyObservers() {
        for (final Observer observer : observers) {
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            observer.notifyDataChanged(Observer.REFRESH_BASE);
                        }
                    }
            );
        }
    }

    private void refreshValues() {
        testMethod = Cache.getTestMethod();
        refreshTestMethod(testMethod);
        testName_tv.setText(Cache.getTestName());
        testId_tv.setText(Cache.getTestId());
        loadMethods();
    }

    private void refreshTestMethod(TestMethod testMethod) {
//        if (formulaSelection == -1) {
        for (int i = 0; i < formulas.size(); i++) {
            if (formulas.get(i).getFormulaName().equals(testMethod.getFormulaName())) {
                formulaSelection = i;
                decimalSelection = formulas.get(i).getDecimal() - 1;
                decimals.clear();
                for (int j = 0; j < formulas.get(i).getDecimal(); j++) {
                    decimals.add((j + 1) + "");
                }
                break;
            }
        }
//        }
        if (testMethod.getDecimals() > decimals.size()) {
            decimalSelection = decimals.size() - 1;
        } else {
            decimalSelection = testMethod.getDecimals() - 1;
        }
        if (testMethod.getConcentration() != null) {
            concentration_tv.setText(testMethod.getConcentration() + "");
        } else {
            concentration_tv.setText("");
        }
        concentrationType_spinner.setSelection(testMethod.getConcentrationType());
        testCount_spinner.setSelection(testMethod.getTestCount() - 1);
        testAccuracy_spinner.setSelection(testMethod.getAccuracy());
        decimal_spinner.setSelection(decimalSelection);
        formula_spinner.setSelection(formulaSelection);
        waveLength_spinner.setSelection(Cache.getWaveLengthPosition(testMethod.getWaveLength()));
        if (testMethod.getTestTubeLength() != null) {
            testTubeLength_tv.setText(testMethod.getTestTubeLength() + "");
        } else {
            testTubeLength_tv.setText("");
        }
        if (testMethod.getSpecificRotation() != null) {
            specificRotation_tv.setText(testMethod.getSpecificRotation() + "");
        } else {
            specificRotation_tv.setText("");
        }
        temperature_tv.setText(testMethod.getTemperature() + "");
        if (testMethod.isUseTemperature()) {
            useTemperature_cb.setChecked(true);
            temperature_tv.setEnabled(true);
            temperatureType_spinner.setEnabled(true);
            temperature_tv.setText(testMethod.getTemperature() + "");
        } else {
            useTemperature_cb.setChecked(false);
            temperature_tv.setEnabled(false);
            temperature_tv.setText("");
            temperatureType_spinner.setEnabled(false);
        }
        if (testMethod.isAutoTest()) {
            autoTest_cb.setChecked(true);
            autoTestTimes_tv.setEnabled(true);
            autoTestInterval_tv.setEnabled(true);
            autoTestTimes_tv.setText(testMethod.getAutoTestTimes() + "");
            autoTestInterval_tv.setText(testMethod.getAutoTestInterval() + "");
        } else {
            autoTest_cb.setChecked(false);
            autoTestTimes_tv.setEnabled(false);
            autoTestInterval_tv.setEnabled(false);
            autoTestTimes_tv.setText("");
            autoTestInterval_tv.setText("");
        }
        atlasX_spinner.setSelection(testMethod.getAtlasX());
        atlasY_spinner.setSelection(testMethod.getAtlasY());
    }
}
