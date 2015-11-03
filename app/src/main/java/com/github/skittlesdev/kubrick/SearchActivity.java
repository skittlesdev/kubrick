package com.github.skittlesdev.kubrick;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.github.skittlesdev.kubrick.asyncs.SearchMediaTask;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;
import android.support.v7.widget.Toolbar;
import java.util.LinkedList;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements SearchListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private TmdbSearch.MultiListResultsPage results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        this.setActionListener();

        ImageButton submitButton = (ImageButton) findViewById(R.id.searchButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        new ToolbarMenu(this).filterItems(menu);
        return true;
    }

    public void executeSearchTask(TextView searchInput, SearchActivity searchActivity) {
        SearchMediaTask searchTask = new SearchMediaTask(searchActivity);
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

    /*@Override
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
    }*/

    @Override
    public void onSearchResults(TmdbSearch.MultiListResultsPage results) {
        this.results = results;
        List<String> titles = new LinkedList<>();
        for (Multi item: results.getResults()) {
            if (item.getMediaType() == Multi.MediaType.MOVIE) {
                titles.add(((MovieDb) item).getTitle());
            }
            if (item.getMediaType() == Multi.MediaType.TV_SERIES) {
                titles.add(((TvSeries) item).getName());
            }
        }
        ArrayAdapter<String> items = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
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
        Multi item = this.results.getResults().get(position);
        if (item.getMediaType() == Multi.MediaType.MOVIE) {
            int movieId = ((IdElement) item).getId();
            Intent intent = new Intent(this, MediaActivity.class);
            intent.putExtra("MEDIA_ID", movieId);
            intent.putExtra("MEDIA_TYPE", "movie");
            startActivity(intent);
        }

        if (item.getMediaType() == Multi.MediaType.TV_SERIES) {
            int seriesId = ((IdElement) item).getId();
            Intent intent = new Intent(this, MediaActivity.class);
            intent.putExtra("MEDIA_ID", seriesId);
            intent.putExtra("MEDIA_TYPE", "tv");
            startActivity(intent);
        }
    }
}
