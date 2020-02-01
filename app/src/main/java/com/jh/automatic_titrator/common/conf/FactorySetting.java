package com.jh.automatic_titrator.common.conf;

import android.os.Environment;

import java.io.File;

/**
 * Created by apple on 2016/12/11.
 */

public class FactorySetting {
    public static void ensureFirstInit() {
        new Thread() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                String basePath;
                if (path.endsWith("/")) {
                    basePath = path + "hanon";
                } else {
                    basePath = path + "/hanon";
                }
                File file = new File(basePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File dataFile = new File(basePath + "/data");
                if (!dataFile.exists()) {
                    dataFile.mkdirs();
                }
                File confFile = new File(basePath + "/conf");
                if (!confFile.exists()) {
                    confFile.mkdirs();
                }
            }
        }.start();
    }
}
