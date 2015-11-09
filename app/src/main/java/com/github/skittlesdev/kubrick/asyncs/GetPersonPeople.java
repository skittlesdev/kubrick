package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.PeopleListener;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.people.PersonPeople;

/**
 * Created by lowgr on 11/9/2015.
 */
public class GetPersonPeople extends AsyncTask<Integer, Void, PersonPeople> {
    private PeopleListener listener;

    public GetPersonPeople(PeopleListener listener) {
        this.listener = listener;
    }

    @Override
    protected PersonPeople doInBackground(Integer... params) {
        if (params[0] == null) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));

        return api.getPeople().getPersonInfo(params[0]);
    }

    @Override
    protected void onPostExecute(PersonPeople personPeople) {
        super.onPostExecute(personPeople);

        if (personPeople != null) {
            this.listener.onPeopleRetrieved(personPeople);
        }
    }
}
