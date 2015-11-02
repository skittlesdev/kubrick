package com.github.skittlesdev.kubrick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.github.skittlesdev.kubrick.events.ParseResultListEvent;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.parse.*;

import java.util.LinkedList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<ParseObject> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        KubrickApplication.getEventBus().register(this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    KubrickApplication.getEventBus().post(new ParseResultListEvent(objects));
                }
            }
        });
    }

    public void onEvent(ParseResultListEvent event) {
        this.favorites = event.getResults();
        List<String> titles = new LinkedList<>();
        for (ParseObject item: this.favorites) {
            titles.add(item.getString("title"));
        }
        ArrayAdapter<String> items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        ListView view = (ListView) findViewById(R.id.favorites);
        view.setAdapter(items);
        view.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseObject item = this.favorites.get(position);
        Intent intent = new Intent(this, MediaActivity.class);
        intent.putExtra("MEDIA_ID", item.getInt("tmdb_id"));
        intent.putExtra("MEDIA_TYPE", "movie");
        startActivity(intent);
    }
}
