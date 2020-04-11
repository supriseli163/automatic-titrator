package com.jh.automatic_titrator.service;

import android.content.Context;

import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.DBHelper;
import com.jh.automatic_titrator.common.db.FormulaHelper;
import com.jh.automatic_titrator.common.db.MD5Helper;
import com.jh.automatic_titrator.common.db.MethodHelper;
import com.jh.automatic_titrator.common.db.StandardValueHelper;
import com.jh.automatic_titrator.common.db.TestHelper;
import com.jh.automatic_titrator.common.db.TestMethodHelper;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.common.db.WaveLengthHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2017/1/2.
 */

public class DBService {

    private static Map<Context, DBHelper> dbHelperMap = new HashMap<>();

    private static Map<Context, AuditHelper> auditHelperMap = new HashMap<>();

    private static Map<Context, MD5Helper> md5HelperMap = new HashMap<>();

    private static Map<Context, TestHelper> testHelperMap = new HashMap();

    private static Map<Context, TestMethodHelper> testMethodHelperMap = new HashMap();

    private static Map<Context, MethodHelper> methodHelperMap = new HashMap<>();

    private static Map<Context, UserHelper> userHelperMap = new HashMap();

    private static Map<Context, WaveLengthHelper> waveLengthHelperMap = new HashMap();

    private static Map<Context, StandardValueHelper> standardValueHelperMap = new HashMap();

    private static Map<Context, FormulaHelper> formulaHelperMap = new HashMap();

    public static AuditHelper getAuditHelper(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if (!auditHelperMap.containsKey(context)) {
            AuditHelper auditHelper = new AuditHelper(dbHelper);
            auditHelperMap.put(context, auditHelper);
            return auditHelper;
        }
        return auditHelperMap.get(context);
    }

    public static MD5Helper getMD5Helper(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if (!md5HelperMap.containsKey(context)) {
            MD5Helper md5Helper = new MD5Helper(dbHelper);
            md5HelperMap.put(context, md5Helper);
            return md5Helper;
        }
        return md5HelperMap.get(context);
    }

    public static TestHelper getTestHelper(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if (!testHelperMap.containsKey(context)) {
            TestHelper testHelper = new TestHelper(dbHelper);
            testHelperMap.put(context, testHelper);
            return testHelper;
        }
        return testHelperMap.get(context);
    }

    public static MethodHelper getMethodHelper(Context context) {
        if(dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if(!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if(!methodHelperMap.containsKey(context)) {
            MethodHelper methodHelper = new MethodHelper(dbHelper);
            methodHelperMap.put(context, methodHelper);
            return methodHelper;
        }
        return methodHelperMap.get(context);
    }

    public static TestMethodHelper getTestMethodHelper(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if (!testMethodHelperMap.containsKey(context)) {
            TestMethodHelper testMethodHelper = new TestMethodHelper(dbHelper);
            testMethodHelperMap.put(context, testMethodHelper);
            return testMethodHelper;
        }
        return testMethodHelperMap.get(context);
    }

    public static UserHelper getUserHelper(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if (!userHelperMap.containsKey(context)) {
            UserHelper userHelper = new UserHelper(dbHelper);
            userHelperMap.put(context, userHelper);
            return userHelper;
        }
        return userHelperMap.get(context);
    }

    public static WaveLengthHelper getWaveLengthHelper(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if (!waveLengthHelperMap.containsKey(context)) {
            WaveLengthHelper waveLengthHelper = new WaveLengthHelper(context);
            waveLengthHelperMap.put(context, waveLengthHelper);
            return waveLengthHelper;
        }
        return waveLengthHelperMap.get(context);
    }

    public static StandardValueHelper getStandardValueHelper(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if (!standardValueHelperMap.containsKey(context)) {
            StandardValueHelper standardValueHelper = new StandardValueHelper(dbHelper);
            standardValueHelperMap.put(context, standardValueHelper);
            return standardValueHelper;
        }
        return standardValueHelperMap.get(context);
    }


    public static FormulaHelper getFormulaHelper(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        if (!formulaHelperMap.containsKey(context)) {
            FormulaHelper formulaHelper = new FormulaHelper(dbHelper);
            formulaHelperMap.put(context, formulaHelper);
            return formulaHelper;
        }
        return formulaHelperMap.get(context);
    }

    public static void clearAllData(Context context) {
        if (!dbHelperMap.containsKey(context)) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        DBHelper dbHelper = dbHelperMap.get(context);
        if (!dbHelper.getWritableDatabase().isOpen()) {
            dbHelperMap.put(context, new DBHelper(context));
        }
        dbHelper = dbHelperMap.get(context);
        dbHelper.cleanAll(dbHelper.getWritableDatabase());
    }

    public static void close(Context context) {
        DBHelper dbHelper = dbHelperMap.get(context);
        if (dbHelper != null) {
            dbHelper.close();
        }
        dbHelperMap.remove(context);
        auditHelperMap.remove(context);
        md5HelperMap.remove(context);
        testHelperMap.remove(context);
        testMethodHelperMap.remove(context);
        userHelperMap.remove(context);
        waveLengthHelperMap.remove(context);
        formulaHelperMap.remove(context);
        standardValueHelperMap.remove(context);
    }

    public void closeAll() {
        for (DBHelper dbHelper : dbHelperMap.values()) {
            dbHelper.close();
        }
        dbHelperMap.clear();
        auditHelperMap.clear();
        md5HelperMap.clear();
        testHelperMap.clear();
        testMethodHelperMap.clear();
        userHelperMap.clear();
        waveLengthHelperMap.clear();
        standardValueHelperMap.clear();
        formulaHelperMap.clear();
    }

}
