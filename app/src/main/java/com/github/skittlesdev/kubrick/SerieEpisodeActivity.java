package com.github.skittlesdev.kubrick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import info.movito.themoviedbapi.model.tv.TvEpisode;

/**
 * Created by louis on 11/6/15.
 */
public class SerieEpisodeActivity extends AppCompatActivity {

    TvEpisode tvEpisode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tv_episode);


        ImageView poster = (ImageView) findViewById(R.id.tvEpisodePoster);
        TextView name = (TextView) findViewById(R.id.tvEpisodeName);
        TextView overview = (TextView) findViewById(R.id.tvEpisodeOverview);

        tvEpisode = (TvEpisode) getIntent().getSerializableExtra("tvEpisode");
        name.setText(tvEpisode.getName());

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