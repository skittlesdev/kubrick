package com.github.skittlesdev.kubrick.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.HomeActivityRecyclerAdapter;
import com.github.skittlesdev.kubrick.asyncs.TmdbApiTask;
import com.github.skittlesdev.kubrick.interfaces.DataListener;

import java.util.List;

import info.movito.themoviedbapi.model.core.IdElement;

public class FragmentHome extends Fragment implements DataListener {
    private TmdbApiTask tmdbApiTask;
    private String apiKey;
    private RecyclerView recyclerView;

    public FragmentHome() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.apiKey = getString(R.string.tmdb_api_key);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.recyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onDataRetrieved(List<? extends IdElement> data) {
        HomeActivityRecyclerAdapter adapter = new HomeActivityRecyclerAdapter(data);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!TextUtils.isEmpty(this.apiKey)) {
            this.tmdbApiTask = new TmdbApiTask(this);
            this.tmdbApiTask.execute(this.apiKey);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.tmdbApiTask != null) {
            this.tmdbApiTask.cancel(true);
        }
    }
}