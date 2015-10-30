package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.skittlesdev.kubrick.adapters.HomeDrawerAdapter;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentHome;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.github.skittlesdev.kubrick.utils.RowElement;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mHomeDrawerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_home);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        FragmentHome fragmentHome = new FragmentHome();

        transaction.add(R.id.homeContainer, fragmentHome);
        transaction.commit();

        this.mToolbar = (Toolbar) this.findViewById(R.id.toolBar);
        this.setActionBar(this.mToolbar);

        this.mRecyclerView = (RecyclerView) findViewById(R.id.homeRecyclerView);
        this.mRecyclerView.setHasFixedSize(true); // for a better performance
        this.mHomeDrawerAdapter = new HomeDrawerAdapter(this.generateTitles(), this.generateProfile());

        this.mRecyclerView.setAdapter(this.mHomeDrawerAdapter);
        this.mLayoutManager = new LinearLayoutManager(this);
        this.mRecyclerView.setLayoutManager(mLayoutManager);

        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.homeDrawerLayout);
        //this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, R.drawable.ic_launcher,  R.string.openDrawer, R.string.closeDrawer){
        this.mDrawerToggle = new ActionBarDrawerToggle(this, this. mDrawerLayout, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code to execute when the drawer is opened
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // code to execute when the drawer is closed
            }
        };

        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
        this.mDrawerToggle.syncState();
    }

    private List<RowElement> generateTitles() {
        List<RowElement> titles = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            titles.add(new RowElement(R.drawable.ic_row_element, "Menu " + i));
        }

        return titles;
    }

    private ProfileElement generateProfile() {
        ProfileElement profileElement = new ProfileElement(R.drawable.default_profile_avatar, "Kubrick", "kubrick@kubrick.com");

        return profileElement;
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
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
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