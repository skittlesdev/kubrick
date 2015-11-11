package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.asyncs.GetTvEpisodeTask;
import com.github.skittlesdev.kubrick.interfaces.TvEpisodeListener;
import com.github.skittlesdev.kubrick.models.SeriesEpisode;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentTvEpisodeOverview;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentTvEpisodeHeader;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.MovieImages;
import info.movito.themoviedbapi.model.tv.TvEpisode;

/**
 * Created by louis on 11/6/15.
 */
public class SerieEpisodeActivity extends AppCompatActivity implements TvEpisodeListener {
    private TvEpisode tvEpisode = null;
    private String seriePosterPath;
    private String serieBackdroptPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_tv_episode);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        this.seriePosterPath = this.getIntent().getStringExtra("seriePoster");
        this.serieBackdroptPath = this.getIntent().getStringExtra("serieBackdrop");

        SeriesEpisode episodeRequest = (SeriesEpisode) getIntent().getSerializableExtra("tvEpisode");
        GetTvEpisodeTask task = new GetTvEpisodeTask(this);
        task.execute(episodeRequest);
    }


    @Override
    public void onTvEpisode(TvEpisode episode) {
        this.tvEpisode = episode;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle optionsHeader = new Bundle();
        optionsHeader.putSerializable("tvEpisode", tvEpisode);
        optionsHeader.putString("seriePoster", this.seriePosterPath);

        Bundle optionsOverview = new Bundle();
        optionsOverview.putSerializable("tvEpisode", tvEpisode);

        FragmentTvEpisodeHeader header = new FragmentTvEpisodeHeader();
        header.setArguments(optionsHeader);

        FragmentTvEpisodeOverview overview = new FragmentTvEpisodeOverview();
        overview.setArguments(optionsOverview);

        transaction.add(R.id.episodeHeaderContainer, header);
        transaction.add(R.id.episodeOverviewContainer, overview);
        transaction.commit();

        this.showBackdrop();
        this.showTitle();
    }

    private void showTitle() {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(true);
        collapsingToolbar.setTitle(tvEpisode.getName());
    }

    private void showBackdrop() {
        MovieImages images = tvEpisode.getImages();
        String path = null;

        if (images != null) {
            List<Artwork> backdrops = tvEpisode.getImages().getBackdrops();

            if (backdrops != null) {
                Artwork artwork = backdrops.get(0);

                if (artwork != null) {
                    path = artwork.getFilePath();
                }
            }
        }

        if (TextUtils.isEmpty(path)) {
            path = this.serieBackdroptPath;
        }

        ((SimpleDraweeView) findViewById(R.id.episodeBackDropPicture)).setImageURI(Uri.parse("http://image.tmdb.org/t/p/w500" + path));
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
        ParseObject favorite = new ParseObject("ViewedTvSeriesEpisodes");
        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(false);

        favorite.put("User", ParseUser.getCurrentUser());
        favorite.put("SerieId", this.tvEpisode.getSeriesId());
        favorite.put("SeasonNumber", this.tvEpisode.getSeasonNumber());
        favorite.put("EpisodeNumber", this.tvEpisode.getEpisodeNumber());
        favorite.put("EpisodeId", this.tvEpisode.getId());
        favorite.put("AirDate", this.tvEpisode.getAirDate());

        favorite.setACL(acl);
        favorite.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(KubrickApplication.getContext(), R.string.tv_episode_toast_watched, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(KubrickApplication.getContext(), "Failed to favorite movie", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        new ToolbarMenu(this).filterItems(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new ToolbarMenu(this).itemSelected(item);
        return super.onOptionsItemSelected(item);
    }
}