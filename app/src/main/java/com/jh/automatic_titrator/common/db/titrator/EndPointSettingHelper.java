package com.jh.automatic_titrator.common.db.titrator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.titrator.EndPointSetting;

import java.util.ArrayList;
import java.util.List;

public class EndPointSettingHelper {
    private SQLiteDatabase db;

    public EndPointSettingHelper(SQLiteDatabase db) {
        this.db = db;
    }

    public void close() {
        db.close();
    }

    public void insertEndPointSetting(EndPointSetting endPointSetting) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert intro end_point_setting (titratorMethodId,burette," +
                "reagentName,reagentConcentrationUnit,addVolume,addSpeed,addTime,referenceEndPoint,delayTime) values (");
        stringBuilder.append(StringUtils.dBValueInputFormat(endPointSetting.getTitratorMethodId()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getBurette()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getReagentName()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getReagentConcentration()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getReagentConcentrationUnit()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getAddSpeed()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getAddTime()));

    }

    public List<EndPointSetting> quertEndPointSettingByMethodId(int methodId) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select * from end_point_setting where titratorMethodId = ")
        .append(methodId);
        Cursor cursor = null;
        List<EndPointSetting> endPointSettings = new ArrayList<>();
        try {
            cursor = db.rawQuery(stringBuilder.toString(), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                EndPointSetting endPointSetting = new EndPointSetting();
                endPointSetting.setId(cursor.getInt(0));
                endPointSetting.setTitratorMethodId(cursor.getInt(1));
                endPointSetting.setBurette(cursor.getInt(2));
                endPointSetting.setReagentName(cursor.getString(3));
                endPointSetting.setReagentConcentration(cursor.getDouble(4));
                endPointSetting.setReagentConcentrationUnit(cursor.getString(5));
                endPointSetting.setAddVolume(cursor.getDouble(6));
                endPointSetting.setAddSpeed(cursor.getInt(7));
                endPointSetting.setAddTime(cursor.getString(8));
                endPointSetting.setReferenceEndPoint(cursor.getInt(9));
                endPointSetting.setDelayTime(cursor.getInt(10));
                cursor.moveToNext();
                endPointSettings.add(endPointSetting);
            }
        } catch (Exception ex) {
            throw ex.getCause();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return endPointSettings;
    }

    public void deleteByMethodId(int methodId) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("delete from end_point_setting where titratorMethodId = ").append(methodId);
        db.execSQL(sqlBuilder.toString());
    }
}
