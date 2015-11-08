package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.utils.GenresUtils;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.DecimalFormat;
import java.util.List;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 04/11/2015.
 */
public class FragmentTvEpisodeHeader extends Fragment {
    private TvEpisode tvEpisode;
    private String seriePosterPath;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.tvEpisode = (TvEpisode) this.getArguments().getSerializable("tvEpisode");
        this.seriePosterPath = this.getArguments().getString("seriePoster");
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showTitle();
        this.showPoster();
        this.showAirDate();
        this.showEpisodeNumber();
    }

    private void showPoster() {
        MovieImages images = tvEpisode.getImages();
        String path = null;

        if (images != null) {
            List<Artwork> posters = tvEpisode.getImages().getPosters();

            if (posters != null) {
                Artwork artwork = posters.get(0);

                if (artwork != null) {
                    path = artwork.getFilePath();
                }
            }
        }

        if (TextUtils.isEmpty(path)) {
            path = this.seriePosterPath;
        }

        Glide.with(getActivity().getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + path)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) this.rootView.findViewById(R.id.tvEpisodePoster));
    }

    private void showTitle() {
        ((TextView) rootView.findViewById(R.id.tvEpisodeName))
                .setText(this.tvEpisode.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_tv_episode_header, container, false);

        return this.rootView;
    }

    private void showAirDate() {
        TextView releaseDateContainer = (TextView) this.rootView.findViewById(R.id.episodeAirDate);
        String releaseDate =  this.tvEpisode.getAirDate();
        releaseDateContainer.setText(releaseDate);
    }

    private void showEpisodeNumber() {
        TextView episodeNumberContainer = (TextView) this.rootView.findViewById(R.id.episodeNumber);
        String episodeNumber =  "S" +
                new DecimalFormat("00").format(this.tvEpisode.getSeasonNumber()) + "E" +
                new DecimalFormat("00").format(this.tvEpisode.getEpisodeNumber());
        episodeNumberContainer.setText(episodeNumber);
    }
}
