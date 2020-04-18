package com.jh.automatic_titrator.common.db.titrator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.titrator.TitratorEndPoint;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;

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
        insertSql.append("insert insert into titrator_endPoint (titratorMethodId, endPointValue, preControlvalue,correlationCoefficient,resultUnit) values (" +
                StringUtils.dBValueInputFormat(titratorEndPoint.getTitratorMethodId()) +
                StringUtils.dBValueInputFormat(titratorEndPoint.getEndPointValue()) +
                StringUtils.dBValueInputFormat(titratorEndPoint.getPreControlvalue()) +
                StringUtils.dBValueInputFormat(titratorEndPoint.getCorrelationCoefficient()) +
                StringUtils.dBValueInputFormat(titratorEndPoint.getResultUnit())
                + ")");
        db.rawQuery(insertSql.toString(), null);
    }

    public List<TitratorEndPoint> queryTitratorEndPoints(int titratorMethodId) {
        StringBuilder querySql = new StringBuilder();
        querySql.append("select * from titrator_endPoint where titratorMethodId =").append(titratorMethodId);
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
}
