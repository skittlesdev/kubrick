package com.github.skittlesdev.kubrick;

import android.app.Application;
import android.content.Context;
import com.parse.Parse;
import de.greenrobot.event.EventBus;
import net.danlew.android.joda.JodaTimeAndroid;

public class KubrickApplication extends Application {
    private static Context sContext;
    private static EventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        KubrickApplication.sContext = this.getApplicationContext();
        this.eventBus = new EventBus();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "rej9XtKMoEPuEkULwnROypntYF5Bg9aKEQhQgYtr", "6kyMBoKJgKsMyQB72IvUWVq1UBZSJsXArxBIvMy3");
    }

    public static Context getContext() {
        return KubrickApplication.sContext;
    }

    public static EventBus getEventBus() {
        return eventBus;
    }
}