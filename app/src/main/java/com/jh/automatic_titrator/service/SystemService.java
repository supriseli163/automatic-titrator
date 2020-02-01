package com.jh.automatic_titrator.service;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import com.jh.automatic_titrator.entity.common.UDisk;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/1/15.
 */

public class SystemService {

    public static final String SDCARD = "/mnt/external_sd";
    public static final String SDCARD1 = "/mnt/external_sd/";
    private static final String EXENRNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static List<UDisk> getUDisksOld(Context context) {
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method method = StorageManager.class.getMethod("getVolumePaths");
            String[] res = (String[]) method.invoke(storageManager);
            if (res != null && res.length > 0) {
                List<UDisk> uDisks = new ArrayList<>();
                for (String diskPath : res) {
                    if (!diskPath.equals(EXENRNAL_PATH) &&
                            !diskPath.equals("SDCARD") &&
                            !diskPath.equals(SDCARD1) &&
                            !diskPath.equals(SDCARD)) {
                        File file = new File(diskPath + "/udisk0/");
                        if (file.exists()) {
                            uDisks.add(new UDisk(diskPath));
                        }
                    }
                }
                return uDisks;
            }
        } catch (Exception e) {
            Log.e("U盘检测", "检测U盘失败");
        }
        return null;
    }

    public static List<UDisk> getUDisksNew(Context context) {
        List<UDisk> udisks = getUDisksNew1(context);
        if (udisks == null || udisks.size() == 0) {
            udisks = getUDisksOld(context);
        }
        return udisks;
    }

    public static List<UDisk> getUDisksNew1(Context context) {
        List<UDisk> udisks = new ArrayList<UDisk>();

        try {
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method method = StorageManager.class.getMethod("getVolumePaths");
            String[] res = (String[]) method.invoke(storageManager);
            if (res != null && res.length > 0) {
                for (String diskPath : res) {
                    if (!diskPath.equals(EXENRNAL_PATH) &&
                            !diskPath.equals("SDCARD") &&
                            !diskPath.equals(SDCARD1) &&
                            !diskPath.equals(SDCARD)) {
                        File file = new File(diskPath);
                        if (file.exists()) {
                            if (file.exists()) {
                                File[] children = file.listFiles();
                                if (children != null && children.length > 0) {
                                    for (File child : children) {
                                        if (child.isDirectory() && child.getName().toLowerCase().startsWith("udisk")) {
                                            udisks.add(new UDisk(child.getAbsolutePath() + "/"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("U盘检测", "检测U盘失败");
        }

        return udisks;
    }
}
