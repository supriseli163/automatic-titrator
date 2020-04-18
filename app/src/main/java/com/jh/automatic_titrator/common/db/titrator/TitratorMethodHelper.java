package com.jh.automatic_titrator.common.db.titrator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TitratorMethodHelper {
    private SQLiteDatabase db;

    private static final int MAX_Line = 10000;

    public void close() {
        db.close();
    }

    public void insertTitratorMethod(TitratorMethod titratorMethod) {
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("insert insert into titrator_method (titratorType, methodName, buretteVolume,workingElectrode,referenceElectrode,sampleMeasurementUnit," +
                "titrationDisplayUnit,replenishmentSpeed,stiringSpeed,electroedEquilibrationTime,electroedEquilibriumPotential,preStiringTime" +
                ",perAddVolume,endVolume,titrationSpeed,slowTitrationVolume,fastTitrationVolume) values (" +
                StringUtils.dBValueInputFormat(titratorMethod.getTitratorType()) +
                StringUtils.dBValueInputFormat(titratorMethod.getMethodName()) +
                StringUtils.dBValueInputFormat(titratorMethod.getBuretteVolume()) +
                StringUtils.dBValueInputFormat(titratorMethod.getWorkingElectrode()) +
                StringUtils.dBValueInputFormat(titratorMethod.getReferenceElectrode()) +
                StringUtils.dBValueInputFormat(titratorMethod.getSampleMeasurementUnit()) +
                StringUtils.dBValueInputFormat(titratorMethod.getTitrationDisplayUnit()) +
                StringUtils.dBValueInputFormat(titratorMethod.getReplenishmentSpeed()) +
                StringUtils.dBValueInputFormat(titratorMethod.getStiringSpeed()) +
                StringUtils.dBValueInputFormat(titratorMethod.getElectroedEquilibrationTime()) +
                StringUtils.dBValueInputFormat(titratorMethod.getElectroedEquilibriumPotential()) +
                StringUtils.dBValueInputFormat(titratorMethod.getElectroedEquilibriumPotential()) +
                StringUtils.dBValueInputFormat(titratorMethod.getPreStiringTime()) +
                StringUtils.dBValueInputFormat(titratorMethod.getPreStiringTime()) +
                StringUtils.dBValueInputFormat(titratorMethod.getEndVolume()) +
                StringUtils.dBValueInputFormat(titratorMethod.getSlowTitrationVolume()) +
                StringUtils.dBValueInputFormat(titratorMethod.getFastTitrationVolume())
                + ")");
        db.execSQL(insertSql.toString());
    }

    public void updateTitratorMethod(TitratorMethod titratorMethod) {
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update titrator_method set ");
        updateSql.append("titratorType = ").append(titratorMethod.getTitratorType()).append(",");
        updateSql.append("methodName = ").append(titratorMethod.getMethodName()).append(",");
        updateSql.append("buretteVolume = ").append(titratorMethod.getBuretteVolume()).append(",");
        updateSql.append("workingElectrode = ").append(titratorMethod.getWorkingElectrode()).append(",");
        updateSql.append("referenceElectrode = ").append(titratorMethod.getReferenceElectrode()).append(",");
        updateSql.append("sampleMeasurementUnit = ").append(titratorMethod.getSampleMeasurementUnit()).append(",");
        updateSql.append("titrationDisplayUnit = ").append(titratorMethod.getTitrationDisplayUnit()).append(",");
        updateSql.append("replenishmentSpeed = ").append(titratorMethod.getReplenishmentSpeed()).append(",");
        updateSql.append("stiringSpeed = ").append(titratorMethod.getStiringSpeed()).append(",");

        updateSql.append("electroedEquilibrationTime = ").append(titratorMethod.getElectroedEquilibrationTime()).append(",");
        updateSql.append("preStiringTime = ").append(titratorMethod.getPreStiringTime()).append(",");
        updateSql.append("perAddVolume = ").append(titratorMethod.getPerAddVolume()).append(",");
        updateSql.append("endVolume = ").append(titratorMethod.getEndVolume()).append(",");
        updateSql.append("titrationSpeed = ").append(titratorMethod.getTitrationSpeed()).append(",");
        updateSql.append("slowTitrationVolume = ").append(titratorMethod.getSlowTitrationVolume()).append(",");
        updateSql.append("fastTitrationVolume = ").append(titratorMethod.getFastTitrationVolume()).append(",");
        db.execSQL(updateSql.toString());
    }

    public void deleteTestMethods(int methodId) {
        StringBuilder deleteSql = new  StringBuilder();
        deleteSql.append("delete from titrator_method where id = ")
                .append(methodId);
        db.execSQL(deleteSql.toString());
    }

    public int count() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from method", null);
            cursor.moveToFirst();
            if(!cursor.isAfterLast()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }
        return 0;
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
}
