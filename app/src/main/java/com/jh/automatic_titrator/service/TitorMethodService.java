package com.jh.automatic_titrator.service;

import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TitorMethodService {
    public static TitorMethodService getInstance() {
        return ourInstance;
    }

    private static TitorMethodService ourInstance = new TitorMethodService();

    private ReadWriteLock readWriteLock;

    private Map<Integer, TitorMethodChangeListener> titorMethodChangeListenerMap;


    public TitorMethodService() {
        titorMethodChangeListenerMap = new HashMap<>();
        readWriteLock = new ReentrantReadWriteLock();
    }

    public void addTitorMethodListneer(int id, TitorMethodChangeListener titorMethodChangeListener) {
        readWriteLock.writeLock().lock();
        try {
            titorMethodChangeListenerMap.put(id, titorMethodChangeListener);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

//    public List<TitratorMethod> list(String startDate, String ) {
//
//    }

    public void onChange(int fromKey) {
        readWriteLock.readLock().lock();
        try {
            for(final Map.Entry<Integer, TitorMethodChangeListener> entry : titorMethodChangeListenerMap.entrySet()) {
                //如果按的键和这个不同，执行中间代码；
                //如果相同，一直循环，跳不出for，就不能解锁
                if(entry.getKey().intValue() != fromKey) {
                    ExecutorService.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            //按下的键.id值。。。。。
                            entry.getValue().onChange();
                        }
                    });
                }
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public interface TitorMethodChangeListener {
        void onChange();
    }
}
