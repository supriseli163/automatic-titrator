package com.jh.automatic_titrator.factory;

import com.jh.automatic_titrator.common.trunk.CommandEnum;
import com.jh.automatic_titrator.common.trunk.DataFrame;
import com.jh.automatic_titrator.common.trunk.TitratorTrunkListener;
import com.jh.automatic_titrator.common.trunk.TitratorTrunkUtil;

public class FunctionFactory {

    public static void registerDataCallBack() {
        TitratorTrunkUtil.getInstance().addListener(new TitratorTrunkListener() {

            @Override
            public CommandEnum getEventType() {
                return CommandEnum.START_TITRATOR;
            }

            @Override
            public void notifyData(DataFrame dataFrame) {

            }
        }, 1);
    }

    public static void start() {
//        TitratorParamsBean bean = Cache.bean;
//        TitratorTrunkUtil.getInstance().sendCmd(bean);
    }

    public static void stop() {
//        TitratorParamsBean bean = Cache.bean;
//        TitratorTrunkUtil.getInstance().sendCmd(bean);
    }
}