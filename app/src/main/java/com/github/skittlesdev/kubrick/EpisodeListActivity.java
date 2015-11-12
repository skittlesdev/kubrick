package com.github.skittlesdev.kubrick;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.skittlesdev.kubrick.models.SeriesEpisode;
import com.github.skittlesdev.kubrick.ui.EpisodeListAdapter;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;


import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 11/11/2015.
 */
public class EpisodeListActivity extends AppCompatActivity {

    private SwipeMenuListView listView;
    private TvSeries tvSeries;
    // private TvSeason tvSeason;
    //private List<TvEpisode> tvEpisodeList;
    private List<SeriesEpisode> episodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.serie_episode_list_main);

        this.episodes = (List<SeriesEpisode>) getIntent().getExtras().getSerializable("episodes");
        this.tvSeries = (TvSeries) getIntent().getExtras().getSerializable("series");

        listView = (SwipeMenuListView) findViewById(R.id.seasonList);

        ListAdapter appAdapter = new EpisodeListAdapter(this.episodes);
        listView.setAdapter(appAdapter);

        setUpEpisodeList();

    }

    private void setUpEpisodeList(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem seasonWatchedItem = new SwipeMenuItem(getApplicationContext());
                seasonWatchedItem.setBackground(new ColorDrawable(Color.WHITE));
                seasonWatchedItem.setWidth(dp2px(90));
                seasonWatchedItem.setIcon(R.drawable.ic_heart);
                menu.addMenuItem(seasonWatchedItem);
            }
        };

        listView.setMenuCreator(creator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                SeriesEpisode item = episodes.get(position);
                switch (index) {
                    case 0:
                        setEpisodeAsWatched(tvSeries, item);
                        break;
                }
                return false;
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeriesEpisode episode = episodes.get(position);

                Intent intent = new Intent(getApplicationContext(), SerieEpisodeActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("tvEpisode", episode);
                bundle.putString("seriePoster", tvSeries.getPosterPath());
                bundle.putString("serieBackdrop", tvSeries.getBackdropPath());

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    private void setEpisodeAsWatched(TvSeries tvSeries, SeriesEpisode tvEpisode){

        ParseObject favorite = new ParseObject("ViewedTvSeriesEpisodes");

        if(ParseUser.getCurrentUser() == null){
            Toast.makeText(KubrickApplication.getContext(), "Please login", Toast.LENGTH_SHORT).show();
            return;
        }
        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(false);

        favorite.put("User", ParseUser.getCurrentUser());
        favorite.put("SerieId", tvSeries.getId());
        favorite.put("SeasonNumber", tvEpisode.seasonNumber);
        favorite.put("EpisodeNumber", tvEpisode.episodeNumber);
        favorite.put("EpisodeId", tvEpisode.id);
        favorite.put("AirDate", tvEpisode.airDate);

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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
