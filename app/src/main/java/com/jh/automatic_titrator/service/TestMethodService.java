package com.jh.automatic_titrator.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 2017/1/7.
 */
public class TestMethodService {

    private Map<Integer, TestMethodChangedListener> changedListenerMap;

    private ReadWriteLock reentrantLock;

    private static TestMethodService ourInstance = new TestMethodService();

    public static TestMethodService getInstance() {
        return ourInstance;
    }

    private TestMethodService() {
        changedListenerMap = new HashMap<>();
        reentrantLock = new ReentrantReadWriteLock();
    }

    public void addTestMethodListener(int id, TestMethodChangedListener listener) {
        reentrantLock.writeLock().lock();
        try {
            changedListenerMap.put(id, listener);
        } finally {
            reentrantLock.writeLock().unlock();
        }
    }

    public void removeListener(int id) {
        reentrantLock.writeLock().lock();
        try {
            changedListenerMap.remove(id);
        } finally {
            reentrantLock.writeLock().unlock();
        }
    }

    public void clearListener() {
        reentrantLock.writeLock().lock();
        try {
            changedListenerMap.clear();
        } finally {
            reentrantLock.writeLock().unlock();
        }
    }

    public void onChange(int fromKey) {
        reentrantLock.readLock().lock();
        try {
            for (final Map.Entry<Integer,TestMethodChangedListener> entry : changedListenerMap.entrySet()) {
                //如果按的键和这个不同，执行中间代码；
                //如果相同，一直循环，跳不出for，就不能解锁
                if (entry.getKey().intValue() != fromKey) {
                    ExecutorService.getInstance().execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    //按下的键.id值。。。。。
                                    entry.getValue().onChange();
                                }
                            }
                    );
                }
            }
        } finally {
            reentrantLock.readLock().unlock();
        }
    }

    public interface TestMethodChangedListener {
        void onChange();
    }
}
