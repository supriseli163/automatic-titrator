package com.jh.automatic_titrator.service;

import android.os.Environment;

import com.jh.automatic_titrator.common.file.FileHelper;
import com.jh.automatic_titrator.common.utils.JsonHelper;
import com.jh.automatic_titrator.entity.common.FactoryInitInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016/12/19.
 */

public class RecoverService {

    public static boolean existRecoverFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String basePath;
        if (path.endsWith("/")) {
            basePath = path + "hanon";
        } else {
            basePath = path + "/hanon";
        }
        File confFile = new File(basePath + "/conf/factory_init.conf");
        return confFile.exists();
    }

    public static FactoryInitInfo readRecoverInfo() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String basePath;
        if (path.endsWith("/")) {
            basePath = path + "hanon";
        } else {
            basePath = path + "/hanon";
        }
        File confFile = new File(basePath + "/conf/factory_init.conf");
        if (!confFile.exists()) {
            return null;
        } else {
            String str = FileHelper.readFile(confFile);
            FactoryInitInfo factoryInitInfo = JsonHelper.fromJson(str, FactoryInitInfo.class);
            return factoryInitInfo;
        }
    }

    public static void writeFactoryInfo(FactoryInitInfo factoryInitInfo) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String basePath;
        if (path.endsWith("/")) {
            basePath = path + "hanon";
        } else {
            basePath = path + "/hanon";
        }
        File confFile = new File(basePath + "/conf/factory_init.conf");
        FileHelper.writeFile(confFile, JsonHelper.toJson(factoryInitInfo));
    }

    public static void writeDefaultFactoryInfo() {
        FactoryInitInfo factoryInitInfo = new FactoryInitInfo();
        factoryInitInfo.setShowLogo(true);
        factoryInitInfo.setTemperature(0);
        List<Integer> waveLengths = new ArrayList<>();
        waveLengths.add(589);
        factoryInitInfo.setWaveLengths(waveLengths);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String basePath;
        if (path.endsWith("/")) {
            basePath = path + "hanon";
        } else {
            basePath = path + "/hanon";
        }
        File confFile = new File(basePath + "/conf/factory_init.conf");
        FileHelper.writeFile(confFile, JsonHelper.toJson(factoryInitInfo));
    }
}
