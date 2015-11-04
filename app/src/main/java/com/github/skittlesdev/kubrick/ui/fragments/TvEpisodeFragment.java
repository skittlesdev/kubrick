package com.github.skittlesdev.kubrick.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.R;

import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 04/11/2015.
 */
public class TvEpisodeFragment extends Fragment {


   /* public static TvEpisodeFragment newInstance(TvSeries tvSeries) {
        final TvEpisodeFragment tweetFragment = new TvEpisodeFragment();
        final Bundle bundle = new Bundle();

        bundle.putParcelable("tvSeries", tvSeries);
        tweetFragment.setArguments(bundle);
        return tweetFragment;
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tv_episode, container, false);

        ImageView poster = (ImageView) rootView.findViewById(R.id.tvEpisodePoster);
        TextView name = (TextView) rootView.findViewById(R.id.tvEpisodeName);
        TextView overview = (TextView) rootView.findViewById(R.id.tvEpisodeOverview);


        return rootView;
    }
}
