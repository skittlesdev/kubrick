package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.github.skittlesdev.kubrick.events.LoginEvent;
import com.github.skittlesdev.kubrick.events.LogoutEvent;
import com.github.skittlesdev.kubrick.ui.fragments.FavoritesOverviewFragment;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.parse.*;
import com.parse.ParseUser;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ParseUser user;
    private boolean followed = false;
    private  MaterialCalendarView calendar;

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
        calendar.setVisibility(View.VISIBLE);

        ParseUser.getQuery().getInBackground(getIntent().getStringExtra("user_id"), new GetCallback<ParseUser>() {
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
                transaction.add(R.id.fragment_movies, movieFavorites, "movies");
                transaction.add(R.id.fragment_series, seriesFavorites, "series");
                transaction.commit();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ViewedTvSeriesEpisodes");
                String userId = getIntent().getStringExtra("user_id");

                query.whereEqualTo("User",null);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> EpisodeList, ParseException e) {
                        if (e == null) {
                            Log.d("Viewed episodes data", "Retrieved " + EpisodeList.size() + " episodes");
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });
            }
        });


    }

    private void buildCalendar(ParseObject parseObject){

    }
    private void buildProfile(ParseUser user) {
        ProfileElement profile = new ProfileElement(user);

        TextView name = (TextView) findViewById(R.id.name);
        ImageView image = (ImageView) findViewById(R.id.circleView);

        name.setText(profile.getName());
        if (!TextUtils.isEmpty(profile.getAvatarUrl())) {
            Glide.with(KubrickApplication.getContext())
                .load(profile.getAvatarUrl())
                .placeholder(R.drawable.poster_default_placeholder)
                .error(R.drawable.poster_default_error)
                .bitmapTransform(new CropCircleTransformation(KubrickApplication.getContext()))
                .into(image);
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
        if (ParseUser.getCurrentUser() == null) {
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
                }
                else {
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
}
