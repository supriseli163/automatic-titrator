package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;

import java.util.ArrayList;
import java.util.List;

public class TitratorMethodHelper {
    private SQLiteDatabase db;

    private static final int MAX_Line = 10000;

    public void close() {
        db.close();
    }

    public String saveMethod() {
        StringBuffer insertSql = new StringBuffer();
        insertSql.append("insert insert into method(methodName)");

        if(db.isOpen()) {
            db.execSQL(insertSql.toString());
        }

        int count = count();
        if(count > MAX_Line) {
            db.execSQL("delete from method where id=(select min(id) from method)");
        }
        return insertSql.toString();
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
}
