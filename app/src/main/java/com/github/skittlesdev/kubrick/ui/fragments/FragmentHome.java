package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
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
import com.github.skittlesdev.kubrick.adapters.HomeActivityAdapter;
import com.github.skittlesdev.kubrick.asyncs.TmdbApiTask;
import com.github.skittlesdev.kubrick.interfaces.DataListener;
import com.github.skittlesdev.kubrick.interfaces.HomeActivityListener;

import java.util.List;

import info.movito.themoviedbapi.model.core.IdElement;

public class FragmentHome extends Fragment implements OnItemClickListener/*, DataReceiver*/, DataListener {
    private GridView mGridView;
    private HomeActivityListener mHomeActivityListener;
    private TmdbApiTask mTmdbApiTask;
    private String mApiKey;

    public FragmentHome() {
        this(null);
    }

    public FragmentHome(String apiKey) {
        this.mApiKey = apiKey;
    }

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

        if (activity instanceof HomeActivityListener){
            this.mHomeActivityListener = (HomeActivityListener) activity;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.mHomeActivityListener != null) {
            this.mHomeActivityListener.onMovieClicked();
        }
    }

    /*@Override
    public void workOnData(List<? extends IdElement> elements) {
        final HomeActivityAdapter movieAdapter = new HomeActivityAdapter(elements);
        this.mGridView.setAdapter(movieAdapter);
    }*/

    @Override
    public void onDataRetrieved(List<? extends IdElement> data) {
        final HomeActivityAdapter adapter = new HomeActivityAdapter(data);
        this.mGridView.setAdapter(adapter);
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