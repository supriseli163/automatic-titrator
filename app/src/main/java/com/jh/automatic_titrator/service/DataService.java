package com.jh.automatic_titrator.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2017/1/2.
 */

public class DataService {
    private static DataService ourInstance = new DataService();

    private Map<Integer, DataChangeListener> listeners;

    private DataService() {
        listeners = new HashMap<>();
    }

    public static DataService getInstance() {
        return ourInstance;
    }

    public void addListener(int id, DataChangeListener dataChangeListener) {
        listeners.put(id, dataChangeListener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void notifyChanged(final int type) {
        for (final DataChangeListener dataChangeListener : listeners.values()) {
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            dataChangeListener.onChanged(type);
                        }
                    }
            );
        }
    }

    public interface DataChangeListener {
        void onChanged(int type);
    }
}
