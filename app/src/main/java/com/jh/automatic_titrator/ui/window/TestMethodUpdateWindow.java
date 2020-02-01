package com.jh.automatic_titrator.ui.window;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.FormulaHelper;
import com.jh.automatic_titrator.common.db.WaveLengthHelper;
import com.jh.automatic_titrator.common.formula.BaseFormulaUtil;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.entity.common.TestMethod;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.FormulaService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.component.FormulaAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/1/2.
 */
public class TestMethodUpdateWindow implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private Context mContext;

    private View mView;

    private AlertDialog dialog;

    private EditText testMethod_tv;

    private EditText concentration_tv;

    private Spinner concentrationType_spinner;

    private Spinner testCount_spinner;

    private Spinner testAccuracy_spinner;

    private Spinner formula_spinner;

    private Spinner waveLength_spinner;

    private EditText testTubeLength_tv;

    private Spinner decimal_spinner;

    private EditText specificRotation_tv;

    private Spinner atlasX_spinner;

    private Spinner atlasY_spinner;

    private CheckBox useTemperature_cb;

    private EditText temperature_tv;

    private Spinner temperatureType_spinner;

    private CheckBox autoTest_cb;

    private EditText autoTestInterval_tv;

    private EditText autoTestTimes_tv;

    private ArrayAdapter<String> concentrationTypeAdapter;

    private WaveLengthHelper waveLengthHelper;

    private OnSaveListener onSaveListener;

    private View specificrotation_layout;
    private View concentration_layout;
    private View tubelength_layout;

    private View saveBtn;

    private TestMethodUpdateWindowHandler handler;

    private View alterBtn;

    private TextView alterBtnTxt;

    private boolean altering;

    private List<Formula> formulas;

    private FormulaHelper formulaHelper;

    private FormulaAdapter formulaAdapter;

    private Toast mToast;

    private List<String> decimals;
    private ArrayAdapter<String> decimalAdapter;

    public TestMethodUpdateWindow(Context mContext, WaveLengthHelper waveLengthHelper) {
        this.mContext = mContext;
        mToast = ToastUtil.createToast(mContext);
        formulaHelper = DBService.getFormulaHelper(mContext);
        this.waveLengthHelper = waveLengthHelper;
        handler = new TestMethodUpdateWindowHandler();
        mView = LayoutInflater.from(mContext).inflate(R.layout.setting_method_fragment_alter, null);
        testMethod_tv = (EditText) mView.findViewById(R.id.setting_method_alter_method_name);
        concentration_tv = (EditText) mView.findViewById(R.id.setting_method_alter_method_concentration);
        concentrationType_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_method_concentrationtype);
        testCount_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_testcount);
        testAccuracy_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_accuracy);
        formula_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_formula);
        waveLength_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_wavelength);
        decimal_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_showdecimals);
        testTubeLength_tv = (EditText) mView.findViewById(R.id.setting_method_alter_testtubelength);
        specificRotation_tv = (EditText) mView.findViewById(R.id.setting_method_alter_specificrotation);
        atlasX_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_atlasX);
        atlasY_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_atlasY);
        useTemperature_cb = (CheckBox) mView.findViewById(R.id.setting_method_alter_usetemperature);
        useTemperature_cb.setOnCheckedChangeListener(this);
        temperature_tv = (EditText) mView.findViewById(R.id.setting_method_alter_temperature);
        temperatureType_spinner = (Spinner) mView.findViewById(R.id.setting_method_alter_temperaturetype);
        autoTest_cb = (CheckBox) mView.findViewById(R.id.setting_method_alter_autotest);
        autoTest_cb.setOnCheckedChangeListener(this);
        autoTestInterval_tv = (EditText) mView.findViewById(R.id.setting_method_alter_autotestinterval);
        autoTestTimes_tv = (EditText) mView.findViewById(R.id.setting_method_alter_autotesttimes);

        specificrotation_layout = mView.findViewById(R.id.setting_method_alter_specificrotation_layout);
        concentration_layout = mView.findViewById(R.id.setting_method_alter_method_concentration_layout);
        tubelength_layout = mView.findViewById(R.id.setting_method_alter_testtubelength_layout);

        String[] concentrationTypes = mContext.getResources().getStringArray(R.array.concentration_type);
        ArrayAdapter<String> concentrationTypeAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, concentrationTypes);
        concentrationTypeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        concentrationType_spinner.setAdapter(concentrationTypeAdapter);

        String[] testCountType = mContext.getResources().getStringArray(R.array.number_test);
        ArrayAdapter<String> testCountTypeAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, testCountType);
        testCountTypeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        testCount_spinner.setAdapter(testCountTypeAdapter);

        String[] testAccuracy = mContext.getResources().getStringArray(R.array.test_accuracy);
        ArrayAdapter<String> testAccuracyAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, testAccuracy);
        testAccuracyAdapter.setDropDownViewResource(R.layout.spinner_item1);
        testAccuracy_spinner.setAdapter(testAccuracyAdapter);

        formulas = new ArrayList<>();
        formulas.addAll(Cache.getFormulas());
        formulaAdapter = new FormulaAdapter(mContext, R.layout.spinner_checked1, Cache.getFormulas());
        formulaAdapter.setDropDownViewResource(R.layout.spinner_item1);
        formula_spinner.setAdapter(formulaAdapter);
        formula_spinner.setOnItemSelectedListener(this);

        decimals = new ArrayList<>();
        int decimal = formulas.get(0).getDecimal();
        for (int i = 1; i <= decimal; i++) {
            decimals.add(i + "");
        }
        decimalAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, decimals);
        decimalAdapter.setDropDownViewResource(R.layout.spinner_item1);
        decimal_spinner.setAdapter(decimalAdapter);

        String[] atlasXs = mContext.getResources().getStringArray(R.array.test_atlasX);
        ArrayAdapter<String> atlasXsAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, atlasXs);
        atlasXsAdapter.setDropDownViewResource(R.layout.spinner_item1);
        atlasX_spinner.setAdapter(atlasXsAdapter);

        String[] atlasYs = mContext.getResources().getStringArray(R.array.test_atlasY);
        ArrayAdapter<String> atlasYsAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, atlasYs);
        atlasYsAdapter.setDropDownViewResource(R.layout.spinner_item1);
        atlasY_spinner.setAdapter(atlasYsAdapter);

        String[] temperatureType = mContext.getResources().getStringArray(R.array.temperature_type);
        ArrayAdapter<String> temperatureTypeAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, temperatureType);
        temperatureTypeAdapter.setDropDownViewResource(R.layout.spinner_item1);
        temperatureType_spinner.setAdapter(temperatureTypeAdapter);

        autoTest_cb.setOnCheckedChangeListener(this);
        autoTestInterval_tv.setEnabled(false);

        saveBtn = mView.findViewById(R.id.setting_method_alter_save_button);
        saveBtn.setOnClickListener(this);
        alterBtnTxt = (TextView) mView.findViewById(R.id.setting_method_alter_save_button_tv);

        FormulaService formulaService = FormulaService.getInstance();
        formulaService.addListener(R.id.setting_method_alter_save_button_tv, new FormulaService.FormulaChangeListener() {
            @Override
            public void onChanged() {
                formulas.clear();
//                formulas.addAll(Cache.getBaseFormula());
                formulas.addAll(Cache.getFormulas());
                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.details));

        builder.setView(mView);
        dialog = builder.create();
    }

    public void show(TestMethod testMethod) {
        mView.setEnabled(false);
        alterBtnTxt.setText("修改");
        altering = false;

        List<String> waveLengths = waveLengthHelper.getWaveLengthStrs();
        ArrayAdapter<String> waveLengthsAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_checked1, waveLengths);
        waveLengthsAdapter.setDropDownViewResource(R.layout.spinner_item1);
        waveLength_spinner.setAdapter(waveLengthsAdapter);
        waveLength_spinner.setSelection(Cache.getWaveLengthPosition(testMethod.getWaveLength()));

        testMethod_tv.setText(testMethod.getTestName());
        testMethod_tv.setEnabled(false);
        testMethod_tv.setAlpha(0.5f);
        if (testMethod.getConcentration() != null) {
            concentration_tv.setText(testMethod.getConcentration() + "");
        } else {
            concentration_tv.setText("");
        }
        concentrationType_spinner.setSelection(testMethod.getConcentrationType());
        testCount_spinner.setSelection(testMethod.getTestCount() - 1);
        testAccuracy_spinner.setSelection(testMethod.getAccuracy());
        decimal_spinner.setSelection(testMethod.getDecimals() - 1);
        int position = 0;
        for (int i = 0; i < formulas.size(); i++) {
            Formula formula = formulas.get(i);
            if (formula.getFormulaName().equals(testMethod.getFormulaName())) {
                position = i;
                break;
            }
        }
        formula_spinner.setSelection(position);
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
        } else {
            useTemperature_cb.setChecked(false);
            temperature_tv.setEnabled(false);
            temperature_tv.setText("");
            temperatureType_spinner.setEnabled(false);
        }
        if (testMethod.isAutoTest()) {
            autoTest_cb.setChecked(true);
            autoTestTimes_tv.setText(testMethod.getAutoTestTimes() + "");
            autoTestInterval_tv.setText(testMethod.getAutoTestInterval() + "");
        } else {
            autoTest_cb.setChecked(false);
            autoTestTimes_tv.setText("");
            autoTestInterval_tv.setText("");
            autoTestTimes_tv.setEnabled(false);
            autoTestInterval_tv.setEnabled(false);
        }
        if (!Cache.containsAuth("method")
                || (Cache.getTestMethod().getId() == testMethod.getId()
                && Cache.isTesting())) {
            saveBtn.setVisibility(View.INVISIBLE);
        } else {
            saveBtn.setVisibility(View.VISIBLE);
        }
        disableAll();
        dialog.show();
    }

    public void disableAll() {
        testCount_spinner.setEnabled(false);
        concentration_tv.setEnabled(false);
        concentrationType_spinner.setEnabled(false);
        testAccuracy_spinner.setEnabled(false);
        formula_spinner.setEnabled(false);
        waveLength_spinner.setEnabled(false);
        decimal_spinner.setEnabled(false);
        tubelength_layout.setEnabled(false);
        specificRotation_tv.setEnabled(false);
        atlasX_spinner.setEnabled(false);
        atlasY_spinner.setEnabled(false);
        useTemperature_cb.setEnabled(false);
        temperature_tv.setEnabled(false);
        temperatureType_spinner.setEnabled(false);
        autoTest_cb.setEnabled(false);
        autoTestInterval_tv.setEnabled(false);
        autoTestTimes_tv.setEnabled(false);
        testTubeLength_tv.setEnabled(false);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void enableAll() {
        testCount_spinner.setEnabled(true);
        concentration_tv.setEnabled(true);
        concentrationType_spinner.setEnabled(true);
        testAccuracy_spinner.setEnabled(true);
        formula_spinner.setEnabled(true);
        waveLength_spinner.setEnabled(true);
        decimal_spinner.setEnabled(true);
        tubelength_layout.setEnabled(true);
        specificRotation_tv.setEnabled(true);
        atlasX_spinner.setEnabled(true);
        atlasY_spinner.setEnabled(true);
        useTemperature_cb.setEnabled(true);
        temperature_tv.setEnabled(true);
        temperatureType_spinner.setEnabled(true);
        autoTest_cb.setEnabled(true);
        autoTestInterval_tv.setEnabled(true);
        autoTestTimes_tv.setEnabled(true);
        testTubeLength_tv.setEnabled(true);
    }

    public void dismiss() {
        dialog.dismiss();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_method_alter_save_button:
                if (!altering) {
                    mView.setEnabled(true);
                    enableAll();
                    altering = true;
                    alterBtnTxt.setText("保存");
                } else {
                    doSave();
                }
                break;
        }
    }

    private void doSave() {
        if (onSaveListener != null) {
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                checkFields();
                                TestMethod testMethod = buildTestMethod();
                                onSaveListener.onSave(testMethod);
                            } catch (Exception e) {
                                Message.obtain(handler, 0, e.getMessage()).sendToTarget();
                            }
                        }
                    }
            );
        }
    }

    private void checkFields() throws Exception {
        String canNotBeNull = mContext.getString(R.string.can_not_be_null);
        if (StringUtils.isEmpty(testMethod_tv.getText().toString())) {
            String test_method = mContext.getString(R.string.test_method_name);
            throw new IllegalArgumentException(test_method + canNotBeNull);
        }
        switch (formula_spinner.getSelectedItemPosition()) {
            case 1:
                if (StringUtils.isEmpty(concentration_tv.getText().toString())) {
                    String concentration = mContext.getString(R.string.concentration);
                    throw new IllegalArgumentException(concentration + canNotBeNull);
                }
                if (StringUtils.isEmpty(testTubeLength_tv.getText().toString())) {
                    String testtubelength = mContext.getString(R.string.testtubelength);
                    throw new IllegalArgumentException(testtubelength + canNotBeNull);
                }
                break;
            case 2:
                if (StringUtils.isEmpty(specificRotation_tv.getText().toString())) {
                    String specificrotation = mContext.getString(R.string.specificrotation);
                    throw new IllegalArgumentException(specificrotation + canNotBeNull);
                }
                if (StringUtils.isEmpty(testTubeLength_tv.getText().toString())) {
                    String testtubelength = mContext.getString(R.string.testtubelength);
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
                    throw new IllegalArgumentException(mContext.getResources().getStringArray(R.array.temperature_range_err)[temperatureType]);
                }
            } else {
                throw new IllegalArgumentException(mContext.getResources().getStringArray(R.array.temperature_range_err)[temperatureType]);
            }
        }
        if (autoTest_cb.isChecked()) {
            if (autoTestTimes_tv.getText().length() > 0) {
                int autoTimes = Integer.parseInt(autoTestTimes_tv.getText().toString());
                if (autoTimes < 1 || autoTimes > 60) {
                    throw new IllegalArgumentException(mContext.getString(R.string.autotest_range));
                }
            } else {
                throw new IllegalArgumentException(mContext.getString(R.string.autotest_range));
            }
        }
    }

    private TestMethod buildTestMethod() throws Exception {
        TestMethod testMethod = new TestMethod();
        testMethod.setTestName(testMethod_tv.getText().toString());
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
        return testMethod;
    }

    public OnSaveListener getOnSaveListener() {
        return onSaveListener;
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.setting_method_alter_usetemperature:
                if (isChecked) {
                    temperature_tv.setEnabled(true);
                    temperature_tv.setAlpha(1.0f);
                    temperatureType_spinner.setEnabled(true);
                    temperatureType_spinner.setAlpha(1.0f);
                } else {
                    temperature_tv.setEnabled(false);
                    temperature_tv.setAlpha(0.5f);
                    temperatureType_spinner.setEnabled(false);
                    temperatureType_spinner.setAlpha(0.5f);
                }
                break;
            case R.id.setting_method_alter_autotest:
                if (isChecked) {
                    autoTestInterval_tv.setEnabled(true);
                    autoTestInterval_tv.setAlpha(1.0f);
                    autoTestTimes_tv.setEnabled(true);
                    autoTestTimes_tv.setAlpha(1.0f);
                    if (autoTestTimes_tv.getText().toString().length() == 0) {
                        autoTestTimes_tv.setText("10");
                        autoTestInterval_tv.setText("1");
                    }
                } else {
                    autoTestInterval_tv.setEnabled(false);
                    autoTestInterval_tv.setAlpha(0.5f);
                    autoTestTimes_tv.setEnabled(false);
                    autoTestTimes_tv.setAlpha(0.5f);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        int decimal = formula.getDecimal();
        decimals.clear();
        for (int i = 0; i < decimal; i++) {
            decimals.add((i + 1) + "");
        }
        decimalAdapter.notifyDataSetChanged();
        decimal_spinner.setSelection(decimal - 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnSaveListener {
        void onSave(TestMethod testMethod);
    }

    public class TestMethodUpdateWindowHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    if (msg.obj != null) {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    }
                    break;
            }
        }
    }
}
