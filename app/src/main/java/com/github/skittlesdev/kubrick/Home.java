package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.os.Bundle;

import com.github.skittlesdev.kubrick.async.TestApiTask;

public class Home extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_home);

        String apiKey = "0d1d0cc3c4aec9ca1c2c8c9e781a7ef1";
        TestApiTask testApiTask = new TestApiTask(this);
        testApiTask.execute(apiKey);
    }
}