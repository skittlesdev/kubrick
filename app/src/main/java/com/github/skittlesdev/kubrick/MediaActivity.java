package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.skittlesdev.kubrick.asyncs.GetMovieTask;
import com.github.skittlesdev.kubrick.asyncs.GetSeriesInfo;
import com.github.skittlesdev.kubrick.asyncs.GetSeriesTask;
import com.github.skittlesdev.kubrick.events.FavoriteStateEvent;
import com.github.skittlesdev.kubrick.events.LoginEvent;
import com.github.skittlesdev.kubrick.events.LogoutEvent;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.interfaces.TvSeriesSeasonsListener;
import com.github.skittlesdev.kubrick.models.SeriesEpisode;
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

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MediaActivity extends AppCompatActivity implements MediaListener, View.OnClickListener, TvSeriesSeasonsListener, OnDateSelectedListener{
    private AsyncTask task;
    private GetSeriesInfo seriesInfoTask;
    private int mediaId;
    private IdElement media;
    private List<SeriesEpisode> episodes;
    private FavoriteState favoriteState;
    private MaterialCalendarView calendar;
    private boolean backPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_media);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        KubrickApplication.getEventBus().register(this);

        FloatingActionButton favoriteFab = (FloatingActionButton) this.findViewById(R.id.favoriteFab);
        favoriteFab.setOnClickListener(this);

        String mediaType;

        if (getIntent().hasExtra("MEDIA_ID") && getIntent().hasExtra("MEDIA_TYPE")) {
            this.mediaId = getIntent().getIntExtra("MEDIA_ID", -1);
            mediaType = getIntent().getStringExtra("MEDIA_TYPE");
        }
        else {
            List<String> segments = this.getIntent().getData().getPathSegments();
            mediaType = segments.get(0);
            this.mediaId = Integer.valueOf(segments.get(1));
        }

        calendar = (MaterialCalendarView) findViewById(R.id.seriesPlanningCalendarView);
        calendar.setOnDateChangedListener(this);
        calendar.setSelectionColor(getResources().getColor(R.color.light_orange));

        if (mediaType.compareTo("tv") == 0) {
            this.task = new GetSeriesTask(this);
            ((GetSeriesTask) this.task).execute(this.mediaId);
        }
        else {
            this.task = new GetMovieTask(this);
            ((GetMovieTask) this.task).execute(this.mediaId);
        }
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
    public void onDestroy() {
        super.onDestroy();
        KubrickApplication.getEventBus().unregister(this);
        if (this.task != null) {
            this.task.cancel(true);
        }
        if (this.seriesInfoTask != null) {
            this.seriesInfoTask.cancel(true);
        }
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
                    setFavoriteFabIcon(new FavoriteStateEvent(FavoriteState.OFF));
                } else {
                    setFavoriteFabIcon(new FavoriteStateEvent(FavoriteState.ON));
                }
            }
        });
    }

    public void displaySerieEpisodesCalendar(final TvSeries tvSeries){
        HashMap<String, String> params = new HashMap<>();
        params.put("seriesId", String.valueOf(tvSeries.getId()));
        ParseCloud.callFunctionInBackground("getSeriesEpisodes", params, new FunctionCallback<List<HashMap<String, Object>>>() {
            @Override
            public void done(List<HashMap<String, Object>> results, ParseException e) {
                List<SeriesEpisode> episodes = new LinkedList<>();
                for (HashMap<String, Object> result : results) {
                    episodes.add(new SeriesEpisode(tvSeries, result));
                }

                setEpisodes(episodes);

                calendar.addDecorators(
                        new CalendarViewSeriesPlanningDecoratorNoEpisode(Color.DKGRAY, CalendarViewUtils.tvSeriesToEpisodeAirDate(episodes)),
                        new CalendarViewSeriesPlanningDecoratorPassedEpisodes(Color.GRAY, CalendarViewUtils.tvSeriesToEpisodeAirDate(episodes)),
                        new CalendarViewSeriesPlanningDecoratorNextEpisodes(R.color.light_orange, CalendarViewUtils.tvSeriesToEpisodeAirDate(episodes)),
                        new CalendarViewSeriesPlanningDecoratorToday(Color.WHITE)
                );

                calendar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onTvSeriesSeasonsRetrieved(TvSeries tvSeries) {

    }

    @Override
    public void onClick(View v) {
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

    private void showBackdrop(IdElement mMedia) {
        String backdrop;

        if (mMedia instanceof MovieDb) {
            backdrop = ((MovieDb) mMedia).getBackdropPath();
        }
        else {
            backdrop = ((TvSeries) mMedia).getBackdropPath();
        }

        ((SimpleDraweeView) findViewById(R.id.movieBackDropPicture)).setImageURI(Uri.parse("http://image.tmdb.org/t/p/w780" + backdrop));
    }

    private void showSimilar(IdElement media) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        final SimilarMoviesOverviewFragment similarFragment = new SimilarMoviesOverviewFragment();
        final Bundle similarFragmentArgs = new Bundle();

        if (media instanceof MovieDb) {
            similarFragmentArgs.putString("type", "movie");
            List<MovieDb> similarMoviesList = ((MovieDb) media).getSimilarMovies();
            ArrayList<MovieDb> similarMoviesArrayList = new ArrayList<>(similarMoviesList.size());
            similarMoviesArrayList.addAll(similarMoviesList);
            similarFragmentArgs.putSerializable("movies", similarMoviesArrayList);

            similarFragment.setArguments(similarFragmentArgs);
            transaction.add(R.id.fragment_similar, similarFragment, "similar");
            transaction.commit();
        }
        else {
            similarFragmentArgs.putString("type", "tv");

            HashMap<String, String> params = new HashMap<>();
            params.put("seriesId", String.valueOf(media.getId()));
            ParseCloud.callFunctionInBackground("similarSeries", params, new FunctionCallback<HashMap>() {
                @Override
                public void done(HashMap results, ParseException e) {
                    if (e == null) {
                        similarFragmentArgs.putSerializable("series", results);

                        similarFragment.setArguments(similarFragmentArgs);
                        transaction.add(R.id.fragment_similar, similarFragment, "similar");
                        transaction.commit();
                    }
                }
            });
        }
    }

    @Override
    public void onMediaRetrieved(IdElement media) {
        this.media = media;

        this.showBackdrop(this.media);
        showSimilar(this.media);

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
        TvSeries serie = (TvSeries) this.media;

        SeriesEpisode tvEpisode =  CalendarViewUtils.getEpisodeFromDate(date, this.episodes);

        if (tvEpisode != null) {

            Intent intent = new Intent(getApplicationContext(), SerieEpisodeActivity.class);
            Bundle bundle = new Bundle();

            bundle.putInt("episode_id", tvEpisode.id);
            bundle.putSerializable("tvEpisode", tvEpisode);
            bundle.putString("seriePoster", serie.getPosterPath());
            bundle.putString("serieBackdrop", serie.getBackdropPath());

            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

    public void setFavoriteFabIcon(FavoriteStateEvent event) {
        final FloatingActionButton toggleView = (FloatingActionButton) findViewById(R.id.favoriteFab);
        CoordinatorLayout.LayoutParams toggleParams = (CoordinatorLayout.LayoutParams) toggleView.getLayoutParams();
        toggleParams.setAnchorId(R.id.app_bar_layout);

        if (event.getFavoriteState() == FavoriteState.OFF) {
            this.favoriteState = FavoriteState.OFF;
            toggleView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart));
        }
        if (event.getFavoriteState() == FavoriteState.ON) {
            this.favoriteState = FavoriteState.ON;
            toggleView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart_broken));
        }

        toggleView.setLayoutParams(toggleParams);
        toggleView.setVisibility(View.VISIBLE);
    }

    public void onEvent(LoginEvent event) {
        getFavoriteStatus();
    }

    public void onEvent(LogoutEvent event) {
        final FloatingActionButton toggleView = (FloatingActionButton) findViewById(R.id.favoriteFab);
        toggleView.setVisibility(View.GONE);
        CoordinatorLayout.LayoutParams toggleParams = (CoordinatorLayout.LayoutParams) toggleView.getLayoutParams();
        toggleParams.setAnchorId(View.NO_ID);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) { // SMALL FIX
        try {
            /*
            *
            * WE MUST DO THIS because sometime a org.json.JSONObject.NULL has to be
            * serialized (received by the TMDB wrapper), and it is not working
            * because org.json.JSONObject.NULL is no serializable.
            *
            * */
            // super.onSaveInstanceState(state); catch not taken?
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEpisodes(List<SeriesEpisode> episodes) {
        this.episodes = episodes;
    }
}
