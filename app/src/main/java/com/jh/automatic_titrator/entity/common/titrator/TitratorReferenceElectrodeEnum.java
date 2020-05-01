package com.jh.automatic_titrator.entity.common.titrator;

public enum TitratorReferenceElectrodeEnum {

    DYQGG_Electrode("DYQGG_Electrode");

    private String desc;

    TitratorReferenceElectrodeEnum(String desc) {
        this.desc = desc;
    }

    public static WorkElectrodeEnnum fromDesc(String desc) {
        for (WorkElectrodeEnnum workElectrodeEnnum : WorkElectrodeEnnum.values()) {
            if (workElectrodeEnnum.getDesc().equals(desc)) {
                return workElectrodeEnnum;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}