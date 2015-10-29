package com.github.skittlesdev.kubrick.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.github.skittlesdev.kubrick.utils.Constants;

import java.io.Serializable;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Created by lowgr on 10/29/2015.
 */
public class ElementService extends IntentService {
    private ElementService() {
        super("");
    }

    private ElementService(String name) {
        super(name);
    }

    public void callBroadcaster(List<? extends IdElement> data) {
        final Intent movieBroadcastReceiverIntent = new Intent(Constants.Intent.ACTION_NEW_DATA);
        final Bundle extras = new Bundle();
        extras.putSerializable(Constants.Intent.INTENT_ID_ELEMENTS, (Serializable) data);
        movieBroadcastReceiverIntent.putExtras(extras);
        this.sendBroadcast(movieBroadcastReceiverIntent);
        this.stopSelf();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String apiKey = intent.getStringExtra(Constants.Intent.INTENT_API_KEY);
        TmdbApi tmdbApi = new TmdbApi(apiKey);
        TmdbMovies tmdbMovies = tmdbApi.getMovies();
        MovieResultsPage movieResultsPage = tmdbMovies.getPopularMovieList("en", 1);
        this.callBroadcaster(movieResultsPage.getResults());
    }
}
