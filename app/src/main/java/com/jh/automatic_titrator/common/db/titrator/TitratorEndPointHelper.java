package com.jh.automatic_titrator.common.db.titrator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.titrator.TitratorEndPoint;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;

import java.util.ArrayList;
import java.util.List;

public class TitratorEndPointHelper {

    private SQLiteDatabase db;

    public TitratorEndPointHelper(SQLiteDatabase db) {
        this.db = db;
    }

    public void insertTitratorEndPoints(List<TitratorEndPoint> titratorEndPoints) {
        for(TitratorEndPoint titratorEndPoint : titratorEndPoints) {
            insertTitratorEndPoint(titratorEndPoint);
        }
    }

    public void insertTitratorEndPoint(TitratorEndPoint titratorEndPoint) {
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("insert into titrator_end_point (titratorMethodId, endPointValue, preControlvalue,correlationCoefficient,resultUnit) values (" +
                StringUtils.dBValueInputFormat(titratorEndPoint.getTitratorMethodId()) +
                StringUtils.dBValueInputFormat(titratorEndPoint.getEndPointValue()) +
                StringUtils.dBValueInputFormat(titratorEndPoint.getPreControlvalue()) +
                StringUtils.dBValueInputFormat(titratorEndPoint.getCorrelationCoefficient()) +
                StringUtils.dBValueInputFormat(titratorEndPoint.getResultUnit(), true));
        db.execSQL(insertSql.toString());
    }

    public List<TitratorEndPoint> queryTitratorEndPoints(int titratorMethodId) {
        StringBuilder querySql = new StringBuilder();
        querySql.append("select * from titrator_end_point where titratorMethodId =").append(titratorMethodId);
        Cursor cursor = db.rawQuery(querySql.toString(), null);
        cursor.moveToFirst();
        List<TitratorEndPoint> titratorEndPoints = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            TitratorEndPoint titratorEndPoint = new TitratorEndPoint();
            titratorEndPoint.setId(cursor.getInt(0));
            titratorEndPoint.setTitratorMethodId(cursor.getInt(1));
            titratorEndPoint.setPreControlvalue(cursor.getInt(2));
            titratorEndPoint.setCorrelationCoefficient(cursor.getDouble(3));
            titratorEndPoint.setResultUnit(cursor.getString(4));
            titratorEndPoints.add(titratorEndPoint);
            cursor.moveToNext();
        }
        return titratorEndPoints;
    }

    public void deleteByTitratorMethodId(int titratorMethodId) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("delete from titrator_end_point where titratorMethodId = ").append(titratorMethodId);
        db.execSQL(sqlBuilder.toString());
    }

    public void updateEndPoint(TitratorEndPoint titratorEndPoint) {
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update titrator_end_point set ");
        updateSql.append("titratorMethodId = ").append(titratorEndPoint.getTitratorMethodId()).append(",");
        updateSql.append("endPointValue = ").append(titratorEndPoint.getEndPointValue()).append(",");
        updateSql.append("preControlvalue = ").append(titratorEndPoint.getPreControlvalue()).append(",");
        updateSql.append("correlationCoefficient = ").append(titratorEndPoint.getCorrelationCoefficient()).append(",");
        updateSql.append("where id=").append(titratorEndPoint.getId());
        db.execSQL(updateSql.toString());
    }

    public void updateEndPoints(List<TitratorEndPoint> titratorEndPoints) {
        for(TitratorEndPoint titratorEndPoint : titratorEndPoints) {
            updateEndPoint(titratorEndPoint);
        }
    }
}
