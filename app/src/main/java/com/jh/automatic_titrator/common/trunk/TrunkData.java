package com.jh.automatic_titrator.common.trunk;

/**
 * Created by apple on 2016/12/14.
 */

public class TrunkData {
    private int dataType;

    private Object data;

    private int dataLength;

    private byte[] originData;

    public TrunkData(int dataType, Object data, int dataLength, byte[] originData) {
        this.dataType = dataType;
        this.data = data;
        this.dataLength = dataLength;
        this.originData = originData;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public byte[] getOriginData() {
        return originData;
    }

    public void setOriginData(byte[] originData) {
        this.originData = originData;
    }
}
