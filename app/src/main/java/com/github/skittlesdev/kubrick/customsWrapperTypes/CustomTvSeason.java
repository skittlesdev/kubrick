package com.github.skittlesdev.kubrick.customsWrapperTypes;

import java.util.List;

import info.movito.themoviedbapi.model.tv.TvEpisode;

/**
 * Created by louis on 11/9/15.
 */
public class CustomTvSeason {

    private int SeasonId;
    private int SeasonNumber;
    private int SerieId;
    private String posterPath;
    private List<TvEpisode> tvEpisodes;

    public CustomTvSeason(int seasonId, int seasonNumber, int serieId, String posterPath, List<TvEpisode> tvEpisodes) {
        SeasonId = seasonId;
        SeasonNumber = seasonNumber;
        SerieId = serieId;
        this.posterPath = posterPath;
        this.tvEpisodes = tvEpisodes;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getSeasonId() {
        return SeasonId;
    }

    public void setSeasonId(int seasonId) {
        SeasonId = seasonId;
    }

    public int getSeasonNumber() {
        return SeasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        SeasonNumber = seasonNumber;
    }

    public int getSerieId() {
        return SerieId;
    }

    public void setSerieId(int serieId) {
        SerieId = serieId;
    }

    public List<TvEpisode> getTvEpisodes() {
        return tvEpisodes;
    }

    public void setTvEpisodes(List<TvEpisode> tvEpisodes) {
        this.tvEpisodes = tvEpisodes;
    }
}
