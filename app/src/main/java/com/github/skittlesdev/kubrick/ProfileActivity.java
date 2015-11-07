package com.github.skittlesdev.kubrick;

import android.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.github.skittlesdev.kubrick.ui.fragments.FavoritesOverviewFragment;
import com.github.skittlesdev.kubrick.ui.menus.DrawerMenu;
import com.github.skittlesdev.kubrick.ui.menus.ToolbarMenu;
import com.github.skittlesdev.kubrick.utils.ProfileElement;
import com.parse.*;
import com.parse.ParseUser;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ProfileActivity extends AppCompatActivity {
    private ParseUser user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new DrawerMenu(this, (DrawerLayout) findViewById(R.id.homeDrawerLayout), (RecyclerView) findViewById(R.id.homeRecyclerView)).draw();
    }

    @Override
    public void onStart() {
        super.onStart();

        ParseUser.getQuery().getInBackground(getIntent().getStringExtra("user_id"), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                buildProfile(user);

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
            }
        });
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

}
