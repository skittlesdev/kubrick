package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;

import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.adapters.MovieAdapter;
import com.github.skittlesdev.kubrick.interfaces.DataReceiver;
import com.github.skittlesdev.kubrick.interfaces.MovieListener;

import java.util.List;

import info.movito.themoviedbapi.model.core.IdElement;

public class FragmentHome extends Fragment implements OnItemClickListener, DataReceiver {
    private GridView mGridView;
    private MovieListener mMovieListener;

    public FragmentHome() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);

        this.mGridView = (GridView) rootView.findViewById(R.id.movieGridView);
        this.mGridView.setEmptyView(progressBar);

        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.fragmentHome);
        root.addView(progressBar);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof MovieListener){
            this.mMovieListener = (MovieListener) activity;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.mMovieListener != null) {
            this.mMovieListener.onMovieClicked();
        }
    }

    @Override
    public void workOnData(List<? extends IdElement> elements) {
        final MovieAdapter movieAdapter = new MovieAdapter(elements);
        this.mGridView.setAdapter(movieAdapter);
    }
}