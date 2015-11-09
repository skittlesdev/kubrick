package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.interfaces.TvSeasonListener;
import com.github.skittlesdev.kubrick.interfaces.TvSeriesSeasonsListener;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.TmdbTvEpisodes;
import info.movito.themoviedbapi.TmdbTvSeasons;
import info.movito.themoviedbapi.model.ExternalIds;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class GetSeasonEpisodeTask extends AsyncTask<List<Integer>, Void, List<TvSeason>> {
    private TvSeasonListener listener;

    public GetSeasonEpisodeTask(TvSeasonListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<TvSeason> doInBackground(List<Integer>... params) {
        if (params[0] == null && params[1] == null) {
            return null;
        }
        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));

        List<TvSeason> result = new ArrayList<>();

        for (int i = 0; i < params[0].size(); i++) {
            int serieId = params[0].get(i);
            int seasonNumber = params[1].get(i);

            TvSeries tvSeries = api.getTvSeries().getSeries(serieId, "en");
            List<TvSeason> tvSeasonList = tvSeries.getSeasons();

            for(TvSeason item : tvSeasonList){
                if(item.getSeasonNumber() == seasonNumber){
                    ExternalIds e = new ExternalIds();
                    e.setId(tvSeries.getId());

                    item.setExternalIds(e);
                    item.setEpisodes(api.getTvSeasons().getSeason(serieId, seasonNumber, "en").getEpisodes());
                    result.add(item);
                }
            }

        }

       return result;

    }

    @Override
    protected void onPostExecute(List<TvSeason> tvSeasonList) {
        super.onPostExecute(tvSeasonList);
        if (tvSeasonList != null) {
            this.listener.onTvSeasonRetrieved(tvSeasonList);
        }
    }
}
