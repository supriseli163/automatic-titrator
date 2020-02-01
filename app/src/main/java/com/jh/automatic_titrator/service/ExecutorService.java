package com.jh.automatic_titrator.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by apple on 2016/12/31.
 */

public class ExecutorService {
    private static ExecutorService Instance;

    private ThreadPoolExecutor threadPoolExecutor;

    private ExecutorService() {
        threadPoolExecutor = new ThreadPoolExecutor(20,50,3600, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public static ExecutorService getInstance() {
        if(Instance == null) {
            Instance = new ExecutorService();
        }
        return Instance;
    }

    //有返回值
    public <V> Future<V> submit(Callable<V> callable) {
        return threadPoolExecutor.submit(callable);
    }

    //无返回值
    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }
}
