package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class SearchMovieTask extends AsyncTask<String, Void, MovieResultsPage> {
    private SearchListener listener;

    public SearchMovieTask(SearchListener listener) {
        this.listener = listener;
    }

    @Override
    protected MovieResultsPage doInBackground(String... params) {
        if (TextUtils.isEmpty(params[0])) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        return api.getSearch().searchMovie(params[0], 0, null, false, 0);
    }

    @Override
    protected void onPostExecute(MovieResultsPage movieDbs) {
        if (movieDbs != null) {
            super.onPostExecute(movieDbs);
            this.listener.onSearchResults(movieDbs);
        }
    }
}
