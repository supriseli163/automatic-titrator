package com.jh.automatic_titrator.entity.common.titrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 进样器枚举
 * 单头 & 多头进样器
 */
@AllArgsConstructor
public enum  SampleTypeEnum {

    ONE("单头", 1),
    Muilite("多头", 2);

    @Getter
    private String description;
    @Getter
    private int code;
}
