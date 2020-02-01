package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016/10/16.
 */
public class TestHelper {

    private static final int MAX_LINE = 10000;

    private SQLiteDatabase db;

    public TestHelper(DBHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public String saveTest(Test test) {
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("insert into test (testName, testId, testMethod, " +
                "date, temperature, temperature1, optical, res, formula, formulaSN, formulaUnit, " +
                "decimal, testCreator, wavelength, tubelength, specificrotation, concentration, test_count) values(" +
                "'" + test.getTestName() + "'," +
                "'" + test.getTestId() + "'," +
                "'" + test.getTestMethod() + "'," +
                "'" + test.getDate() + "',");
        insertSql.append("'" + test.getWantedTemperature() + "',");
        insertSql.append("'" + test.getRealTemperature() + "',");
        insertSql.append("" + test.getOptical() + "," +
                "" + test.getRes() + "," +
                "'" + test.getFormulaName() + "'," +
                "'" + test.getSimpleFormulaName() + "'," +
                "'" + test.getFormulaUnit() + "'," +
                "" + test.getDecimal() + "," +
                "'" + test.getTestCreator() + "'," +
                "'" + test.getWavelength() + "',");
        if (test.getTubelength() != null) {
            insertSql.append("'" + test.getTubelength() + "',");
        } else {
            insertSql.append("" + null + ",");
        }
        if (test.getSpecificrotation() != null) {
            insertSql.append("'" + test.getSpecificrotation() + "',");
        } else {
            insertSql.append("" + null + ",");
        }
        if (test.getConcentration() != null) {
            insertSql.append("'" + test.getConcentration() + "',");
        } else {
            insertSql.append("" + null + ",");
        }
        if (test.getTestCount() != null) {
            insertSql.append("'" + test.getTestCount() + "')");
        } else {
            insertSql.append("" + null + ")");
        }
        if (db.isOpen()) {
            db.execSQL(insertSql.toString());
        }
        int count = count();
        if (count > MAX_LINE) {
            db.execSQL("delete from test where id=(select min(id) from test)");
        }
        return insertSql.toString();
    }

    public int count() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from test", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }
        return 0;
    }

    public int count(String startDate, String endDate, String sampleName, Double gt, Double lt, String creator) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from test ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)
                || StringUtils.isNotEmpty(sampleName)
                || gt != null
                || lt != null) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datetime(`date`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("datetime(`date`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(sampleName)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("testName").append(" = '").append(sampleName).append("' ");
                needAnd = true;
            }
            if (gt != null) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("res").append(" >= ").append(gt.doubleValue()).append(" ");
                needAnd = true;
            }
            if (lt != null) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("res").append(" <= ").append(lt.doubleValue()).append(" ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("testCreator = '").append(creator).append("' ");
            }
        }
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                int count = cursor.getInt(0);
                return count;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public List<Test> listTest(int page, int pageSize) {
        List<Test> tests = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from test order by id desc LIMIT " + pageSize + " OFFSET " + (page - 1) * pageSize, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Test test = new Test();
                test.setId(cursor.getInt(0));
                test.setTestName(cursor.getString(1));
                test.setTestId(cursor.getString(2));
                test.setTestMethod(cursor.getString(3));
                test.setDate(cursor.getString(4));
                test.setWantedTemperature(cursor.getString(5));
                test.setRealTemperature(cursor.getString(6));
                test.setOptical(cursor.getDouble(7));
                test.setRes(cursor.getDouble(8));
                test.setFormulaName(cursor.getString(9));
                test.setSimpleFormulaName(cursor.getString(10));
                test.setFormulaUnit(cursor.getString(11));
                test.setDecimal(cursor.getInt(12));
                test.setTestCreator(cursor.getString(13));
                test.setWavelength(cursor.getString(14));
                test.setTubelength(cursor.getString(15));
                test.setSpecificrotation(cursor.getString(16));
                test.setConcentration(cursor.getString(17));
                tests.add(test);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tests;
    }

    public List<Test> listTest(String startDate, String endDate, String sampleName, Double gt, Double lt, String creator, int page, int pageSize) {
        List<Test> tests = new ArrayList<>();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from test ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)
                || StringUtils.isNotEmpty(sampleName)
                || gt != null
                || lt != null) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datetime(`date`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("datetime(`date`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(sampleName)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("testName").append(" = '").append(sampleName).append("' ");
                needAnd = true;
            }
            if (gt != null) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("res").append(" >= ").append(gt.doubleValue()).append(" ");
                needAnd = true;
            }
            if (lt != null) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("res").append(" <= ").append(lt.doubleValue()).append(" ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("testCreator = '").append(creator).append("' ");
            }
        }
        sqlSb.append(" order by id desc LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Test test = new Test();
                test.setId(cursor.getInt(0));
                test.setTestName(cursor.getString(1));
                test.setTestId(cursor.getString(2));
                test.setTestMethod(cursor.getString(3));
                test.setDate(cursor.getString(4));
                test.setWantedTemperature(cursor.getString(5));
                test.setRealTemperature(cursor.getString(6));
                test.setOptical(cursor.getDouble(7));
                test.setRes(cursor.getDouble(8));
                test.setFormulaName(cursor.getString(9));
                test.setSimpleFormulaName(cursor.getString(10));
                test.setFormulaUnit(cursor.getString(11));
                test.setDecimal(cursor.getInt(12));
                test.setTestCreator(cursor.getString(13));
                test.setWavelength(cursor.getString(14));
                test.setTubelength(cursor.getString(15));
                test.setSpecificrotation(cursor.getString(16));
                test.setConcentration(cursor.getString(17));
                tests.add(test);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tests;
    }

    public List<Test> listTest(String startDate, String endDate, String sampleName, Double gt, Double lt, String creator) {
        List<Test> tests = new ArrayList<>();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from test ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)
                || StringUtils.isNotEmpty(sampleName)
                || gt != null
                || lt != null) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datetime(`date`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("datetime(`date`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(sampleName)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("testName").append(" = '").append(sampleName).append("' ");
                needAnd = true;
            }
            if (gt != null) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("res").append(" >= ").append(gt.doubleValue()).append(" ");
                needAnd = true;
            }
            if (lt != null) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("res").append(" <= ").append(lt.doubleValue()).append(" ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("testCreator = '").append(creator).append("' ");
            }
        }
        sqlSb.append(" order by id desc");
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Test test = new Test();
                test.setId(cursor.getInt(0));
                test.setTestName(cursor.getString(1));
                test.setTestId(cursor.getString(2));
                test.setTestMethod(cursor.getString(3));
                test.setDate(cursor.getString(4));
                test.setWantedTemperature(cursor.getString(5));
                test.setRealTemperature(cursor.getString(6));
                test.setOptical(cursor.getDouble(7));
                test.setRes(cursor.getDouble(8));
                test.setFormulaName(cursor.getString(9));
                test.setSimpleFormulaName(cursor.getString(10));
                test.setFormulaUnit(cursor.getString(11));
                test.setDecimal(cursor.getInt(12));
                test.setTestCreator(cursor.getString(13));
                test.setWavelength(cursor.getString(14));
                test.setTubelength(cursor.getString(15));
                test.setSpecificrotation(cursor.getString(16));
                test.setConcentration(cursor.getString(17));
                tests.add(test);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tests;
    }

    public void deleteTest(List<Integer> ids) {
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from test where id in (");
        for (int id : ids) {
            deleteSql.append(id).append(",");
        }
        deleteSql.deleteCharAt(deleteSql.length() - 1);
        deleteSql.append(")");
        db.execSQL(deleteSql.toString());
    }

    public void deleteAll(String startDate, String endDate, String creator) {
        StringBuilder deleteSql = new StringBuilder();
        boolean needAnd = false;
        deleteSql.append("delete from test");
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(creator)) {
            deleteSql.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                deleteSql.append("datetime(`date`) >= datetime('").append(startDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(endDate)) {
                if (needAnd) {
                    deleteSql.append("and ");
                }
                deleteSql.append("datetime(`date`) <= datetime('").append(endDate).append("') ");
                needAnd = true;
            }
            if (StringUtils.isNotEmpty(creator)) {
                if (needAnd) {
                    deleteSql.append("and ");
                }
                deleteSql.append("testCreator = '").append(creator).append("' ");
            }
        }
        db.execSQL(deleteSql.toString());
    }
}
