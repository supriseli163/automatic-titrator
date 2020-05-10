package com.jh.automatic_titrator.common.trunk;

import com.jh.automatic_titrator.common.utils.HexUtil;

import org.apache.commons.codec.binary.Hex;

public class DataFrameUtil {

    public String convertDataframeToByteString(DataFrame dataFrame) {
        if(dataFrame == null) return "";
        byte[] result = new byte[HexUtil.getInt(dataFrame.getSize())];


        return HexUtil.toHexString(result);

    }
}
