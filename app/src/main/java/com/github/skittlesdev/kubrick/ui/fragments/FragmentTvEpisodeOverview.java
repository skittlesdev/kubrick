package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;

import info.movito.themoviedbapi.model.tv.TvEpisode;

/**
 * Created by lowgr on 11/2/2015.
 */
public class FragmentTvEpisodeOverview extends Fragment {
    private TvEpisode tvEpisode;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tvEpisode = (TvEpisode) getArguments().getSerializable("tvEpisode");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tv_episode_overview, container, false);
        this.rootView = rootView;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showOverview();
    }

    private void showOverview() {
        String overview = this.tvEpisode.getOverview();
        TextView container = (TextView) rootView.findViewById(R.id.episodeOverviewContent);

        if (!TextUtils.isEmpty(overview)) {
            container.setText(overview);
        } else {
            ((CardView) this.rootView.findViewById(R.id.overviewCardView)).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KubrickApplication.getRefWatcher(getActivity()).watch(this);
    }
}