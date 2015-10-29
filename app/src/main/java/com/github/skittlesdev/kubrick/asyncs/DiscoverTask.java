package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.github.skittlesdev.kubrick.interfaces.DataListener;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Created by lowgr on 10/29/2015.
 */
/*







USELESS (FOR NOW?)









 */
public class DiscoverTask extends AsyncTask<String, Integer, List<? extends IdElement>> {
    private DataListener mDataListener;

    public DiscoverTask(DataListener dataListener) {
        this.mDataListener = dataListener;
    }

    @Override
    protected List<? extends IdElement> doInBackground(String... params) {
        if (!TextUtils.isEmpty(params[0])) {
            TmdbApi tmdbApi = new TmdbApi(params[0]);
            TmdbMovies tmdbMovies = tmdbApi.getMovies();
            MovieResultsPage movieResultsPage = tmdbMovies.getPopularMovieList("en", 1);

            return movieResultsPage.getResults();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<? extends IdElement> data) {
        if (this.mDataListener != null) {
            this.mDataListener.onDataRetrieved(data);
        }
    }
}
