package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.FavoritesOverviewAdapter;
import com.github.skittlesdev.kubrick.events.ParseResultListEvent;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.parse.*;

import java.util.List;

public class FavoritesOverviewFragment extends Fragment {
    private UltimateRecyclerView view;
    private String mediaType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mediaType = getArguments().getString("MEDIA_TYPE");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_favorites_overview, container, false);

        TextView typeView = (TextView) layout.findViewById(R.id.type);
        if (this.mediaType.compareTo("movie") == 0) {
            typeView.setText("Favorite movies");
        }

        if (this.mediaType.compareTo("tv") == 0) {
            typeView.setText("Favorite series");
        }

        this.view = (UltimateRecyclerView) layout.findViewById(R.id.recyclerView);
        this.view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("user", ParseUser.getCurrentUser());

        if (this.mediaType.compareTo("movie") == 0) {
            query.whereExists("tmdb_movie_id");
        }
        if (this.mediaType.compareTo("tv") == 0) {
            query.whereExists("tmdb_series_id");
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    onResults(objects);
                }
            }
        });
        return layout;
    }

    public void onResults(List<ParseObject> results) {
        this.view.setAdapter(new FavoritesOverviewAdapter(results));
    }
}
