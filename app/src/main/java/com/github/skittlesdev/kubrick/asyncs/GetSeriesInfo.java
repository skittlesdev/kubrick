package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.interfaces.TvSeriesSeasonsListener;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.TmdbTvEpisodes;
import info.movito.themoviedbapi.TmdbTvSeasons;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class GetSeriesInfo extends AsyncTask<Integer, Void, TvSeries> {
    private TvSeriesSeasonsListener listener;

    public GetSeriesInfo(TvSeriesSeasonsListener listener) {
        this.listener = listener;
    }

    @Override
    protected TvSeries doInBackground(Integer... params) {
        if (params[0] == null) {
            return null;
        }
        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        TvSeries tvSeries = api.getTvSeries().getSeries(params[0], "en");
        List<TvSeason> tvSeasonList = tvSeries.getSeasons();

        for (int i = 0; i < tvSeasonList.size(); i++) {
            tvSeasonList.set(i,api.getTvSeasons().getSeason(params[0], tvSeasonList.get(i).getSeasonNumber(), "en"));
        }

        System.out.print("d");
        return tvSeries ;
    }

    @Override
    protected void onPostExecute(TvSeries tvSeries) {
        super.onPostExecute(tvSeries);
        if (tvSeries != null) {
            this.listener.onTvSeriesSeasonsRetrieved(tvSeries);
        }
    }
}
