package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.MovieListener;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;

public class GetMovieTask extends AsyncTask<Integer, Void, MovieDb> {
    private MovieListener listener;

    public GetMovieTask(MovieListener listener) {
        this.listener = listener;
    }

    @Override
    protected MovieDb doInBackground(Integer... params) {
        if (params[0] == null) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        return api.getMovies().getMovie(params[0], "en");
    }

    @Override
    protected void onPostExecute(MovieDb movieDb) {
        super.onPostExecute(movieDb);
        this.listener.onMovieRetrieved(movieDb);
    }
}
