package com.jh.automatic_titrator.common.trunk.data;

import lombok.Data;

@Data
public class CleanStatusResponseData {
    //清洗模块地址
    private int moduleAddress;
    //已清洗次数
    private int cleanCount;
}
