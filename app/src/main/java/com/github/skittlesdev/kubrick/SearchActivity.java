package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.github.skittlesdev.kubrick.asyncs.SearchMovieTask;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;
import info.movito.themoviedbapi.model.core.MovieResultsPage;


public class SearchActivity extends Activity implements SearchListener, View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button submitButton = (Button) findViewById(R.id.searchButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onSearchResults(MovieResultsPage results) {
        Toast.makeText(this, String.valueOf(results.getTotalResults()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        EditText searchInput = (EditText) findViewById(R.id.search);
        SearchMovieTask searchTask = new SearchMovieTask(this);
        searchTask.execute(searchInput.getText().toString());
    }
}
