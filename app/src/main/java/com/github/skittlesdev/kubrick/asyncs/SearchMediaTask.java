package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.SearchListener;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;

public class SearchMediaTask extends AsyncTask<String, Void, TmdbSearch.MultiListResultsPage> {
    private SearchListener listener;

    public SearchMediaTask(SearchListener listener) {
        this.listener = listener;
    }

    @Override
    protected TmdbSearch.MultiListResultsPage doInBackground(String... params) {
        if (TextUtils.isEmpty(params[0])) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        return api.getSearch().searchMulti(params[0], "en", 0);
    }

    @Override
    protected void onPostExecute(TmdbSearch.MultiListResultsPage results) {
        if (results != null) {
            super.onPostExecute(results);
            this.listener.onSearchResults(results);
        }
    }
}
