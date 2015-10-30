package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.skittlesdev.kubrick.ui.fragments.FragmentHome;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_home);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        FragmentHome fragmentHome = new FragmentHome();

        transaction.add(R.id.homeContainer, fragmentHome);
        transaction.commit();

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.tool_bar);
        this.setActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        String tmpStringToShowItWorks;

        switch (id) {
            case R.id.action_search:
                tmpStringToShowItWorks = "Search";
                break;
            case R.id.action_user:
                tmpStringToShowItWorks = "User";
                break;
            case R.id.action_settings:
                tmpStringToShowItWorks = "Settings";
                break;
            case R.id.action_about:
                tmpStringToShowItWorks = "About";
                break;
            case R.id.action_help:
                tmpStringToShowItWorks = "Help";
                break;
            case R.id.action_refresh:
                tmpStringToShowItWorks = "Refresh";
                break;
            default:
                tmpStringToShowItWorks = "WTF?";
                break;
        }

        Toast.makeText(this, tmpStringToShowItWorks, Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
}