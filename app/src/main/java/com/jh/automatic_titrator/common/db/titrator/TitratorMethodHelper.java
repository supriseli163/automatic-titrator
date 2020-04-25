package com.jh.automatic_titrator.common.db.titrator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum;

import java.util.ArrayList;
import java.util.List;

public class TitratorMethodHelper {
    private SQLiteDatabase db;
    private TitratorEndPointHelper titratorEndPointHelper;
    private EndPointSettingHelper endPointSettingHelper;

    public TitratorMethodHelper(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
    }

    private static final int MAX_Line = 10000;

    public void close() {
        db.close();
    }

    public void doInsertTitratorMethod(TitratorMethod titratorMethod) {
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("insert into titrator_method (titratorType, methodName, buretteVolume,workingElectrode,referenceElectrode,sampleMeasurementUnit," +
                "titrationDisplayUnit,replenishmentSpeed,stiringSpeed,electroedEquilibrationTime,electroedEquilibriumPotential,preStiringTime" +
                ",perAddVolume,endVolume,titrationSpeed,slowTitrationVolume,fastTitrationVolume,modifyTime,userName) values (" +
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
                StringUtils.dBValueInputFormat(titratorMethod.getFastTitrationVolume()) +
                StringUtils.dBValueInputFormat(titratorMethod.getModifyTime()) +
                StringUtils.dBValueInputFormat(titratorMethod.getUserName())
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
        updateSql.append("modifyTime = ").append(titratorMethod.getModifyTime());
        updateSql.append("userName = ").append(titratorMethod.getUserName());
        db.execSQL(updateSql.toString());
    }

    public void deleteTestMethods(int methodId) {
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from titrator_method where id = ")
                .append(methodId);
        db.execSQL(deleteSql.toString());
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

    public void deleteMethod(List<Integer> ids) {
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from titrator_method where id in(");
        for (int id : ids) {
            deleteSql.append(id).append(",");
        }
        deleteSql.deleteCharAt(deleteSql.length() - 1);
        deleteSql.append(")");
        db.execSQL(deleteSql.toString());
    }

//    public TitratorMethod

    public List<TitratorMethod> listTitorMethod(String startDate, String enddate,
                                                String sampleName,
                                                Double gt,
                                                Double lt,
                                                String creator,
                                                int page,
                                                int pageSize) {
        List<TitratorMethod> titratorMethods = new ArrayList<>();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from titrator_method");
        boolean neeAnd = false;
        if (StringUtils.isNotEmpty(startDate) ||
                StringUtils.isNotEmpty(enddate) ||
                StringUtils.isNotEmpty(creator) ||
                StringUtils.isNotEmpty(sampleName) ||
                gt != null ||
                lt != null) {
            sqlSb.append("where ");
            if (StringUtils.isNotEmpty(startDate)) {
                sqlSb.append("datatime(`date`) >= datetime('").append(startDate);
            }
        }

        return titratorMethods;
    }

    public List<TitratorMethod> listMethodByType(TitratorTypeEnum titratorTypeEnum, int pagNum, int pageSize) {
        List<TitratorMethod> result = new ArrayList<>();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from titrator_method where titratorType = ").append(titratorTypeEnum.getDesc());
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            cursor.moveToFirst();
            TitratorMethod titratorMethod = new TitratorMethod();
            titratorMethod.setId(cursor.getInt(0));
            titratorMethod.setTitratorType(cursor.getString(1));
            titratorMethod.setMethodName(cursor.getString(2));
            titratorMethod.setBuretteVolume(cursor.getDouble(3));
            titratorMethod.setWorkingElectrode(WorkElectrodeEnnum.fromDesc(cursor.getString(4)));
            titratorMethod.setReferenceElectrode(cursor.getDouble(5));
            titratorMethod.setSampleMeasurementUnit(cursor.getString(6));
            titratorMethod.setTitrationDisplayUnit(cursor.getString(7));
            titratorMethod.setPreStiringTime(cursor.getString(8));
            titratorMethod.setEndVolume(cursor.getString(9));
            titratorMethod.setTitrationSpeed(cursor.getString(10));
            titratorMethod.setSlowTitrationVolume(cursor.getString(11));
            titratorMethod.setFastTitrationVolume(cursor.getString(12));
            result.add(titratorMethod);
        } catch (Exception ex) {
            Log.d("sql Exception", String.format("listMethodByType titratorTypeEnum:{}, pagNum:{},pageSize:{}", titratorTypeEnum.getDesc(), pageSize, pagNum, ex.getMessage()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public Integer listMethodCountByType(TitratorTypeEnum titratorTypeEnum) {
        int count = 0;
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from titrator_method where titratorType =").append(titratorTypeEnum.getDesc());
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

    public int count(String startDate, String endDate, String creator) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from test_method");
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
}
