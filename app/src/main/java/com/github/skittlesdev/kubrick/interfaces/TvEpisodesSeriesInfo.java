package com.github.skittlesdev.kubrick.interfaces;

import java.util.List;

import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 12/11/2015.
 */
public interface TvEpisodesSeriesInfo {

    public void onTvEpisodesSeriesInfo(List<TvSeries> tvSeriesList);
}
