package com.github.skittlesdev.kubrick;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.skittlesdev.kubrick.asyncs.GetMovieTask;
import com.github.skittlesdev.kubrick.interfaces.MovieListener;
import com.squareup.picasso.Picasso;
import info.movito.themoviedbapi.model.MovieDb;
import org.joda.time.Duration;
import org.joda.time.format.*;

public class MovieActivity extends Activity implements MovieListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_movie);

        GetMovieTask task = new GetMovieTask(this);
        int movieId = this.getIntent().getIntExtra("ITEM_ID", -1);
        task.execute(movieId);
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
