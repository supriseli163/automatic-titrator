package com.jh.automatic_titrator.common.db;

import android.content.Context;

import com.jh.automatic_titrator.common.utils.JsonHelper;
import com.jh.automatic_titrator.common.utils.SharedPreferenceUtils;
import com.jh.automatic_titrator.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2016/10/16.
 */
public class WaveLengthHelper {

    private Context context;

    public WaveLengthHelper(Context context) {
        this.context = context;
    }

    public Map<String, Integer> getWaveLengths() {
        String json = (String) SharedPreferenceUtils.get(context, "waveLengths", getFactoryWaveLengthStr());
        Map<String, Integer> res = new LinkedHashMap<>();

        if (json != null && json.length() > 0) {
            try {
                String[] array = JsonHelper.fromJson(json, String[].class);
                for (int i = 0; i < array.length; i++) {
                    if (StringUtils.isNotEmpty(array[i])) {
                        res.put(array[i], i);
                    }
                }
            } catch (Exception e) {
                //DONOTHING
            }
        }
        return res;
    }

    public Map<Integer, String> getWaveLengthMap() {
        String json = (String) SharedPreferenceUtils.get(context, "waveLengths", getFactoryWaveLengthStr());
        Map<Integer, String> res = new LinkedHashMap<>();

        if (json != null && json.length() > 0) {
            try {
                String[] array = JsonHelper.fromJson(json, String[].class);
                for (int i = 0; i < array.length; i++) {
                    if (StringUtils.isNotEmpty(array[i])) {
                        res.put(i, array[i]);
                    }
                }
            } catch (Exception e) {
                //DONOTHING
            }
        }
        return res;
    }

    public List<String> getWaveLengthStrs() {
        String json = (String) SharedPreferenceUtils.get(context, "waveLengths", getFactoryWaveLengthStr());
        List<String> waveLengthStrs = new ArrayList<>();
        if (json != null && json.length() > 0) {
            try {
                String[] array = JsonHelper.fromJson(json, String[].class);
                for (String item : array) {
                    if (item != null && item.length() > 0) {
                        waveLengthStrs.add(item);
                    }
                }
            } catch (Exception e) {
                //DONOTHING
            }
        }
        return waveLengthStrs;
    }

    public String getFactoryWaveLengthStr() {
        return (String) SharedPreferenceUtils.get(context, "initWaveLengths", "[\"589\"]");
    }

    public void refreshWaveLength(String[] waveLengths) {
        String json = JsonHelper.toJson(waveLengths);
        SharedPreferenceUtils.put(context, "waveLengths", json);
    }

    public void coverInitWaveLength(String[] waveLengths) {
        String json = JsonHelper.toJson(waveLengths);
        SharedPreferenceUtils.put(context, "waveLengths", json);
        SharedPreferenceUtils.put(context, "initWaveLengths", json);
    }

    public void recoverInitWaveLength() {
        SharedPreferenceUtils.put(context, "waveLengths", getFactoryWaveLengthStr());
    }
}
