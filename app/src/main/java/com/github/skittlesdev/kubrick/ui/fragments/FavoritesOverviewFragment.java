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
import com.github.skittlesdev.kubrick.KubrickApplication;
import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.FavoritesOverviewAdapter;
import com.github.skittlesdev.kubrick.events.ParseResultListEvent;
import com.parse.*;

import java.util.List;

public class FavoritesOverviewFragment extends Fragment {
    private RecyclerView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KubrickApplication.getEventBus().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_favorites_overview, container, false);

        this.view = (RecyclerView) layout.findViewById(R.id.recyclerView);
        this.view.setHasFixedSize(true);
        this.view.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    KubrickApplication.getEventBus().post(new ParseResultListEvent(objects));
                }
            }
        });
        return layout;
    }

    public void onEvent(ParseResultListEvent event) {
        this.view.setAdapter(new FavoritesOverviewAdapter(event.getResults()));
    }
}
