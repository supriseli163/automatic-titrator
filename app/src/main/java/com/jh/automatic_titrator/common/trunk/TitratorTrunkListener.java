package com.jh.automatic_titrator.common.trunk;

import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;

public interface TitratorTrunkListener {
    public CommandEnum getEventType();

    public void notifyData(DataFrame dataFrame);
}
