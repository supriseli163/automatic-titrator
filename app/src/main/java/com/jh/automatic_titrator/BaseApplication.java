package com.jh.automatic_titrator;

import android.app.Application;

public class BaseApplication extends Application {

    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}