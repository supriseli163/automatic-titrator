package com.jh.automatic_titrator.common.db.titrator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.TestMethod;
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

    public void insertEndPointSetting(List<EndPointSetting> endPointSettings) {
        for(EndPointSetting endPointSetting : endPointSettings) {
            insertEndPointSetting(endPointSetting);
        }
    }

    public void insertEndPointSetting(EndPointSetting endPointSetting) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into end_point_setting (titratorMethodId,burette," +
                "reagentName,reagentConcentration, reagentConcentrationUnit,addVolume,addSpeed,addTime,referenceEndPoint,delayTime) values (");
        stringBuilder.append(StringUtils.dBValueInputFormat(endPointSetting.getTitratorMethodId()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getBurette()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getReagentName()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getReagentConcentration()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getReagentConcentrationUnit()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getAddVolume()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getAddSpeed()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getAddTime()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getReferenceEndPoint()))
                .append(StringUtils.dBValueInputFormat(endPointSetting.getDelayTime(), true));
        db.execSQL(stringBuilder.toString());
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
                cursor.moveToNext();
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

    public void updateEndPoint(EndPointSetting endPointSetting) {
        StringBuilder updateSB = new StringBuilder();
        updateSB.append("update end_point_setting set");
        updateSB.append("titratorMethodId = ").append(endPointSetting.getTitratorMethodId()).append(",");
        updateSB.append("burette = ").append(endPointSetting.getBurette()).append(",");
        updateSB.append("reagentName = ").append(endPointSetting.getReagentName()).append(",");
        updateSB.append("reagentConcentration = ").append(endPointSetting.getReagentConcentration()).append(",");
        updateSB.append("reagentConcentrationUnit = '").append(endPointSetting.getReagentConcentrationUnit()).append("',");
        updateSB.append("addVolume = ").append(endPointSetting.getAddVolume()).append(",");
        updateSB.append("addSpeed = ").append(endPointSetting.getAddSpeed()).append(",");
        updateSB.append("addTime = ").append(endPointSetting.getAddTime()).append(",");
        updateSB.append("referenceEndPoint = ").append(endPointSetting.getReferenceEndPoint()).append(",");
        updateSB.append("delayTime = ").append(endPointSetting.getDelayTime()).append(",");
        updateSB.append("where ");
        db.execSQL(updateSB.toString());
    }

    public void updateEndPoint(List<EndPointSetting> endPointSettings) {
        for(EndPointSetting endPointSetting : endPointSettings) {
            updateEndPoint(endPointSetting);
        }
    }
}
