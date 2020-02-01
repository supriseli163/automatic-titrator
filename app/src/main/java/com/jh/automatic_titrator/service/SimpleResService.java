package com.jh.automatic_titrator.service;

import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.entity.common.SimpleRes;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2017/1/14.
 */
public class SimpleResService {
    private static SimpleResService ourInstance;

    private List<SimpleRes> simpleRess;

    private int maxValueSize;

    private Map<Integer, SimpleResChangeListener> listeners;

    public static SimpleResService getInstance(int maxValueSize) {
        if (ourInstance == null) {
            ourInstance = new SimpleResService(maxValueSize);
        } else {
            ourInstance.maxValueSize = maxValueSize;
        }
        return ourInstance;
    }

    private SimpleResService(int maxValueSize) {
        simpleRess = new LinkedList<>();
        this.maxValueSize = maxValueSize;
    }

    public void addValue(float value) {
        SimpleRes simpleRes = new SimpleRes();
        simpleRes.setTime(Cache.currentTime());
        simpleRes.setRes(value);
        simpleRess.add(simpleRes);
        if (simpleRess.size() > maxValueSize) {
            simpleRess.remove(0);
        }
        if (listeners != null && listeners.size() > 0) {
            for (final SimpleResChangeListener simpleResChangeListener : listeners.values()) {
                ExecutorService.getInstance().execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                simpleResChangeListener.onChange(simpleRess);
                            }
                        }
                );
            }
        }
    }

    public void clean() {
        simpleRess.clear();
    }

    public void addListener(int code, SimpleResChangeListener simpleResChangeListener) {
        if (listeners == null) {
            listeners = new LinkedHashMap<>();
        }
        listeners.put(code, simpleResChangeListener);
    }

    public void removeListener(int code) {
        listeners.remove(code);
    }

    public interface SimpleResChangeListener {
        void onChange(final List<SimpleRes> simpleRes);
    }
}
