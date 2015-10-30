package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.os.Bundle;
import com.github.skittlesdev.kubrick.asyncs.GetMovieTask;
import com.github.skittlesdev.kubrick.interfaces.MovieListener;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieActivity extends Activity implements MovieListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.movies_layout);

        GetMovieTask task = new GetMovieTask(this);

    }

    @Override
    public void onMovieRetrieved(MovieDb movie) {

    }
}
