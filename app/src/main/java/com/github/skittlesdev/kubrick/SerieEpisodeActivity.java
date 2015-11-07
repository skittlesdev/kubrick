package com.github.skittlesdev.kubrick;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 11/6/15.
 */
public class SerieEpisodeActivity extends AppCompatActivity {

    TvEpisode tvEpisode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tv_episode);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView cardView = (CardView) this.findViewById(R.id.overviewTVEpisodeCardView);
        cardView.setBackgroundColor(Color.BLACK);

        ImageView poster = (ImageView) findViewById(R.id.tvEpisodePoster);
        ImageView backdrop = (ImageView) findViewById(R.id.episodeBackDropPicture);
        TextView name = (TextView) findViewById(R.id.tvEpisodeName);
        TextView overview = (TextView) findViewById(R.id.tvEpisodeOverview);

        tvEpisode = (TvEpisode) getIntent().getSerializableExtra("tvEpisode");
        name.setText(tvEpisode.getName());
        overview.setText(tvEpisode.getOverview());

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(tvEpisode.getName());

        if (tvEpisode.getImages() != null) {
            if (tvEpisode.getImages().getBackdrops() != null && tvEpisode.getImages().getBackdrops().get(0) != null) {
                Picasso.with(this)
                        .load("http://image.tmdb.org/t/p/w500" + tvEpisode.getImages().getBackdrops().get(0))
                        .placeholder(R.drawable.poster_default_placeholder)
                        .error(R.drawable.poster_default_error)
                        .fit()
                        .into(backdrop);
            }

            if (tvEpisode.getImages().getPosters() != null && tvEpisode.getImages().getPosters().get(0) != null) {
                Picasso.with(this)
                        .load("http://image.tmdb.org/t/p/w500" + tvEpisode.getImages().getPosters().get(0))
                        .placeholder(R.drawable.poster_default_placeholder)
                        .error(R.drawable.poster_default_error)
                        .fit()
                        .into(poster);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FloatingActionButton watchFab = (FloatingActionButton) this.findViewById(R.id.watchFab);
        watchFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();

                if (context instanceof SerieEpisodeActivity) {
                    ((SerieEpisodeActivity) context).handleWatch(v);
                }
            }
        });
    }

    public void handleWatch(View v) {
        Toast.makeText(v.getContext(), "YOU LOST. >:3", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }
}