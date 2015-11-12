package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.PeopleSearchListener;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbPeople.PersonResultsPage;

public class SearchPeopleTask extends AsyncTask<String, Void, PersonResultsPage> {
    private PeopleSearchListener listener;

    public SearchPeopleTask(PeopleSearchListener listener) {
        this.listener = listener;
    }

    @Override
    protected PersonResultsPage doInBackground(String... params) {
        if (TextUtils.isEmpty(params[0])) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        return api.getSearch().searchPerson(params[0], false, 0);
    }

    @Override
    protected void onPostExecute(PersonResultsPage results) {
        if (results != null) {
            super.onPostExecute(results);
            this.listener.onSearchResults(results);
        }
    }
}
