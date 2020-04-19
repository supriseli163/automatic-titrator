package com.jh.automatic_titrator.common.db.titrator;

import android.database.sqlite.SQLiteDatabase;

import com.jh.automatic_titrator.common.db.DBHelper;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;

public class TitratorMethodSettingHelper {
    private TitratorEndPointHelper titratorEndPointHelper;
    private EndPointSettingHelper endPointSettingHelper;
    private TitratorMethodHelper titratorMethodHelper;
    private SQLiteDatabase db;

    public TitratorMethodSettingHelper(DBHelper dbHelper) {
        this.titratorEndPointHelper = new TitratorEndPointHelper(dbHelper.getWritableDatabase());
        this.endPointSettingHelper = new EndPointSettingHelper(dbHelper.getWritableDatabase());
        this.titratorMethodHelper = new TitratorMethodHelper(dbHelper.getWritableDatabase());
        this.db = dbHelper.getWritableDatabase();
    }

    public void insert(TitratorMethod titratorMethod) {
        titratorMethodHelper.doInsertTitratorMethod(titratorMethod);
        endPointSettingHelper.insertEndPointSetting(titratorMethod.getEndPointSettingList());
        titratorEndPointHelper.insertTitratorEndPoints(titratorMethod.getTitratorEndPoints());
    }
}
