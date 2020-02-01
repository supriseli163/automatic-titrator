package com.jh.automatic_titrator.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2017/1/2.
 */
public class WaveLengthService {
    private static WaveLengthService ourInstance = new WaveLengthService();

    public static WaveLengthService getInstance() {
        return ourInstance;
    }

    private Map<Integer, WaveLengthChangeListener> listeners;

    private WaveLengthService() {
        listeners = new HashMap<>();
    }

    public void addListener(int id, WaveLengthChangeListener waveLengthChangeListener) {
        listeners.put(id, waveLengthChangeListener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void notifyChanged() {
        for (final WaveLengthChangeListener waveLengthChangeListener : listeners.values()) {
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            waveLengthChangeListener.onChanged();
                        }
                    }
            );
        }
    }

    public interface WaveLengthChangeListener {
        void onChanged();
    }
}
