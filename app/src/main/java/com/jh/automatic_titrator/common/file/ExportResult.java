package com.jh.automatic_titrator.common.file;

/**
 * Created by apple on 2016/11/19.
 */
public interface ExportResult {
    void failed(String msg);

    void success();
}
