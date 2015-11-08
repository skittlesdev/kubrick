package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.ui.dialog.PosterFullScreenDialog;
import com.github.skittlesdev.kubrick.utils.GenresUtils;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class FragmentMovieHeader extends Fragment implements View.OnClickListener {
    private IdElement media;
    private ImageView posterView;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.media = (IdElement) getArguments().getSerializable("media");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_header, container, false);
        this.rootView = rootView;

        this.posterView = (ImageView) rootView.findViewById(R.id.moviePosterPicture);
        this.posterView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showPoster();
        this.showTitle();
        this.showDuration();
        this.showGenres();
        this.showReleaseDate();
        this.showStats();
    }

    private void showPoster() {
        String posterPath;

        if (media instanceof MovieDb) {
            posterPath = ((MovieDb) media).getPosterPath();
        }
        else {
            posterPath = ((TvSeries) media).getPosterPath();
        }

        Glide.with(getActivity().getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + posterPath)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) rootView.findViewById(R.id.moviePosterPicture));
    }

    private void showTitle() {
        TextView titleView = (TextView) rootView.findViewById(R.id.movieTitle);

        if (media instanceof MovieDb) {
            titleView.setText(((MovieDb) media).getTitle());
        }
        else {
            titleView.setText(((TvSeries) media).getName());
        }
    }

    private void showReleaseDate() {
        TextView releaseDate = (TextView) rootView.findViewById(R.id.movieReleaseDate);

        if (media instanceof MovieDb) {
            releaseDate.setText("(" + ((MovieDb) media).getReleaseDate().split("-")[0] + ")");
        }
        else {
            String firstYear = ((TvSeries) media).getFirstAirDate().split("-")[0];
            String lastYear = ((TvSeries) media).getLastAirDate().split("-")[0];
            releaseDate.setText(" (" + firstYear + " - " + lastYear + ")");
        }
    }

    private void showDuration() {
        if (this.media instanceof MovieDb) {
            MovieDb movieDb = (MovieDb) this.media;

            TextView durationView = (TextView) rootView.findViewById(R.id.movieDuration);

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

    private void showStats() {
        if (this.media instanceof TvSeries) {
            TvSeries tvSeries = (TvSeries) this.media;
            TextView durationView = (TextView) rootView.findViewById(R.id.movieDuration);
            String stats = tvSeries.getNumberOfSeasons() + " seasons, " + tvSeries.getNumberOfEpisodes() + " episodes";
            durationView.setText(stats);
        }
    }

    private void showGenres(){
        TextView genresView = (TextView) rootView.findViewById(R.id.movieGenres);

        List<Genre> genres;
        if (media instanceof MovieDb) {
            genres = ((MovieDb) media).getGenres();
        }
        else {
            genres = ((TvSeries) media).getGenres();
        }

        genresView.setText(GenresUtils.getGenrePrintableString(genres));
    }

    @Override
    public void onClick(View v) {
        Drawable drawable = this.posterView.getDrawable();
        PosterFullScreenDialog posterDialog = new PosterFullScreenDialog(getActivity(), drawable);
        posterDialog.show();
    }
}