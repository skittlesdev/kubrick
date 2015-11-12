package com.github.skittlesdev.kubrick.asyncs;

/**
 * Created by louis on 12/11/2015.
 */
import android.os.AsyncTask;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.TvEpisodeListener;
import com.github.skittlesdev.kubrick.interfaces.TvEpisodesSeriesInfo;
import com.github.skittlesdev.kubrick.models.SeriesEpisode;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTvEpisodes;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;


public class GetEpisodesSeriesInfo extends AsyncTask<List<Integer>, Void, List<TvSeries>> {
    private TvEpisodesSeriesInfo listener;

    public GetEpisodesSeriesInfo(TvEpisodesSeriesInfo listener) {
        this.listener = listener;
    }

    @Override
    protected List<TvSeries> doInBackground(List<Integer>... params) {
        if (params[0] == null) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));

        List<TvSeries> tvSeriesList = new ArrayList<>();

        for(Integer id : params[0]){
            tvSeriesList.add(api.getTvSeries().getSeries(id, "en"));
        }

        return tvSeriesList;
    }

    @Override
    protected void onPostExecute(List<TvSeries> tvSeriesList) {
        super.onPostExecute(tvSeriesList);
        this.listener.onTvEpisodesSeriesInfo(tvSeriesList);
    }
}