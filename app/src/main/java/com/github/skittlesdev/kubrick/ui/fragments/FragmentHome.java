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
    private TmdbApiTask mTmdbApiTask;
    private String mApiKey;
    private RecyclerView mRecyclerView;

    public FragmentHome() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mApiKey = getString(R.string.tmdb_api_key);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        this.mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.mRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onDataRetrieved(List<? extends IdElement> data) {
        HomeActivityRecyclerAdapter adapter = new HomeActivityRecyclerAdapter(data);
        this.mRecyclerView.setAdapter(adapter);
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!TextUtils.isEmpty(this.mApiKey)) {
            this.mTmdbApiTask = new TmdbApiTask(this);
            this.mTmdbApiTask.execute(this.mApiKey);
        }
    }
}