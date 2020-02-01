package com.jh.automatic_titrator.ui.test;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.Observer;
import com.jh.automatic_titrator.common.ObserverAble;
import com.jh.automatic_titrator.common.db.TestHelper;
import com.jh.automatic_titrator.common.formula.BaseFormulaUtil;
import com.jh.automatic_titrator.common.trunk.TrunkConst;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.Concentration;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.entity.common.SingleResult;
import com.jh.automatic_titrator.entity.common.Test;
import com.jh.automatic_titrator.entity.common.TestData;
import com.jh.automatic_titrator.entity.common.TestMethod;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.MethodService;
import com.jh.automatic_titrator.service.SimpleResService;
import com.jh.automatic_titrator.service.TestMethodService;
import com.jh.automatic_titrator.ui.BaseActivity;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 16/9/17.
 */
public class TestTestFragment extends Fragment implements Observer, ObserverAble, View.OnClickListener {

    public static final long MIN_REFRESH_INTERVAL = 50;

    public static final int SHOW_MSG = 0xFFFFFFFF;

    public static final int CHANGE_STATE = 0xFFFFFFFE;
    public volatile boolean destoried = false;
    private TextView test_test_name;
    private TextView test_test_type;
    private View test_test_tupu;
    private TextView test_test_value;
    private TextView test_test_unit;
    private View test_test_ceshi;
    private Button test_test_ceshi_tv;
    private View test_test_qingling;
    private List<SingleResult> singleResults;
    private String[] temperatureTypes;
    private String currentTemperatureType;
    private String formulaName;
    private String formulaSimpleName;
    private String formulaUnit;
    private boolean cycleTest;
    private int decimal;
    private FragmentManager fragmentManager;
    private TestTestListFragment testTestListFragment;
    private TestTestTupuFragment testTestTupuFragment;
    private boolean isInTupuMode = false;
    private List<Observer> observers;
    private TestHelper testHelper;
    private TrunkUtil trunkUtil;
    private TestTestHandler handler;
    private ReadWriteLock readWriteLock;
    private volatile boolean testing;
    private boolean cycleTesting;
    private Future<Void> autoTestThread;
    private boolean stateChanged = false;
    private boolean startRefreshTupu = false;
    private SimpleResService simpleResService;
    private volatile double lastValue = 0;
    private volatile long lastUpdateTime = 0;
    private volatile long lastUpdateTime1 = 0;
    private long baseInterval = 1000;
    private int currentTemperature = 0;
    private boolean cleaned = true;
    private Toast mToast;
    private String[] concentrationUnits;
    boolean autoTest = false;

    private FileOutputStream fileOutputStream;

    private BaseActivity mActivity;

    private TrunkListener trunkCleanListener = new TrunkListener() {
        @Override
        public int getListenType() {
            return TrunkConst.TYPE_CLEAN;
        }

        @Override
        public void notifyData(TrunkData trunkData) {
            int status = (int) trunkData.getData();
            switch (status) {
                default:
                    readWriteLock.writeLock().lock();
                    try {
                        singleResults.clear();
                        Cache.setLastModify(System.currentTimeMillis());
                    } finally {
                        readWriteLock.writeLock().unlock();
                    }
                    Message.obtain(handler, BaseActivity.NEED_CLEAR, "清零成功").sendToTarget();
                    break;
            }
        }
    };

    private TrunkListener temperatureListener = new TrunkListener() {
        @Override
        public int getListenType() {
            return TrunkConst.TYPE_TEMPRETURE_CHANGE;
        }

        @Override
        public void notifyData(TrunkData trunkData) {
            currentTemperature = (int) trunkData.getData();
        }
    };

    private TrunkListener temperatureCompListener = new TrunkListener() {
        @Override
        public int getListenType() {
            return TrunkConst.TYPE_TEMPRETURE_COMPLETE;
        }

        @Override
        public void notifyData(TrunkData trunkData) {
            Message.obtain(handler, SHOW_MSG, getString(R.string.temp_control_success)).sendToTarget();
        }
    };
    private TrunkListener trunkDataListener = new TrunkListener() {
        @Override
        public int getListenType() {
            return TrunkConst.TYPE_TESTDATA;
        }

        @Override
        public void notifyData(TrunkData trunkData) {
            if (handler == null) {
                return;
            }
            TestData testData = (TestData) trunkData.getData();
            readWriteLock.writeLock().lock();
            try {
                if (cycleTest) {
                    cycleTestDataDeal(testData);
                } else {
                    commonTestDataDeal(testData);
                }
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_test_fragment, container, false);
        mActivity = (BaseActivity) getActivity();
        mToast = ToastUtil.createToast(getActivity());
        readWriteLock = new ReentrantReadWriteLock();

        temperatureTypes = this.getActivity().getResources().getStringArray(R.array.temperature_type);

        test_test_name = (TextView) view.findViewById(R.id.test_test_name);
        test_test_type = (TextView) view.findViewById(R.id.test_test_type);
        test_test_tupu = view.findViewById(R.id.test_test_tupu);
        test_test_tupu.setOnClickListener(this);

        test_test_value = (TextView) view.findViewById(R.id.test_test_value);
        test_test_unit = (TextView) view.findViewById(R.id.test_test_unit);
        test_test_ceshi_tv = (Button) view.findViewById(R.id.test_test_ceshi);
        test_test_ceshi = view.findViewById(R.id.test_test_ceshi);
        test_test_ceshi.setOnClickListener(this);

        test_test_qingling = view.findViewById(R.id.test_test_qingling);
        test_test_qingling.setOnClickListener(this);

        singleResults = new ArrayList<>();
        Cache.setSingleResults(singleResults);
        fragmentManager = getFragmentManager();

        observers = new ArrayList<>();
        addTestTestFrame();
        showDefault();
        testing = false;

        TestMethod testMethod = Cache.getTestMethod();
        Formula formula = Cache.getCurrentFormula();
        if (formula == null) {
            Cache.setCurrentFormula(Cache.getBaseFormula().get(0));
        }
        formulaName = formula.getFormulaName();
        formulaSimpleName = formula.getSimpleName();
        formulaUnit = formula.getUnit();
        currentTemperatureType = temperatureTypes[testMethod.getTemperatureType()];

        test_test_name.setText(testMethod.getTestName());
        if (StringUtils.isNotEmpty(formulaSimpleName)) {
            test_test_type.setText(formulaName + "(" + formulaSimpleName + ")");
        } else {
            test_test_type.setText(formulaName);
        }
        concentrationUnits = getResources().getStringArray(R.array.concentration_type);
        test_test_value.setText(String.format("%." + testMethod.getDecimals() + "f", 0.0));
        if (formula instanceof Concentration) {
            formulaUnit = concentrationUnits[Cache.getTestMethod().getConcentrationType()];
        }
        test_test_unit.setText(formulaUnit);
        cycleTest = testMethod.isAutoTest();
        decimal = testMethod.getDecimals();


        testHelper = DBService.getTestHelper(this.getActivity());
        handler = new TestTestHandler();
        trunkUtil = TrunkUtil.getInstance();
        trunkUtil.addListener(trunkDataListener, R.id.test_test_ceshi);
        trunkUtil.addListener(trunkCleanListener, R.id.test_test_qingling);
        trunkUtil.addListener(temperatureListener, R.id.test_test_tupu);
        trunkUtil.addListener(temperatureCompListener, R.id.test_test_value);

        refreshTupu();
        simpleResService = SimpleResService.getInstance(3600);

        baseInterval = 1000 << Cache.getTestMethod().getAtlasX();

        MethodService.getInstance().addListener(R.id.test_test_ceshi, new MethodService.MethodChangeListener() {
            @Override
            public void onChanged(TestMethod testMethod, int type) {
                notifyDataChanged(Observer.REFRESH_BASE);
            }
        });

        TestMethodService.getInstance().addTestMethodListener(R.id.test_test_ceshi, new TestMethodService.TestMethodChangedListener() {
            @Override
            public void onChange() {
                notifyDataChanged(Observer.REFRESH_BASE);
            }
        });

        /**
         * 0点自动清零
         */
//        ExecutorService.getInstance().execute(new Runnable() {
//            @Override
//            public void run() {
//                while (!destoried) {
//                    try {
//                        long currentTime = System.currentTimeMillis();
////                        boolean autoTest = mActivity.getAutoTest();
//                        if (!testing && !cycleTest) {
////                            readWriteLock.writeLock().lock();
////                            try {
//                            if (lastValue == 0 && singleResults.size() > 0 && currentTime - lastUpdateTime > 2000 && singleResults.get(singleResults.size() - 1).getRes() != 0) {
//                                singleResults.clear();
//                                lastUpdateTime = currentTime;
//                                Cache.setLastModify(currentTime);
//                            }
////                            } finally {
////                                readWriteLock.writeLock().unlock();
////                            }
//                        }
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_test_ceshi:
                if (stateChanged) {
                    singleResults.clear();
                    Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                }
                test();
                stateChanged = false;
                break;
            case R.id.test_test_qingling:
                doClean();
                break;
            case R.id.test_test_tupu:
                if (isInTupuMode) {
                    changeToListFragment();
                    isInTupuMode = false;
                } else {
                    changeToTupuFragment();
                    isInTupuMode = true;
                }
                break;
        }
    }

    private void test() {
        if (cycleTest == true) {
            if (!cycleTesting) {
                if (singleResults.size() > 0) {
                    singleResults.clear();
                    Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                }
                Cache.setTesting(true);
                cleaned = false;
                doCycleTest();
            } else {
                stopTest();
            }
        } else {
            Cache.setTesting(true);
            cleaned = false;
            if (singleResults.size() >= Cache.getTestMethod().getTestCount()) {
                singleResults.clear();
                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
            }
            doTest();
        }
    }

    private void doTest() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        testing = true;
                        trunkUtil.singleTest();
                    }
                }
        );
    }

    private void doCycleTest() {
        cycleTesting = true;
        test_test_ceshi.setBackground(getResources().getDrawable(R.drawable.ceshi_btn));
        test_test_ceshi_tv.setText(getString(R.string.cancel));
        testWithInterval(0);
    }

    private void testWithInterval(final long interval) {
        autoTestThread = ExecutorService.getInstance().submit(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        if (interval > 0) {
                            try {
                                Thread.sleep(interval);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (cycleTest && cycleTesting) {
                            testing = true;
                            doTest();
                        }
                        return null;
                    }
                }
        );
    }

    private void cycleTestDataDeal(TestData testData) {
        SingleResult singleResult = createSingleResult(testData);
        Message.obtain(handler, BaseActivity.NEED_REFRESH, singleResult).sendToTarget();
        if (testData.getType() == 1) {
            testing = false;
            if (singleResults.size() < Cache.getTestMethod().getAutoTestTimes()) {
                singleResults.add(singleResult);
                saveToTest(singleResult);
                if (singleResults.size() < Cache.getTestMethod().getAutoTestTimes()) {
                    testWithInterval(Cache.getTestMethod().getAutoTestInterval() * 1000 * 60);
                } else {
                    Cache.setTesting(false);
                    cycleTesting = false;
                }
                Message.obtain(handler, CHANGE_STATE).sendToTarget();
            }
        }
    }

    private void commonTestDataDeal(TestData testData) {
        SingleResult singleResult = createSingleResult(testData);
        long currentTime = System.currentTimeMillis();
        double currentValue = singleResult.getRes();
        if ((currentTime - lastUpdateTime1) > MIN_REFRESH_INTERVAL) {
            Message.obtain(handler, BaseActivity.NEED_REFRESH, singleResult).sendToTarget();
            lastUpdateTime1 = currentTime;
        }

        if (testData.getType() != 1 && !testing) {
            if (autoTest && currentTime - lastUpdateTime > 2000 && lastValue == 0) {
                if (singleResults != null && singleResults.size() > 0 && singleResults.get(singleResults.size() - 1).getRes() != 0) {
                    singleResults.clear();
                    lastUpdateTime = currentTime;
                    Cache.setLastModify(currentTime);
                }
            }
        } else if (testData.getType() == 1) {
            boolean needClean = true;
            if (testing) {
                testing = false;
                if (singleResults.size() < Cache.getTestMethod().getTestCount()) {
                    cleaned = false;
                    singleResults.add(singleResult);
                    saveToTest(singleResult);
                    if (singleResults.size() < Cache.getTestMethod().getTestCount()) {
                        doTest();
                    } else {
                        Cache.setTesting(false);
                    }
                    Message.obtain(handler, CHANGE_STATE).sendToTarget();
                    return;
                }
            } else {
                double q = Cache.getTestMethod().getDecimals() > 3 ? Cache.getAutoClean4() : Cache.getAutoClean3();
                boolean p = Math.abs(currentValue - lastValue) < (3 * q);
                if (singleResults != null && singleResults.size() > 0) {
                    double r = singleResults.get(singleResults.size() - 1).getRes();
                    needClean = cleaned || (p && (Math.abs(r - currentValue) > (3 * q)));
                }
            }

            if (autoTest && needClean && testData.getType() == 1 && !cycleTest && Math.abs(singleResult.getRes()) > 0.01) {
                singleResults.clear();
                Cache.setTesting(true);
                testing = false;
                cleaned = false;
                if (singleResults.size() < Cache.getTestMethod().getTestCount()) {
                    singleResults.add(singleResult);
                    saveToTest(singleResult);
                    if (singleResults.size() < Cache.getTestMethod().getTestCount() && !Cache.getTestMethod().isAutoTest()) {
                        doTest();
                    } else {
                        Cache.setTesting(false);
                    }
                }
            } else {
                testing = false;
            }
            if (currentValue != lastValue || currentValue != 0) {
                lastValue = currentValue;
                lastUpdateTime = currentTime;
            } else {
                testing = false;
            }
        }
    }

    private void stopTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.prompt)).setMessage(R.string.stop_test)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cycleTesting = false;
                        try {
                            autoTestThread.get(1, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            Log.d("testing", "cancel exception " + e);
                        }
                        if (!autoTestThread.isCancelled()) {
                            Log.d("testing", "cancel false");
                            autoTestThread.cancel(true);
                        }
                        Cache.setTesting(false);
                        Message.obtain(handler, CHANGE_STATE).sendToTarget();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void doClean() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        readWriteLock.writeLock().lock();
                        try {
                            trunkUtil.clean();
                        } finally {
                            readWriteLock.writeLock().unlock();
                        }
                    }
                }
        );
    }

    private void saveToTest(SingleResult singleResult) {
        Test test = new Test();
        test.setRes(getLastRes(singleResult));
        test.setOptical(singleResult.getOptical());
        test.setFormulaName(formulaName);
        test.setFormulaUnit(singleResult.getResUnit());
        test.setSimpleFormulaName(formulaSimpleName);
        test.setDate(TimeTool.currentDate());
        test.setTestName(Cache.getTestName());
        test.setTestId(Cache.getTestId());
        test.setTestMethod(Cache.getTestMethod().getTestName());
        test.setDecimal(Cache.getTestMethod().getDecimals());
        double temperatureValue = BaseFormulaUtil.changeTemperature(Cache.getTestMethod().getTemperatureType(), Cache.getTestMethod().getTemperature());
        double temperatureValue1 = BaseFormulaUtil.changeTemperature(Cache.getTestMethod().getTemperatureType(), currentTemperature / 10.0);
        if (Cache.getTestMethod().isUseTemperature()) {
            test.setWantedTemperature(temperatureValue + currentTemperatureType);
        } else {
            test.setWantedTemperature("--");
        }
        test.setRealTemperature(temperatureValue1 + currentTemperatureType);

        test.setTestCreator(Cache.getUser().getUserName());
        test.setWavelength(Cache.getTestMethod().getWaveLength());
        if (Cache.getTestMethod().getTestTubeLength() != null) {
            test.setTubelength(Cache.getTestMethod().getTestTubeLength() + "cm");
        }
        if (Cache.getTestMethod().getSpecificRotation() != null) {
            test.setSpecificrotation(Cache.getTestMethod().getSpecificRotation() + "°");
        }
        if (Cache.getTestMethod().getConcentration() != null) {
            test.setConcentration(Cache.getTestMethod().getConcentration() + concentrationUnits[Cache.getTestMethod().getConcentrationType()]);
        }
        testHelper.saveTest(test);

        Cache.setLastModify(System.currentTimeMillis());
    }

    private double getLastRes(SingleResult singleResult) {
        return Double.parseDouble(String.format("%." + decimal + "f", singleResult.getRes()));
    }

    private SingleResult createSingleResult(TestData testData) {
        SingleResult singleResult = new SingleResult();

        singleResult.setTemperatureType(currentTemperatureType);
        int temperatureType = Cache.getTestMethod().getTemperatureType();
        double temperatureValue = BaseFormulaUtil.changeTemperature(temperatureType, currentTemperature / 10.0);

        singleResult.setRealTemperature(temperatureValue + " " + currentTemperatureType);
        singleResult.setWantedTemperature(Cache.getTestMethod().getTemperature() + " " + currentTemperatureType);

        singleResult.setRes(testData.getData() / 10000.0);
        Formula formula = Cache.getCurrentFormula();
        singleResult.setResType(formula.getFormulaName());
        singleResult.setDecimal(decimal);
        double optical = testData.getData() / 10000.0;
        if (Cache.isAutoClean() && testData.getType() == 1) {
            if (Cache.getTestMethod().getDecimals() <= 3) {
                if (Math.abs(optical) <= Cache.getAutoClean3()) {
                    optical = 0.0;
                }
            } else {
                if (Math.abs(optical) <= Cache.getAutoClean4()) {
                    optical = 0.0;
                }
            }
        }
        singleResult.setOptical(optical);
        Double c = Cache.getTestMethod().getConcentration();
        if (c == null) {
            c = 0.0;
        }
        Double a = Cache.getTestMethod().getSpecificRotation();
        if (a == null) {
            a = 0.0;
        }
        Double l = Cache.getTestMethod().getTestTubeLength();
        if (l == null) {
            l = 0.0;
        } else {
            l = l.doubleValue() / 10.0;
        }
        switch (Cache.getTestMethod().getConcentrationType()) {
            case 0:
                l = l * 0.001;
                break;
            case 1:
                l = l * 1;
                break;
            case 2:
                l = l * 0.01;
                break;
        }


        singleResult.setRes(formula.getDesRes(optical, currentTemperature / 10.0, c, a, l));
        if (formula instanceof Concentration) {
            singleResult.setResUnit(concentrationUnits[Cache.getTestMethod().getConcentrationType()]);
        } else {
            singleResult.setResUnit(formulaUnit);
        }

        return singleResult;
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (testTestListFragment != null) {
            fragmentTransaction.hide(testTestListFragment);
        }
        if (testTestTupuFragment != null) {
            fragmentTransaction.hide(testTestTupuFragment);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            reloadWaveLength();
        } else {
            cleaned = false;
        }
    }

    private void reloadWaveLength() {
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
                                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                                trunkUtil.removeListener(R.id.setting_correcting_wavelength);
                                Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.wavelength_choose)).sendToTarget();
                            }
                        }, R.id.setting_correcting_wavelength);
                        trunkUtil.chooseWaveLength(Cache.getWaveLengthPosition(Cache.getTestMethod().getWaveLength()) + 1);
                    }
                }
        );
    }

    private void changeToTupuFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        fragmentTransaction.show(testTestTupuFragment);
        fragmentTransaction.commit();
    }

    private void changeToListFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        fragmentTransaction.show(testTestListFragment);
        fragmentTransaction.commit();
    }

    private void refreshTupu() {
        startRefreshTupu = true;
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        long intervalOffset = 0;
                        long lastTime = System.currentTimeMillis();
                        while (startRefreshTupu) {
                            try {
                                if (baseInterval < intervalOffset) {
                                    Thread.sleep(baseInterval);
                                } else {
                                    Thread.sleep(baseInterval - intervalOffset);
                                }
                                int newBaseInterval = 1000 << Cache.getTestMethod().getAtlasX();
                                if (newBaseInterval != baseInterval) {
                                    simpleResService.clean();
                                    baseInterval = newBaseInterval;
                                }
                                float addValue = (float) lastValue;
                                simpleResService.addValue(addValue);
                                long currentTime = System.currentTimeMillis();
                                intervalOffset = currentTime - lastTime - baseInterval;
                                lastTime = currentTime;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    public void addTestTestFrame() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (testTestListFragment == null) {
            testTestListFragment = new TestTestListFragment();
            fragmentTransaction.add(R.id.test_test_frame_frame, testTestListFragment);
        }
        if (testTestTupuFragment == null) {
            testTestTupuFragment = new TestTestTupuFragment();
            fragmentTransaction.add(R.id.test_test_frame_frame, testTestTupuFragment);
        }
        fragmentTransaction.commit();
    }

    public void showDefault() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        fragmentTransaction.show(testTestListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startRefreshTupu = false;
        cycleTesting = false;
    }

    @Override
    public void onDestroyView() {
        destoried = true;
        super.onDestroyView();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(int sig) {
        readWriteLock.readLock().lock();
        try {
            for (Observer observer : observers) {
                observer.notifyDataChanged(sig);
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void notifyDataChanged(int sig) {
        if (sig == REFRESH_BASE) {
            Message.obtain(handler, REFRESH_BASE).sendToTarget();
        }
        for (final Observer observer : observers) {
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            observer.notifyDataChanged(REFRESH_BASE);
                        }
                    }
            );
        }
    }

    private void refresh() {
        TestMethod testMethod = Cache.getTestMethod();
        Formula formula = Cache.getCurrentFormula();
        formulaName = formula.getFormulaName();
        formulaSimpleName = formula.getSimpleName();
        if (formula instanceof Concentration) {
            formulaUnit = concentrationUnits[Cache.getTestMethod().getConcentrationType()];
        } else {
            formulaUnit = formula.getUnit();
        }
        currentTemperatureType = temperatureTypes[testMethod.getTemperatureType()];

        test_test_name.setText(testMethod.getTestName());
        if (StringUtils.isNotEmpty(formulaSimpleName)) {
            test_test_type.setText(formulaName + "(" + formulaSimpleName + ")");
        } else {
            test_test_type.setText(formulaName);
        }
        stateChanged = true;
        cycleTest = testMethod.isAutoTest();
        if (decimal != testMethod.getDecimals()) {
            decimal = testMethod.getDecimals();
            Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
        }
        autoTest = testMethod.isAutoTest();
    }

    private class TestTestHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    if (msg.obj != null && msg.obj instanceof SingleResult) {
                        SingleResult singleResult = (SingleResult) msg.obj;
                        test_test_value.setText(String.format("%." + Cache.getTestMethod().getDecimals() + "f", singleResult.getRes()));
                        test_test_unit.setText(singleResult.getResUnit());
                        notifyObservers(ADD);
                    } else {
                        test_test_value.setText(String.format("%." + Cache.getTestMethod().getDecimals() + "f", lastValue));
                        test_test_unit.setText(formulaUnit);
                    }
                    break;
                case BaseActivity.NEED_CLEAR:
                    test_test_value.setText(String.format("%." + Cache.getTestMethod().getDecimals() + "f", 0.0));
                    test_test_unit.setText(formulaUnit);
                    notifyObservers(CLEAR);
                    cleaned = true;
                    break;
                case REFRESH_BASE:
                    refresh();
                    break;
                case CHANGE_STATE:
                    test_test_ceshi.setBackground(getResources().getDrawable(R.drawable.ceshi_btn));
                    test_test_ceshi_tv.setText(getString(R.string.test));
                    break;
                case BaseActivity.SHOW_MSG:
                    Object obj = msg.obj;
                    if (obj != null && !destoried) {
                        ToastUtil.toastShow(mToast, String.valueOf(obj));
                    }
                    break;
            }
        }
    }

}
