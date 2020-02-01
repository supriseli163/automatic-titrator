package com.jh.automatic_titrator.service;

import android.util.Log;

import com.jh.automatic_titrator.ui.BaseActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by apple on 2016/12/27.
 */

public class TimeService implements Runnable {

    private BaseActivity baseActivity;

    private boolean started;

    private Map<Integer, TimeListener> timeListenerMap;

    private static Map<BaseActivity, TimeService> timeServiceMap;

    private ReentrantLock reentrantLock = new ReentrantLock();

    private TimeService(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        timeListenerMap = new HashMap<>();
    }

    public static TimeService getInstance(BaseActivity baseActivity) {
        if (timeServiceMap == null) {
            timeServiceMap = new HashMap<>();
        }
        if (!timeServiceMap.containsKey(baseActivity)) {
            timeServiceMap.put(baseActivity, new TimeService(baseActivity));
        }
        TimeService timeService = timeServiceMap.get(baseActivity);
        if(!timeService.started) {
            timeService.start();
        }
        return timeService;
    }

    public void start() {
        ExecutorService.getInstance().execute(this);
    }

    public void stop() {
        this.started = false;
        timeServiceMap.remove(baseActivity);
    }

    @Override
    public void run() {
        started = true;
        while (started) {
            try {
                Thread.sleep(1000);
                final long currentTime = System.currentTimeMillis() + baseActivity.getTimeAdd();
                for (final TimeListener timeListener : timeListenerMap.values()) {
                    ExecutorService.getInstance().execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    timeListener.timeRefresh(currentTime);
                                }
                            }
                    );
                }
            } catch (Exception e) {
                //IGNORE
                Log.e("TimeService", e.getMessage());
            }
        }
    }

    public interface TimeListener {
        void timeRefresh(long currentTime);
    }

    public void addTimeListener(TimeListener timeListener, int id) {
        reentrantLock.lock();
        try {
            timeListenerMap.put(id, timeListener);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void removeTimeListener(int id) {
        reentrantLock.lock();
        try {
            timeListenerMap.remove(id);
        } finally {
            reentrantLock.unlock();
        }
    }
}
