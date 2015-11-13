package com.github.skittlesdev.kubrick;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 11/11/2015.
 */
public class DayEpisodeActivity extends AppCompatActivity {

    private SwipeMenuListView listView;
    private List<TvSeries> tvSeriesList;
    private List<SeriesEpisode> tvEpisodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.serie_episode_list_main);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        this.tvSeriesList = (List<TvSeries>) getIntent().getExtras().getSerializable("resultSeries");
        List<TvEpisode> tvEpisodeListWrapper = (List<TvEpisode>) getIntent().getExtras().getSerializable("result");

        this.tvEpisodeList = new ArrayList<>();

        int i = 0;
        for(TvEpisode item : tvEpisodeListWrapper){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", item.getId());
            hashMap.put("air_date", item.getAirDate());
            hashMap.put("season_number", item.getSeasonNumber());
            hashMap.put("episode_number", item.getEpisodeNumber());
            hashMap.put("name", item.getName());
            tvEpisodeList.add(new SeriesEpisode(tvSeriesList.get(i),hashMap));
            i++;
        }

        //this.tvEpisodeList = (List<SeriesEpisode>)
       // this.tvSeriesList = (TvSeries) getIntent().getExtras().getSerializable("series");

        listView = (SwipeMenuListView) findViewById(R.id.seasonList);

        ListAdapter appAdapter = new EpisodeListAdapter(this.tvEpisodeList);
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
                seasonWatchedItem.setIcon(R.drawable.ic_view);
                menu.addMenuItem(seasonWatchedItem);
            }
        };

        listView.setMenuCreator(creator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                SeriesEpisode item = tvEpisodeList.get(position);
                switch (index) {
                    case 0:
                        setEpisodeAsWatched(tvSeriesList.get(position), item);
                        break;
                }
                return false;
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeriesEpisode episode = tvEpisodeList.get(position);

                Intent intent = new Intent(getApplicationContext(), SerieEpisodeActivity.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("tvEpisode", episode);
                bundle.putString("seriePoster", tvSeriesList.get(position).getPosterPath());
                bundle.putString("serieBackdrop", tvSeriesList.get(position).getBackdropPath());

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

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }
}
