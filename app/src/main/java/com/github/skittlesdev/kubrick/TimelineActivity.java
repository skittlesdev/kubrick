package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.github.skittlesdev.kubrick.ui.fragments.TimelineFragment;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;

public class TimelineActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.timeline, new TimelineFragment());
        transaction.commit();
    }
}
