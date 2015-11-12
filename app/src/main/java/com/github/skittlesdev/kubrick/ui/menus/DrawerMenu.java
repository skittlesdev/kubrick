package com.github.skittlesdev.kubrick.ui.menus;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.github.skittlesdev.kubrick.*;
import com.github.skittlesdev.kubrick.adapters.HomeDrawerAdapter;
import com.github.skittlesdev.kubrick.events.LoginEvent;
import com.github.skittlesdev.kubrick.events.LogoutEvent;
import com.github.skittlesdev.kubrick.utils.Callback;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.github.skittlesdev.kubrick.utils.RowElement;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DrawerMenu {
    private Activity activity;
    private DrawerLayout layout;
    private RecyclerView view;

    public DrawerMenu(Activity activity, DrawerLayout layout, RecyclerView view) {
        this.activity = activity;
        this.layout = layout;
        this.view = view;

        // KubrickApplication.getEventBus().register(this);
    }

    public void draw() {
        view.setHasFixedSize(true); // for a better performance
        HomeDrawerAdapter mHomeDrawerAdapter = new HomeDrawerAdapter(this.activity, generateTitles(), generateProfile());

        this.view.setAdapter(mHomeDrawerAdapter);
        this.view.setLayoutManager(new LinearLayoutManager(activity));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this.activity, this.layout, R.string.openDrawer, R.string.closeDrawer) {
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

        //toggle.setDrawerIndicatorEnabled(false);
        // this.layout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private List<RowElement> generateTitles() {
        List<RowElement> titles = new ArrayList<>();

        RowElement loginElement = new RowElement(R.drawable.ic_row_element, "Login", new Callback(this.activity) {
            @Override
            public void execute() {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
            }
        });

        RowElement signupElement = new RowElement(R.drawable.ic_row_element, "Signup", new Callback(this.activity) {
            @Override
            public void execute() {
                Intent intent = new Intent(getContext(), SignupActivity.class);
                getContext().startActivity(intent);
            }
        });

        RowElement favoritesElement = new RowElement(R.drawable.ic_row_element, "Favorites", new Callback(this.activity) {
            @Override
            public void execute() {
                Intent intent = new Intent(getContext(), FavoritesActivity.class);
                getContext().startActivity(intent);
            }
        });

        RowElement timelineElement = new RowElement(R.drawable.ic_row_element, "Timeline", new Callback(this.activity) {
            @Override
            public void execute() {
                Intent intent = new Intent(getContext(), TimelineActivity.class);
                getContext().startActivity(intent);
            }
        });

        RowElement followersElement = new RowElement(R.drawable.ic_row_element, "Followers", new Callback(this.activity) {
            @Override
            public void execute() {
                Intent intent = new Intent(getContext(), ProfileRelationsActivity.class);
                intent.putExtra("user_id", ParseUser.getCurrentUser().getObjectId());
                intent.putExtra("type", ProfileRelationsActivity.Types.FOLLOWERS);
                getContext().startActivity(intent);
            }
        });

        RowElement followingsElement = new RowElement(R.drawable.ic_row_element, "Following", new Callback(this.activity) {
            @Override
            public void execute() {
                Intent intent = new Intent(getContext(), ProfileRelationsActivity.class);
                intent.putExtra("user_id", ParseUser.getCurrentUser().getObjectId());
                intent.putExtra("type", ProfileRelationsActivity.Types.FOLLOWINGS);
                getContext().startActivity(intent);
            }
        });

        RowElement logoutElement = new RowElement(R.drawable.ic_row_element, "Logout", new Callback(this.activity) {
            @Override
            public void execute() {
                ParseUser.logOut();
                KubrickApplication.getEventBus().post(new LogoutEvent());
                DrawerLayout drawer = (DrawerLayout) ((Activity) getContext()).findViewById(R.id.homeDrawerLayout);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        if (ParseUser.getCurrentUser() == null) {
            titles.add(loginElement);
            titles.add(signupElement);
        }
        else {
            titles.add(timelineElement);
            titles.add(favoritesElement);
            titles.add(followersElement);
            titles.add(followingsElement);
            titles.add(logoutElement);
        }

        return titles;
    }

    private ProfileElement generateProfile() {
        ProfileElement profileElement;

        if (ParseUser.getCurrentUser() == null) {
            profileElement = new ProfileElement();
        }
        else {
            profileElement = new ProfileElement(ParseUser.getCurrentUser());
        }

        return profileElement;
    }

    public void onEvent(LoginEvent event) {
        draw();
    }

    public void onEvent(LogoutEvent event) {
        draw();
    }
}
