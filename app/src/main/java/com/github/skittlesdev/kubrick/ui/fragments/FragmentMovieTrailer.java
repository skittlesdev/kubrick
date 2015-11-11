package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.HashMapVideoAdapter;
import com.github.skittlesdev.kubrick.adapters.ListVideoAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.movito.themoviedbapi.model.Video;

public class FragmentMovieTrailer extends Fragment {
    private UltimateRecyclerView view;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_videos, container, false);

        this.view = (UltimateRecyclerView) layout.findViewById(R.id.recyclerView);
        this.view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        try {
            this.adapter = new ListVideoAdapter((List<Video>) this.getArguments().getSerializable("videos"));
        } catch (ClassCastException exception) {
            try {
                this.adapter = new HashMapVideoAdapter((ArrayList<HashMap<String, String>>)(((HashMap) this.getArguments().getSerializable("videos")).get("results")));
            } catch (ClassCastException exception2) {
                this.adapter = null;
            }
        }

        if (this.adapter != null) {
            this.view.setAdapter(this.adapter);
        }

        return layout;
    }
}
