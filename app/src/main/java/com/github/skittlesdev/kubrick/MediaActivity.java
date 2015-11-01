package com.github.skittlesdev.kubrick;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.github.skittlesdev.kubrick.asyncs.GetMovieTask;
import com.github.skittlesdev.kubrick.asyncs.GetSeriesTask;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.github.skittlesdev.kubrick.utils.CastUtils;
import com.github.skittlesdev.kubrick.utils.GenresUtils;
import com.squareup.picasso.Picasso;

import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;
import org.joda.time.Duration;
import org.joda.time.format.*;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.List;

public class MediaActivity extends AppCompatActivity implements MediaListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_movie);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        ((ProgressActivity) findViewById(R.id.progressActivity)).showLoading();

        int mediaId = this.getIntent().getIntExtra("MEDIA_ID", -1);

        if (this.getIntent().getStringExtra("MEDIA_TYPE").compareTo("tv") == 0) {
            GetSeriesTask task = new GetSeriesTask(this);
            task.execute(mediaId);
        }
        else {
            GetMovieTask task = new GetMovieTask(this);
            task.execute(mediaId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

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
        TextView castView = (TextView) findViewById(R.id.cast);

        Credits credits;
        if (media instanceof MovieDb) {
            credits = ((MovieDb) media).getCredits();
            castView.setText("Cast : " + CastUtils.getCastPrintableString(credits.getCast()) + "\n" + "Crew : " + CastUtils.getCrewPrintableString(credits.getCrew()));

        } else {
            credits = ((TvSeries) media).getCredits();
            castView.setText("Cast : " + CastUtils.getCastPrintableString(credits.getCast()) + "\n");
        }

    }

    @Override
    public void onMediaRetrieved(IdElement media) {
        showPoster(media);
        showTitle(media);
        showGenres(media);
        showCast(media);

        if (media instanceof MovieDb) {
            showDuration((MovieDb) media);
        }
        else {
            showStats((TvSeries) media);
        }

        showOverview(media);

        ((ProgressActivity) findViewById(R.id.progressActivity)).showContent();
    }
}
