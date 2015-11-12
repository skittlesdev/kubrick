package com.github.skittlesdev.kubrick.models;

import info.movito.themoviedbapi.model.tv.TvSeries;

import java.io.Serializable;
import java.util.HashMap;

public class SeriesEpisode implements Serializable {
    public int id;
    public TvSeries series;
    public String airDate;
    public int seasonNumber;
    public int episodeNumber;
    public String name;

    public SeriesEpisode(HashMap<String, Object> episode) {
        this.id = (int) episode.get("id");
        if (episode.get("air_date") instanceof String) {
            this.airDate = (String) episode.get("air_date");
        }
        this.seasonNumber = (int) episode.get("season_number");
        this.episodeNumber = (int) episode.get("episode_number");
        this.name = (String) episode.get("name");
    }

    public SeriesEpisode(TvSeries series, HashMap<String, Object> episode) {
        this(episode);
        this.series = series;
    }
}
