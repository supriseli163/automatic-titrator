package com.jh.automatic_titrator.common;

import android.util.Log;

import com.jh.automatic_titrator.entity.Concentration;
import com.jh.automatic_titrator.entity.OpticalRotation;
import com.jh.automatic_titrator.entity.SpecificRotation;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.entity.common.SingleResult;
import com.jh.automatic_titrator.entity.common.TestMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 2016/10/18.
 */
public class Cache {

    private static volatile User user;

    private static volatile String testName;

    private static volatile String testId;

    private static volatile TestMethod testMethod;

    private static volatile TitratorMethod titratorMethod;

    private static volatile List<SingleResult> singleResults;

    private static volatile Set<String> managerAuth;

    private static volatile long dateOffset = 0;

    private static volatile boolean autoClean = false;

    private static volatile float autoClean3 = 0;

    private static volatile float autoClean4 = 0;

    private static volatile boolean testing = false;

//    private static volatile boolean correctTesting = false;

    private static volatile int currentWaveLength = 1;

    private static volatile Formula currentFormula;

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static volatile List<Formula> formulas = new ArrayList<>();

    private static volatile List<Formula> baseFormula;

    private static volatile int formulaPositon = 0;

    private static volatile long lastModify = 0;

    //权限条目
    private static void initManagerAuth() {
        managerAuth = new HashSet<>();
        managerAuth.add("paramsetting");
        managerAuth.add("datadelete");
        managerAuth.add("datasearch");
        managerAuth.add("dataexport");
        managerAuth.add("dataprint");
        managerAuth.add("dataupload");
        managerAuth.add("recovery");
        managerAuth.add("language");
        managerAuth.add("timeupdate");
        managerAuth.add("correct");
        managerAuth.add("method");
        managerAuth.add("usermanager");
        managerAuth.add("audit");
        managerAuth.add("wavelength");
        managerAuth.add("formula");//formula
        managerAuth.add("cloud");
        managerAuth.add("network");
    }

    static {
        baseFormula = new ArrayList<>();
        baseFormula.add(new OpticalRotation());
        baseFormula.add(new SpecificRotation());
        baseFormula.add(new Concentration());
    }

    private static Map<String, Integer> waveLengthMap;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Cache.user = user;
    }

    public static String getTestName() {
        return testName;
    }

    public static void setTestName(String testName) {
        Cache.testName = testName;
    }

    public static String getTestId() {
        return testId;
    }

    public static void setTestId(String testId) {
        Cache.testId = testId;
    }

    public static TestMethod getTestMethod() {
        return testMethod;
    }

    public static TitratorMethod getTitratorMethod() {
        return titratorMethod;
    }

    public static void setTitratorMethod(TitratorMethod titratorMethod) {
        Cache.titratorMethod = titratorMethod;
    }

    public static void setTestMethod(TestMethod testMethod) {
        Cache.testMethod = testMethod;
    }

    public static List<SingleResult> getSingleResults() {
        return singleResults;
    }

    public static void setSingleResults(List<SingleResult> singleResults) {
        Cache.singleResults = singleResults;
    }

    public static boolean containsAuth(String auth) {
        if (user.getAuth() == null) {
            return false;
        }
        if (user.getAuthList() == null) {
            String[] auths = user.getAuth().split("\\|");
            Set<String> authset = new HashSet<>();
            for (String temp : auths) {
                authset.add(temp);
            }
            user.setAuthList(authset);
        }
        if (user.getAuthList().contains("all")) {
            return true;
        }
        if (user.getAuthList().contains("manager")) {
            if (managerAuth == null) {
                initManagerAuth();
            }
            return managerAuth.contains(auth);
        } else {
            return user.getAuthList().contains(auth);
        }
    }

    public static long currentTime() {
        return dateOffset + System.currentTimeMillis();
    }

    public static void setDateOffset(long dateOffset) {
        Cache.dateOffset = dateOffset;
    }

    public static boolean isTesting() {
        return Cache.testing;
    }

    public static void setTesting(boolean testing) {
        Cache.testing = testing;
    }

//    public static boolean isCorrectTesting() {
//        return correctTesting;
//    }

//    public static void setCorrectTesting(boolean correctTesting) {
//        Cache.correctTesting = correctTesting;
//    }

    public void setWaveLengths(List<String> waveLengths) {
        readWriteLock.writeLock().lock();
        try {
            if (waveLengthMap == null) {
                waveLengthMap = new LinkedHashMap<>();
            }
            for (int i = 0; i < waveLengths.size(); i++) {
                waveLengthMap.put(waveLengths.get(i), i + 1);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public static void setWaveLengths(Map<String, Integer> waveLengthMap) {
        readWriteLock.writeLock().lock();
        try {
            if (Cache.waveLengthMap != null) {
                Cache.waveLengthMap.clear();
                Cache.waveLengthMap.putAll(waveLengthMap);
            } else {
                Cache.waveLengthMap = waveLengthMap;
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public static int getWaveLengthPosition(String wavelength) {
        readWriteLock.readLock().lock();
        try {
            Log.d("Cache", "getWaveLengthPosition: " + waveLengthMap + ":" + wavelength);
            if (waveLengthMap.containsKey(wavelength)) {
                return waveLengthMap.get(wavelength);
            }
            return 0;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public List<String> getWaveLengths() {
        List<String> waveLengths = new ArrayList<>();
        if (waveLengthMap != null) {
            waveLengths.addAll(waveLengthMap.keySet());
        }
        return waveLengths;
    }

    public static boolean isAutoClean() {
        return autoClean;
    }

    public static void setAutoClean(boolean autoClean) {
        Cache.autoClean = autoClean;
    }

    public static float getAutoClean3() {
        return autoClean3;
    }

    public static void setAutoClean3(float autoClean3) {
        Cache.autoClean3 = autoClean3;
    }

    public static float getAutoClean4() {
        return autoClean4;
    }

    public static void setAutoClean4(float autoClean4) {
        Cache.autoClean4 = autoClean4;
    }

    public static int getCurrentWaveLength() {
        return currentWaveLength;
    }

    public static void setCurrentWaveLength(int currentWaveLength) {
        Cache.currentWaveLength = currentWaveLength;
    }

    public static Formula getCurrentFormula() {
        return currentFormula;
    }

    public static void setCurrentFormula(Formula currentFormula) {
        Cache.currentFormula = currentFormula;
        Log.d("setCurrentFormula", "setCurrentFormula: ");
        boolean changed = false;
        for (int position = 0; position < formulas.size(); position++) {
            if (currentFormula.getFormulaName().equals(formulas.get(position).getFormulaName())) {
                formulaPositon = position;
                changed = true;
                break;
            }
        }
        if (!changed) {
            formulaPositon = 0;
        }
    }

    public static List<Formula> getBaseFormula() {
        return baseFormula;
    }

    public static int getCurrentFormulaPosition() {
        return formulaPositon;
    }


    public static void refreshFormulas(List<Formula> formulas) {
        Cache.formulas = formulas;
    }

    public static List<Formula> getFormulas() {
        return Cache.formulas;
    }

    public static Formula getFormulaByName(String formulaName) {
        boolean changed = false;
        for (int position = 0; position < formulas.size(); position++) {
            if (formulaName.equals(formulas.get(position).getFormulaName())) {
                return formulas.get(position);
            }
        }
        return formulas.get(0);
    }

    public static List<String> getFormulaDescs() {
        List<String> formulaDescs = new ArrayList<>();
        if (formulas != null) {
            for (Formula formula : formulas) {
                if (formula.getSimpleName() != null && formula.getSimpleName().length() > 0) {
                    formulaDescs.add(formula.getFormulaName() + "(" + formula.getSimpleName() + ")");
                } else {
                    formulaDescs.add(formula.getFormulaName());
                }
            }
        }
        return formulaDescs;
    }

    public static void cleanAll() {
        try {
            Cache.currentWaveLength = 0;
            Cache.singleResults.clear();
            Cache.testing = false;
            try {
                Cache.readWriteLock.writeLock().unlock();
            } catch (Exception e) {
                //do nothing
            }
        } catch (Exception e) {
            //do nothing
        }
    }

    public static long getLastModify() {
        return lastModify;
    }

    public static void setLastModify(long lastModify) {
        Cache.lastModify = lastModify;
    }
}
