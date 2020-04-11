package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;

import java.util.ArrayList;
import java.util.List;

public class MethodHelper {
    private SQLiteDatabase db;

    public MethodHelper(DBHelper dbHelper) {
        db = dbHelper.getWritableDatabase();
    }

    private static final int MAX_Line = 10000;

    public void close() {
        db.close();
    }

    public String saveMethod(TitratorMethod titratorMethod) {
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("insert insert into method(methodName)");

        if (db.isOpen()) {
            db.execSQL(insertSql.toString());
        }

        int count = count();
        if (count > MAX_Line) {
            db.execSQL("delete from method where id=(select min(id) from method)");
        }
        return insertSql.toString();
    }

    public int count() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from method", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }
        return 0;
    }

    public List<TitratorMethod> listTest(int page, int pageSize) {
        List<TitratorMethod> methods = new ArrayList<>();
        Cursor cursor = null;
        try {

        } catch (Exception ex) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return methods;
    }

    public List<TitratorMethod> list(String startDate, String endDate, String creator, int page, int pageSize) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from titrator_method");
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
        sqlSb.append("order by id desc LIMIT").append(" OFFSET ").append((page -1) * pageSize);

        List<TitratorMethod> titratorMethods = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TitratorMethod titratorMethod = parseTitratorMethod(cursor);
                titratorMethods.add(titratorMethod);
            }
        } catch (Exception ex) {

        } finally {
            cursor.close();
        }
        return titratorMethods;
    }


    public List<TitratorMethod> listAll() {
        Cursor cursor = db.rawQuery("select * from titrator_method", null);
        cursor.moveToFirst();
        List<TitratorMethod> testMethods = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            TitratorMethod testMethod = new TitratorMethod();
            testMethod.setId(cursor.getInt(0));
            testMethod.setTitratorType(cursor.getString(1));
            testMethod.setMethodName(cursor.getString(2));
            //滴定管体积
            String buretteVolume = cursor.getString(3);
            testMethod.setBuretteVolume(Double.parseDouble(buretteVolume));
            //工作电级
            String workingElectrode = cursor.getString(4);
            testMethod.setWorkingElectrode(Double.parseDouble(workingElectrode));

            //参比电级(参考电极)
            String referenceElectrode = cursor.getString(5);
            testMethod.setReferenceElectrode(Double.parseDouble(referenceElectrode));
            //样品计量单位
            testMethod.setSampleMeasurementUnit(cursor.getColumnName(5));
            //滴定显示单位
            testMethod.setTitrationDisplayUnit(cursor.getColumnName(6));
            //补液速度
            testMethod.setReplenishmentSpeed(cursor.getColumnName(7));
            //搅拌速度
            testMethod.setStiringSpeed(cursor.getColumnName(8));
            //电极平衡时间
            testMethod.setElectroedEquilibrationTime(cursor.getColumnName(9));
            //电极平衡电位
            testMethod.setElectroedEquilibriumPotential(cursor.getColumnName(10));
            //预搅拌时间
            testMethod.setPreStiringTime(cursor.getColumnName(11));
            //
            testMethods.add(testMethod);
            cursor.moveToNext();
        }
        return testMethods;
    }

    public void deleteMethod(List<Integer> ids) {
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from method where id in(");
        for(int id : ids) {
            deleteSql.append(id).append(",");
        }
        deleteSql.deleteCharAt(deleteSql.length() -1);
        deleteSql.append(")");
        db.execSQL(deleteSql.toString());
    }

    public void deleteAll() {

    }

    public List<TitratorMethod> listTitorMethod(String startDate,String enddate,
                                                String sampleName,
                                                Double gt,
                                                Double lt,
                                                String creator,
                                                int page,
                                                int pageSize) {
        List<TitratorMethod> titratorMethods = new ArrayList<>();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from method");
        boolean neeAnd = false;
        if(StringUtils.isNotEmpty(startDate) ||
        StringUtils.isNotEmpty(enddate) ||
        StringUtils.isNotEmpty(creator) ||
        StringUtils.isNotEmpty(sampleName) ||
        gt != null ||
        lt != null) {
            sqlSb.append("where ");
            if(StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datatime(`date`) >= datetime('").append(startDate);
            }
        }
        return titratorMethods;
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

    public TitratorMethod findTitratorMethod(String methodName) {
        Cursor cursor = db.rawQuery("select * from test_method where methodName = '" + methodName + "'", null);
        cursor.moveToFirst();
        if(cursor.isAfterLast()) {
            return null;
        } else {
            return parseTitratorMethod(cursor);
        }
    }

    public TitratorMethod parseTitratorMethod(Cursor cursor) {
        TitratorMethod titratorMethod = new TitratorMethod();
        return titratorMethod;
    }

    public void updateTitorMethod(TitratorMethod titratorMethod) {
        StringBuffer updateSB = new StringBuffer();
        updateSB.append("update tirator_method set ");
//        updateSB.append("testCount = ").append(true)
        db.execSQL(updateSB.toString());
    }
}
