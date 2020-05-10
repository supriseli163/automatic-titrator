package com.jh.automatic_titrator.common.db.titrator;

import android.database.sqlite.SQLiteDatabase;

import com.google.common.collect.Lists;
import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.common.db.DBHelper;
import com.jh.automatic_titrator.common.utils.CollectionUtils;
import com.jh.automatic_titrator.entity.common.titrator.EndPointSetting;
import com.jh.automatic_titrator.entity.common.titrator.TitratorEndPoint;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;

import java.util.List;

public class TitratorParamsBeanHelper {
    private TitratorEndPointHelper titratorEndPointHelper;
    private EndPointSettingHelper endPointSettingHelper;
    private TitratorMethodHelper titratorMethodHelper;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public TitratorParamsBeanHelper() {
        dbHelper = new DBHelper(BaseApplication.getApplication());
        this.titratorEndPointHelper = new TitratorEndPointHelper(dbHelper.getWritableDatabase());
        this.endPointSettingHelper = new EndPointSettingHelper(dbHelper.getWritableDatabase());
        this.titratorMethodHelper = new TitratorMethodHelper(dbHelper.getWritableDatabase());
        this.db = dbHelper.getWritableDatabase();
    }

    public void insert(TitratorParamsBean titratorParamsBean) {
        TitratorMethod titratorMethod = titratorParamsBean.getTitratorMethod();
        titratorMethodHelper.doInsertTitratorMethod(titratorMethod);
        TitratorMethod insertTitratorMethod = titratorMethodHelper.selectByNameAndType(titratorMethod.getTitratorType(), titratorMethod.getMethodName());
        titratorMethod.setId(insertTitratorMethod.getId());
        fillEndPointSettingId(titratorParamsBean.getEndPointSettings(), insertTitratorMethod.getId());
        fillTitratorEndPointId(titratorParamsBean.getTitratorEndPoint(), insertTitratorMethod.getId());
        endPointSettingHelper.insertEndPointSetting(titratorParamsBean.getEndPointSettings());
        titratorEndPointHelper.insertTitratorEndPoints(titratorMethod.getTitratorEndPoints());
    }

    public TitratorParamsBean selectByMethodId(int methodId) throws Throwable {
        TitratorParamsBean titratorParamsBean = new TitratorParamsBean();
        TitratorMethod titratorMethod = titratorMethodHelper.selectByMethod(methodId);
        if(titratorMethod == null) {
            return null;
        }
        List<EndPointSetting> endPointSettings = endPointSettingHelper.quertEndPointSettingByMethodId(methodId);
        List<TitratorEndPoint> titratorEndPoints =  titratorEndPointHelper.queryTitratorEndPoints(methodId);
        titratorParamsBean.setTitratorMethod(titratorMethod);
        titratorParamsBean.setTitratorEndPoints(titratorEndPoints);
        titratorParamsBean.setEndPointSettings(endPointSettings);
        return titratorParamsBean;
    }

    public List<TitratorParamsBean> listMethodByType(TitratorTypeEnum titratorTypeEnum, int pageNum, int pageSize) throws Throwable {
        List<TitratorMethod> titratorMethods = titratorMethodHelper.listMethodByType(titratorTypeEnum.getDesc(), pageNum, pageSize);
        if (CollectionUtils.isEmpty(titratorMethods)) {
            return Lists.newArrayList();
        }

        List<TitratorParamsBean> titratorParamsBeans = Lists.newArrayList();
        for (TitratorMethod titratorMethod : titratorMethods) {
            TitratorParamsBean titratorParamsBean = new TitratorParamsBean();
            List<EndPointSetting> endPointSettings = endPointSettingHelper.quertEndPointSettingByMethodId(titratorMethod.getId());
            List<TitratorEndPoint> titratorEndPoints = titratorEndPointHelper.queryTitratorEndPoints(titratorMethod.getId());
            titratorMethod.setTitratorEndPoints(titratorEndPoints);
//            titratorMethod.setEndPointSettingList(endPointSettings);
            titratorParamsBean.setTitratorMethod(titratorMethod);
            titratorParamsBean.setEndPointSettings(endPointSettings);
            titratorParamsBean.setTitratorEndPoints(titratorEndPoints);
            titratorParamsBeans.add(titratorParamsBean);
        }
        return titratorParamsBeans;
    }

    public int countMethod(TitratorTypeEnum titratorTypeEnum) {
        return titratorMethodHelper.countMethod();
    }

    public void updateTitratorMethod(TitratorParamsBean titratorParamsBean) {
        titratorMethodHelper.updateTitratorMethod(titratorParamsBean.getTitratorMethod());
        endPointSettingHelper.updateEndPoint(titratorParamsBean.getEndPointSettings());
        titratorEndPointHelper.updateEndPoints(titratorParamsBean.getTitratorEndPoint());
    }

    public void deleteByTitratorMethodId(int titratorMethodId) {
        titratorMethodHelper.deleteTestMethods(titratorMethodId);
        endPointSettingHelper.deleteByMethodId(titratorMethodId);
        titratorEndPointHelper.deleteByTitratorMethodId(titratorMethodId);
    }


    public static void fillEndPointSettingId(List<EndPointSetting> endPointSettings, int methodId) {
        if (endPointSettings == null || endPointSettings.isEmpty()) {
            return;
        }
        for (EndPointSetting endPointSetting : endPointSettings) {
            endPointSetting.setTitratorMethodId(methodId);
        }
    }

    public static void fillTitratorEndPointId(List<TitratorEndPoint> titratorEndPoints, int methodId) {
        if (titratorEndPoints == null || titratorEndPoints.isEmpty()) {
            return;
        }
        for (TitratorEndPoint titratorEndPoint : titratorEndPoints) {
            titratorEndPoint.setTitratorMethodId(methodId);
        }
    }

}
