package com.github.skittlesdev.kubrick;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import com.parse.Parse;
import com.parse.ParseInstallation;
import de.greenrobot.event.EventBus;
import net.danlew.android.joda.JodaTimeAndroid;

public class KubrickApplication extends MultiDexApplication {
    private static Context sContext;
    private static EventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        KubrickApplication.sContext = this.getApplicationContext();
        this.eventBus = new EventBus();

        Parse.initialize(this, "rej9XtKMoEPuEkULwnROypntYF5Bg9aKEQhQgYtr", "6kyMBoKJgKsMyQB72IvUWVq1UBZSJsXArxBIvMy3");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static Context getContext() {
        return KubrickApplication.sContext;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }
}