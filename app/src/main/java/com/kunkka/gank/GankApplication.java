package com.kunkka.gank;

import android.app.Application;

/**
 * @author fengdianzhang
 * @version 1.0
 */

public class GankApplication extends Application {
    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Application getApplication() {
        return sApplication;
    }
}
