package com.jh.automatic_titrator;

import android.app.Application;

import com.jh.automatic_titrator.factory.FunctionFactory;

import androidx.multidex.MultiDex;

public class BaseApplication extends Application {

    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MultiDex.install(this);
        FunctionFactory.registerDataCallBack();
    }
}