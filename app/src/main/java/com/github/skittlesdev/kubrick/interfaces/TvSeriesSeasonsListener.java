package com.github.skittlesdev.kubrick.interfaces;

import java.util.ArrayList;

import info.movito.themoviedbapi.TmdbTvSeasons;

/**
 * Created by louis on 11/3/15.
 */
public interface TvSeriesSeasonsListener {
    public void onTvSeriesSeasonsRetrieved(TmdbTvSeasons tmdbTvSeasons);
}
