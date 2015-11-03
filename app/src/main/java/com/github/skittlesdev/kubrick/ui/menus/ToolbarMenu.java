package com.github.skittlesdev.kubrick.ui.menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import com.github.skittlesdev.kubrick.AboutActivity;
import com.github.skittlesdev.kubrick.ProfileActivity;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.SearchActivity;

public class ToolbarMenu {
    private Context context;

    public ToolbarMenu(Context context) {
        this.context = context;
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
}
