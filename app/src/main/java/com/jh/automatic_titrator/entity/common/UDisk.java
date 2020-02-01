package com.jh.automatic_titrator.entity.common;

/**
 * Created by apple on 2017/1/15.
 */

public class UDisk {
    private String filePath;

    public UDisk() {
    }

    public UDisk(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
