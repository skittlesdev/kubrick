package com.github.skittlesdev.kubrick;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.parse.*;

import java.util.LinkedList;
import java.util.List;

public class ProfileRelationsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView view;
    private List<String> userIds;

    public enum Types {
        FOLLOWINGS,
        FOLLOWERS
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relations);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        this.view = (ListView) findViewById(R.id.results);
        this.view.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        ParseUser user = ParseObject.createWithoutData(ParseUser.class, getIntent().getStringExtra("user_id"));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");

        if (getIntent().getSerializableExtra("type") == Types.FOLLOWERS) {
            query.whereEqualTo("other_user", user);
            query.include("user");
        }
        else {
            query.whereEqualTo("user", user);
            query.include("other_user");
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    retrieveData(objects);
                }
            }
        });
    }

    public void retrieveData(List<ParseObject> results) {
        List<String> usernames = new LinkedList<>();
        this.userIds = new LinkedList<>();

        for (ParseObject result: results) {
            if (getIntent().getSerializableExtra("type") == Types.FOLLOWERS) {
                usernames.add(result.getParseUser("user").getUsername());
                this.userIds.add(result.getParseUser("user").getObjectId());
            }
            else {
                usernames.add(result.getParseUser("other_user").getUsername());
                this.userIds.add(result.getParseUser("other_user").getObjectId());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usernames);
        this.view.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String userId = this.userIds.get(position);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        new ToolbarMenu(this).filterItems(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new ToolbarMenu(this).itemSelected(item);
        return super.onOptionsItemSelected(item);
    }
}
