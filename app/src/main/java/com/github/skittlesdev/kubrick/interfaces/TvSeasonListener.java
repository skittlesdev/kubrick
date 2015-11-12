package com.github.skittlesdev.kubrick.interfaces;

import java.util.List;

import info.movito.themoviedbapi.model.tv.TvSeason;

/**
 * Created by louis on 08/11/2015.
 */
public interface TvSeasonListener {

    public void onTvSeasonRetrieved(List<TvSeason> tvSeasonList);
}
