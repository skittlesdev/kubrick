package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.utils.GenresUtils;
import com.squareup.picasso.Picasso;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by lowgr on 11/2/2015.
 */
public class FragmentMovieHeader extends Fragment {
    private IdElement mMedia;
    private View r;

    public FragmentMovieHeader() {
    }

    public FragmentMovieHeader(IdElement mMedia) {
        this.mMedia = mMedia;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_header, container, false);
        r = rootView;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showPoster();
        this.showTitle();
        this.showDuration();
        this.showGenres();
        this.showBackdrop();
        this.showReleaseDate();
    }

    private void showBackdrop() {
        String backdrop;

        if (mMedia instanceof MovieDb) {
            backdrop = ((MovieDb) mMedia).getBackdropPath();
        }
        else {
            backdrop = ((TvSeries) mMedia).getBackdropPath();
        }

        Picasso.with(getActivity().getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + backdrop)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) r.findViewById(R.id.movieBackDropPicture));
    }

    private void showPoster() {
        String posterPath;

        if (mMedia instanceof MovieDb) {
            posterPath = ((MovieDb) mMedia).getPosterPath();
        }
        else {
            posterPath = ((TvSeries) mMedia).getPosterPath();
        }

        Picasso.with(getActivity().getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + posterPath)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) r.findViewById(R.id.moviePosterPicture));
    }

    private void showTitle() {
        TextView titleView = (TextView) r.findViewById(R.id.movieTitle);

        if (mMedia instanceof MovieDb) {
            titleView.setText(((MovieDb) mMedia).getTitle());
        }
        else {
            titleView.setText(((TvSeries) mMedia).getName());
        }
    }

    private void showReleaseDate() {
        TextView releaseDate = (TextView) r.findViewById(R.id.movieReleaseDate);

        if (mMedia instanceof MovieDb) {
            releaseDate.setText("(" + ((MovieDb) mMedia).getReleaseDate().split("-")[0] + ")");
        }
        else {
            String firstYear = ((TvSeries) mMedia).getFirstAirDate().split("-")[0];
            String lastYear = ((TvSeries) mMedia).getLastAirDate().split("-")[0];
            releaseDate.setText(" (" + firstYear + " - " + lastYear + ")");
        }
    }

    private void showDuration() {
        if (this.mMedia instanceof MovieDb) {
            MovieDb movieDb = (MovieDb) this.mMedia;

            TextView durationView = (TextView) r.findViewById(R.id.movieDuration);

            Duration duration = new Duration(movieDb.getRuntime() * 1000 * 60);
            PeriodFormatter formatter = new PeriodFormatterBuilder()
                    .appendHours()
                    .appendSuffix(":")
                    .minimumPrintedDigits(2)
                    .appendMinutes()
                    .toFormatter();

            String durationDisplay = formatter.print(duration.toPeriod());

            durationView.setText(durationDisplay);
        }
    }

    /*private void showStats() {
        if (this.mMedia instanceof TvSeries) {
            TvSeries tvSeries = (TvSeries) this.mMedia;
            TextView durationView = (TextView) r.findViewById(R.id.duration);
            durationView.setText(tvSeries.getNumberOfSeasons() + " seasons, " + tvSeries.getNumberOfEpisodes() + " episodes");
        }
    }*/

    private void showGenres(){
        TextView genresView = (TextView) r.findViewById(R.id.movieGenres);

        List<Genre> genres;
        if (mMedia instanceof MovieDb) {
            genres = ((MovieDb) mMedia).getGenres();
        }
        else {
            genres = ((TvSeries) mMedia).getGenres();
        }

        genresView.setText(GenresUtils.getGenrePrintableString(genres));
    }
}