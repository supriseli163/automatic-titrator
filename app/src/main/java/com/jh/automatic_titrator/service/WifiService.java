package com.jh.automatic_titrator.service;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class WifiService implements Runnable {
    private volatile static boolean connected = false;
    private static Map<Activity, WifiService> wifiServiceMap;
    private Activity activity;
    private boolean started;
    private Map<Integer, WifiListener> wifiListenerMap;
    private ConnectivityManager mConnectivityManager;


    private ReentrantLock reentrantLock = new ReentrantLock();

    private WifiService(Activity activity) {
        this.activity = activity;
        mConnectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiListenerMap = new HashMap<>();
    }

    public static WifiService getInstance(Activity activity) {
        if (wifiServiceMap == null) {
            wifiServiceMap = new HashMap<>();
        }
        if (!wifiServiceMap.containsKey(activity)) {
            wifiServiceMap.put(activity, new WifiService(activity));
        }
        WifiService wifiService = wifiServiceMap.get(activity);
        if (!wifiService.started) {
            wifiService.start();
        }
        return wifiService;
    }

    public void start() {
        ExecutorService.getInstance().execute(this);
    }

    public void stop() {
        this.started = false;
        wifiServiceMap.remove(activity);
    }

    @Override
    public void run() {
        started = true;
        while (started) {
            try {
                Thread.sleep(5000);
                final boolean currentConnected = isWifiConned();
                connected = currentConnected;
                for (final WifiListener wifiListener : wifiListenerMap.values()) {
                    ExecutorService.getInstance().execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    wifiListener.onChange(currentConnected);
                                }
                            }
                    );
                }
            } catch (Exception e) {
                //IGNORE
                Log.e("WifiService", e.getMessage());
            }
        }
    }

    private boolean isWifiConned() {
        NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    public void addWifiListener(WifiListener wifiListener, int id) {
        reentrantLock.lock();
        try {
            wifiListenerMap.put(id, wifiListener);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void removeWifiListener(int id) {
        reentrantLock.lock();
        try {
            wifiListenerMap.remove(id);
        } finally {
            reentrantLock.unlock();
        }
    }

    public interface WifiListener {
        void onChange(boolean connected);
    }
}
