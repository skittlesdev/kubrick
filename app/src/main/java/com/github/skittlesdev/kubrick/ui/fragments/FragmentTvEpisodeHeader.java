package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.skittlesdev.kubrick.R;

import java.util.List;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.tv.TvEpisode;

/**
 * Created by louis on 04/11/2015.
 */
public class FragmentTvEpisodeHeader extends Fragment {
    private TvEpisode tvEpisode;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.tvEpisode = (TvEpisode) getArguments().getSerializable("tvEpisode");
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showTitle();
        this.showPoster();
    }

    private void showPoster() {
        MovieImages images = tvEpisode.getImages();

        if (images != null) {
            List<Artwork> posters = tvEpisode.getImages().getPosters();
            if (posters != null) {
                Artwork artwork = posters.get(0);

                if (artwork != null) {
                    Glide.with(getActivity().getApplicationContext())
                            .load("http://image.tmdb.org/t/p/w500" + artwork)
                            .placeholder(R.drawable.poster_default_placeholder)
                            .error(R.drawable.poster_default_error)
                            .into((ImageView) this.rootView.findViewById(R.id.tvEpisodePoster));
                } else {
                    // get serie poster
                }
            }
        }
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
}
