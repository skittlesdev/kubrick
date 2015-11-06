package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.R;
import com.github.skittlesdev.kubrick.utils.GenresUtils;
import com.squareup.picasso.Picasso;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by lowgr on 11/2/2015.
 */
public class FragmentMovieOverview extends Fragment {
    private IdElement mMedia;
    private View r;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mMedia = (IdElement) getArguments().getSerializable("media");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_overview, container, false);
        r = rootView;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.showOverview();
    }

    private void showOverview() {
        TextView overviewView = (TextView) r.findViewById(R.id.movieOverviewContent);

        String overview;

        if (mMedia instanceof MovieDb) {
            overview = ((MovieDb) mMedia).getOverview();
        } else {
            overview = ((TvSeries) mMedia).getOverview();
        }

        overviewView.setText(overview);
    }
}