package com.jh.automatic_titrator.ui.chat.test;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class ThreadTest {
    private static ThreadTest myHandler;
    private HandlerTest handlerTest;
    private Handler mSubThreadHandler;

    public static ThreadTest getInstance() {
        if (myHandler == null) {
            myHandler = new ThreadTest();
        }
        return myHandler;
    }

    private ThreadTest() {
        this.handlerTest = new HandlerTest("MyHandler");
    }

    public void start(OnDataOperateListener listener) {
        handlerTest.start();
        Looper looper = handlerTest.getLooper();
        mSubThreadHandler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (listener != null) {
                            listener.onDataChange();
                        }
                        mSubThreadHandler.sendEmptyMessageDelayed(1, 500);
                        break;
                }
            }
        };
        mSubThreadHandler.sendEmptyMessageDelayed(1, 50);
    }

    public class HandlerTest extends HandlerThread {

        public HandlerTest(String name) {
            super(name);
        }
    }

    public interface OnDataOperateListener {
        void onDataChange();
    }
}