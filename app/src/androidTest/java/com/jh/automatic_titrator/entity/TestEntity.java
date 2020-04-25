package com.jh.automatic_titrator.entity;

import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum;

public class TestEntity {
    public static TitratorMethod getTitratorMethod() {
        TitratorMethod titratorMethod = new TitratorMethod();
        titratorMethod.setTitratorType(TitratorTypeEnum.DynamicTitrator.getName());
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

        titratorMethod.setPerAddVolume("10");
        titratorMethod.setReferenceElectrode(10);
        titratorMethod.setPreStiringTime("15");
        titratorMethod.setSampleMeasurementUnit("10");
        titratorMethod.setModifyTime();
        titratorMethod.setUserName();
        return titratorMethod;
    }
}
