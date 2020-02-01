package com.jh.automatic_titrator.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2017/1/2.
 */
public class FormulaService {
    private static FormulaService ourInstance = new FormulaService();

    public static FormulaService getInstance() {
        return ourInstance;
    }

    private Map<Integer, FormulaChangeListener> listeners;

    private FormulaService() {
        listeners = new HashMap<>();
    }

    public void addListener(int id, FormulaChangeListener formulaChangeListener) {
        listeners.put(id, formulaChangeListener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void notifyChanged() {
        for (final FormulaChangeListener formulaChangeListener : listeners.values()) {
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            formulaChangeListener.onChanged();
                        }
                    }
            );
        }
    }

    public interface FormulaChangeListener {
        public void onChanged();
    }
}
