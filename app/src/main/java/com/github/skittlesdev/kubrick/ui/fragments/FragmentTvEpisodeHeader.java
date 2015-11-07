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
        if (tvEpisode.getImages() != null) {
            if (tvEpisode.getImages().getPosters() != null && tvEpisode.getImages().getPosters().get(0) != null) {
                Glide.with(getActivity().getApplicationContext())
                        .load("http://image.tmdb.org/t/p/w500" + tvEpisode.getImages().getPosters().get(0))
                        .placeholder(R.drawable.poster_default_placeholder)
                        .error(R.drawable.poster_default_error)
                        .into((ImageView) this.rootView.findViewById(R.id.tvEpisodePoster));
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
