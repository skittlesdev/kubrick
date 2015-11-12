package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.skittlesdev.kubrick.asyncs.GetEpisodesSeriesInfo;
import com.github.skittlesdev.kubrick.asyncs.GetSeasonEpisodeTask;
import com.github.skittlesdev.kubrick.asyncs.GetSeriesTask;
import com.github.skittlesdev.kubrick.customsWrapperTypes.CustomTvEpisode;
import com.github.skittlesdev.kubrick.customsWrapperTypes.CustomTvSeason;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.events.LoginEvent;
import com.github.skittlesdev.kubrick.events.LogoutEvent;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.interfaces.TvEpisodesSeriesInfo;
import com.github.skittlesdev.kubrick.interfaces.TvSeasonListener;
import com.github.skittlesdev.kubrick.ui.calendar.decorators.CalendarViewSeriesPlanningDecoratorNextEpisodes;
import com.github.skittlesdev.kubrick.ui.calendar.decorators.CalendarViewSeriesPlanningDecoratorNoEpisode;
import com.github.skittlesdev.kubrick.ui.calendar.decorators.CalendarViewSeriesPlanningDecoratorPassedEpisodes;
import com.github.skittlesdev.kubrick.ui.calendar.decorators.CalendarViewSeriesPlanningDecoratorToday;
import com.github.skittlesdev.kubrick.ui.fragments.FavoritesOverviewFragment;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.github.skittlesdev.kubrick.utils.CalendarViewUtils.CalendarViewUtils;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.parse.*;
import com.parse.ParseUser;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, TvSeasonListener, OnDateSelectedListener, TvEpisodesSeriesInfo {
    private ParseUser user;
    private boolean followed = false;
    private  MaterialCalendarView calendar;
    private List<CustomTvEpisode> tvEpisodesList;
    private List<TvSeason> tvSeasonsList;
    private Map<Integer, List<CustomTvEpisode>> map;
    private Map<Integer, CustomTvSeason> customTvSeasonMap;
    private List<List<TvEpisode>> finalList;
    private ArrayList<TvEpisode> result;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        KubrickApplication.getEventBus().register(this);

        final Button toggle = (Button) findViewById(R.id.followToggle);
        toggle.setOnClickListener(this);

        final LinearLayout followersLayout = (LinearLayout) findViewById(R.id.followersLayout);
        followersLayout.setOnClickListener(this);

        final LinearLayout followingsLayout = (LinearLayout) findViewById(R.id.followingsLayout);
        followingsLayout.setOnClickListener(this);

        calendar = (MaterialCalendarView) findViewById(R.id.userSerieCalendarPlanning);
        calendar.setOnDateChangedListener(this);
        calendar.setVisibility(View.VISIBLE);

        String user_id;
        if (getIntent().hasExtra("user_id")) {
            user_id = getIntent().getStringExtra("user_id");
        }
        else {
            user_id = getIntent().getData().getPathSegments().get(0);
        }

        ParseUser.getQuery().getInBackground(user_id, new GetCallback<ParseUser>() {

            @Override
            public void done(ParseUser user, ParseException e) {
                buildProfile(user);
                buildFollowStats(user);
                setUser(user);
                getFollowStatus(user);

                FavoritesOverviewFragment movieFavorites = new FavoritesOverviewFragment();
                Bundle movieFavoritesArgs = new Bundle();
                movieFavoritesArgs.putString("user_id", user.getObjectId());
                movieFavoritesArgs.putString("MEDIA_TYPE", "movie");
                movieFavorites.setArguments(movieFavoritesArgs);

                FavoritesOverviewFragment seriesFavorites = new FavoritesOverviewFragment();
                Bundle seriesFavoritesArgs = new Bundle();
                seriesFavoritesArgs.putString("user_id", user.getObjectId());
                seriesFavoritesArgs.putString("MEDIA_TYPE", "tv");
                seriesFavorites.setArguments(seriesFavoritesArgs);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ViewedTvSeriesEpisodes");

                query.whereEqualTo("User", user);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> EpisodeList, ParseException e) {
                        if (e == null) {
                            tvEpisodesList = new ArrayList<>();

                            for (ParseObject item : EpisodeList) {
                                CustomTvEpisode customTvEpisode = new CustomTvEpisode(
                                        item.getString("AirDate"),
                                        item.getInt("SerieId"),
                                        item.getInt("EpisodeNumber"),
                                        item.getInt("SeasonNumber"),
                                        item.getInt("EpisodeId"));

                                tvEpisodesList.add(customTvEpisode);
                            }
                            buildCalendar(tvEpisodesList);

                            Log.d("Viewed episodes data", "Retrieved " + EpisodeList.size() + " episodes");
                        } else {
                            Log.d("Viewed episodes data", "Error: " + e.getMessage());
                        }
                    }
                });

                transaction.replace(R.id.fragment_movies, movieFavorites, "movies");
                transaction.replace(R.id.fragment_series, seriesFavorites, "series");
                transaction.commitAllowingStateLoss();
            }
        });


    }

    @Override
    public void onTvSeasonRetrieved(List<TvSeason> tvSeasonList) {

        this.tvSeasonsList = tvSeasonList;
        customTvSeasonMap = new HashMap<>();

        for(TvSeason item : tvSeasonList){

            customTvSeasonMap.put(item.getExternalIds().getId(),new CustomTvSeason(item.getId(),item.getSeasonNumber(),item.getExternalIds().getId(),item.getPosterPath(),item.getEpisodes()));

        }

        finalList = new ArrayList<>();

        for(CustomTvEpisode item : tvEpisodesList){
            List<TvEpisode> subList;

            subList = removeEpisodesBeforeDate(customTvSeasonMap.get(item.getSerieId()).getTvEpisodes(), item.getAirDate());

            finalList.add(subList);
        }

        Set<List<TvEpisode>> hs = new HashSet<>();
        hs.addAll(finalList);
        finalList.clear();
        finalList.addAll(hs);

        HashSet<CalendarDay> result = new HashSet<>();

        for (List<TvEpisode> tvEpisodeList : finalList){
            result.addAll(CalendarViewUtils.tvEpiosdeListToAirDateHash(tvEpisodeList));
        }

        calendar.addDecorators(
                new CalendarViewSeriesPlanningDecoratorNoEpisode(Color.WHITE, result),
                new CalendarViewSeriesPlanningDecoratorPassedEpisodes(Color.GRAY, result),
                new CalendarViewSeriesPlanningDecoratorNextEpisodes(Color.RED, result),
                new CalendarViewSeriesPlanningDecoratorToday(Color.WHITE)
        );
    }


    @Override
    public void onDateSelected(MaterialCalendarView materialCalendarView, CalendarDay calendarDay, boolean b) {

        String date = calendarDay.getYear() + "-" + (calendarDay.getMonth() + 1) + "-" + calendarDay.getDay();

        result = new ArrayList<>();

        for (List<TvEpisode> tvEpisodeList : finalList){
            for(TvEpisode episode : tvEpisodeList){
                if(episode.getAirDate().compareTo(date) == 0){
                    result.add(episode);
                }
            }
        }

        ArrayList<Integer> serieIdList = new ArrayList<>();

        boolean found = false;
        int j = 0;
        int k = 0;

        if(!result.isEmpty()) {
            for (TvEpisode item : result) {
                j = 0;
                found = false;
                while (!found && j < tvSeasonsList.size()) {
                    Log.d("episodeList", String.valueOf(j));
                    TvSeason tvSeason = tvSeasonsList.get(j);
                    k=0;
                    while(! found && k < tvSeason.getEpisodes().size()){
                        TvEpisode tvEpisode = tvSeason.getEpisodes().get(k);
                        Log.d("episodeList", tvEpisode.getName());
                        if (tvEpisode.getId() == item.getId()) {
                            found = true;
                            serieIdList.add(tvSeason.getExternalIds().getId());
                        }
                        k++;
                    }
                    j++;
                }
            }

            if (!serieIdList.isEmpty()) {
                GetEpisodesSeriesInfo getSeriesInfoTask = new GetEpisodesSeriesInfo(this);
                getSeriesInfoTask.execute(serieIdList);
            }
        }
        System.out.print("ee");
        // Afficher l'activité avec en intent la liste result.
    }

    @Override
    public void onTvEpisodesSeriesInfo(List<TvSeries> tvSeriesList) {

        ArrayList<TvSeries> tvSeriesArrayList = new ArrayList<>();
        tvSeriesArrayList.addAll(tvSeriesList);

        Bundle bundle = new Bundle();

        bundle.putSerializable("result", result);
        bundle.putSerializable("resultSeries", tvSeriesArrayList);

        Intent intent = new Intent(this, DayEpisodeActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);


    }

    private List<TvEpisode> removeEpisodesBeforeDate(List<TvEpisode> tvEpisodes, String date){

        String[] dateOrig = date.split("-");
        int size = tvEpisodes.size();

        for(int i = 0; i < tvEpisodes.size()-1; i++){
            if(tvEpisodes.get(i) != null && !tvEpisodes.get(i).getAirDate().isEmpty()){
                String[] split = tvEpisodes.get(i).getAirDate().split("-");

                if(Integer.valueOf(split[0]) < Integer.valueOf(dateOrig[0])){
                    tvEpisodes.set(i, null);
                }else if(Integer.valueOf(split[1]) < Integer.valueOf(dateOrig[1])){
                    tvEpisodes.set(i, null);
                } else if(Integer.valueOf(split[2]) < Integer.valueOf(dateOrig[2])){
                    tvEpisodes.set(i, null);
                }
            }
        }
        tvEpisodes.removeAll(Collections.singleton(null));
        return tvEpisodes;
    }

    private void buildCalendar(List<CustomTvEpisode> customTvEpisodesList){

        map = new HashMap<>();

        for(CustomTvEpisode customTvEpisode : customTvEpisodesList){

            if(map.get(customTvEpisode.getSerieId()) == null){
                List<CustomTvEpisode> subList = new ArrayList<>();
                subList.add(customTvEpisode);
                map.put(customTvEpisode.getSerieId(), subList);
            }else{
                List<CustomTvEpisode> subList = map.get(customTvEpisode.getSerieId());
                subList.add(customTvEpisode);
            }
        }

        Iterator it = map.values().iterator();

        List<Integer> serieIdArray = new ArrayList<>();
        List<Integer> seasonNumber = new ArrayList<>();

        while(it.hasNext()){
            List<CustomTvEpisode> iteratorItem = (List<CustomTvEpisode>) it.next();
            serieIdArray.add(iteratorItem.get(0).getSerieId());
            seasonNumber.add(iteratorItem.get(0).getSeasonNumber());
        }

        GetSeasonEpisodeTask getSeasonEpisodeTask = new GetSeasonEpisodeTask(this);
        getSeasonEpisodeTask.execute(serieIdArray, seasonNumber);

    }


    private void buildProfile(ParseUser user) {
        ProfileElement profile = new ProfileElement(user);

        TextView name = (TextView) findViewById(R.id.name);
        SimpleDraweeView image = (SimpleDraweeView) findViewById(R.id.circleView);

        name.setText(profile.getName());
        if (!TextUtils.isEmpty(profile.getAvatarUrl())) {
            image.setImageURI(Uri.parse(profile.getAvatarUrl()));
        }
    }

    private void buildFollowStats(ParseUser user) {
        ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery("Follow");
        followersQuery.whereEqualTo("other_user", user);
        followersQuery.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {
                    setFollowers(count);
                }
            }
        });

        ParseQuery<ParseObject> followingsQuery = ParseQuery.getQuery("Follow");
        followingsQuery.whereEqualTo("user", user);
        followingsQuery.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null) {
                    setFollowings(count);
                }
            }
        });
    }

    private void setFollowings(int count) {
        TextView view = (TextView) findViewById(R.id.following_number);
        view.setText(String.valueOf(count));
    }

    private void setFollowers(int count) {
        TextView view = (TextView) findViewById(R.id.followers_number);
        view.setText(String.valueOf(count));
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

    public void getFollowStatus(ParseUser user) {
        if (ParseUser.getCurrentUser() == null || user.getObjectId().compareTo(ParseUser.getCurrentUser().getObjectId()) == 0) {
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("other_user", user);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object != null) {
                    setFollowed(true);
                } else {
                    setFollowed(false);
                }
            }
        });
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
        final Button toggle = (Button) findViewById(R.id.followToggle);

        if (!this.followed) {
            toggle.setText("FOLLOW");
        }
        else {
            toggle.setText("UNFOLLOW");
        }

        toggle.setVisibility(View.VISIBLE);
    }

    public void toggleFollow() {
        if (!this.followed) {
            ParseObject follow = new ParseObject("Follow");
            follow.put("user", ParseUser.getCurrentUser());
            follow.put("other_user", this.user);
            follow.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        setFollowed(true);
                    }
                }
            });
        }
        else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereEqualTo("other_user", this.user);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    setFollowed(false);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.followToggle) {
            toggleFollow();
        }

        if (v.getId() == R.id.followersLayout) {
            showFollowers();
        }

        if (v.getId() == R.id.followingsLayout) {
            showFollowings();
        }
    }

    private void showFollowers() {
        Intent intent = new Intent(this, ProfileRelationsActivity.class);
        intent.putExtra("user_id", this.user.getObjectId());
        intent.putExtra("type", ProfileRelationsActivity.Types.FOLLOWERS);
        startActivity(intent);
    }

    private void showFollowings() {
        Intent intent = new Intent(this, ProfileRelationsActivity.class);
        intent.putExtra("user_id", this.user.getObjectId());
        intent.putExtra("type", ProfileRelationsActivity.Types.FOLLOWINGS);
        startActivity(intent);
    }

    public void onEvent(LoginEvent e) {
        getFollowStatus(this.user);
    }

    public void onEvent(LogoutEvent e) {
        final Button toggle = (Button) findViewById(R.id.followToggle);
        toggle.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KubrickApplication.getEventBus().unregister(this);
    }
}
