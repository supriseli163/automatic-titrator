package com.jh.automatic_titrator.entity.common.titrator;

/**
 * 滴定类型枚举
 */
public enum  TitratorTypeEnum {

    EqualTitrator("EqualTitrator", "等量滴定"),
    DynamicTitrator("DynamicTitrator", "动态滴定"),
    ManualTitrator("ManualTitrator", "手动滴定"),
    EndPointTitrator("EndPointTitrator", "终点滴定"),
    StopForverTitrator("StopForverTitrator", "永停滴定");

    private String desc;
    private String name;

    private TitratorTypeEnum(String desc, String name) {
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
