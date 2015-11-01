package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.TmdbTvSeasons;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class GetSeriesTask extends AsyncTask<Integer, Void, TvSeries> {
    private MediaListener listener;

    public GetSeriesTask(MediaListener listener) {
        this.listener = listener;
    }

    @Override
    protected TvSeries doInBackground(Integer... params) {
        if (params[0] == null) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        return api.getTvSeries().getSeries(params[0], "en", TmdbTV.TvMethod.credits);
    }

    @Override
    protected void onPostExecute(TvSeries tvSeries) {
        super.onPostExecute(tvSeries);
        if (tvSeries != null) {
            this.listener.onMediaRetrieved(tvSeries);
        }
    }
}
