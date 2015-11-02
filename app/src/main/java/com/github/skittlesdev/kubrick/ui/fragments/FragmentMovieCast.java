package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.utils.CastUtils;

import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by lowgr on 11/2/2015.
 */
public class FragmentMovieCast extends Fragment {
    private IdElement mMedia;
    private View r;

    public FragmentMovieCast() {
    }

    public FragmentMovieCast(IdElement mMedia) {
        this.mMedia = mMedia;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_cast, container, false);
        r = rootView;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showCast();
    }

    private void showCast() {
        TextView castView = (TextView) r.findViewById(R.id.movieCastContent);

        Credits credits;
        if (mMedia instanceof MovieDb) {
            credits = ((MovieDb) mMedia).getCredits();
            castView.setText("Cast : " + CastUtils.getCastPrintableString(credits.getCast()) + "\n" + "Crew : " + CastUtils.getCrewPrintableString(credits.getCrew()));

        } else {
            credits = ((TvSeries) mMedia).getCredits();
            castView.setText("Cast : " + CastUtils.getCastPrintableString(credits.getCast()) + "\n");
        }

    }
}