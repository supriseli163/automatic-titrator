package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.TestMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by apple on 2016/10/16.
 */
public class TestMethodHelper {

    private SQLiteDatabase db;

    public TestMethodHelper(DBHelper dbHelper) {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public List<TestMethod> listAll() {
        Cursor cursor = db.rawQuery("select * from test_method", null);
        cursor.moveToFirst();
        List<TestMethod> testMethods = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            TestMethod testMethod = new TestMethod();
            testMethod.setId(cursor.getInt(0));
            testMethod.setTestName(cursor.getString(1));
            testMethod.setTestCount(cursor.getInt(2));
            String concentration = cursor.getString(3);
            if (concentration != null) {
                testMethod.setConcentration(Double.parseDouble(concentration));
            }
            testMethod.setConcentrationType(cursor.getInt(4));
            testMethod.setAccuracy(cursor.getInt(5));
            testMethod.setFormulaName(cursor.getString(6));
            testMethod.setWaveLength(cursor.getString(7));
            testMethod.setDecimals(cursor.getInt(8));
            String tubeLength = cursor.getString(9);
            if (tubeLength != null) {
                testMethod.setTestTubeLength(Double.parseDouble(tubeLength));
            }
            String specificRotation = cursor.getString(10);
            if (specificRotation != null) {
                testMethod.setSpecificRotation(Double.parseDouble(specificRotation));
            }
            testMethod.setAtlasX(cursor.getInt(11));
            testMethod.setAtlasY(cursor.getInt(12));
            testMethod.setUseTemperature(cursor.getInt(13) != 0);
            testMethod.setTemperatureType(cursor.getInt(14));
            testMethod.setTemperature(cursor.getDouble(15));
            testMethod.setAutoTest(cursor.getInt(16) != 0);
            testMethod.setAutoTestInterval(cursor.getInt(17));
            testMethod.setAutoTestTimes(cursor.getInt(18));
            testMethod.setCreateDate(cursor.getString(19));
            testMethod.setCreator(cursor.getString(20));
            testMethods.add(testMethod);
            cursor.moveToNext();
        }
        return testMethods;
    }

    public int count() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from test_method", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public int count(String startDate, String endDate, String creator) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from test_method ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datetime(`createDate`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("datetime(`createDate`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("creator = '").append(creator).append("' ");
            }
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public List<TestMethod> list(int page, int pageSize) {
        List<TestMethod> testMethods = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from test_method order by id desc LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TestMethod testMethod = new TestMethod();
                testMethod.setId(cursor.getInt(0));
                testMethod.setTestName(cursor.getString(1));
                testMethod.setTestCount(cursor.getInt(2));
                String concentration = cursor.getString(3);
                if (concentration != null) {
                    testMethod.setConcentration(Double.parseDouble(concentration));
                }
                testMethod.setConcentrationType(cursor.getInt(4));
                testMethod.setAccuracy(cursor.getInt(5));
                testMethod.setFormulaName(cursor.getString(6));
                testMethod.setWaveLength(cursor.getString(7));
                testMethod.setDecimals(cursor.getInt(8));
                String tubeLength = cursor.getString(9);
                if (tubeLength != null) {
                    testMethod.setTestTubeLength(Double.parseDouble(tubeLength));
                }
                String specificRotation = cursor.getString(10);
                if (specificRotation != null) {
                    testMethod.setSpecificRotation(Double.parseDouble(specificRotation));
                }
                testMethod.setAtlasX(cursor.getInt(11));
                testMethod.setAtlasY(cursor.getInt(12));
                testMethod.setUseTemperature(cursor.getInt(13) != 0);
                testMethod.setTemperatureType(cursor.getInt(14));
                testMethod.setTemperature(cursor.getDouble(15));
                testMethod.setAutoTest(cursor.getInt(16) != 0);
                testMethod.setAutoTestInterval(cursor.getInt(17));
                testMethod.setAutoTestTimes(cursor.getInt(18));
                testMethod.setCreateDate(cursor.getString(19));
                testMethod.setCreator(cursor.getString(20));
                testMethods.add(testMethod);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return testMethods;
    }

    public List<TestMethod> list(String startDate, String endDate, String creator, int page, int pageSize) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from test_method ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datetime(`createDate`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("datetime(`createDate`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("creator = '").append(creator).append("' ");
            }
        }
        sqlSb.append("order by id desc LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);

        List<TestMethod> testMethods = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TestMethod testMethod = new TestMethod();
                testMethod.setId(cursor.getInt(0));
                testMethod.setTestName(cursor.getString(1));
                testMethod.setTestCount(cursor.getInt(2));
                String concentration = cursor.getString(3);
                if (concentration != null) {
                    testMethod.setConcentration(Double.parseDouble(concentration));
                }
                testMethod.setConcentrationType(cursor.getInt(4));
                testMethod.setAccuracy(cursor.getInt(5));
                testMethod.setFormulaName(cursor.getString(6));
                testMethod.setWaveLength(cursor.getString(7));
                testMethod.setDecimals(cursor.getInt(8));
                String tubeLength = cursor.getString(9);
                if (tubeLength != null) {
                    testMethod.setTestTubeLength(Double.parseDouble(tubeLength));
                }
                String specificRotation = cursor.getString(10);
                if (specificRotation != null) {
                    testMethod.setSpecificRotation(Double.parseDouble(specificRotation));
                }
                testMethod.setAtlasX(cursor.getInt(11));
                testMethod.setAtlasY(cursor.getInt(12));
                testMethod.setUseTemperature(cursor.getInt(13) != 0);
                testMethod.setTemperatureType(cursor.getInt(14));
                testMethod.setTemperature(cursor.getDouble(15));
                testMethod.setAutoTest(cursor.getInt(16) != 0);
                testMethod.setAutoTestInterval(cursor.getInt(17));
                testMethod.setAutoTestTimes(cursor.getInt(18));
                testMethod.setCreateDate(cursor.getString(19));
                testMethod.setCreator(cursor.getString(20));
                testMethods.add(testMethod);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return testMethods;
    }

    public void addTestMethod(TestMethod testMethod) {
        String insertSql = "insert into test_method (testName," +
                "testCount,concentration,concentrationType,accuracy," +
                "formula,waveLength,decimals,testTubeLength,specificRotation," +
                "atlasX,atlasY,useTemperature,temperatureType,temperature,autoTest," +
                "autoTestInterval,autoTestTimes, createDate, creator) values (" +
                "'" + testMethod.getTestName() + "'," +
                "" + testMethod.getTestCount() + "," +
                "" + testMethod.getConcentration() + "," +
                "" + testMethod.getConcentrationType() + "," +
                "" + testMethod.getAccuracy() + "," +
                "'" + testMethod.getFormulaName() + "'," +
                "'" + testMethod.getWaveLength() + "'," +
                "" + testMethod.getDecimals() + "," +
                "" + testMethod.getTestTubeLength() + "," +
                "" + testMethod.getSpecificRotation() + "," +
                "" + testMethod.getAtlasX() + "," +
                "" + testMethod.getAtlasY() + "," +
                "" + (testMethod.isUseTemperature() ? 1 : 0) + "," +
                "" + testMethod.getTemperatureType() + "," +
                "" + testMethod.getTemperature() + "," +
                "" + (testMethod.isAutoTest() ? 1 : 0) + "," +
                "" + testMethod.getAutoTestInterval() + "," +
                "" + testMethod.getAutoTestTimes() + "," +
                "'" + testMethod.getCreateDate() + "'," +
                "'" + testMethod.getCreator() + "'" +
                ");";
        db.execSQL(insertSql);
    }

    public void updateTestMethod(TestMethod testMethod) {
         StringBuilder updateSB = new StringBuilder();
        updateSB.append("update test_method set ");
        updateSB.append("testCount = ").append(testMethod.getTestCount()).append(",");
        updateSB.append("concentration = ").append(testMethod.getConcentration()).append(",");
        updateSB.append("concentrationType = ").append(testMethod.getConcentrationType()).append(",");
        updateSB.append("accuracy = ").append(testMethod.getAccuracy()).append(",");
        updateSB.append("formula = '").append(testMethod.getFormulaName()).append("',");
        updateSB.append("waveLength = ").append(testMethod.getWaveLength()).append(",");
        updateSB.append("decimals = ").append(testMethod.getDecimals()).append(",");
        updateSB.append("testTubeLength = ").append(testMethod.getTestTubeLength()).append(",");
        updateSB.append("specificRotation = ").append(testMethod.getSpecificRotation()).append(",");
        updateSB.append("atlasX = ").append(testMethod.getAtlasX()).append(",");
        updateSB.append("atlasY = ").append(testMethod.getAtlasY()).append(",");
        updateSB.append("useTemperature = ").append((testMethod.isUseTemperature() ? 1 : 0)).append(",");
        updateSB.append("temperatureType = ").append(testMethod.getTemperatureType()).append(",");
        updateSB.append("temperature = ").append(testMethod.getTemperature()).append(",");
        updateSB.append("autoTest = ").append((testMethod.isAutoTest() ? 1 : 0)).append(",");
        updateSB.append("autoTestInterval = ").append(testMethod.getAutoTestInterval()).append(",");
        updateSB.append("autoTestTimes = ").append(testMethod.getAutoTestTimes()).append(",");
        updateSB.append("createDate = '").append(testMethod.getCreateDate()).append("' ");
        updateSB.append("where testName = '").append(testMethod.getTestName()).append("'");
        db.execSQL(updateSB.toString());
    }

    public void deleteTestMethods(Collection<Integer> ids) {
        StringBuilder deleteSqlSB = new StringBuilder();
        deleteSqlSB.append("delete from test_method where id in (");
        for (int id : ids) {
            deleteSqlSB.append(id).append(",");
        }
        deleteSqlSB.deleteCharAt(deleteSqlSB.length() - 1);
        deleteSqlSB.append(");");
        db.execSQL(deleteSqlSB.toString());
    }

    public void deleteAll() {
        StringBuilder deleteSqlSB = new StringBuilder();
        deleteSqlSB.append("delete from test_method");
        db.execSQL(deleteSqlSB.toString());
    }

    public void deleteAll(String startDate, String endDate, String creator) {
        StringBuilder deleteSql = new StringBuilder();
        boolean needAnd = false;
        deleteSql.append("delete from test_method");
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)) {
            deleteSql.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                deleteSql.append("datetime(`createDate`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    deleteSql.append("and ");
                }
                deleteSql.append("datetime(`createDate`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    deleteSql.append("and ");
                }
                deleteSql.append("creator = '").append(creator).append("' ");
            }
        }
        db.execSQL(deleteSql.toString());
    }

    public TestMethod findTestMethod(int id) {
        Cursor cursor = db.rawQuery("select * from test_method where id = " + id, null);
        cursor.moveToFirst();
        TestMethod testMethod = new TestMethod();
        if (!cursor.isAfterLast()) {
            testMethod.setId(cursor.getInt(0));
            testMethod.setTestName(cursor.getString(1));
            testMethod.setTestCount(cursor.getInt(2));
            String concentration = cursor.getString(3);
            if (concentration != null) {
                testMethod.setConcentration(Double.parseDouble(concentration));
            }
            testMethod.setConcentrationType(cursor.getInt(4));
            testMethod.setAccuracy(cursor.getInt(5));
            testMethod.setFormulaName(cursor.getString(6));
            testMethod.setWaveLength(cursor.getString(7));
            testMethod.setDecimals(cursor.getInt(8));
            String tubeLength = cursor.getString(9);
            if (tubeLength != null) {
                testMethod.setTestTubeLength(Double.parseDouble(tubeLength));
            }
            String specificRotation = cursor.getString(10);
            if (specificRotation != null) {
                testMethod.setSpecificRotation(Double.parseDouble(specificRotation));
            }
            testMethod.setAtlasX(cursor.getInt(11));
            testMethod.setAtlasY(cursor.getInt(12));
            testMethod.setUseTemperature(cursor.getInt(13) != 0);
            testMethod.setTemperatureType(cursor.getInt(14));
            testMethod.setTemperature(cursor.getDouble(15));
            testMethod.setAutoTest(cursor.getInt(16) != 0);
            testMethod.setAutoTestInterval(cursor.getInt(17));
            testMethod.setAutoTestTimes(cursor.getInt(18));
            testMethod.setCreateDate(cursor.getString(19));
            testMethod.setCreator(cursor.getString(20));
        } else {
            return null;
        }
        cursor.close();
        return testMethod;
    }

    public TestMethod findTestMethod(String testName) {
        Cursor cursor = db.rawQuery("select * from test_method where testName = '" + testName + "'", null);
        cursor.moveToFirst();
        TestMethod testMethod = new TestMethod();
        if (!cursor.isAfterLast()) {
            testMethod.setId(cursor.getInt(0));
            testMethod.setTestName(cursor.getString(1));
            testMethod.setTestCount(cursor.getInt(2));
            String concentration = cursor.getString(3);
            if (concentration != null) {
                testMethod.setConcentration(Double.parseDouble(concentration));
            }
            testMethod.setConcentrationType(cursor.getInt(4));
            testMethod.setAccuracy(cursor.getInt(5));
            testMethod.setFormulaName(cursor.getString(6));
            testMethod.setWaveLength(cursor.getString(7));
            testMethod.setDecimals(cursor.getInt(8));
            String tubeLength = cursor.getString(9);
            if (tubeLength != null) {
                testMethod.setTestTubeLength(Double.parseDouble(tubeLength));
            }
            String specificRotation = cursor.getString(10);
            if (specificRotation != null) {
                testMethod.setSpecificRotation(Double.parseDouble(specificRotation));
            }
            testMethod.setAtlasX(cursor.getInt(11));
            testMethod.setAtlasY(cursor.getInt(12));
            testMethod.setUseTemperature(cursor.getInt(13) != 0);
            testMethod.setTemperatureType(cursor.getInt(14));
            testMethod.setTemperature(cursor.getDouble(15));
            testMethod.setAutoTest(cursor.getInt(16) != 0);
            testMethod.setAutoTestInterval(cursor.getInt(17));
            testMethod.setAutoTestTimes(cursor.getInt(18));
            testMethod.setCreateDate(cursor.getString(19));
            testMethod.setCreator(cursor.getString(20));
        } else {
            return null;
        }
        cursor.close();
        return testMethod;
    }

    public Collection<? extends TestMethod> list(String testName, int page, int pageSize) {
        List<TestMethod> testMethods = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from test_method where testName LIKE '" + testName + "%' order by id desc LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TestMethod testMethod = new TestMethod();
                testMethod.setId(cursor.getInt(0));
                testMethod.setTestName(cursor.getString(1));
                testMethod.setTestCount(cursor.getInt(2));
                String concentration = cursor.getString(3);
                if (concentration != null) {
                    testMethod.setConcentration(Double.parseDouble(concentration));
                }
                testMethod.setConcentrationType(cursor.getInt(4));
                testMethod.setAccuracy(cursor.getInt(5));
                testMethod.setFormulaName(cursor.getString(6));
                testMethod.setWaveLength(cursor.getString(7));
                testMethod.setDecimals(cursor.getInt(8));
                String tubeLength = cursor.getString(9);
                if (tubeLength != null) {
                    testMethod.setTestTubeLength(Double.parseDouble(tubeLength));
                }
                String specificRotation = cursor.getString(10);
                if (specificRotation != null) {
                    testMethod.setSpecificRotation(Double.parseDouble(specificRotation));
                }
                testMethod.setAtlasX(cursor.getInt(11));
                testMethod.setAtlasY(cursor.getInt(12));
                testMethod.setUseTemperature(cursor.getInt(13) != 0);
                testMethod.setTemperatureType(cursor.getInt(14));
                testMethod.setTemperature(cursor.getDouble(15));
                testMethod.setAutoTest(cursor.getInt(16) != 0);
                testMethod.setAutoTestInterval(cursor.getInt(17));
                testMethod.setAutoTestTimes(cursor.getInt(18));
                testMethod.setCreateDate(cursor.getString(19));
                testMethod.setCreator(cursor.getString(20));
                testMethods.add(testMethod);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return testMethods;
    }

    public List<String> allMethodNames() {
        List<String> testMethods = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select testName from test_method", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                testMethods.add(cursor.getString(0));
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return testMethods;
    }
}
