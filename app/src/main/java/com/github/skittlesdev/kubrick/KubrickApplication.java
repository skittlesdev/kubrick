package com.github.skittlesdev.kubrick;

import android.app.Application;
import android.content.Context;
import com.parse.Parse;
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

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "rej9XtKMoEPuEkULwnROypntYF5Bg9aKEQhQgYtr", "6kyMBoKJgKsMyQB72IvUWVq1UBZSJsXArxBIvMy3");
    }

    public static Context getContext() {
        return KubrickApplication.sContext;
    }
}