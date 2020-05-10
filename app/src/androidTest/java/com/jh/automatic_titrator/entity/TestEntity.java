package com.jh.automatic_titrator.entity;

import androidx.test.espresso.core.internal.deps.guava.collect.Lists;

import com.jh.automatic_titrator.entity.common.titrator.EndPointSetting;
import com.jh.automatic_titrator.entity.common.titrator.TitratorEndPoint;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum;

public class TestEntity {

    public static TitratorTypeEnum titratorTypeEnum = TitratorTypeEnum.DynamicTitrator;

    public static TitratorParamsBean getTitratorParamsBean() {
        TitratorParamsBean  titratorParamsBean = new TitratorParamsBean();
        titratorParamsBean.setTitratorMethod(getTitratorMethod());
        titratorParamsBean.setTitratorEndPoints(Lists.newArrayList(getTitratorMethodEndPoint()));
        titratorParamsBean.setEndPointSettings(Lists.newArrayList(getEndPointSetting()));
        return titratorParamsBean;
    }

    public static TitratorMethod getTitratorMethod() {
        TitratorMethod titratorMethod = new TitratorMethod();
        titratorMethod.setTitratorType(titratorTypeEnum.getDesc());
        titratorMethod.setMethodName("测试永停滴定");
        titratorMethod.setBuretteVolume(10);
        titratorMethod.setWorkingElectrode(WorkElectrodeEnnum.Calcium_Electrode);
        titratorMethod.setReferenceElectrode(10);
        titratorMethod.setTitrationDisplayUnit("1");
        titratorMethod.setReplenishmentSpeed("10");
        titratorMethod.setStiringSpeed("1");
        titratorMethod.setElectroedEquilibrationTime("1");
        titratorMethod.setElectroedEquilibriumPotential("1");
        titratorMethod.setPreStiringTime("1");
        titratorMethod.setPerAddVolume("1");
        titratorMethod.setEndVolume("20");
        titratorMethod.setStiringSpeed("1");
        titratorMethod.setSlowTitrationVolume("1");
        titratorMethod.setFastTitrationVolume("1");

        titratorMethod.setTitrationSpeed(1);
        titratorMethod.setPerAddVolume("10");
        titratorMethod.setReferenceElectrode(10);
        titratorMethod.setPreStiringTime("15");
        titratorMethod.setSampleMeasurementUnit("10");
        titratorMethod.setModifyTime(String.valueOf(System.currentTimeMillis()));
        titratorMethod.setUserName("admin");
        titratorMethod.setEndPointSettingList(Lists.newArrayList(getEndPointSetting()));
        titratorMethod.setTitratorEndPoints(Lists.newArrayList(getTitratorMethodEndPoint()));
        return titratorMethod;
    }

    public static EndPointSetting getEndPointSetting() {
        EndPointSetting endPointSetting = new EndPointSetting();
        endPointSetting.setBurette(4);
        endPointSetting.setReagentName("sss");
        endPointSetting.setReagentConcentrationUnit("11");
        endPointSetting.setReagentConcentration(1.1);
        endPointSetting.setAddVolume(1.2);
        endPointSetting.setAddTime(String.valueOf(System.currentTimeMillis()));
        endPointSetting.setAddSpeed(1);
        endPointSetting.setReferenceEndPoint(1);
        endPointSetting.setDelayTime(10);
        return endPointSetting;
    }

    public static TitratorEndPoint getTitratorMethodEndPoint() {
        TitratorEndPoint titratorEndPoint = new TitratorEndPoint();
        titratorEndPoint.setResultUnit("1");
        titratorEndPoint.setPreControlvalue(1.2);
        titratorEndPoint.setEndPointValue(1.2);
        titratorEndPoint.setCorrelationCoefficient(11);
        return titratorEndPoint;
    }
}
