package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.Audit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016/10/16.
 */
public class AuditHelper {

    private static final int MAX_LINE = 2000;

    private static int currentLine = -1;

    private SQLiteDatabase db;

    public AuditHelper(DBHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    public void addAudit(Audit audit) {
        String insertAudit = "insert into audit (operator, date, fragment,subfragment, event) " +
                "values (" +
                "'" + audit.getOperator() + "'," +
                "'" + audit.getDate() + "'," +
                "'" + audit.getFragment() + "'," +
                "'" + audit.getSubFragment() + "'," +
                "'" + audit.getEvent() + "'" +
                ")";
        db.execSQL(insertAudit);
        if (currentLine < 0) {
            currentLine = count();
        }
        currentLine++;
        if (currentLine > MAX_LINE) {
            db.execSQL("delete from audit where id=(select min(id) from audit)");
            currentLine--;
        }
    }

    public int count() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from audit", null);
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

    public int count(String startDate, String endDate, String operator) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from audit ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(operator)) {
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
            if (StringUtils.isNotEmpty(operator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("operator = '").append(operator).append("' ");
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

    public void deleteAudit(String startDate, String endDate, String operator) {
        StringBuilder deleteSql = new StringBuilder();
        boolean needAnd = false;
        deleteSql.append("delete from audit ");
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(operator)) {
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
            if (StringUtils.isNotEmpty(operator)) {
                if (needAnd) {
                    deleteSql.append("and ");
                }
                deleteSql.append("operator = '").append(operator).append("' ");
            }
        }
        db.execSQL(deleteSql.toString());
    }

    public void deleteAudit(List<Integer> ids) {
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from audit where id in (");
        for (int id : ids) {
            deleteSql.append(id).append(",");
        }
        deleteSql.deleteCharAt(deleteSql.length() - 1);
        deleteSql.append(")");
        db.execSQL(deleteSql.toString());
    }

    public int countAudit(String startDate, String endDate, String operator) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from audit ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(operator)) {
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
            if (StringUtils.isNotEmpty(operator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("operator = '").append(operator).append("' ");
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

    public List<Audit> listAudit(String startDate, String endDate, String operator, int page, int pageSize) {
        List<Audit> audits = new ArrayList<>();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from audit ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(operator)) {
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
            if (StringUtils.isNotEmpty(operator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("operator = '").append(operator).append("' ");
            }
        }
        sqlSb.append(" order by id desc LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            int count = cursor.getCount();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Audit audit = new Audit();
                audit.setId(cursor.getInt(0));
                audit.setOperator(cursor.getString(1));
                audit.setDate(cursor.getString(2));
                audit.setFragment(cursor.getString(3));
                audit.setSubFragment(cursor.getString(4));
                audit.setEvent(cursor.getString(5));

                audits.add(audit);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return audits;
    }

    public List<Audit> listAudit(String startDate, String endDate, String operator) {
        List<Audit> audits = new ArrayList<>();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from audit ");
        boolean needAnd = false;
        if (StringUtils.isNotEmpty(startDate)
                || StringUtils.isNotEmpty(endDate)
                || StringUtils.isNotEmpty(operator)) {
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
            if (StringUtils.isNotEmpty(operator)) {
                if (needAnd) {
                    sqlSb.append("and ");
                }
                sqlSb.append("operator = '").append(operator).append("' ");
            }
        }
        sqlSb.append(" order by id desc ");
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sqlSb.toString(), null);
            int count = cursor.getCount();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Audit audit = new Audit();
                audit.setId(cursor.getInt(0));
                audit.setOperator(cursor.getString(1));
                audit.setDate(cursor.getString(2));
                audit.setFragment(cursor.getString(3));
                audit.setSubFragment(cursor.getString(4));
                audit.setEvent(cursor.getString(5));

                audits.add(audit);
                cursor.moveToNext();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return audits;
    }

    public void deleteAuditAll() {
        String deleteSql = "delete from audit";
        db.execSQL(deleteSql);
    }
}
