package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.interfaces.TvSeriesSeasonsListener;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.TmdbTvSeasons;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class GetSeriesInfo extends AsyncTask<Integer, Void, TmdbTvSeasons> {
    private TvSeriesSeasonsListener listener;

    public GetSeriesInfo(TvSeriesSeasonsListener listener) {
        this.listener = listener;
    }

    @Override
    protected TmdbTvSeasons doInBackground(Integer... params) {
        if (params[0] == null) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        //return api.getTvSeries().getSeries(params[0], "en", TmdbTV.TvMethod.credits);
        List<TvSeason> tvSeasonList = api.getTvSeries().getSeries(params[0], "en").getSeasons();

        for(TvSeason tvSeason : tvSeasonList){
            Object a = null;
        }

        Object obj = api.getTvEpisodes();
        return null ;
    }

    @Override
    protected void onPostExecute(TmdbTvSeasons tmdbTvSeasons) {
        super.onPostExecute(tmdbTvSeasons);
        if (tmdbTvSeasons != null) {
            this.listener.onTvSeriesSeasonsRetrieved(tmdbTvSeasons);
        }
    }
}
