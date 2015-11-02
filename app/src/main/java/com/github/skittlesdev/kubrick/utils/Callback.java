package com.github.skittlesdev.kubrick.utils;

import android.content.Context;

public abstract class Callback {
    private Context context;

    public Callback(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return this.context;
    }

    public abstract void execute();
}
