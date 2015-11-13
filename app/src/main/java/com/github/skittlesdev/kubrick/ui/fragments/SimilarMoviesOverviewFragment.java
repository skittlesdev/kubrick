package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.CreditsOverviewAdapter;
import com.github.skittlesdev.kubrick.adapters.SimilarMoviesOverviewAdapter;
import com.github.skittlesdev.kubrick.adapters.SimilarSeriesOverviewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCrew;

public class SimilarMoviesOverviewFragment extends Fragment {
    private UltimateRecyclerView view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_favorites_overview, container, false);

        String type = getArguments().getString("type");
        TextView typeView = (TextView) layout.findViewById(R.id.type);

        if (type.compareTo("movie") == 0) {
            typeView.setText("Similar movies");
        }
        else{
            typeView.setText("Similar series");
        }

        this.view = (UltimateRecyclerView) layout.findViewById(R.id.recyclerView);
        this.view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (type.compareTo("movie") == 0) {
            ArrayList<MovieDb> similarMovies = (ArrayList<MovieDb>) getArguments().getSerializable("movies");
            this.view.setAdapter(new SimilarMoviesOverviewAdapter(similarMovies));
        }
        else {
            this.view.setAdapter(new SimilarSeriesOverviewAdapter((HashMap<String, Object>) getArguments().getSerializable("series")));
        }

        return layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
