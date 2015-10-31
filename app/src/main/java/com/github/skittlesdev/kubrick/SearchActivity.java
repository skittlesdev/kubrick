package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.github.skittlesdev.kubrick.asyncs.SearchMovieTask;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

import java.util.LinkedList;
import java.util.List;


public class SearchActivity extends Activity implements SearchListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private MovieResultsPage results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.setActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        this.setActionListener();

        ImageButton submitButton = (ImageButton) findViewById(R.id.searchButton);
        submitButton.setOnClickListener(this);
    }

    public void executeSearchTask(TextView searchInput, SearchActivity searchActivity) {
        SearchMovieTask searchTask = new SearchMovieTask(searchActivity);
        searchTask.execute(searchInput.getText().toString());
    }

    private void setActionListener() {
        final EditText searchInput = (EditText) findViewById(R.id.search);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView searchInput, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Context context = searchInput.getContext();

                    if (context instanceof SearchActivity) {
                        SearchActivity searchActivity = (SearchActivity) context;
                        searchActivity.executeSearchTask(searchInput, searchActivity);
                        handled = true;
                    }
                }

                return handled;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new ToolbarMenu(this).itemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchResults(MovieResultsPage results) {
        this.results = results;
        List<String> titles = new LinkedList<>();
        for(MovieDb item: results.getResults()) {
            titles.add(item.getTitle());
        }
        ArrayAdapter<String> items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        ListView view = (ListView) findViewById(R.id.results);
        view.setAdapter(items);
        view.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.executeSearchTask((EditText) findViewById(R.id.search), this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieDb item = this.results.getResults().get(position);
        Intent intent = new Intent(this, MediaActivity.class);
        intent.putExtra("MEDIA_ID", item.getId());
        intent.putExtra("MEDIA_TYPE", "movie");
        startActivity(intent);
    }
}
