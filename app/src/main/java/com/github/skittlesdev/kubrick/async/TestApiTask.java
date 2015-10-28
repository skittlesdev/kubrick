package com.github.skittlesdev.kubrick.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by lowgr on 10/28/2015.
 */
public class TestApiTask extends AsyncTask<String, Integer, String> {
    private Context mContext;

    public TestApiTask() {
        this(null);
    }

    public TestApiTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        TmdbApi tmdbApi = new TmdbApi(params[0]);
        TmdbMovies movies = tmdbApi.getMovies();
        MovieDb movieDb = movies.getMovie(5353, "en");
        String movieOriginalTitle = movieDb.getOriginalTitle();

        return movieOriginalTitle;
    }

    @Override
    protected void onPostExecute(String movieOriginalTitle) {
        if (this.mContext != null) {
            Toast.makeText(this.mContext, "Connection done. :3 Movie found is " + movieOriginalTitle, Toast.LENGTH_LONG).show();
        }
    }
}
