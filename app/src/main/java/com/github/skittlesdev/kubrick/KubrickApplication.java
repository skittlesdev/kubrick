package com.github.skittlesdev.kubrick;

import android.app.Application;
import android.content.Context;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by lowgr on 10/29/2015.
 */
public class KubrickApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        KubrickApplication.sContext = this.getApplicationContext();
    }

    public static Context getContext() {
        return KubrickApplication.sContext;
    }
}