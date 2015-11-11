package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.FavoritesOverviewAdapter;
import com.github.skittlesdev.kubrick.events.FavoriteStateEvent;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.parse.*;

import java.util.Collections;
import java.util.List;

public class FavoritesOverviewFragment extends Fragment {
    private UltimateRecyclerView view;
    private String mediaType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mediaType = getArguments().getString("MEDIA_TYPE");
        KubrickApplication.getEventBus().register(this);
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

        fetchItems();

        return layout;
    }

    public void fetchItems() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        ParseUser user = ParseUser.createWithoutData(ParseUser.class, getArguments().getString("user_id"));
        query.whereEqualTo("user", user);

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
    }

    public void onResults(List<ParseObject> results) {
        Collections.reverse(results);
        this.view.setAdapter(new FavoritesOverviewAdapter(results));
    }

    public void onEvent(FavoriteStateEvent event) {
        fetchItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KubrickApplication.getEventBus().unregister(this);
        KubrickApplication.getRefWatcher(getActivity()).watch(this);
    }
}
