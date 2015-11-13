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

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by lowgr on 11/2/2015.
 */
public class FragmentMovieOverview extends Fragment {
    private IdElement media;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.media = (IdElement) getArguments().getSerializable("media");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_overview, container, false);
        this.rootView = rootView;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showOverview();
    }

    private void showOverview() {
        TextView overviewView = (TextView) rootView.findViewById(R.id.movieOverviewContent);

        String overview;

        if (media instanceof MovieDb) {
            overview = ((MovieDb) media).getOverview();
        } else {
            overview = ((TvSeries) media).getOverview();
        }

        if (!TextUtils.isEmpty(overview)) {
            overviewView.setText(overview);
        } else {
            ((CardView) this.rootView.findViewById(R.id.overviewCardView)).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}