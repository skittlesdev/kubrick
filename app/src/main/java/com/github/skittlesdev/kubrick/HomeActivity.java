package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.github.skittlesdev.kubrick.ui.fragments.FragmentHome;
import com.github.skittlesdev.kubrick.utils.Constants;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_home);

        String apiKey = "0d1d0cc3c4aec9ca1c2c8c9e781a7ef1";

        Bundle bundle = new Bundle();
        bundle.putString(Constants.Intent.INTENT_API_KEY, apiKey);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        FragmentHome fragmentHome = new FragmentHome();
        fragmentHome.setArguments(bundle);

        transaction.add(R.id.homeContainer, fragmentHome);
        transaction.commit();
    }
}