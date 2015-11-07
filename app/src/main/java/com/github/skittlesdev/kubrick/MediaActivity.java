package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.github.skittlesdev.kubrick.asyncs.GetMovieTask;
import com.github.skittlesdev.kubrick.asyncs.GetSeriesInfo;
import com.github.skittlesdev.kubrick.asyncs.GetSeriesTask;
import com.github.skittlesdev.kubrick.events.FavoriteStateEvent;
import com.github.skittlesdev.kubrick.events.LoginEvent;
import com.github.skittlesdev.kubrick.events.LogoutEvent;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.interfaces.TvSeriesSeasonsListener;
import com.github.skittlesdev.kubrick.ui.calendar.decorators.CalendarViewSeriesPlanningDecoratorNextEpisodes;
import com.github.skittlesdev.kubrick.ui.calendar.decorators.CalendarViewSeriesPlanningDecoratorNoEpisode;
import com.github.skittlesdev.kubrick.ui.calendar.decorators.CalendarViewSeriesPlanningDecoratorPassedEpisodes;
import com.github.skittlesdev.kubrick.ui.calendar.decorators.CalendarViewSeriesPlanningDecoratorToday;
import com.github.skittlesdev.kubrick.ui.fragments.*;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.github.skittlesdev.kubrick.utils.CalendarViewUtils.CalendarViewUtils;
import com.github.skittlesdev.kubrick.utils.FavoriteState;
import com.parse.*;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class MediaActivity extends AppCompatActivity implements MediaListener, View.OnClickListener, TvSeriesSeasonsListener, OnDateSelectedListener{
    private int mediaId;
    private IdElement media;
    private FavoriteState favoriteState;
    private MaterialCalendarView calendar;
    private boolean backPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_media);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        KubrickApplication.getEventBus().register(this);

        final Button toggleView = (Button) findViewById(R.id.favoriteToggle);
        toggleView.setOnClickListener(this);

        this.mediaId = this.getIntent().getIntExtra("MEDIA_ID", -1);

        calendar = (MaterialCalendarView) findViewById(R.id.seriesPlanningCalendarView);
        calendar.setOnDateChangedListener(this);
        calendar.setSelectionColor(Color.YELLOW);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (this.getIntent().getStringExtra("MEDIA_TYPE").compareTo("tv") == 0) {
            GetSeriesTask task = new GetSeriesTask(this);
            task.execute(this.mediaId);
        }
        else {
            GetMovieTask task = new GetMovieTask(this);
            task.execute(this.mediaId);
        }

        FloatingActionButton favoriteFab = (FloatingActionButton) this.findViewById(R.id.favoriteFab);
        favoriteFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();

                if (context instanceof MediaActivity) {
                    ((MediaActivity) context).handleFavorite(v);
                }
            }
        });
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

    private void showTitle(IdElement media) {
        TextView titleView = (TextView) findViewById(R.id.title);
        if (media instanceof MovieDb) {
            String title = ((MovieDb) media).getTitle();
            String year = "(" + ((MovieDb) media).getReleaseDate().split("-")[0] + ")";
            titleView.setText(title + " " + year);
        }
        else {
            String title = ((TvSeries) media).getName();
            String firstYear = ((TvSeries) media).getFirstAirDate().split("-")[0];
            String lastYear = ((TvSeries) media).getLastAirDate().split("-")[0];
            titleView.setText(title + " (" + firstYear + " - " + lastYear + ")");
        }
    }

    private void getFavoriteStatus() {
        if (ParseUser.getCurrentUser() == null) {
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("user", ParseUser.getCurrentUser());

        if (this.media instanceof MovieDb) {
            query.whereEqualTo("tmdb_movie_id", this.mediaId);
        }
        else {
            query.whereEqualTo("tmdb_series_id", this.mediaId);
        }

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    favoriteStateChange(new FavoriteStateEvent(FavoriteState.OFF));
                } else {
                    favoriteStateChange(new FavoriteStateEvent(FavoriteState.ON));
                }
            }
        });
    }

    public void displaySerieEpisodesCalendar(TvSeries tvSeries){
        GetSeriesInfo task = new GetSeriesInfo(this);
        task.execute(this.mediaId);
    }

    @Override
    public void onTvSeriesSeasonsRetrieved(TvSeries tvSeries) {
        calendar.setVisibility(View.VISIBLE);

        this.media = tvSeries;

        calendar.addDecorators(
                new CalendarViewSeriesPlanningDecoratorNoEpisode(Color.BLACK, CalendarViewUtils.tvSeriesToEpisodeAirDate(tvSeries)),
                new CalendarViewSeriesPlanningDecoratorPassedEpisodes(Color.GREEN, CalendarViewUtils.tvSeriesToEpisodeAirDate(tvSeries)),
                new CalendarViewSeriesPlanningDecoratorNextEpisodes(Color.RED, CalendarViewUtils.tvSeriesToEpisodeAirDate(tvSeries)),
                new CalendarViewSeriesPlanningDecoratorToday(Color.RED)
        );
    }

    public void handleFavorite(View v) {
        if (v.getId() == R.id.favoriteFab) {
            if (this.favoriteState == FavoriteState.OFF) {
                ParseObject favorite = new ParseObject("Favorite");
                ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(false);

                favorite.put("user", ParseUser.getCurrentUser());

                if (this.media instanceof MovieDb) {
                    favorite.put("tmdb_movie_id", this.mediaId);
                    favorite.put("title", ((MovieDb) this.media).getTitle());
                    favorite.put("poster_path", ((MovieDb) this.media).getPosterPath());
                }
                else {
                    favorite.put("tmdb_series_id", this.mediaId);
                    favorite.put("title", ((TvSeries) this.media).getName());
                    favorite.put("poster_path", ((TvSeries) this.media).getPosterPath());
                }

                favorite.setACL(acl);
                favorite.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            setFavoriteFabIcon(new FavoriteStateEvent(FavoriteState.ON));
                        }
                        else {
                            Toast.makeText(KubrickApplication.getContext(), "Failed to favorite movie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
                query.whereEqualTo("user", ParseUser.getCurrentUser());

                if (this.media instanceof MovieDb) {
                    query.whereEqualTo("tmdb_movie_id", this.mediaId);
                }
                else {
                    query.whereEqualTo("tmdb_series_id", this.mediaId);
                }

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (object == null) {
                            Toast.makeText(KubrickApplication.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Toast.makeText(KubrickApplication.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        setFavoriteFabIcon(new FavoriteStateEvent(FavoriteState.OFF));
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.favoriteToggle) {
            if (this.favoriteState == FavoriteState.OFF) {
                ParseObject favorite = new ParseObject("Favorite");
                ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(false);

                favorite.put("user", ParseUser.getCurrentUser());

                if (this.media instanceof MovieDb) {
                    favorite.put("tmdb_movie_id", this.mediaId);
                    favorite.put("title", ((MovieDb) this.media).getTitle());
                    favorite.put("poster_path", ((MovieDb) this.media).getPosterPath());
                }
                else {
                    favorite.put("tmdb_series_id", this.mediaId);
                    favorite.put("title", ((TvSeries) this.media).getName());
                    favorite.put("poster_path", ((TvSeries) this.media).getPosterPath());
                }

                favorite.setACL(acl);
                favorite.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            favoriteStateChange(new FavoriteStateEvent(FavoriteState.ON));
                        }
                        else {
                            Toast.makeText(KubrickApplication.getContext(), "Failed to favorite movie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
                query.whereEqualTo("user", ParseUser.getCurrentUser());

                if (this.media instanceof MovieDb) {
                    query.whereEqualTo("tmdb_movie_id", this.mediaId);
                }
                else {
                    query.whereEqualTo("tmdb_series_id", this.mediaId);
                }

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (object == null) {
                            Toast.makeText(KubrickApplication.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            object.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Toast.makeText(KubrickApplication.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        favoriteStateChange(new FavoriteStateEvent(FavoriteState.OFF));
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void showBackdrop(IdElement mMedia) {
        String backdrop;

        if (mMedia instanceof MovieDb) {
            backdrop = ((MovieDb) mMedia).getBackdropPath();
        }
        else {
            backdrop = ((TvSeries) mMedia).getBackdropPath();
        }

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w500" + backdrop)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) this.findViewById(R.id.movieBackDropPicture));
    }

    @Override
    public void onMediaRetrieved(IdElement media) {
        this.media = media;

        this.showBackdrop(this.media);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (media instanceof MovieDb) {
            collapsingToolbar.setTitle(((MovieDb) this.media).getTitle());
        }
        else {
            collapsingToolbar.setTitle(((TvSeries) this.media).getName());
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle options = new Bundle();
        options.putSerializable("media", this.media);

        FragmentMovieHeader header = new FragmentMovieHeader();
        header.setArguments(options);

        FragmentMovieOverview overview = new FragmentMovieOverview();
        overview.setArguments(options);

        transaction.add(R.id.movieHeaderContainer, header);
        transaction.add(R.id.movieOverviewContainer, overview);

        CreditsOverviewFragment movieCast = new CreditsOverviewFragment();
        Bundle movieCastOptions = new Bundle();
        movieCastOptions.putString("type", "cast");

        if (media instanceof MovieDb) {
            movieCastOptions.putSerializable("credits", ((MovieDb) media).getCredits());
        }
        else {
            movieCastOptions.putSerializable("credits", ((TvSeries) media).getCredits());
        }

        movieCast.setArguments(movieCastOptions);
        transaction.add(R.id.movieCastContainer, movieCast, "movieCast");

        if (media instanceof MovieDb) {
            CreditsOverviewFragment movieCrew = new CreditsOverviewFragment();
            Bundle movieCrewOptions = new Bundle();
            movieCrewOptions.putString("type", "crew");
            movieCrewOptions.putSerializable("credits", ((MovieDb) this.media).getCredits());
            movieCrew.setArguments(movieCrewOptions);
            transaction.add(R.id.movieCrewContainer, movieCrew, "movieCrew");
        }

        if(!backPressed){
            transaction.commit();

            if (media instanceof TvSeries) {
                displaySerieEpisodesCalendar((TvSeries) media);
            }
        }

        getFavoriteStatus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            backPressed = true;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        Log.d("onSelectedDayChange", "day changed");

        TvEpisode tvEpisode =  CalendarViewUtils.getEpisodeFromDate(date, (TvSeries) media);

        if (tvEpisode != null) {

            Intent intent = new Intent(getApplicationContext(), SerieEpisodeActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("tvEpisode", tvEpisode);

            intent.putExtras(bundle);

            startActivity(intent);

        }
    }

    public void setFavoriteFabIcon(FavoriteStateEvent event) {
        final FloatingActionButton toggleView = (FloatingActionButton) findViewById(R.id.favoriteFab);
        if (event.getFavoriteState() == FavoriteState.OFF) {
            this.favoriteState = FavoriteState.OFF;
            toggleView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart));
        }
        if (event.getFavoriteState() == FavoriteState.ON) {
            this.favoriteState = FavoriteState.ON;
            toggleView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart_broken));
        }

        toggleView.setVisibility(View.VISIBLE);
    }


    public void favoriteStateChange(FavoriteStateEvent event) {
        final Button toggleView = (Button) findViewById(R.id.favoriteToggle);
        if (event.getFavoriteState() == FavoriteState.OFF) {
            this.favoriteState = FavoriteState.OFF;
            toggleView.setText("Add to favorites");
        }
        if (event.getFavoriteState() == FavoriteState.ON) {
            this.favoriteState = FavoriteState.ON;
            toggleView.setText("Delete from favorites");
        }
        toggleView.setVisibility(View.VISIBLE);
    }

    public void onEvent(LoginEvent event) {
        getFavoriteStatus();
    }

    public void onEvent(LogoutEvent event) {
        final Button toggleView = (Button) findViewById(R.id.favoriteToggle);
        toggleView.setVisibility(View.GONE);
    }
}
