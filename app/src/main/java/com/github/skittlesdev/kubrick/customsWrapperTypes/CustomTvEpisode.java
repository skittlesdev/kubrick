package com.github.skittlesdev.kubrick.customsWrapperTypes;

/**
 * Created by louis on 08/11/2015.
 */
public class CustomTvEpisode {

    private String airDate;
    private int serieId;
    private int episodeNumber;
    private int seasonNumber;
    private int episodeId;

    public CustomTvEpisode(String airDate, int serieId, int episodeNumber, int seasonNumber, int episodeId) {
        this.airDate = airDate;
        this.serieId = serieId;
        this.episodeNumber = episodeNumber;
        this.seasonNumber = seasonNumber;
        this.episodeId = episodeId;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public int getSerieId() {
        return serieId;
    }

    public void setSerieId(int serieId) {
        this.serieId = serieId;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }
}
