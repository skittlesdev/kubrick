package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.KubrickApplication;
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
        } else {
            posterPath = ((TvSeries) media).getPosterPath();
        }

        ((SimpleDraweeView) rootView.findViewById(R.id.moviePosterPicture)).setImageURI(Uri.parse("http://image.tmdb.org/t/p/w154" + posterPath));
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
        String releaseDateString;

        if (media instanceof MovieDb) {
            releaseDateString = "[" + ((MovieDb) media).getReleaseDate().split("-")[0] + "]";
        }
        else {
            String firstYear = ((TvSeries) media).getFirstAirDate().split("-")[0];
            String lastYear = ((TvSeries) media).getLastAirDate().split("-")[0];
            releaseDateString = "[" + firstYear + "~" + lastYear + "]";
        }

        releaseDate.setText(releaseDateString);
    }

    private void showDuration() {
        if (this.media instanceof MovieDb) {
            MovieDb movieDb = (MovieDb) this.media;
            TextView durationView = (TextView) rootView.findViewById(R.id.movieDuration);
            Duration duration = new Duration(movieDb.getRuntime() * 1000 * 60);
            String hours = " hr";
            String mins = " min";

            if (duration.getStandardHours() > 1) {
                hours += "s";
            }

            if (duration.getStandardMinutes() > 1) {
                mins += "s";
            }

            PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendHours()
                    .appendSuffix(hours + " ")
                .minimumPrintedDigits(2)
                    .appendMinutes()
                    .appendSuffix(mins)
                .toFormatter();

            String durationDisplay = formatter.print(duration.toPeriod());

            durationView.setText(durationDisplay);
        }
    }

    private void showStats() {
        if (this.media instanceof TvSeries) {
            TvSeries tvSeries = (TvSeries) this.media;
            TextView durationView = (TextView) rootView.findViewById(R.id.movieDuration);
            String seasons = "season", episodes = "episode";

            if (tvSeries.getNumberOfSeasons() > 1) {
                seasons += "s";
            }

            if (tvSeries.getNumberOfEpisodes() > 1) {
                episodes += "s";
            }

            String stats = tvSeries.getNumberOfSeasons() + " " + seasons + " | " + tvSeries.getNumberOfEpisodes() + " " + episodes;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}