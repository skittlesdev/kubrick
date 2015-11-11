package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.TvEpisodeListener;
import com.github.skittlesdev.kubrick.models.SeriesEpisode;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.tv.TvEpisode;


public class GetTvEpisodeTask extends AsyncTask<SeriesEpisode, Void, TvEpisode> {
    private TvEpisodeListener listener;

    public GetTvEpisodeTask(TvEpisodeListener listener) {
        this.listener = listener;
    }

    @Override
    protected TvEpisode doInBackground(SeriesEpisode... params) {
        if (params[0] == null) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        return api.getTvEpisodes().getEpisode(params[0].series.getId(), params[0].seasonNumber, params[0].episodeNumber, "en");
    }

    @Override
    protected void onPostExecute(TvEpisode tvEpisode) {
        super.onPostExecute(tvEpisode);
        this.listener.onTvEpisode(tvEpisode);
    }
}
