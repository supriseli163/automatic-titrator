package com.jh.automatic_titrator.service;

import com.jh.automatic_titrator.entity.common.TestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2017/1/2.
 */
public class MethodService {
    private static MethodService ourInstance = new MethodService();

    public static MethodService getInstance() {
        return ourInstance;
    }

    private Map<Integer, MethodChangeListener> listeners;

    private MethodService() {
        listeners = new HashMap<>();
    }

    public void addListener(int id, MethodChangeListener methodChangeListener) {
        listeners.put(id, methodChangeListener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void notifyChanged(final TestMethod testMethod, final int type) {
        for (final MethodChangeListener methodChangeListener : listeners.values()) {
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            methodChangeListener.onChanged(testMethod, type);
                        }
                    }
            );
        }
    }

    public interface MethodChangeListener {
        public void onChanged(TestMethod testMethod, int type);
    }
}
