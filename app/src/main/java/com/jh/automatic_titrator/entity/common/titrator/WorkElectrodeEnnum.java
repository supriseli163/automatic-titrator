package com.jh.automatic_titrator.entity.common.titrator;

/**
 * 工作电极枚举
 */
public enum  WorkElectrodeEnnum {
    PH_Electrode("PH_Electrode", "PH 电极"),
    PH_Mixed_Electrode("PH_Mixed_Electrode", "PH复合电极"),
    Platinum_Electrode("Platinum_Electrode", "铂电极"),
    Silver_Electrode("Silver_Electrode", "银电极"),
    Calcium_Electrode("Calcium_Electrode", "钙电极"),
    Fluoride_Electrode("Fluoride_Electrode", "氟电极"),
    Copper_Electrode("Copper_Electrode", "铜电极");

    private String desc;
    private String name;

    private WorkElectrodeEnnum(String desc, String name) {
        this.desc = desc;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
