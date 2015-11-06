package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;
import com.github.skittlesdev.kubrick.asyncs.GetMovieTask;
import com.github.skittlesdev.kubrick.asyncs.GetSeriesTask;
import com.github.skittlesdev.kubrick.events.FavoriteStateEvent;
import com.github.skittlesdev.kubrick.events.LoginEvent;
import com.github.skittlesdev.kubrick.events.LogoutEvent;
import com.github.skittlesdev.kubrick.interfaces.MediaListener;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentMovieCast;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentMovieHeader;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentMovieOverview;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.github.skittlesdev.kubrick.utils.FavoriteState;
import com.parse.*;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.tv.TvSeries;

import com.squareup.picasso.Picasso;
import com.vlonjatg.progressactivity.ProgressActivity;

public class MediaActivity extends AppCompatActivity implements MediaListener, View.OnClickListener {
    private int mediaId;
    private IdElement media;
    private FavoriteState favoriteState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_movie);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();

        KubrickApplication.getEventBus().register(this);

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

    /*private void showStats(TvSeries media) {
        TextView durationView = (TextView) findViewById(R.id.duration);
        durationView.setText(media.getNumberOfSeasons() + " seasons, " + media.getNumberOfEpisodes() + " episodes");
    }*/

    private void getFavoriteStatus() {
        if (ParseUser.getCurrentUser() == null) {
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("tmdb_id", this.mediaId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    KubrickApplication.getEventBus().post(new FavoriteStateEvent(FavoriteState.OFF));
                }
                else {
                    KubrickApplication.getEventBus().post(new FavoriteStateEvent(FavoriteState.ON));
                }
            }
        });
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
                favorite.put("tmdb_id", this.mediaId);
                favorite.put("title", ((MovieDb) this.media).getTitle());
                favorite.setACL(acl);
                favorite.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            KubrickApplication.getEventBus().post(new FavoriteStateEvent(FavoriteState.ON));
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
                query.whereEqualTo("tmdb_id", this.mediaId);
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
                                        KubrickApplication.getEventBus().post(new FavoriteStateEvent(FavoriteState.OFF));
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

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.movieCastContainer, new FragmentMovieCast(media));
        transaction.add(R.id.movieHeaderContainer, new FragmentMovieHeader(media));
        transaction.add(R.id.movieOverviewContainer, new FragmentMovieOverview(media));
        transaction.commit();

        if (media instanceof MovieDb) {
            getFavoriteStatus();
        }
        else {
            //showStats((TvSeries) media);
        }
    }

    public void onEvent(FavoriteStateEvent event) {
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
