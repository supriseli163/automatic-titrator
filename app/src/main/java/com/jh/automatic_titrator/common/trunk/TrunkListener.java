package com.jh.automatic_titrator.common.trunk;

/**
 * Created by apple on 2016/12/14.
 */

public interface TrunkListener {

    public int getListenType();

    public void notifyData(TrunkData trunkData);
}
