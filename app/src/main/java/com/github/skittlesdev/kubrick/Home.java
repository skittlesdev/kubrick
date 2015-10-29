package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.github.skittlesdev.kubrick.receivers.TmdbApiBroadcastReceiver;
import com.github.skittlesdev.kubrick.services.TmdbApiCallService;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentHome;
import com.github.skittlesdev.kubrick.utils.Constants;

public class Home extends Activity {
    /*private Intent mTmdbApiCallService;
    private TmdbApiBroadcastReceiver mTmdbApiBroadcastReceiver;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_home);

        String apiKey = "0d1d0cc3c4aec9ca1c2c8c9e781a7ef1";

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FragmentHome fragmentHome = new FragmentHome(apiKey);

        /*this.mTmdbApiCallService = new Intent(this, TmdbApiCallService.class);
        this.mTmdbApiCallService.putExtra(Constants.Intent.INTENT_API_KEY, apiKey);

        this.mTmdbApiBroadcastReceiver = new TmdbApiBroadcastReceiver(fragmentHome);
        this.registerReceiver(this.mTmdbApiBroadcastReceiver, new IntentFilter(Constants.Intent.ACTION_NEW_DATA));*/

        fragmentHome.onAttach(this);
        transaction.add(R.id.homeContainer, fragmentHome);
        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //this.startService(this.mTmdbApiCallService);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //this.stopService(this.mTmdbApiCallService);
    }
}