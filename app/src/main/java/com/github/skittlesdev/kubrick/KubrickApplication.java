package com.github.skittlesdev.kubrick;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import de.greenrobot.event.EventBus;
import net.danlew.android.joda.JodaTimeAndroid;

public class KubrickApplication extends MultiDexApplication {
    private static Context sContext;
    private static EventBus eventBus;

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        KubrickApplication application = (KubrickApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        JodaTimeAndroid.init(this);
        Fresco.initialize(this);
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