package com.jh.automatic_titrator.common.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.file.FileHelper;
import com.jh.automatic_titrator.common.utils.TimeTool;

import java.io.File;

/**
 * Created by apple on 2016/10/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int version = 5;
    private static final String name = "user";

    public DBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addTableMD5(db);
        addTableUser(db);
        addTableHistoryUser(db);
        addTableLastUser(db);
        addTableWaveLength(db);
        addTableTest(db);
        addTableTestMethod(db);
        addTableAudit(db);
        addTableFormula(db);
        addTableConversion(db);
        addTableStandardValue(db);

        addBaseUser(db);
        addBaseWavelength(db);
        addBaseFormula(db);
        writeVersion();

//        //滴定仪
//        addTitratorEndPoint(db);
//        addEndPointSetting(db);
//        addTitratorEndPoint(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            writeVersion();
            updateTest(db);
        }
    }

    public void cleanAll(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS md5");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS wavelength");
        db.execSQL("DROP TABLE IF EXISTS test");
        db.execSQL("DROP TABLE IF EXISTS test_method");
        db.execSQL("DROP TABLE IF EXISTS history_user");
        db.execSQL("DROP TABLE IF EXISTS audit");
        db.execSQL("DROP TABLE IF EXISTS formula");
        db.execSQL("DROP TABLE IF EXISTS conversion");
        db.execSQL("DROP TABLE IF EXISTS standardvalue");

        db.execSQL("DROP TABLE IF EXISTS titrator_method");
        db.execSQL("DROP TABLE IF EXISTS end_point_setting");
        db.execSQL("DROP TABLE IF EXISTS titrator_end_point");


        addTableMD5(db);
        addTableUser(db);
        addTableHistoryUser(db);
        addTableLastUser(db);
        addTableWaveLength(db);
        addTableTest(db);
        addTableTestMethod(db);
        addTableAudit(db);
        addTableFormula(db);
        addTableConversion(db);
        addTableStandardValue(db);

        addBaseUser(db);
        addBaseWavelength(db);
        addBaseFormula(db);

//        //滴定仪
//        addTitratorEndPoint(db);
//        addEndPointSetting(db);
//        addTitratorEndPoint(db);

    }

    private void updateTest(SQLiteDatabase db) {
        db.execSQL("alter table test add column test_count varchar;");
    }

    private void addTableMD5(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS md5 " +
                "(" +
                "id integer primary key autoincrement," +
                "filename varchar(256)," +
                "filepath varchar(10)," +
                "date varchar(20)," +
                "filelength INTEGER," +
                "md5 varchar(32)" +
                ")");
    }

    private void addTableUser(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user " +
                "(" +
                "userName varchar(256) primary key," +
                "password varchar(36)," +
                "role integer," +
                "createdate varchar(24)," +
                "`desc` varchar(256)," +
                "headid integer," +
                "auth varchar(256)," +
                "creator varchar(256)," +
                "conf blob" +
                ")");
    }

    private void writeVersion() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String basePath;
        if (path.endsWith("/")) {
            basePath = path + "hanon";
        } else {
            basePath = path + "/hanon";
        }
        File confFile = new File(basePath + "/conf/version.conf");
        FileHelper.writeFile(confFile, version + "");
    }

    private void addTableStandardValue(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS standardvalue" +
                "(" +
                "id integer primary key autoincrement," +
                "testValue double, " +
                "standardValue double, " +
                "temperature double, " +
                "waveIndex integer" +
                ")");
    }

    private void addTableConversion(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS conversion" +
                "(" +
                "id integer primary key autoincrement," +
                "formulaId integer, " +
                "start double, " +
                "`end` double, " +
                "p0 double, " +
                "p1 double, " +
                "p2 double," +
                "p3 double," +
                "p4 double," +
                "p5 double" +
                ")");
    }

    private void addTableFormula(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS formula" +
                "(" +
                "id integer primary key autoincrement," +
                "formulaName varchar(256), " +
                "simpleName varchar(256), " +
                "unit varchar(256), " +
                "decimal int, " +
                "showPercent integer," +
                "date varchar(32)," +
                "`desc` varchar(256)," +
                "creator varchar(256)" +
                ")");
    }

    private void addTableLastUser(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS last_user(" +
                "userName varchar(256))");
    }

    private void addTableAudit(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS audit" +
                "(" +
                "id integer primary key autoincrement," +
                "operator varchar(256), " +
                "date varchar(256), " +
                "fragment varchar(256), " +
                "subfragment varchar(256), " +
                "event varchar(256)" +
                ")");
    }

    private void addTableTest(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS test " +
                "(" +
                "id integer primary key autoincrement," +
                "testName varchar(256)," +
                "testId varchar(256)," +
                "testMethod varchar(256)," +
                "date varchar(32)," +
                "temperature String," +
                "temperature1 String," +
                "optical double," +
                "res double," +
                "formula String," +
                "formulaSN varchar(10)," +
                "formulaUnit varchar(10)," +
                "decimal integer," +
                "testCreator varchar(256)," +
                "wavelength String," +
                "tubelength String," +
                "specificrotation String," +
                "concentration String," +
                "test_count String" +
                ")");
    }

    private void addTableTestMethod(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS test_method " +
                "(" +
                "id integer primary key autoincrement," +
                "testName varchar(256)," +
                "testCount integer," +
                "concentration varchar(20)," +
                "concentrationType integer," +
                "accuracy integer," +
                "formula string," +
                "waveLength integer," +
                "decimals integer," +
                "testTubeLength varchar(20)," +
                "specificRotation varchar(20)," +
                "atlasX integer," +
                "atlasY integer," +
                "useTemperature integer," +
                "temperatureType integer," +
                "temperature double," +
                "autoTest integer," +
                "autoTestInterval integer," +
                "autoTestTimes integer," +
                "createDate varchar(32)," +
                "creator varchar(256)" +
                ")");
    }

    private void addTableWaveLength(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS wavelength " +
                "(" +
                "id integer primary key autoincrement," +
                "wavelength int" +
                ")");
    }

    private void addTableHistoryUser(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS history_user" +
                "(" +
                "id integer primary key autoincrement," +
                "userName varchar(256) " +
                ")");
    }

    public void addBaseUser(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from user where username = 'admin'", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                int count = cursor.getInt(0);
                if (count == 0) {
                    db.execSQL("insert into user (username,password,role,createdate,`desc`,headid,auth,creator) values ('admin','888888',0,'20160305 12:00:00',''," + R.drawable.head_portrait1 + ",'all','admin')");
                    db.execSQL("insert into last_user (userName) values ('admin')");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void addBaseWavelength(SQLiteDatabase db) {
        db.execSQL("insert into wavelength (id, wavelength) values (1, '589')");
    }

    private void addTitratorMethod(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS titrator_method (\n" +
                "\tid integer primary key autoincrement,\n" +
                "\ttitratorType varchar(20),\n" +
                "\tmethodName varchar(50),\n" +
                "\tburetteVolume double,\n" +
                "\tworkingElectrode integer,\n" +
                "\treferenceElectrode double,\n" +
                "\tsampleMeasurementUnit varchar(50),\n" +
                "\ttitrationDisplayUnit varchar(50),\n" +
                "\treplenishmentSpeed integer,\n" +
                "\tstiringSpeed integer,\n" +
                "\telectroedEquilibrationTime integer,\n" +
                "\tpreStiringTime double,\n" +
                "\tendVolume integer,\n" +
                "\ttitrationSpeed integer,\n" +
                "\tslowTitrationVolume integer,\n" +
                "\tfastTitrationVolume integer\n" +
                ")";
        db.execSQL(sql);
    }

    private void addEndPointSetting(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS end_point_setting (\n" +
                "\tid integer primary key autoincrement,\n" +
                "\ttitratorMethodId integer,\n" +
                "\tburette integer,\n" +
                "\treagentName double,\n" +
                "\treagentConcentration double,\n" +
                "\treagentConcentrationUnit varchar(50),\n" +
                "\taddVolume double,\n" +
                "\taddSpeed integer,\n" +
                "\taddTime varchar(50),\n" +
                "\treferenceEndPoint integer,\n" +
                "\tdelayTime integer\n" +
                ")";
        db.execSQL(sql);
    }

    private void addTitratorEndPoint(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS titrator_end_point (\n" +
                "\tid integer primary key autoincrement,\n" +
                "\ttitratorMethodId integer,\n" +
                "\tendPointValue double,\n" +
                "\tpreControlvalue double,\n" +
                "\tcorrelationCoefficient double,\n" +
                "\tresultUnit varchar(50)\n" +
                ")";
        db.execSQL(sql);
    }


    private void addBaseFormula(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) from formula where formulaName = '国际糖度'", null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                int count = cursor.getInt(0);
                if (count == 0) {
                    db.execSQL("insert into formula (formulaName, simpleName, unit, " +
                            "decimal, showPercent, date, `desc`, creator) " +
                            "values('国际糖度','Z','',3,0," +
                            "'" + TimeTool.currentDate() + "'," +
                            "'','admin')");
                    cursor = db.rawQuery("select MAX(id) from formula", null);

                    cursor.moveToFirst();
                    int id = 0;
                    if (!cursor.isAfterLast()) {
                        id = cursor.getInt(0);
                    }
                    db.execSQL("insert into conversion (formulaId, start,`end`,p0,p1,p2,p3,p4,p5) values(" + id + ",-90,90,0,2.888,0,0,0,0)");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
