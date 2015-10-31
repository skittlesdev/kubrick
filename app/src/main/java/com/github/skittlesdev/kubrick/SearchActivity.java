package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.github.skittlesdev.kubrick.asyncs.SearchMovieTask;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


public class SearchActivity extends Activity implements SearchListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private MovieResultsPage results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button submitButton = (Button) findViewById(R.id.searchButton);
        submitButton.setOnClickListener(this);
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
    public void onClick(View v) {
        EditText searchInput = (EditText) findViewById(R.id.search);
        SearchMovieTask searchTask = new SearchMovieTask(this);
        searchTask.execute(searchInput.getText().toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieDb item = this.results.getResults().get(position);
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("ITEM_ID", item.getId());
        startActivity(intent);
    }
}
