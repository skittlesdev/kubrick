package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import com.github.skittlesdev.kubrick.asyncs.GetMovieTask;
import com.github.skittlesdev.kubrick.interfaces.MovieListener;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.squareup.picasso.Picasso;
import info.movito.themoviedbapi.model.MovieDb;
import org.joda.time.Duration;
import org.joda.time.format.*;

public class MovieActivity extends Activity implements MovieListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_movie);

        this.setActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        GetMovieTask task = new GetMovieTask(this);
        int movieId = this.getIntent().getIntExtra("ITEM_ID", -1);
        task.execute(movieId);
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

    @Override
    public void onMovieRetrieved(MovieDb movie) {
        Picasso.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .into((ImageView) findViewById(R.id.poster));

        TextView titleView = (TextView) findViewById(R.id.title);
        TextView durationView = (TextView) findViewById(R.id.duration);
        TextView overviewView = (TextView) findViewById(R.id.overview);

        String year = "(" + movie.getReleaseDate().split("-")[0] + ")";

        titleView.setText(movie.getTitle() + " " + year);

        Duration duration = new Duration(movie.getRuntime() * 1000 * 60);
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendSuffix(":")
                .minimumPrintedDigits(2)
                .appendMinutes()
                .toFormatter();

        String durationDisplay = formatter.print(duration.toPeriod());

        durationView.setText(durationDisplay);

        overviewView.setText(movie.getOverview());
    }
}
