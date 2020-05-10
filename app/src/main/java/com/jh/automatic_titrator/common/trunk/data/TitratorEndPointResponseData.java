package com.jh.automatic_titrator.common.trunk.data;

import lombok.Data;

@Data
public class TitratorEndPointResponseData {
    //主控板地址
    private int moduleAddress;
    //第几个终点
    private int endPointNum;
    //终点体积值对
    private int endPointVolume;
}
