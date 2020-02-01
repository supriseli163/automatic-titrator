package com.jh.automatic_titrator.common.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016/10/16.
 */
public class MD5Helper {

    private static final int MAX_LINE = 2000;

    private static int currentLine = -1;

    private DBHelper dbHelper;

    private SQLiteDatabase db;

    public MD5Helper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.db = dbHelper.getWritableDatabase();
    }

    public void addMD5(MD5 md5) {
        String insertMD5Sql = "insert into md5 (filename, filepath, date, filelength, md5) " +
                "values (" +
                "'" + md5.getFileName() + "'," +
                "'" + md5.getFilePath() + "'," +
                "'" + md5.getCreateDate() + "'," +
                "'" + md5.getFileSize() + "'," +
                "'" + md5.getMd5() + "'" +
                ")";
        db.execSQL(insertMD5Sql);
        if (currentLine < 0) {
            currentLine = count();
        }
        currentLine++;
        if (currentLine > MAX_LINE) {
            db.execSQL("delete from md5 where id=(select min(id) from md5)");
            currentLine--;
        }
    }

    public int count() {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from md5", null);
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

    public void clean() {
        String deleteSql = "delete from md5";
        db.execSQL(deleteSql);
    }

    public List<MD5> list(int page, int paseSize) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from md5 order by id desc", null);
            cursor.moveToFirst();
            List<MD5> md5s = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                MD5 md5 = new MD5();
                md5.setId(cursor.getInt(0));
                md5.setFileName(cursor.getString(1));
                md5.setFilePath(cursor.getString(2));
                md5.setCreateDate(cursor.getString(3));
                md5.setFileSize(cursor.getInt(4));
                md5.setMd5(cursor.getString(5));
                md5s.add(md5);
                cursor.moveToNext();
            }
            return md5s;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int count(String startDate, String endDate, String operator) {
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from md5 ");
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

    public List<MD5> list(String startDate, String endDate, String operator, int page, int pageSize) {

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from md5 ");
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
            List<MD5> md5s = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                MD5 md5 = new MD5();
                md5.setId(cursor.getInt(0));
                md5.setFileName(cursor.getString(1));
                md5.setFilePath(cursor.getString(2));
                md5.setCreateDate(cursor.getString(3));
                md5.setFileSize(cursor.getInt(4));
                md5.setMd5(cursor.getString(5));
                md5s.add(md5);
                cursor.moveToNext();
            }
            return md5s;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<MD5> list(String startDate, String endDate, String operator) {

        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from md5 ");
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
            List<MD5> md5s = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                MD5 md5 = new MD5();
                md5.setId(cursor.getInt(0));
                md5.setFileName(cursor.getString(1));
                md5.setFilePath(cursor.getString(2));
                md5.setCreateDate(cursor.getString(3));
                md5.setFileSize(cursor.getInt(4));
                md5.setMd5(cursor.getString(5));
                md5s.add(md5);
                cursor.moveToNext();
            }
            return md5s;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String checkSum(String fileName) {
        return checkSum(new File(fileName));
    }

    public static String checkSum(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return checkSum(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String checkSum(InputStream inputStream) {
        try {
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead = -1;

            while ((numRead = inputStream.read(buffer)) != -1) {
                complete.update(buffer, 0, numRead);
            }
            byte[] b = complete.digest();
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < b.length; i++) {
                result.append(Integer.toString((b[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addMD5(String fileName) {
        addMD5(new File(fileName));
    }

    public void addMD5(File file) {
        MD5 md5 = new MD5();
        md5.setCreateDate(TimeTool.currentDate());
        md5.setFileName(file.getName());
        md5.setFilePath(file.getAbsolutePath());
        md5.setFileSize(file.length());
        md5.setMd5(MD5Helper.checkSum(file));
        addMD5(md5);
    }
}
