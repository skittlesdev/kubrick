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
import com.github.skittlesdev.kubrick.ui.SeasonListAdapter;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 10/11/2015.
 */
public class SeasonListActivity extends AppCompatActivity {

    private SwipeMenuListView listView;
    private TvSeries tvSeries;
    private List<TvSeason> tvSeasonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.serie_episode_list_main);
        tvSeries = (TvSeries) getIntent().getExtras().getSerializable("tvSerie");

        listView = (SwipeMenuListView) findViewById(R.id.seasonList);

        this.tvSeasonList = this.tvSeries.getSeasons();
        ListAdapter appAdapter = new SeasonListAdapter(tvSeries.getSeasons());
        listView.setAdapter(appAdapter);

        setUpSeasonList();

    }

    private void setUpSeasonList(){
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
                TvSeason item = tvSeasonList.get(position);
                switch (index) {
                    case 0:
                        setSeasonAsWatched(tvSeries,item);
                        Toast.makeText(KubrickApplication.getContext(), "Season set as watched", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplication().getApplicationContext(), EpisodeListActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("tvSeason", tvSeasonList.get(position));
                bundle.putSerializable("tvSeries", tvSeries);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    private void setSeasonAsWatched(TvSeries tvSeries, TvSeason tvSeason){

        for(TvEpisode tvEpisode : tvSeason.getEpisodes()){
            setEpisodeAsWatched(tvSeries, tvSeason, tvEpisode);
        }
    }

    private void setEpisodeAsWatched(TvSeries tvSeries, TvSeason tvSeason, TvEpisode tvEpisode) {

        ParseObject favorite = new ParseObject("ViewedTvSeriesEpisodes");

        if (ParseUser.getCurrentUser() == null) {
            Toast.makeText(KubrickApplication.getContext(), "Please login", Toast.LENGTH_SHORT).show();
            return;
        }
        ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(false);

        favorite.put("User", ParseUser.getCurrentUser());
        favorite.put("SerieId", tvSeries.getId());
        favorite.put("SeasonNumber", tvSeason.getSeasonNumber());
        favorite.put("EpisodeNumber", tvEpisode.getEpisodeNumber());
        favorite.put("EpisodeId", tvEpisode.getId());

        favorite.setACL(acl);
        favorite.saveInBackground();

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


}
