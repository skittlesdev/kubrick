package com.github.skittlesdev.kubrick.ui.menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import com.github.skittlesdev.kubrick.*;
import com.github.skittlesdev.kubrick.events.LoginEvent;
import com.github.skittlesdev.kubrick.events.LogoutEvent;
import com.parse.ParseUser;

public class ToolbarMenu {
    private Context context;
    private MenuItem userItem;

    public ToolbarMenu(Context context) {
        this.context = context;
        KubrickApplication.getEventBus().register(this);
    }

    public void filterItems(Menu menu) {
        this.userItem = menu.findItem(R.id.action_user);
        if (ParseUser.getCurrentUser() == null) {
            this.userItem.setVisible(false);
        }
    }

    public void itemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            context.startActivity(new Intent(context, SearchActivity.class));
        }

        if (item.getItemId() == android.R.id.home) {
            DrawerLayout drawer = (DrawerLayout) ((Activity) context).findViewById(R.id.homeDrawerLayout);
            drawer.openDrawer(Gravity.LEFT);
        }

        if (item.getItemId() == R.id.action_user) {
            context.startActivity(new Intent(context, ProfileActivity.class));
        }

        if (item.getItemId() == R.id.action_about) {
            context.startActivity(new Intent(context, AboutActivity.class));
        }
    }

    public void onEvent(LoginEvent event) {
        this.userItem.setVisible(true);
    }

    public void onEvent(LogoutEvent event) {
        this.userItem.setVisible(false);
    }
}
