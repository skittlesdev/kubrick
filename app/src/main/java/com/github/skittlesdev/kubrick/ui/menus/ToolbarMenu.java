package com.github.skittlesdev.kubrick.ui.menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
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
            ((Activity) context).finish();
        }
    }
}
