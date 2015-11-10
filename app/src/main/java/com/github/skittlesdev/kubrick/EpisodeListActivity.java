package com.github.skittlesdev.kubrick;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.skittlesdev.kubrick.asyncs.GetSeriesTask;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.ui.AppAdapter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Created by louis on 10/11/2015.
 */
public class EpisodeListActivity extends AppCompatActivity implements MediaListener {

    private SwipeMenuListView listView;
    private TvSeries tvSeries;
    private int tvseriesId;
    private List<TvSeason> tvSeasonList;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
       super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.serie_episode_list_main);
        tvSeries = (TvSeries) getIntent().getExtras().getSerializable("tvSerie");

        getIntent().setAction("");
        //this.tvseriesId =  bundle.getInt("tvSeriesId");

        listView = (SwipeMenuListView) findViewById(R.id.seasonList);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ptr_rotate_arrow);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);

       // AsyncTask task = new GetSeriesTask(this);
        //((GetSeriesTask) task).execute(this.tvseriesId);

        this.tvSeasonList = this.tvSeries.getSeasons();
        ListAdapter appAdapter = new AppAdapter(tvSeries.getSeasons());
        listView.setAdapter(appAdapter);

        setUpSeasonList();


    }

    @Override
    public void onMediaRetrieved(IdElement media) {


    }

    private void setUpSeasonList(){
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.WHITE));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_heart);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //ApplicationInfo item = listView.get(position);
                switch (index) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "fav", Toast.LENGTH_SHORT).show();
                        // open(item);
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), tvSeasonList.get(position).getName() + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


}
