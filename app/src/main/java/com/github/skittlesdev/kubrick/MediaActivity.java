package com.github.skittlesdev.kubrick;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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
import com.github.skittlesdev.kubrick.ui.fragments.CreditsOverviewFragment;
import com.github.skittlesdev.kubrick.ui.fragments.TvEpisodeFragment;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.github.skittlesdev.kubrick.utils.CalendarViewUtils.CalendarViewUtils;
import com.github.skittlesdev.kubrick.utils.FavoriteState;
import com.github.skittlesdev.kubrick.utils.GenresUtils;
import com.parse.*;
import com.prolificinteractive.materialcalendarview.CalendarUtils;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.squareup.picasso.Picasso;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.joda.time.Duration;
import org.joda.time.format.*;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.List;

public class MediaActivity extends AppCompatActivity implements MediaListener, View.OnClickListener, TvSeriesSeasonsListener, CalendarView.OnDateChangeListener {
    private int mediaId;
    private IdElement media;
    private FavoriteState favoriteState;
    private MaterialCalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_media);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        KubrickApplication.getEventBus().register(this);
        ((ProgressActivity) findViewById(R.id.progressActivity)).showLoading();

        final Button toggleView = (Button) findViewById(R.id.favoriteToggle);
        toggleView.setOnClickListener(this);

        this.mediaId = this.getIntent().getIntExtra("MEDIA_ID", -1);

        if (this.getIntent().getStringExtra("MEDIA_TYPE").compareTo("tv") == 0) {
            GetSeriesTask task = new GetSeriesTask(this);
            task.execute(this.mediaId);
        }
        else {
            GetMovieTask task = new GetMovieTask(this);
            task.execute(this.mediaId);
        }

        calendar = (MaterialCalendarView) findViewById(R.id.seriesPlanningCalendarView);

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

    private void showPoster(IdElement media) {
        String posterPath;

        if (media instanceof MovieDb) {
            posterPath = ((MovieDb) media).getPosterPath();
        }
        else {
            posterPath = ((TvSeries) media).getPosterPath();
        }

        Picasso.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + posterPath)
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) findViewById(R.id.poster));
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

    private void showDuration(MovieDb movie) {
        TextView durationView = (TextView) findViewById(R.id.duration);

        Duration duration = new Duration(movie.getRuntime() * 1000 * 60);
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendSuffix(":")
                .minimumPrintedDigits(2)
                .appendMinutes()
                .toFormatter();

        String durationDisplay = formatter.print(duration.toPeriod());

        durationView.setText(durationDisplay);
    }

    private void showOverview(IdElement media) {
        TextView overviewView = (TextView) findViewById(R.id.overview);

        String overview;
        if (media instanceof MovieDb) {
            overview = ((MovieDb) media).getOverview();
        }
        else {
            overview = ((TvSeries) media).getOverview();
        }

        overviewView.setText(overview);
    }

    private void showStats(TvSeries media) {
        TextView durationView = (TextView) findViewById(R.id.duration);
        durationView.setText(media.getNumberOfSeasons() + " seasons, " + media.getNumberOfEpisodes() + " episodes");
    }

    private void showGenres(IdElement media){
        TextView genresView = (TextView) findViewById(R.id.genres);

        List<Genre> genres;
        if (media instanceof MovieDb) {
            genres = ((MovieDb) media).getGenres();
        }
        else {
            genres = ((TvSeries) media).getGenres();
        }

        genresView.setText(GenresUtils.getGenrePrintableString(genres));
    }

    private void showCast(IdElement media) {
        CreditsOverviewFragment castFragment = new CreditsOverviewFragment();
        CreditsOverviewFragment crewFragment = new CreditsOverviewFragment();

        Bundle castFragmentArgs = new Bundle();
        castFragmentArgs.putString("type", "cast");

        Bundle crewFragmentArgs = new Bundle();
        crewFragmentArgs.putString("type", "crew");

        if (media instanceof MovieDb) {
            castFragmentArgs.putSerializable("credits", ((MovieDb) media).getCredits());
            crewFragmentArgs.putSerializable("credits", ((MovieDb) media).getCredits());
        }
        else {
            castFragmentArgs.putSerializable("credits", ((TvSeries) media).getCredits());
            crewFragmentArgs.putSerializable("credits", ((TvSeries) media).getCredits());
        }

        castFragment.setArguments(castFragmentArgs);
        crewFragment.setArguments(crewFragmentArgs);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_cast, castFragment, "cast");
        if (media instanceof MovieDb) {
            transaction.add(R.id.fragment_crew, crewFragment, "crew");
        }
        transaction.commit();
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

        calendar.addDecorators(
                new CalendarViewSeriesPlanningDecoratorNoEpisode(Color.GRAY, CalendarViewUtils.tvSeriesToEpisodeAirDate(tvSeries)),
                new CalendarViewSeriesPlanningDecoratorPassedEpisodes(Color.WHITE, CalendarViewUtils.tvSeriesToEpisodeAirDate(tvSeries)),
                new CalendarViewSeriesPlanningDecoratorNextEpisodes(Color.RED, CalendarViewUtils.tvSeriesToEpisodeAirDate(tvSeries)),
                new CalendarViewSeriesPlanningDecoratorToday(Color.WHITE)
        );
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

    @Override
    public void onMediaRetrieved(IdElement media) {
        this.media = media;

        showPoster(media);
        showTitle(media);
        showGenres(media);
        showCast(media);

        if (media instanceof MovieDb) {
            showDuration((MovieDb) media);
        }
        else {
            showStats((TvSeries) media);
            displaySerieEpisodesCalendar((TvSeries) media);
        }

        showOverview(media);
        getFavoriteStatus();

        ((ProgressActivity) findViewById(R.id.progressActivity)).showContent();
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("tweet_item_transaction"); // Needed to be able to go back to the tweet list with the back button, if not, app will go back to precedent activity, loginActivity in this case
        fragmentTransaction.replace(R.id.homeDrawerLayout, TvEpisodeFragment.newInstance(tweet));
        fragmentTransaction.commit();*/

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
        KubrickApplication.getEventBus().post(event);
    }

    public void onEvent(LoginEvent event) {
        getFavoriteStatus();
    }

    public void onEvent(LogoutEvent event) {
        final Button toggleView = (Button) findViewById(R.id.favoriteToggle);
        toggleView.setVisibility(View.GONE);
    }
}
