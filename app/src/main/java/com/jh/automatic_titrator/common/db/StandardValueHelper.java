package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.entity.common.StandardData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/1/14.
 */

public class StandardValueHelper {

    private SQLiteDatabase db;

    public StandardValueHelper(DBHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    public List<StandardData> getStandardData(int waveIndex) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from standardvalue where waveIndex = " + waveIndex, null);
//            cursor = db.rawQuery("select * from standardvalue", null);
            cursor.moveToFirst();
            List<StandardData> standardDatas = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                StandardData standardData = new StandardData();
                standardData.setId(cursor.getInt(0));
                standardData.setTestValue(cursor.getDouble(1));
                standardData.setStandardValue(cursor.getDouble(2));
                standardData.setTemperature(cursor.getDouble(3));
                standardData.setWaveIndex(cursor.getInt(4));
                standardDatas.add(standardData);
                cursor.moveToNext();
            }
            return standardDatas;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void cleanStandardData(int waveIndex) {
        db.execSQL("delete from standardvalue where waveIndex = " + waveIndex);
    }

    public void refreshStandardDatas(List<StandardData> standardDatas, int waveIndex) {
        cleanStandardData(waveIndex);
        insertStandardDatas(standardDatas);
    }

    public void insertStandardDatas(List<StandardData> standardDatas) {
        if (standardDatas != null && standardDatas.size() > 0) {
            for (StandardData standardData : standardDatas) {
                db.execSQL("insert into standardvalue (testValue,standardValue,temperature, waveIndex) " +
                        "values(" +
                        standardData.getTestValue() + ", " +
                        standardData.getStandardValue() + ", " +
                        standardData.getTemperature() + ", " +
                        standardData.getWaveIndex() + "" +
                        ")");
            }
        }
    }

    public List<StandardData> listAll() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from standardvalue", null);
//            cursor = db.rawQuery("select * from standardvalue", null);
            cursor.moveToFirst();
            List<StandardData> standardDatas = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                StandardData standardData = new StandardData();
                standardData.setId(cursor.getInt(0));
                standardData.setTestValue(cursor.getDouble(1));
                standardData.setStandardValue(cursor.getDouble(2));
                standardData.setTemperature(cursor.getDouble(3));
                standardData.setWaveIndex(cursor.getInt(4));
                standardDatas.add(standardData);
                cursor.moveToNext();
            }
            return standardDatas;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
