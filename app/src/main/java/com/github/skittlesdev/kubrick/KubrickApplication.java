package com.github.skittlesdev.kubrick;

import android.app.Application;
import android.content.Context;

/**
 * Created by lowgr on 10/29/2015.
 */
public class KubrickApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        KubrickApplication.sContext = this.getApplicationContext();
    }

    public static Context getContext() {
        return KubrickApplication.sContext;
    }
}