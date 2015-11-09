package com.github.skittlesdev.kubrick.asyncs;

import android.os.AsyncTask;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class GetMovieTask extends AsyncTask<Integer, Void, MovieDb> {
    private MediaListener listener;

    public GetMovieTask(MediaListener listener) {
        this.listener = listener;
    }

    @Override
    protected MovieDb doInBackground(Integer... params) {
        if (params[0] == null) {
            return null;
        }

        TmdbApi api = new TmdbApi(KubrickApplication.getContext().getString(R.string.tmdb_api_key));
        return api.getMovies().getMovie(params[0], "en", TmdbMovies.MovieMethod.credits, TmdbMovies.MovieMethod.similar_movies);
    }

    @Override
    protected void onPostExecute(MovieDb movieDb) {
        super.onPostExecute(movieDb);
        if (movieDb != null) {
            this.listener.onMediaRetrieved(movieDb);
        }
    }
}
